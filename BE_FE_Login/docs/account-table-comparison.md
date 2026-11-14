# So sanh bang account giua iot va iot_demo

## Muc tieu dieu chinh
- Dung lai DB `iot` ma khong can sua cac bang co khoa ngoai nhu `device` va `notification`.
- Giu nguyen luong demo auth: register, login, JWT cookie, role `ADMIN/USER`.
- Giam toi thieu nguy co Hibernate can thiep vao schema dang duoc cac bang khac tham chieu.

## Giong nhau
- Cung co cac cot: `id_account`, `username`, `password`, `name`, `number_phone`, `email`, `location`, `role`, `created_at`.
- Cung dung bang `account` cho authentication va authorization.
- Cung cho phep luu `id_account` dang chuoi va luu `password` dang ma hoa BCrypt.
- Cung dung `role` de cap quyen `ADMIN` va `USER`.

## Khac nhau
| Noi dung | `iot.account` goc | `iot_demo.account` demo |
| --- | --- | --- |
| `id_account` | `VARCHAR(255)` | `VARCHAR(36)` |
| `username` | `VARCHAR(255) NOT NULL` | `VARCHAR(100) NOT NULL UNIQUE` |
| `email` | `VARCHAR(255)` | `VARCHAR(150) UNIQUE` |
| `number_phone` | `VARCHAR(255)` | `VARCHAR(20)` |
| `role` | `VARCHAR(255)`, cho phep null | `VARCHAR(20) NOT NULL DEFAULT 'USER'` |
| Rang buoc role | Chua ro rang | `CHECK(role IN ('ADMIN','USER'))` |
| Anh huong bang khac | Dang duoc `device`, `notification` tham chieu | Khong bi rang buoc boi cac bang khac |

## Cach xu ly da chon
- Code `Account` duoc dua ve khop schema `iot.account` goc o muc do cot va do dai du lieu.
- Logic auth van giu `normalizeRole()` de quy moi gia tri role ve `ADMIN` hoac `USER`.
- Validation dang ky van kiem tra trung `username` va `email` o tang ung dung, du DB `iot` chua co UNIQUE.
- Cac bang `device`, `notification` khong can doi khoa ngoai vi `id_account` van giu `VARCHAR(255)`.

## Loi ich cua cach nay
- Khong phai drop/recreate foreign key.
- Khong lam vo du lieu hay quan he voi cac bang khac.
- Van giu duoc demo auth da chay thu tren local.
- De dang nang cap sau nay: khi team san sang, co the tao migration rieng cho bang `account` trong DB `iot`.
