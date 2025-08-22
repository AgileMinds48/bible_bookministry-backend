## Role testing guide

This project uses database roles with JWT auth (cookie-based). Use these steps to verify login, authorization, and role APIs.

### 1) Start the app

```bash
mvn spring-boot:run
```

### 2) Create an admin user (public endpoint)

POST `http://localhost:8085/api/v1/admin/register`

```json
{
  "firstName": "Admin",
  "lastName": "User",
  "userName": "admin",
  "email": "admin@ministry.com",
  "phoneNumber": "1234567890",
  "password": "Admin123!"
}
```

### 3) Login (cookie-based JWT)

POST `http://localhost:8085/api/v1/auth/login`

```json
{
  "usernameOrEmail": "admin",
  "password": "Admin123!"
}
```

Expected: Response sets cookie `JWTAccess_token`. In Postman, open the Cookies tab for `localhost:8085` and confirm it was saved. All subsequent requests should automatically include it.

If needed, copy it manually from the `Set-Cookie` response header and add a header:

```
Cookie: JWTAccess_token=<paste-token-here>
```

### 4) Verify admin-only access

GET `http://localhost:8085/api/v1/admin/get-users`

- With admin cookie → 200 OK
- Without cookie → 401 Unauthorized
- With a customer cookie (see customer flow below) → 403 Forbidden

### 5) Role APIs (require admin cookie)

- Create role
  - POST `http://localhost:8085/api/v1/roles`
  - Body:
  ```json
  {
    "roleName": "Content Manager",
    "roleCode": "CONTENT_MANAGER",
    "description": "Can manage content"
  }
  ```
- Get all roles
  - GET `http://localhost:8085/api/v1/roles`
- Get by code
  - GET `http://localhost:8085/api/v1/roles/code/CONTENT_MANAGER`
- Update role
  - PUT `http://localhost:8085/api/v1/roles/{roleId}`
- Deactivate/Activate
  - PATCH `http://localhost:8085/api/v1/roles/{roleId}/deactivate`
  - PATCH `http://localhost:8085/api/v1/roles/{roleId}/activate`

### 6) Customer flow (for negative authorization tests)

1. Sign up (multipart): POST `http://localhost:8085/api/v1/auth/signup`
   - `userInfo` (text):
   ```json
   {
     "firstName": "Customer",
     "lastName": "User",
     "userName": "customer",
     "email": "customer@ministry.com",
     "phoneNumber": "2345678901",
     "password": "Customer123!",
     "city": "",
     "country": "",
     "state": ""
   }
   ```
   - `userImage` (file): any image
2. Login as customer to get a customer cookie
3. Try admin-only endpoint again → expect 403 Forbidden

### 7) Optional: inspect JWT

Paste the cookie value at `https://jwt.io` and verify the `role` claim (e.g., `ADMIN`).

### Notes

- Cookies must be sent on each protected request. Postman usually handles this automatically after login.
- Admin-only routes include: `/api/v1/admin/**`, some `/api/v1/books/**` updates, orders management, etc.
