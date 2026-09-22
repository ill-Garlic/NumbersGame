package com.numbergame.server.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {

    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();

            // 1. Kiểm tra xem có truyền biến môi trường DB_URL hay không
            String dbUrl = System.getenv("DB_URL");
            if (dbUrl == null || dbUrl.isBlank()) {
                // Nếu không có, dùng cấu hình mặc định cho máy local
                String host = System.getenv().getOrDefault("DB_HOST", "localhost");
                String port = System.getenv().getOrDefault("DB_PORT", "1433");
                String dbName = System.getenv().getOrDefault("DB_NAME", "NumberGameDB");
                dbUrl = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true;", host, port, dbName);
            }

            // 2. Điền username và password của SQL Server (đổi mật khẩu ở tham số thứ 2 nếu máy bạn dùng pass khác)
            String user = System.getenv().getOrDefault("DB_USER", "sa");
            String password = System.getenv().getOrDefault("DB_PASSWORD", "123456");

            config.setJdbcUrl(dbUrl);
            config.setUsername(user);
            config.setPassword(password);
            config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 3. Cấu hình tối ưu pool
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(15000); // 15 giây chờ kết nối

            dataSource = new HikariDataSource(config);
            System.out.println(">>> [HikariCP] Khoi tao ket noi Database thanh cong!");
        } catch (Exception e) {
            System.err.println(">>> [HikariCP] Loi khoi tao Database: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource chua san sang hoac sai thong tin dang nhap DB!");
        }
        return dataSource.getConnection();
    }
}