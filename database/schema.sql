-- 1. Khởi tạo Database
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'NumberGameDB')
BEGIN
    CREATE DATABASE NumberGameDB;
END;
GO

USE NumberGameDB;
GO

-- =========================================================================
-- BẢNG 1: Users (Quản lý tài khoản & hồ sơ cá nhân)
-- =========================================================================
IF OBJECT_ID('dbo.Users', 'U') IS NOT NULL DROP TABLE dbo.Users;
CREATE TABLE Users (
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    gender NVARCHAR(10) NOT NULL,
    dob DATE NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);
GO

-- =========================================================================
-- BẢNG 2: UserSessions (Quản lý người dùng online/offline & IP kết nối)
-- =========================================================================
IF OBJECT_ID('dbo.UserSessions', 'U') IS NOT NULL DROP TABLE dbo.UserSessions;
CREATE TABLE UserSessions (
    session_id VARCHAR(100) PRIMARY KEY,
    user_id INT NOT NULL,
    ip_address VARCHAR(45) NOT NULL,
    login_time DATETIME DEFAULT GETDATE(),
    last_active DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Sessions_Users FOREIGN KEY (user_id) 
        REFERENCES Users(user_id) ON DELETE CASCADE
);
GO

-- =========================================================================
-- BẢNG 3: UserStats (Thống kê phục vụ truy vấn Bảng xếp hạng tức thì)
-- =========================================================================
IF OBJECT_ID('dbo.UserStats', 'U') IS NOT NULL DROP TABLE dbo.UserStats;
CREATE TABLE UserStats (
    user_id INT PRIMARY KEY,
    total_matches INT DEFAULT 0,
    wins INT DEFAULT 0,
    losses INT DEFAULT 0,
    draws INT DEFAULT 0,
    total_score INT DEFAULT 0,
    CONSTRAINT FK_UserStats_Users FOREIGN KEY (user_id) 
        REFERENCES Users(user_id) ON DELETE CASCADE
);
GO

-- =========================================================================
-- BẢNG 4: GameConfigurations (Cấu hình tùy biến dải số, thời gian ván đấu)
-- =========================================================================
IF OBJECT_ID('dbo.GameConfigurations', 'U') IS NOT NULL DROP TABLE dbo.GameConfigurations;
CREATE TABLE GameConfigurations (
    config_id INT IDENTITY(1,1) PRIMARY KEY,
    config_name NVARCHAR(50) NOT NULL,
    min_number INT DEFAULT 1,
    max_number INT DEFAULT 100,
    duration_seconds INT DEFAULT 120,
    max_players INT DEFAULT 3,
    is_active BIT DEFAULT 1
);
GO

-- Thêm cấu hình mặc định theo đề tài
INSERT INTO GameConfigurations (config_name, min_number, max_number, duration_seconds, max_players)
VALUES (N'Mặc định (1-100, 2 phút)', 1, 100, 120, 3),
       (N'Nhanh (1-50, 1 phút)', 1, 50, 60, 3);
GO

-- =========================================================================
-- BẢNG 5: Items (Quản lý các loại vật phẩm trong trò chơi)
-- =========================================================================
IF OBJECT_ID('dbo.Items', 'U') IS NOT NULL DROP TABLE dbo.Items;
CREATE TABLE Items (
    item_id INT IDENTITY(1,1) PRIMARY KEY,
    item_code VARCHAR(30) NOT NULL UNIQUE,     -- 'LUCKY_NUMBER', 'BLIND_OPPONENT'
    item_name NVARCHAR(50) NOT NULL,           -- 'Số may mắn', 'Ưu tiên'
    description NVARCHAR(255) NOT NULL,        -- 'Nhân đôi điểm', 'Che số đối thủ trong 3s'
    duration_seconds INT DEFAULT 0             -- Thời gian hiệu lực (ví dụ 3 giây)
);
GO

-- Thêm dữ liệu vật phẩm theo đề bài
INSERT INTO Items (item_code, item_name, description, duration_seconds)
VALUES ('LUCKY_NUMBER', N'Số may mắn', N'Ghi được cộng gấp đôi số điểm', 0),
       ('BLIND_OPPONENT', N'Ưu tiên', N'Che mờ bảng số của đối thủ trong phòng', 3);
GO

-- =========================================================================
-- BẢNG 6: Matches (Lưu thông tin tổng quan của ván đấu)
-- =========================================================================
IF OBJECT_ID('dbo.Matches', 'U') IS NOT NULL DROP TABLE dbo.Matches;
CREATE TABLE Matches (
    match_id INT IDENTITY(1,1) PRIMARY KEY,
    config_id INT NOT NULL,
    room_code VARCHAR(10) NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NULL,
    duration_seconds INT NULL,
    status VARCHAR(20) DEFAULT 'IN_PROGRESS', -- 'IN_PROGRESS', 'FINISHED', 'ABORTED'
    winner_id INT NULL,
    initial_board_matrix NVARCHAR(MAX) NOT NULL, -- Chuỗi JSON lưu vị trí 100 số ban đầu để Replay
    CONSTRAINT FK_Matches_Config FOREIGN KEY (config_id) 
        REFERENCES GameConfigurations(config_id),
    CONSTRAINT FK_Matches_Winner FOREIGN KEY (winner_id) 
        REFERENCES Users(user_id)
);
GO

-- =========================================================================
-- BẢNG 7: MatchParticipants (Chi tiết người tham gia & Khán giả)
-- =========================================================================
IF OBJECT_ID('dbo.MatchParticipants', 'U') IS NOT NULL DROP TABLE dbo.MatchParticipants;
CREATE TABLE MatchParticipants (
    match_id INT NOT NULL,
    user_id INT NOT NULL,
    role VARCHAR(20) DEFAULT 'PLAYER',        -- 'PLAYER' (2-3 người), 'SPECTATOR' (Khán giả xem)
    score INT DEFAULT 0,
    numbers_found INT DEFAULT 0,
    is_winner BIT DEFAULT 0,
    PRIMARY KEY (match_id, user_id),
    CONSTRAINT FK_MP_Matches FOREIGN KEY (match_id) 
        REFERENCES Matches(match_id) ON DELETE CASCADE,
    CONSTRAINT FK_MP_Users FOREIGN KEY (user_id) 
        REFERENCES Users(user_id)
);
GO

-- =========================================================================
-- BẢNG 8: ReplayLogs (Ghi lại sự kiện theo dòng thời gian phục vụ Replay ván chơi)
-- =========================================================================
IF OBJECT_ID('dbo.ReplayLogs', 'U') IS NOT NULL DROP TABLE dbo.ReplayLogs;
CREATE TABLE ReplayLogs (
    event_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    match_id INT NOT NULL,
    user_id INT NOT NULL,
    timestamp_offset_ms INT NOT NULL,         -- Thời gian tính từ khi ván đấu bắt đầu (ms)
    event_type VARCHAR(30) NOT NULL,           -- 'CORRECT_HIT', 'WRONG_HIT', 'ITEM_USED'
    target_number INT NULL,                    -- Số mục tiêu cần tìm lúc đó
    clicked_number INT NULL,                   -- Số mà người chơi bấm
    item_id INT NULL,                          -- Vật phẩm đã kích hoạt (nếu có)
    CONSTRAINT FK_Replay_Matches FOREIGN KEY (match_id) 
        REFERENCES Matches(match_id) ON DELETE CASCADE,
    CONSTRAINT FK_Replay_Users FOREIGN KEY (user_id) 
        REFERENCES Users(user_id),
    CONSTRAINT FK_Replay_Items FOREIGN KEY (item_id) 
        REFERENCES Items(item_id)
);
GO

-- =========================================================================
-- TRIGGER TỰ ĐỘNG TẠO BẢN GHI USERSTATS KHI CÓ NGƯỜI ĐĂNG KÝ MỚI
-- =========================================================================
CREATE OR ALTER TRIGGER trg_AfterUserInsert
ON Users
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO UserStats (user_id, total_matches, wins, losses, draws, total_score)
    SELECT user_id, 0, 0, 0, 0, 0
    FROM INSERTED;
END;
GO