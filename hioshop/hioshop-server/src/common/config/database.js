const mysql = require('think-model-mysql');

module.exports = {
    handle: mysql,
    database: 'hiolabsGit',
    prefix: 'hiolabs_',
    encoding: 'utf8mb4',
    host: '127.0.0.1',
    port: '3306',
    user: 'root',
    password: '12345678',
    dateStrings: true
};
