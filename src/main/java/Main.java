import java.io.IOException;
import java.util.Scanner;

class WrongStudentName extends Exception { }
class InvalidStudentAgeException extends Exception { }
class InvalidDateException extends Exception { }

class Main {
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            try {
                int ex = menu();
                switch (ex) {
                    case 1: exercise1(); break;
                    case 2: exercise2(); break;
                    case 3: exercise3(); break;
                    case 4: exercise4(); break;
                    case 5: exercise5(); break;
                    case 6: exercise6(); break;
                    default: return;
                }
            } catch (IOException e) {
                System.out.println("Wystąpił błąd wejścia/wyjścia.");
            } catch (WrongStudentName e) {
                System.out.println("Błędne imię studenta!");
            } catch (InvalidStudentAgeException e) {
                System.out.println("Błędny wiek studenta!");
            } catch (InvalidDateException e) {
                System.out.println("Nieprawidłowy format daty! Użyj formatu DD-MM-YYYY, np. 05-09-2001.");
            }
        }
    }

    public static int menu() {
        System.out.println("Wciśnij:");
        System.out.println("1 - aby dodać studenta");
        System.out.println("2 - aby wypisać wszystkich studentów");
        System.out.println("3 - aby wyszukać studenta po imieniu");
        System.out.println("4 - aby zaktualizować dane studenta");
        System.out.println("5 - aby wyszukać studentów urodzonych w danym roku");
        System.out.println("6 - aby wyświetlić statystyki studentów");
        System.out.println("0 - aby wyjść z programu");
        return scan.nextInt();
    }

    public static String ReadName() throws WrongStudentName {
        scan.nextLine(); // Czyści bufor
        System.out.println("Podaj imię: ");
        String name = scan.nextLine();
        if (name.contains(" "))
            throw new WrongStudentName();

        return name;
    }

    public static void validateDate(String date) throws InvalidDateException {
        String[] parts = date.split("-");
        if (parts.length != 3) throw new InvalidDateException();

        String day = parts[0];
        String month = parts[1];
        String year = parts[2];

        if (day.length() != 2 || month.length() != 2 || year.length() != 4)
            throw new InvalidDateException();

        if (!day.matches("\\d{2}") || !month.matches("\\d{2}") || !year.matches("\\d{4}"))
            throw new InvalidDateException();
    }

    public static void exercise1() throws IOException, WrongStudentName, InvalidStudentAgeException, InvalidDateException {
        String name = ReadName();
        System.out.println("Podaj wiek: ");
        int age = scan.nextInt();
        if (age < 1 || age > 99) {
            throw new InvalidStudentAgeException();
        }
        scan.nextLine(); // Czyści bufor
        System.out.println("Podaj datę urodzenia (format DD-MM-YYYY):");
        String date = scan.nextLine();
        validateDate(date);
        (new Service()).addStudent(new Student(name, age, date));
    }

    public static void exercise2() throws IOException {
        var students = (new Service()).getStudents();
        for (Student current : students) {
            System.out.println(current.ToString());
        }
    }

    public static void exercise3() throws IOException {
        scan.nextLine();
        System.out.println("Podaj imię: ");
        String name = scan.nextLine();
        var wanted = (new Service()).findStudentByName(name);
        if (wanted == null)
            System.out.println("Nie znaleziono...");
        else {
            System.out.println("Znaleziono: ");
            System.out.println(wanted.ToString());
        }
    }

    public static void exercise4() throws IOException, InvalidDateException, InvalidStudentAgeException {
        scan.nextLine();
        System.out.println("Podaj imię studenta do aktualizacji: ");
        String name = scan.nextLine();
        Service service = new Service();
        Student s = service.findStudentByName(name);
        if (s == null) {
            System.out.println("Nie znaleziono studenta.");
            return;
        }

        System.out.println("Podaj nowy wiek: ");
        int age = scan.nextInt();
        if (age < 1 || age > 99) throw new InvalidStudentAgeException();

        scan.nextLine();
        System.out.println("Podaj nową datę urodzenia (DD-MM-YYYY): ");
        String date = scan.nextLine();
        validateDate(date);

        service.updateStudent(name, age, date);
        System.out.println("Zaktualizowano dane studenta.");
    }

    public static void exercise5() throws IOException {
        scan.nextLine();
        System.out.println("Podaj rok urodzenia (np. 2002): ");
        String year = scan.nextLine();
        var students = (new Service()).findStudentsByBirthYear(year);
        if (students.isEmpty()) {
            System.out.println("Nie znaleziono studentów urodzonych w " + year + ".");
        } else {
            System.out.println("Studenci urodzeni w " + year + ":");
            for (Student s : students) {
                System.out.println(s.ToString());
            }
        }
    }

    public static void exercise6() throws IOException {
        var stats = (new Service()).getStatistics();
        if (stats.count == 0) {
            System.out.println("Brak studentów.");
        } else {
            System.out.println("Liczba studentów: " + stats.count);
            System.out.println("Średni wiek: " + stats.averageAge);
            System.out.println("Najmłodszy student: " + stats.youngest.ToString());
            System.out.println("Najstarszy student: " + stats.oldest.ToString());
        }
    }
}