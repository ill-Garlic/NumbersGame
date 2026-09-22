# Number Game

Tro choi tim so nhieu nguoi choi theo thoi gian thuc.

Nguoi choi tham gia phong, cung nhin mot bang so va lan luot bam vao so dang duoc yeu cau. Server phan xu ly thao tac bam truoc, dong bo trang thai cho tat ca nguoi choi va luu ket qua vao SQL Server.

## 1. Cong nghe

- Java 21
- Maven multi-module
- Java Swing + FlatLaf: giao dien client
- TCP Socket: giao tiep client-server
- Microsoft SQL Server: luu tai khoan, tran dau, ket qua va xep hang
- HikariCP: quan ly ket noi database
- Gson: dong goi du lieu JSON
- BCrypt: bam mat khau

## 2. Cau truc project

```text
NumbersGame/
|-- pom.xml                 # POM cha, quan ly module va Java 21
|-- common/                 # Model va protocol dung chung
|-- client/                 # Ung dung Java Swing cho nguoi choi
|-- server/                 # TCP server, xu ly phong va luat game
|-- database/
|   `-- schema.sql          # Script tao NumberGameDB va cac bang
`-- README.md
```

## 3. Trang thai hien tai

Da hoan thanh phan khung:

- Maven build duoc ca ba module `common`, `server`, `client`.
- Client Swing co the khoi dong giao dien mau.
- Server co the mo TCP port `8888` va nhan ket noi.
- SQL Server JDBC va HikariCP da duoc khai bao.
- Schema database da co cac nhom bang tai khoan, session, tran dau, nguoi tham gia, vat pham, replay va xep hang.

Chua hoan thanh:

- Dang ky, dang nhap va quan ly tai khoan.
- Tao/tham gia phong.
- Ban so va xu ly luat choi.
- Dong bo trang thai giua nhieu client.
- Luu du lieu qua cac repository/service.
- Bang xep hang va replay.

## 4. Yeu cau moi truong

Cai dat:

- JDK 21
- Maven 3.9 tro len
- SQL Server 2019 tro len
- SQL Server Management Studio (khuyen nghi)

Kiem tra Java va Maven:

```powershell
java -version
mvn -version
```

Project dung Java 21. Neu Maven dang dung JDK khac, dat lai `JAVA_HOME`:

```powershell
$env:JAVA_HOME = "D:\Program Files\Java\jdk-21"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
mvn -version
```

## 5. Tao database

Mo SQL Server Management Studio, chay toan bo file:

```text
database/schema.sql
```

Script se tao database `NumberGameDB` va cac bang can thiet.

Mac dinh server dang doc cac bien moi truong sau:

```text
DB_HOST=localhost
DB_PORT=1433
DB_NAME=NumberGameDB
DB_USER=sa
DB_PASSWORD=123456
```

Khong commit mat khau that vao Git. Khi lam viec tren moi truong ca nhan, co the dat bien moi truong trong PowerShell:

```powershell
$env:DB_HOST = "localhost"
$env:DB_PORT = "1433"
$env:DB_NAME = "NumberGameDB"
$env:DB_USER = "sa"
$env:DB_PASSWORD = "mat_khau_sql_server"
```

Hoac dung mot JDBC URL day du:

```powershell
$env:DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=NumberGameDB;encrypt=true;trustServerCertificate=true;"
```

## 6. Build project

Luon chay lenh tu thu muc goc `NumbersGame`:

```powershell
cd D:\DOAN_LTUDM\NumbersGame
mvn clean package
```

Neu Maven vua bi cache dependency loi, dung:

```powershell
mvn clean package -U
```

JAR sau khi build:

```text
server/target/server-1.0.0.jar
client/target/client-1.0.0.jar
```

## 7. Chay server va client

Mo terminal thu nhat va chay server:

```powershell
cd D:\DOAN_LTUDM\NumbersGame
java -jar server\target\server-1.0.0.jar
```

Server mac dinh lang nghe port `8888`.

Mo terminal thu hai va chay client:

```powershell
cd D:\DOAN_LTUDM\NumbersGame
java -jar client\target\client-1.0.0.jar
```

Tren Windows co the kiem tra port server:

```powershell
Test-NetConnection localhost -Port 8888
```

## 8. Quy uoc phat trien

- Khong sua truc tiep cac file trong `target/`; day la file build tu dong.
- Code dung chung dat trong module `common`.
- Client khong ket noi truc tiep den SQL Server. Client chi giao tiep voi server.
- Server la noi xu ly luat game, xac thuc, dong bo va ghi database.
- Khong luu mat khau dang plain text; dung BCrypt.
- Khong hard-code mat khau SQL Server khi commit code.
- Moi thay doi dependency phai cap nhat POM va ghi chu trong pull request.
- Moi tinh nang moi nen co model/protocol, server handler va client UI tuong ung.

## 9. Huong phat trien de xuat

1. Tao model va message protocol trong `common`.
2. Dung man hinh dang nhap/dang ky trong `client`.
3. Tao server handler cho ket noi va message.
4. Tao repository/service ket noi SQL Server.
5. Tao phong choi va dong bo danh sach nguoi choi.
6. Tao ban so, timer va xu ly bam so.
7. Luu lich su tran dau, thong ke va xep hang.
8. Test voi nhieu client tren localhost.
9. Deploy server va SQL Server tren VPS; client ket noi toi IP/domain cua VPS.

## 10. Git workflow de xuat

Truoc khi code:

```powershell
git pull
```

Tao nhanh rieng cho tung tinh nang:

```powershell
git checkout -b feature/ten-tinh-nang
```

Truoc khi tao pull request:

```powershell
mvn clean package
git status
git diff
```

Khong commit cac thu muc build:

```text
target/
```
