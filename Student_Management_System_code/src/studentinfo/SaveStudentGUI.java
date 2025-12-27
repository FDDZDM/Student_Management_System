package studentinfo;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class SaveStudentGUI extends JFrame {
    private JTextField nameField = new JTextField(20);
    private JTextField idField = new JTextField(20);
    private JTextField classField = new JTextField(20);
    private JTextField addressField = new JTextField(20);

    public SaveStudentGUI() {
        super("学生信息录入");
        setLayout(new GridLayout(5, 2));
        add(new JLabel("姓名:"));
        add(nameField);
        add(new JLabel("学号:"));
        add(idField);
        add(new JLabel("班级:"));
        add(classField);
        add(new JLabel("地址:"));
        add(addressField);

        JButton saveBtn = new JButton("保存");
        saveBtn.addActionListener(e -> saveToFile());
        add(saveBtn);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Student_Info.txt", true))) {
            writer.write("Name: " + nameField.getText() + "\n");
            writer.write("Student ID: " + idField.getText() + "\n");
            writer.write("Class: " + classField.getText() + "\n");
            writer.write("Address: " + addressField.getText() + "\n\n");
            JOptionPane.showMessageDialog(this, "保存成功!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "保存失败: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new SaveStudentGUI();
    }
}