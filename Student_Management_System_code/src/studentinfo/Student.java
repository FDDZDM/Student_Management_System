package studentinfo;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String studentId;
    private String className;
    private String address;

    public Student(String name, String studentId, String className, String address) {
        this.name = name;
        this.studentId = studentId;
        this.className = className;
        this.address = address;
    }

    // Getters
    public String getName() { return name; }
    public String getStudentId() { return studentId; }
    public String getClassName() { return className; }
    public String getAddress() { return address; }
}