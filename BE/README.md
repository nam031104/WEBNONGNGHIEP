# BE Login - Authentication Module

REST API xac thuc tai khoan su dung Spring Boot, Spring Security, JWT va PostgreSQL.

## Tinh nang

- Dang ky tai khoan moi qua `POST /api/auth/register`
- Dang nhap qua `POST /api/auth/login`
- Kiem tra suc khoe API qua `GET /api/auth/health`
- Xac thuc token qua `GET /api/auth/validate`
- Ma hoa mat khau bang BCrypt
- Xac thuc stateless bang JWT

## Cong nghe

- Java 21
- Spring Boot 3.5.13
- Spring Security
- Spring Data JPA
- JJWT 0.13.0
- PostgreSQL
- Maven
- Lombok

## Cau truc chinh

```text
BE_Login/
|- src/main/java/btl/nongnghiep/
|  |- BeLoginApplication.java
|  \- authentication/
|     |- config/
|     |- controller/
|     |- dto/
|     |- entity/
|     |- exception/
|     |- repository/
|     |- security/
|     \- service/
|- src/main/resources/
|  |- application.yaml
|  \- db-schema.sql
|- sql-init-database.sql


## Database schema

Bang xac thuc hien tai la `account`:

```sql
CREATE TABLE account (
    id_account VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    number_phone VARCHAR(255),
    email VARCHAR(255),
    location VARCHAR(255),
    role VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

Trong code:

- `id_account` la khoa chinh cua tai khoan
- `password` luu chuoi BCrypt
- `accountId` duoc tra ve trong response va dua vao JWT claim

## Cai dat nhanh

1. Tao database PostgreSQL:

```sql
CREATE DATABASE auth_db;
```

2. Chon database profile:

- PostgreSQL: mac dinh cho demo cua ban
- MySQL: danh cho cac ban trong nhom

3. Cau hinh file [application.yaml](/d:/BTL%20OOP/BE_Login/src/main/resources/application.yaml):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/auth_db
    username: postgres
    password: YOUR_PASSWORD
```

4. Chay script khoi tao:

```bash
psql -U postgres -d auth_db -f sql-init-database.sql
```

Voi MySQL:

```bash
mysql -u root -p < sql-init-mysql.sql
```

5. Build va run:

```bash
mvn clean install
mvn spring-boot:run
```

Ung dung mac dinh chay profile `postgres` va chay tai `http://localhost:8080/api`.

Neu muon chay MySQL:

```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=mysql"
```

Hoac:

```bash
$env:APP_DB_PROFILE="mysql"
mvn spring-boot:run
```

## API

### Register

`POST /api/auth/register`

```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

Response:

```json
{
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER",
  "message": "Dang ky tai khoan thanh cong",
  "success": true
}
```

### Login

`POST /api/auth/login`

```json
{
  "username": "john_doe",
  "password": "password123"
}
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER"
}
```

### Validate token

`GET /api/auth/validate`

Header:

```text
Authorization: Bearer <TOKEN>
```

Response:

```json
{
  "valid": true,
  "message": "Token hop le",
  "username": "john_doe",
  "accountId": "550e8400-e29b-41d4-a716-446655440000"
}
```

## Bao mat

- Password duoc ma hoa bang BCrypt truoc khi luu vao bang `account`
- JWT luu `accountId` va `username`
- Security filter chain dang de `register`, `login`, `health` public
- Cac endpoint con lai yeu cau token hop le

## Ho tro ca PostgreSQL va MySQL

Repo hien tai da ho tro 2 profile:

- [application-postgres.yaml](/d:/BTL%20OOP/BE_Login/src/main/resources/application-postgres.yaml)
- [application-mysql.yaml](/d:/BTL%20OOP/BE_Login/src/main/resources/application-mysql.yaml)

File schema tuong ung:

- [db-schema.sql](/d:/BTL%20OOP/BE_Login/src/main/resources/db-schema.sql)
- [db-schema-mysql.sql](/d:/BTL%20OOP/BE_Login/src/main/resources/db-schema-mysql.sql)

Script khoi tao tuong ung:

- [sql-init-database.sql](/d:/BTL%20OOP/BE_Login/sql-init-database.sql)
- [sql-init-mysql.sql](/d:/BTL%20OOP/BE_Login/sql-init-mysql.sql)

## Luu y

- Tai lieu cu co nhac den `users` va `userId`; hien tai da chuyen sang `account` va `accountId`
- Neu database cua ban da co bang `account`, code backend hien tai da map dung theo bang do
- Neu da co du lieu cu, can dam bao `username` va `email` khong bi trung

