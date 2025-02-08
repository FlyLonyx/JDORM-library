# 🚀 ORM Library for Java

![Java](https://img.shields.io/badge/Java-17%2B-blue?logo=java)
![License](https://img.shields.io/badge/License-MIT-green)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen)
![Version](https://img.shields.io/badge/Version-1.0.0-orange)

**ORM Library for Java** is a lightweight, efficient, and developer-friendly Object-Relational Mapping (ORM) tool designed to simplify mysql database interactions in Java applications. It allows you to map Java objects to database tables seamlessly, reducing boilerplate code and improving productivity.

---

## ✨ Features

- **📦 Entity Mapping**: Automatically map Java classes to database tables.
- **🛠️ CRUD Operations**: Perform Create, Read, Update, and Delete operations with ease.
- **🔍 Query Builder**: Build complex SQL queries using a fluent and intuitive API.
- **💾 Transactions**: Manage database transactions effortlessly.
- **⚡ Lightweight**: Minimal dependencies and optimized for performance.
- **🔒 Connection Pooling**: Built-in support for efficient database connections.

---

## 🛠️ Installation

### Library

Install the library with the release buttons.

### Initialisation

Add the following code to your main class with your credentials :

```java
Connection.configure("jdbc:mysql://", "localhost", "my_database", "my_user", "my_password");
java.sql.Connection connection = Connection.getConnection();
Schema schema = new Schema(connection);
MigrationManager.initialize();
```

---

## 🤝 Contributing
Contributions are welcome! Please read the [Contributing Guidelines](https://github.com) to get started.

## 📜 License
This project is licensed under the **MIT License**. See the [LICENSE](https://github.com) file for details.

## 💡 Support
If you find this project helpful, consider giving it a ⭐️ on GitHub! For any issues or feature requests, please open an  [issue](https://github.com).
