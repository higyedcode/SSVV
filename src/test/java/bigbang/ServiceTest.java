package bigbang;

import org.example.domain.Nota;
import org.example.domain.Student;
import org.example.domain.Tema;
import org.example.repository.NotaXMLRepo;
import org.example.repository.StudentXMLRepo;
import org.example.repository.TemaXMLRepo;
import org.example.service.Service;
import org.example.validation.NotaValidator;
import org.example.validation.StudentValidator;
import org.example.validation.TemaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    private Service service;

    @BeforeEach
    public void setUp() {
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";

        StudentXMLRepo studentRepo = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaRepo = new TemaXMLRepo(filenameTema);
        NotaXMLRepo notaRepo = new NotaXMLRepo(filenameNota);

        service = new Service(
                studentRepo,
                new StudentValidator(),
                temaRepo,
                new TemaValidator(),
                notaRepo,
                new NotaValidator(studentRepo, temaRepo)
        );
    }

    @Test
    public void testAddStudent_ExistingStudent() {
        Student student = new Student("1", "Ana", 931, "ana@email.com");
        assertNotNull(service.addStudent(student), "Student should be added successfully.");
        assertNotNull(service.findStudent("1"), "Student should exist in repository.");
    }

    @Test
    public void testAddTema_ExistingTema() {
        Tema tema = new Tema("1", "Descriere", 10, 8);
        assertNotNull(service.addTema(tema), "Tema should be added successfully.");
        assertNotNull(service.findTema("1"), "Tema should exist in repository.");
    }

    @Test
    public void testAddNota() {
        Student student = new Student("2", "Dan", 932, "dan@email.com");
        Tema tema = new Tema("2", "Lab", 10, 8);
        service.addStudent(student);
        service.addTema(tema);

        Nota nota = new Nota("1", "2", "2", 9.5, LocalDate.now());
        double result = service.addNota(nota, "ok");

        assertEquals(9.5, result, 0.001, "Grade should be saved and returned correctly.");
        assertNotNull(service.findNota("1"), "Nota should be found.");
    }

    @Test
    public void testBigBangIntegration_addStudentTemaNota() {
        Student student = new Student("3", "Maria", 933, "maria@email.com");
        Tema tema = new Tema("3", "Proiect", 10, 8);
        Nota nota = new Nota("3", "3", "3", 10.0, LocalDate.now());

        assertNotNull(service.addStudent(student));
        assertNotNull(service.addTema(tema));
        double result = service.addNota(nota, "fb");

        assertEquals(10.0, result, 0.001);
        assertNotNull(service.findNota("3"));
    }
}
