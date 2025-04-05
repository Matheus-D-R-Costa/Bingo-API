const winston = require("winston");
const DailyRotateFile = require("winston-daily-rotate-file");
const path = require("path");

class Logger {
    constructor(config) {
        this.config = config;
        this.logger = this._createLogger();
    }

    _createLogger() {
        const logDir = path.join(process.cwd(), "logs");
        const { combine, timestamp, printf, colorize } = winston.format;

        const customFormat = printf(({ level, message, timestamp, ...metadata }) => {
            let msg = `${timestamp} [${level.padEnd(5)}]: ${message}`;
            if (Object.keys(metadata).length > 0) {
                msg += ` ${JSON.stringify(metadata)}`;
            }
            return msg;
        });

        const fileTransport = new DailyRotateFile({
            dirname: logDir,
            filename: "application-%DATE%.log",
            datePattern: "YYYY-MM-DD",
            zippedArchive: true,
            maxSize: this.config.maxSize || "20m",
            maxFiles: this.config.maxFiles || "14d",
            level: this.config.fileLogLevel || "info",
            format: combine(
                timestamp(),
                customFormat,
            ),
        });

        const consoleTransport = new winston.transports.Console({
            level: this.config.consoleLogLevel || "debug",
            format: combine(
                colorize(),
                timestamp(),
                customFormat,
            ),
        });

        return winston.createLogger({
            level: this.config.logLevel || "info",
            levels: {
                error: 0,
                warn: 1,
                info: 2,
                debug: 3,
            },
            format: combine(
                timestamp(),
                customFormat,
            ),
            transports: [
                fileTransport,
                consoleTransport,
            ],
            exitOnError: false,
        });
    }

    error(message, metadata = {}) {
        this.logger.error(message, metadata);
    }

    warn(message, metadata = {}) {
        this.logger.warn(message, metadata);
    }

    info(message, metadata = {}) {
        this.logger.info(message, metadata);
    }

    debug(message, metadata = {}) {
        this.logger.debug(message, metadata);
    }

    httpLog(req, res, responseTime) {
        const logData = {
            method: req.method,
            path: req.path,
            statusCode: res.statusCode,
            responseTime: `${responseTime}ms`,
            userAgent: req.headers["user-agent"],
            ip: req.info.remoteAddress,
        };

        if (res.statusCode >= 400) {
            this.error("HTTP Request Error", logData);
            return;
        }

        this.info("HTTP Request", logData);
    }

    logError(error, metadata = {}) {
        this.error(error.message, {
            ...metadata,
            stack: error.stack,
            name: error.name,
        });
    }
}

module.exports = Logger;
