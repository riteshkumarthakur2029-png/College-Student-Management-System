public class Student {

    private String studentId;
    private final String name;
    private String course;
    private double cgpa;

    public Student(String studentId, String name, String course, double cgpa) {
        this.studentId = studentId;
        this.name = name;
        this.course = course;
        this.cgpa = cgpa;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public double getCgpa() {
        return cgpa;
    }

    @Override
    public String toString() {
        return studentId + " | " + name + " | " + course + " | " + cgpa;
    }
}