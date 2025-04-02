import org.example.domain.Student;
import org.example.repository.NotaXMLRepo;
import org.example.repository.StudentXMLRepo;
import org.example.repository.TemaXMLRepo;
import org.example.service.Service;
import org.example.validation.NotaValidator;
import org.example.validation.StudentValidator;
import org.example.validation.TemaValidator;
import org.example.validation.ValidationException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;

public class StudentTest {

    @Test
    void addStudentWithEmptyId() {
        Student student = new Student("", "John", 22, "john@example.com");
        Service service = getServiceInstance();
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudentWithNullId() {
        Student student = new Student(null, "John", 22, "john@example.com");
        Service service = getServiceInstance();
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudentWithEmptyName() {
        Student student = new Student("1001", "", 22, "john@example.com");
        Service service = getServiceInstance();
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudentWithNullName() {
        Student student = new Student("1001", null, 22, "john@example.com");
        Service service = getServiceInstance();
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudentWithNegativeGroup() {
        Student student = new Student("1001", "John", -1, "john@example.com");
        Service service = getServiceInstance();
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudentWithEmptyEmail() {
        Student student = new Student("1001", "John", 22, "");
        Service service = getServiceInstance();
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudentWithNullEmail() {
        Student student = new Student("1001", "John", 22, null);
        Service service = getServiceInstance();
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    private Service getServiceInstance() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);

        return new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }
}