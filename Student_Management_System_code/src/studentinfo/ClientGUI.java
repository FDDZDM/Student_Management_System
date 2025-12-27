package studentinfo;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ClientGUI extends JFrame {
    private JTextField idField = new JTextField(15);
    private JTextArea resultArea = new JTextArea(10, 30);

    public ClientGUI() {
        super("学生信息查询客户端");
        JPanel panel = new JPanel();
        panel.add(new JLabel("学号:"));
        panel.add(idField);

        JButton queryBtn = new JButton("查询");
        queryBtn.addActionListener(e -> queryStudent());

        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(queryBtn, BorderLayout.SOUTH);

        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void queryStudent() {
        new Thread(() -> {
            try (Socket socket = new Socket("localhost", 12345);
                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                 DataInputStream dis = new DataInputStream(socket.getInputStream())) {

                socket.setSoTimeout(5000);
                dos.writeUTF(idField.getText());
                dos.flush();

                int length = dis.readInt();
                byte[] buffer = new byte[length];
                dis.readFully(buffer);
                String response = new String(buffer, StandardCharsets.UTF_8);

                SwingUtilities.invokeLater(() -> {
                    resultArea.setText(formatResponse(response));
                    saveResponse(response);
                });

            } catch (IOException ex) {
                SwingUtilities.invokeLater(() ->
                        resultArea.setText("查询失败: " + ex.getMessage()));
            }
        }).start();
    }

    private String formatResponse(String response) {
        String[] parts = response.split("\\|");
        if (parts[0].equals("NOT_FOUND")) {
            return "学生不存在\n请求时间: " + parts[1];
        }
        return String.format("姓名: %s\n学号: %s\n班级: %s\n地址: %s\n请求时间: %s",
                parts[0], parts[1], parts[2], parts[3], parts[4]);
    }

    private synchronized void saveResponse(String response) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("Server_Response.txt", true))) {
            writer.write(response + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ClientGUI();
    }
}