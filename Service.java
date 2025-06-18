import java.io.*;
import java.util.ArrayList;

public class Service {

    public void addStudent(Student student) throws IOException {
        var f = new FileWriter("db.txt", true);
        var b = new BufferedWriter(f);
        b.append(student.ToString());
        b.newLine();
        b.close();
    }

    public ArrayList<Student> getStudents() throws IOException {
        var ret = new ArrayList<Student>();
        var f = new FileReader("db.txt");
        var reader = new BufferedReader(f);
        String line;
        while ((line = reader.readLine()) != null) {
            ret.add(Student.Parse(line));
        }
        reader.close();
        return ret;
    }

    public Student findStudentByName(String name) throws IOException {
        var students = this.getStudents();
        for (Student current : students) {
            if (current.GetName().equals(name))
                return current;
        }
        return null;
    }

    public void updateStudent(String name, int newAge, String newDate) throws IOException {
        var students = this.getStudents(); // teraz to ArrayList<Student>
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).GetName().equals(name)) {
                students.set(i, new Student(name, newAge, newDate));
                break;
            }
        }

        var writer = new BufferedWriter(new FileWriter("db.txt"));
        for (Student s : students) {
            writer.write(s.ToString());
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<Student> findStudentsByBirthYear(String year) throws IOException {
        var students = this.getStudents();
        var result = new ArrayList<Student>();
        for (Student s : students) {
            String[] parts = s.GetDate().split("-");
            if (parts.length == 3 && parts[2].equals(year)) {
                result.add(s);
            }
        }
        return result;
    }

    public static class StudentStatistics {
        public int count;
        public double averageAge;
        public Student youngest;
        public Student oldest;
    }

    public StudentStatistics getStatistics() throws IOException {
        var students = this.getStudents();
        var stats = new StudentStatistics();
        if (students.isEmpty()) return stats;

        int sum = 0;
        stats.youngest = students.get(0);
        stats.oldest = students.get(0);

        for (Student s : students) {
            int age = s.GetAge();
            sum += age;
            if (age < stats.youngest.GetAge()) stats.youngest = s;
            if (age > stats.oldest.GetAge()) stats.oldest = s;
        }

        stats.count = students.size();
        stats.averageAge = sum / (double) stats.count;
        return stats;
    }
}
