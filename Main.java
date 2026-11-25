import java.util.*;
import java.io.*;

public class Main {

    static ArrayList<Student> students = new ArrayList<>();
    static HashMap<String, Student> studentMap = new HashMap<>();

    static Scanner sc = new Scanner(System.in);

    static int nextId = 101;

    static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {

        loadFromFile();

        int choice;

        do {
            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Sort by CGPA");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
                System.out.print("Enter your choice: ");
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addStudent();
                    break;

                case 2:
                    displayStudents();
                    break;

                case 3:
                    searchStudent();
                    break;

                case 4:
                    sortByCGPA();
                    break;

                case 5:
                    deleteStudent();
                    break;

                case 6:
                    saveToFile();
                    System.out.println("Records saved successfully.");
                    System.out.println("Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 6);

        sc.close();
    }

    // Add Student

    static void addStudent() {

        System.out.println("\n--- ADD STUDENT ---");

        System.out.print("Enter Student Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter Course: ");
        String course = sc.nextLine().trim();

        if (name.isEmpty() || course.isEmpty()
                || name.contains("|") || course.contains("|")) {
            System.out.println("Invalid student details.");
            return;
        }

        double cgpa;

        while (true) {

            System.out.print("Enter CGPA (0-10): ");

            if (sc.hasNextDouble()) {

                cgpa = sc.nextDouble();
                sc.nextLine();

                if (cgpa >= 0 && cgpa <= 10) {
                    break;
                }

            } else {
                sc.nextLine();
            }

            System.out.println("Invalid CGPA! Enter between 0 and 10.");
        }

        String id = "S" + nextId;
        nextId++;

        Student student = new Student(id, name, course, cgpa);

        students.add(student);
        studentMap.put(id, student);

        System.out.println("\nStudent added successfully!");
        System.out.println("Student ID: " + id);

        saveToFile();
    }

    // Display Students

    static void displayStudents() {

        System.out.println("\n--- STUDENT RECORDS ---");

        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        for (Student s : students) {

            System.out.println(
                s.getStudentId() + " | " +
                s.getName() + " | " +
                s.getCourse() + " | " +
                s.getCgpa()
            );
        }
    }

    // Search Student

    static void searchStudent() {

        System.out.println("\n1. Search by ID");
        System.out.println("2. Search by Name");

        System.out.print("Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {

            System.out.print("Enter Student ID: ");

            String id = sc.nextLine().trim().toUpperCase();

            searchById(id);

        } else if (choice == 2) {

            System.out.print("Enter Student Name: ");

            String name = sc.nextLine().trim();

            searchByName(name);

        } else {

            System.out.println("Invalid choice!");
        }
    }

    // Search by ID using HashMap

    static void searchById(String id) {

        Student student = studentMap.get(id);

        if (student != null) {

            System.out.println("Student Found!");
            System.out.println(student);

        } else {

            System.out.println("Student ID " + id + " not found.");
        }
    }

    // Search by Name using Linear Search

    static void searchByName(String name) {

        boolean found = false;

        for (Student s : students) {

            if (s.getName().equalsIgnoreCase(name)) {

                System.out.println("Student Found!");
                System.out.println(s);

                found = true;
            }
        }

        if (!found) {
            System.out.println("Student not found.");
        }
    }

    // Sort by CGPA using Comparator

    static void sortByCGPA() {

        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        Collections.sort(
            students,
            Comparator.comparingDouble(Student::getCgpa).reversed()
        );

        System.out.println("\n--- STUDENTS SORTED BY CGPA ---");

        for (int i = 0; i < students.size(); i++) {

            System.out.println(
                (i + 1) + ". " + students.get(i)
            );
        }
    }

    // Delete Student

    static void deleteStudent() {

        System.out.print("Enter Student ID: ");

        String id = sc.nextLine().trim().toUpperCase();

        Student student = studentMap.remove(id);

        if (student != null) {

            students.remove(student);

            saveToFile();

            System.out.println("Student " + id + " deleted successfully.");

        } else {

            System.out.println("Student not found.");
        }
    }

    // Save Records to File

    static void saveToFile() {

        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Student s : students) {

                writer.write(
                    s.getStudentId() + "|" +
                    s.getName() + "|" +
                    s.getCourse() + "|" +
                    s.getCgpa()
                );

                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println("Error saving records: " + e.getMessage());
        }
    }

    // Load Records from File

    static void loadFromFile() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                if (data.length != 4) {
                    continue;
                }

                try {

                    String id = data[0].trim().toUpperCase();
                    String name = data[1].trim();
                    String course = data[2].trim();
                    double cgpa = Double.parseDouble(data[3].trim());

                    if (id.isEmpty() || name.isEmpty()
                            || course.isEmpty()
                            || cgpa < 0 || cgpa > 10
                            || studentMap.containsKey(id)) {
                        continue;
                    }

                    Student student = new Student(
                        id, name, course, cgpa
                    );

                    students.add(student);
                    studentMap.put(id, student);

                    if (id.matches("S\\d+")) {

                        int number = Integer.parseInt(id.substring(1));

                        nextId = Math.max(nextId, number + 1);
                    }

                } catch (NumberFormatException e) {

                    System.out.println("Invalid record skipped.");
                }
            }

        } catch (IOException e) {

            System.out.println("Error loading records: " + e.getMessage());
        }
    }
}
