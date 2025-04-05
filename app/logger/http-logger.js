class HttpLogger {
    constructor(logger) {
        this.logger = logger;
    }

    middleware() {
        const plugin = {
            register: (server, options, next) => {
                server.ext("onRequest", (request, reply) => {
                    request.app.requestStartTime = Date.now();
                    return reply.continue();
                });

                server.ext("onPreResponse", (request, reply) => {
                    const responseTime = Date.now() - request.app.requestStartTime;
                    const response = request.response;
                    const statusCode = response.isBoom ? response.output.statusCode : response.statusCode;

                    // Dados comuns para todos os logs
                    const logData = {
                        method: request.method.toUpperCase(),
                        path: request.path,
                        statusCode,
                        responseTime: `${responseTime}ms`,
                        userAgent: request.headers["user-agent"],
                        ip: request.info.remoteAddress,
                    };

                    // Ignorar logs de favicon.ico
                    if (request.path === "/favicon.ico") {
                        return reply.continue();
                    }

                    // Categorizar os logs baseado no status code
                    if (statusCode >= 500) {
                        // Erro do servidor
                        this.logger.error("Server Error", logData);
                    } else if (statusCode >= 400 && statusCode !== 404) {
                        // Erro do cliente (exceto 404)
                        this.logger.warn("Client Error", logData);
                    } else if (statusCode === 404) {
                        // Not Found - log como debug apenas
                        this.logger.debug("Not Found", logData);
                    } else {
                        // Sucesso
                        this.logger.info("Request Completed", logData);
                    }

                    return reply.continue();
                });

                next();
            },
        };

        plugin.register.attributes = {
            name: "http-logger",
            version: "1.0.0",
        };

        return plugin;
    }
}

module.exports = HttpLogger;
