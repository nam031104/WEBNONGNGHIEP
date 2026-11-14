# MySQL Setup Guide

Tai lieu nay dung cho thanh vien trong nhom muon chay `BE_Login` bang MySQL thay vi PostgreSQL.

## 1. Yeu cau

- MySQL da cai dat va dang chay
- Java 21+
- Maven

## 2. Tao database `iot`

Mo MySQL command line hoac MySQL Workbench, sau do chay:

```sql
CREATE DATABASE IF NOT EXISTS iot;
```

## 3. Chay script khoi tao bang

Tai thu muc [sql-init-mysql.sql](/d:/BTL%20OOP/BE_Login/sql-init-mysql.sql), script hien tai da tao va su dung DB `iot`.

Chay bang terminal:

```bash
mysql -u root -p < sql-init-mysql.sql
```

Sau do kiem tra:

```sql
USE iot;
SELECT * FROM account;
```

Neu thay du lieu mau `admin` va `john_doe` thi script da chay dung.

## 4. Kiem tra profile MySQL

File [application-mysql.yaml](/d:/BTL%20OOP/BE_Login/src/main/resources/application-mysql.yaml) dang mac dinh:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/iot?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:}
```

Ban co the giu nguyen hoac override bang bien moi truong.

## 5. Set bien moi truong trong PowerShell

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="mat_khau_mysql_cua_ban"
```

Neu can doi host/port/database:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/iot?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
```

## 6. Chay ung dung voi profile MySQL

Dung lenh nay trong thu muc [BE_Login](/d:/BTL%20OOP/BE_Login):

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=mysql"
```

Hoac:

```powershell
$env:APP_DB_PROFILE="mysql"
mvn spring-boot:run
```

## 7. Test nhanh

### Health

```powershell
(Invoke-WebRequest -UseBasicParsing http://localhost:8080/api/auth/health).Content
```

### Login voi du lieu mau

```powershell
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/auth/login" `
  -ContentType "application/json" `
  -Body '{"username":"admin","password":"password123"}'
```

### Register tai khoan moi

```powershell
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/auth/register" `
  -ContentType "application/json" `
  -Body '{"username":"testmysql1","email":"testmysql1@example.com","password":"123456"}'
```

## 8. Loi hay gap

### Unknown database `iot`

Nguyen nhan:
- Chua tao DB

Cach sua:
- Chay lai `CREATE DATABASE IF NOT EXISTS iot;`

### Access denied for user

Nguyen nhan:
- Sai `DB_USERNAME` hoac `DB_PASSWORD`

Cach sua:
- Set lai env trong PowerShell

### Login fail 401

Nguyen nhan:
- Chua co account
- Password trong DB khong phai BCrypt

Cach sua:
- Chay `sql-init-mysql.sql`
- Hoac dang ky bang API truoc roi login lai
