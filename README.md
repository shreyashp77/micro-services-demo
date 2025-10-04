## 🔧 Prerequisites

Ensure the following tools are installed on your local machine:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)  
- [Docker](https://docs.docker.com/desktop/setup/install/windows-install/)  
- [Apache Maven](https://maven.apache.org/download.cgi)  
- An IDE of your choice (e.g., IntelliJ IDEA, VS Code)

---

## 🚀 Project Setup Guide

Follow the steps below to set up the project in your local development environment:

### 1. Environment Configuration

- Rename the `.env.sample` file located in the root directory to `.env`.
- Add the required values such as database URLs, usernames, and passwords.

### 2. Microservice Configurations

- Repeat the same steps for the `.env.sample` files located in:
  - `user-service`
  - `product-service`
  - `auth-service`
  - `api-gateway`

> ⚠️ **Note:**  
The `.env` files in both `api-gateway` and `auth-service` require an additional environment variable:

- `JWT_SECRET` – a strong, base64-encoded secret key used for JWT authentication.

### 3. Generate a JWT Secret

- You can generate a secure, base64-encoded JWT secret from [this tool](https://generate.plus/en/base64).
- **Ensure the same `JWT_SECRET` value is used in both `api-gateway` and `auth-service`.**

---

### 4. Start the Application

Once all environment variables are configured, navigate to the root directory and run:

```bash
docker-compose up -d --build
```

---

## 🛠️ Troubleshooting

If you encounter issues while setting up or running the project, refer to the common problems and solutions below:

### 🔄 Docker Containers Not Starting

- **Symptom:** One or more containers keep restarting or fail to start.
- **Solution:**
  - Run `docker-compose logs -f` to view real-time logs.
  - Check if all required `.env` files are present and properly configured.
  - Ensure no other services (e.g., databases) are running on conflicting ports.

### 🔐 Invalid or Missing JWT_SECRET

- **Symptom:** Authentication fails with JWT-related errors.
- **Solution:**
  - Confirm that the `JWT_SECRET` environment variable is **present and identical** in both `api-gateway` and `auth-service`.
  - Regenerate the key using [this tool](https://generate.plus/en/base64) if needed.

### 🧱 Database Connection Issues

- **Symptom:** Services cannot connect to the database.
- **Solution:**
  - Verify that your database containers are running.
  - Double-check your `.env` files for correct database URLs, usernames, and passwords.
  - Ensure the ports in the `.env` and `docker-compose.yml` match.

### 🐘 Maven Build Fails

- **Symptom:** Build fails with errors related to missing dependencies or plugins.
- **Solution:**
  - Make sure you have **Apache Maven** installed and it's accessible via your system's PATH.
  - Run `mvn clean install` in each service directory to resolve dependencies.

### 🌐 Port Conflicts

- **Symptom:** Docker reports that a port is already in use.
- **Solution:**
  - Identify the conflicting process using `lsof -i :<port>` (Linux/macOS) or `netstat -aon | findstr :<port>` (Windows).
  - Stop the conflicting process or change the port in your `docker-compose.yml`.

---
