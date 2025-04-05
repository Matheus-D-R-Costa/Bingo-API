const path = require("path");
const fs = require("fs").promises;
const config = require("./config/default");
const HttpServer = require("./app/server/http-server");
const Logger = require("./app/logger/logger");

class Application {
    constructor() {
        this.httpServer = null;
        this.logger = null;
    }

    async initialize() {
        try {
            await this._ensureDirectories();
            this.logger = new Logger(config.logger);
            this.httpServer = new HttpServer(config);
            await this.httpServer.start();
            this._setupProcessHandlers();
            this._logEnvironmentInfo();
        } catch (_) {
            process.exit(1);
        }
    }

    async shutdown() {
        try {
            this.logger.info("Application shutdown initiated");
            await this.httpServer.stop();
            //TODO: Adicionar mais lógica de cleanUp (fechar conexão com o mongo e tals)
            this.logger.info("Application shutdown completed");
            process.exit(0);
        } catch (error) {
            this.logger.error("Error during shutdown", { error });
            process.exit(1);
        }
    }

    async _ensureDirectories() {
        const dirs = [
            "logs",
            "temp",
        ];

        for (const dir of dirs) {
            const dirPath = path.join(process.cwd(), dir);
            try {
                await fs.mkdir(dirPath, { recursive: true });
            } catch (error) {
                if (error.code !== "EEXIST") {
                    throw error;
                }
            }
        }
    }

    _setupProcessHandlers() {
        process.on("uncaughtException", error => {
            this.logger.error("Uncaught Exception", {
                error: error.message,
                stack: error.stack,
            });
            this.shutdown();
        });

        process.on("unhandledRejection", (reason, promise) => {
            this.logger.error("Unhandled Rejection", {
                reason: reason,
                promise: promise,
            });
            this.shutdown();
        });

        const signals = ["SIGTERM", "SIGINT", "SIGUSR2"];

        signals.forEach(signal => {
            process.on(signal, () => {
                this.logger.info(`Received ${signal} signal`);
                this.shutdown();
            });
        });
    }

    _logEnvironmentInfo() {
        const envInfo = {
            nodeVersion: process.version,
            platform: process.platform,
            arch: process.arch,
            pid: process.pid,
            env: process.env.NODE_ENV || "development",
            memory: process.memoryUsage(),
            config: {
                host: config.server.host,
                port: config.server.port,
                logLevel: config.logger.logLevel,
            },
        };

        this.logger.info("Application environment", envInfo);
    }
}

const app = new Application();

(async () => {
    try {
        await app.initialize();
    } catch (_) {
        process.exit(1);
    }
})();

module.exports = Application;
