import java.util.ArrayList;
import java.util.Scanner;

public class StudentGradeTracker {

    static class Student {
        String name;
        ArrayList<Double> grades;

        Student(String name) {
            this.name = name;
            this.grades = new ArrayList<>();
        }

        void addGrade(double grade) {
            grades.add(grade);
        }

        double getAverage() {
            if (grades.isEmpty()) return 0;
            double sum = 0;
            for (double grade : grades) {
                sum += grade;
            }
            return sum / grades.size();
        }

        double getHighest() {
            if (grades.isEmpty()) return 0;
            double highest = grades.get(0);
            for (double grade : grades) {
                if (grade > highest) highest = grade;
            }
            return highest;
        }

        double getLowest() {
            if (grades.isEmpty()) return 0;
            double lowest = grades.get(0);
            for (double grade : grades) {
                if (grade < lowest) lowest = grade;
            }
            return lowest;
        }

        void printReport() {
            System.out.println("Student: " + name);
            System.out.println("Grades: " + grades);
            System.out.printf("Average: %.2f%n", getAverage());
            System.out.printf("Highest: %.2f%n", getHighest());
            System.out.printf("Lowest: %.2f%n", getLowest());
            System.out.println("-----------------------------");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        System.out.println("=== Student Grade Tracker ===");
        boolean running = true;

        while (running) {
            System.out.println("\n1. Add Student");
            System.out.println("2. Add Grade");
            System.out.println("3. Display Summary");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter student name: ");
                    String name = scanner.nextLine();
                    students.add(new Student(name));
                    System.out.println("Student added.");
                    break;
                case 2:
                    if (students.isEmpty()) {
                        System.out.println("No students available. Add students first.");
                        break;
                    }
                    System.out.println("Select student:");
                    for (int i = 0; i < students.size(); i++) {
                        System.out.println((i + 1) + ". " + students.get(i).name);
                    }
                    int studentIndex = scanner.nextInt() - 1;
                    if (studentIndex < 0 || studentIndex >= students.size()) {
                        System.out.println("Invalid student.");
                        break;
                    }
                    System.out.print("Enter grade: ");
                    double grade = scanner.nextDouble();
                    students.get(studentIndex).addGrade(grade);
                    System.out.println("Grade added.");
                    break;
                case 3:
                    if (students.isEmpty()) {
                        System.out.println("No students to display.");
                        break;
                    }
                    System.out.println("\n=== Student Summary Report ===");
                    for (Student s : students) {
                        s.printReport();
                    }
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }
}
