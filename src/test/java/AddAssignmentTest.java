import org.example.domain.Tema;
import org.example.repository.NotaXMLRepo;
import org.example.repository.StudentXMLRepo;
import org.example.repository.TemaXMLRepo;
import org.example.service.Service;
import org.example.validation.NotaValidator;
import org.example.validation.StudentValidator;
import org.example.validation.TemaValidator;
import org.example.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddAssignmentTest {
    private Service service;
    @BeforeEach
    void setUp() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);

        this.service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @Test
    void addTema_ValidTema_ShouldAddSuccessfully() {
        Tema tema = new Tema("1", "Descriere", 5, 2);

        assertEquals(service.addTema(tema), tema);
    }

    @Test
    void addTema_ExistingTema_ShouldReturnExistingTema() {
        Tema tema = new Tema("1", "Descriere", 5, 2);
        assertEquals(tema, service.addTema(tema));
    }

    @Test
    void addTema_InvalidId_ShouldThrowValidationException() {
        Tema tema = new Tema("", "Descriere", 5, 2);

        ValidationException exception = assertThrows(ValidationException.class, () -> service.addTema(tema));
        assertEquals("Numar tema invalid!", exception.getMessage());
    }

    @Test
    void addTema_InvalidDescriere_ShouldThrowValidationException() {
        Tema tema = new Tema("1", "", 5, 2);

        ValidationException exception = assertThrows(ValidationException.class, () -> service.addTema(tema));
        assertEquals("Descriere invalida!", exception.getMessage());
    }

    @Test
    void addTema_InvalidDeadline_ShouldThrowValidationException() {
        Tema tema = new Tema("1", "Descriere", 15, 2);

        ValidationException exception = assertThrows(ValidationException.class, () -> service.addTema(tema));
        assertEquals("Deadlineul trebuie sa fie intre 1-14.", exception.getMessage());
    }

    @Test
    void addTema_InvalidPrimire_ShouldThrowValidationException() {
        Tema tema = new Tema("1", "Descriere", 5, 15);

        ValidationException exception = assertThrows(ValidationException.class, () -> service.addTema(tema));
        assertEquals("Saptamana primirii trebuie sa fie intre 1-14.", exception.getMessage());
    }
}
