module.exports = {
    server: {
        host: process.env.HOST || "localhost",
        port: process.env.PORT || 3000,
        env: process.env.NODE_ENV || "development",
    },
    logger: {
        logLevel: process.env.LOG_LEVEL || "info",
        fileLogLevel: process.env.FILE_LOG_LEVEL || "info",
        consoleLogLevel: process.env.CONSOLE_LOG_LEVEL || "debug",
        maxSize: "20m",
        maxFiles: "14d",
    },
    cors: {
        enabled: true,
        origins: ["*"],
    },
    swagger: {
        enabled: true,
    },
};
