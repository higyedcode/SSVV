package integration;

import org.example.domain.Student;
import org.example.domain.Tema;
import org.example.repository.NotaXMLRepo;
import org.example.repository.StudentXMLRepo;
import org.example.repository.TemaXMLRepo;
import org.example.service.Service;
import org.example.validation.NotaValidator;
import org.example.validation.StudentValidator;
import org.example.validation.TemaValidator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("IntegrationTests")
class AddAssignmentIntegrationTest {

    @Test
    void addAssignmentAfterAddingStudent() {
        Student student = new Student("2001", "Jane Smith", 932, "jane@example.com");
        Tema tema = new Tema("3001", "Assignment 1", 10, 8);

        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();

        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);

        Service service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);

        service.addStudent(student);
        Tema result = service.addTema(tema);

        assertEquals("3001", result.getID(), "The assignment should be added successfully with ID 3001");
    }
}
