package studentinfo;

import studentinfo.Student;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class ServerGUI extends JFrame {
    private JTextArea logArea = new JTextArea();
    private ServerSocket serverSocket;
    private Map<String, Student> studentMap = new ConcurrentHashMap<>();
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public ServerGUI() {
        super("学生信息服务器");
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        JButton startBtn = new JButton("启动服务器");
        startBtn.addActionListener(e -> startServer());
        add(startBtn, BorderLayout.SOUTH);

        loadStudents();
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadStudents() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Student_Info.txt"))) {
            String line;
            String[] currentStudent = new String[4];
            int index = 0;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Name: ")) {
                    currentStudent[0] = line.substring(6);
                } else if (line.startsWith("Student ID: ")) {
                    currentStudent[1] = line.substring(12);
                } else if (line.startsWith("Class: ")) {
                    currentStudent[2] = line.substring(7);
                } else if (line.startsWith("Address: ")) {
                    currentStudent[3] = line.substring(9);
                    studentMap.put(currentStudent[1],
                            new Student(currentStudent[0], currentStudent[1], currentStudent[2], currentStudent[3]));
                    Arrays.fill(currentStudent, null);
                    index = 0;
                }
            }
        } catch (IOException e) {
            log("加载学生数据错误: " + e.getMessage());
        }
    }

    private void startServer() {
        pool.execute(() -> {
            try {
                serverSocket = new ServerSocket(12345);
                log("服务器已启动，端口 12345");
                while (true) {
                    Socket socket = serverSocket.accept();
                    pool.execute(new ClientHandler(socket));
                }
            } catch (IOException e) {
                log("服务器异常: " + e.getMessage());
            }
        });
    }

    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (DataInputStream dis = new DataInputStream(socket.getInputStream());
                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

                socket.setSoTimeout(5000);
                String studentId = dis.readUTF();
                log("收到请求: " + studentId);

                Student student = studentMap.get(studentId);
                String time = new SimpleDateFormat("yyyy HH:mm:ss").format(new Date());

                String response;
                if (student != null) {
                    response = student.getName() + "|" + student.getStudentId() + "|"
                            + student.getClassName() + "|" + student.getAddress() + "|" + time;
                } else {
                    response = "NOT_FOUND|" + time;
                }

                byte[] data = response.getBytes("UTF-8");
                dos.writeInt(data.length);
                dos.write(data);
                dos.flush();

            } catch (IOException e) {
                log("客户端处理错误: " + e.getMessage());
            }
        }
    }

    private void log(String msg) {
        SwingUtilities.invokeLater(() -> logArea.append(msg + "\n"));
    }

    public static void main(String[] args) {
        new ServerGUI();
    }
}