const Hapi = require("hapi");
const Hoek = require("@hapi/hoek");
const Package = require("../../package.json");
const Logger = require("../logger/logger");
const HttpLogger = require("../logger/http-logger");

class HttpServer {
    constructor(config) {
        this.config = Hoek.clone(config);
        this.server = null;
        this.logger = new Logger(config.logger);
        this.httpLogger = new HttpLogger(this.logger);
    }

    async createServer() {
        this.server = new Hapi.Server();

        this.server.connection({
            host: this.config.server.host || "localhost",
            port: this.config.server.port || 3000,
        });

        this.server.route({
            method: "*",
            path: "/{p*}",
            config: {
                cors: {
                    origin: ["*"],
                    headers: ["Accept", "Authorization", "Content-Type", "If-None-Match"],
                },
            },
            handler: function (request, reply) {
                reply().code(404);
            },
        });

        await this.server.register(this.httpLogger.middleware());
        await this._registerRoutes(this.server);
        await this._setupErrorHandlers();

        return this.server;
    }

    async _registerRoutes(server) {
        server.route({
            method: "GET",
            path: "/health",
            handler: (request, reply) => {
                reply({
                    status: "ok",
                    version: Package.version,
                    timestamp: new Date().toISOString(),
                });
            },
            config: {
                tags: ["api", "health"],
                description: "Health check endpoint",
            },
        });
    }

    _setupErrorHandlers() {
        process.on("unhandledRejection", err => {
            this.logger.error("Unhandled Rejection", { error: err });
            process.exit(1);
        });

        this.server.on("request-error", (request, err) => {
            this.logger.error("Server Error", {
                error: err.message,
                stack: err.stack,
                path: request.path,
                method: request.method,
            });
        });

        this.server.ext("onPreResponse", (request, reply) => {
            const response = request.response;

            if (response.isBoom) {
                this.logger.error("Request Error", {
                    error: response.message,
                    statusCode: response.output.statusCode,
                    path: request.path,
                    method: request.method,
                });
            }

            return reply.continue();
        });

        process.on("SIGTERM", async () => {
            this.logger.info("Received SIGTERM signal");
            await this.stop();
            process.exit(0);
        });

        process.on("SIGINT", async () => {
            this.logger.info("Received SIGINT signal");
            await this.stop();
            process.exit(0);
        });
    }

    async start() {
        if (!this.server) {
            await this.createServer();
        }

        await this.server.start();
        this.logger.info(`Server running at: ${this.server.info.uri}`);
        return this.server;
    }

    async stop() {
        if (this.server) {
            await this.server.stop();
            this.logger.info("Server stopped");
        }
    }

    getServer() {
        return this.server;
    }

    getLogger() {
        return this.logger;
    }
}

module.exports = HttpServer;
