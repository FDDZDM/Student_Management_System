# 学生信息管理系统 (Student Management System)

**简介**

这是一个基于 Java Swing 的简易学生信息管理与查询系统，包含三个图形界面程序：

- **ServerGUI**：启动服务器，监听本地端口并响应客户端查询；同时在启动时从 `Student_Info.txt` 加载学生数据。
- **ClientGUI**：客户端查询界面，通过学号向服务器发送请求并显示返回结果，同时将服务器响应追加到 `Server_Response.txt`。
- **SaveStudentGUI**：用于录入学生信息并将其追加保存到 `Student_Info.txt`。

✅ 适合用于教学演示或小型本地网络实验。

---

## 功能概览 🔧

- 本地 TCP 服务端，监听 **12345** 端口
- 支持按学号查询学生信息（姓名、学号、班级、地址、请求时间）
- 学生信息以文本方式保存到 `Student_Info.txt`，客户端响应保存到 `Server_Response.txt`
- 基于 Java Swing 的简单 GUI，易于运行与测试

---

## 文件说明 📁

- `src/studentinfo/ServerGUI.java` — 服务器程序，加载 `Student_Info.txt`、监听端口并处理客户端请求
- `src/studentinfo/ClientGUI.java` — 客户端查询界面，发送学号并显示响应，保存到 `Server_Response.txt`
- `src/studentinfo/SaveStudentGUI.java` — 学生信息录入界面，追加写入 `Student_Info.txt`
- `src/studentinfo/Student.java` — 学生实体类，支持序列化
- `Student_Info.txt` — 学生数据文本文件（按特定格式保存）
- `Server_Response.txt` — 客户端保存的服务器响应记录

---

## 数据格式 & 通信协议 💡

`Student_Info.txt` 中每个学生示例格式：

```
Name: 张三
Student ID: 2021001
Class: 计科一班
Address: 北京市

```

网络协议（简要）：

- 客户端用 `DataOutputStream.writeUTF()` 发送要查询的学号（UTF-8）
- 服务器返回：先发送响应字节长度（`int`），再发送 UTF-8 字节数组
- 响应格式（字符串，字段用 `|` 分隔）：
  - 找到：`姓名|学号|班级|地址|请求时间`
  - 未找到：`NOT_FOUND|请求时间`

---

## 编译与运行 🔧

需要 Java 8 及以上版本。

推荐命令行（在项目根目录）：

```bash
# 编译
javac -d out src/studentinfo/*.java

# 启动服务器（在单独终端）
java -cp out studentinfo.ServerGUI

# 启动查询客户端
java -cp out studentinfo.ClientGUI

# 启动录入界面（添加学生）
java -cp out studentinfo.SaveStudentGUI
```

或者使用 IDE（如 IntelliJ IDEA）：将 `src` 设为源代码根，分别运行每个类的 `main` 方法。

---

## 使用示例 ✅

1. 用 `SaveStudentGUI` 添加一条学生记录（会追加写入 `Student_Info.txt`）。
2. 启动 `ServerGUI`（会加载 `Student_Info.txt` 并开始监听端口 12345）。
3. 在 `ClientGUI` 输入学号并点击“查询”。
4. 若存在，将显示：姓名 / 学号 / 班级 / 地址 / 请求时间；同时响应追加到 `Server_Response.txt`。

示例服务器响应：

```
张三|2021001|计科一班|北京市|2025 12:34:56
```

未找到示例：

```
NOT_FOUND|2025 12:35:10
```

---

## 注意事项 / 故障排查 ⚠️

- 确保没有防火墙阻止本地端口 `12345`。
- 服务端和客户端需在相同主机或能互相访问的网络上运行。
- 代码中设置了 5 秒的 socket 超时（`setSoTimeout(5000)`），网络不稳定可能导致查询失败。
- 如果 `Student_Info.txt` 不存在或格式错误，服务器启动时会显示加载错误，但仍可在运行后通过 `SaveStudentGUI` 追加创建文件。

---

---

**作者**：自动生成 · 如需更改说明或添加内容，请告诉我要补充的点。
