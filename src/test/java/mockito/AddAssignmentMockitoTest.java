package mockito;

import org.example.domain.Tema;
import org.example.repository.TemaXMLRepo;
import org.example.service.Service;
import org.example.validation.NotaValidator;
import org.example.validation.StudentValidator;
import org.example.validation.TemaValidator;
import org.example.validation.ValidationException;
import org.example.repository.StudentXMLRepo;
import org.example.repository.NotaXMLRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AssignmentMockitoTest {

    @Mock
    private TemaXMLRepo temaRepository;

    @Mock
    private StudentXMLRepo studentRepository;

    @Mock
    private NotaXMLRepo notaRepository;

    @Mock
    private NotaValidator notaValidator;

    private TemaValidator temaValidator;
    private StudentValidator studentValidator;
    private Service service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        temaValidator = new TemaValidator(); // Using real validator for validation testing
        studentValidator = new StudentValidator();

        service = new Service(
                studentRepository,
                studentValidator,
                temaRepository,
                temaValidator,
                notaRepository,
                notaValidator
        );
    }

    @Test
    void testAddValidAssignment() {
        // Arrange
        Tema assignment = new Tema("1", "Test Assignment", 10, 5);
        when(temaRepository.save(assignment)).thenReturn(assignment);

        // Act
        Tema result = service.addTema(assignment);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getID());
        assertEquals("Test Assignment", result.getDescriere());
        assertEquals(10, result.getDeadline());
        assertEquals(5, result.getPrimire());

        // Verify the repository's save method was called once with the assignment
        verify(temaRepository, times(1)).save(assignment);
    }

    @Test
    void testAddAssignmentWithEmptyId() {
        // Arrange
        Tema assignment = new Tema("", "Test Assignment", 10, 5);

        // Act & Assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.addTema(assignment)
        );

        assertEquals("Numar tema invalid!", exception.getMessage());
        verify(temaRepository, never()).save(any()); // Verify save was never called
    }

    @Test
    void testAddAssignmentWithNullId() {
        // Arrange
        Tema assignment = new Tema(null, "Test Assignment", 10, 5);

        // Act & Assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.addTema(assignment)
        );

        assertEquals("Numar tema invalid!", exception.getMessage());
        verify(temaRepository, never()).save(any()); // Verify save was never called
    }

    @Test
    void testAddAssignmentWithEmptyDescription() {
        // Arrange
        Tema assignment = new Tema("1", "", 10, 5);

        // Act & Assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.addTema(assignment)
        );

        assertEquals("Descriere invalida!", exception.getMessage());
        verify(temaRepository, never()).save(any()); // Verify save was never called
    }

    @Test
    void testAddAssignmentWithInvalidDeadline() {
        // Too early deadline
        Tema assignment1 = new Tema("1", "Test Assignment", 0, 5);
        ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> service.addTema(assignment1)
        );
        assertEquals("Deadlineul trebuie sa fie intre 1-14.", exception1.getMessage());

        // Too late deadline
        Tema assignment2 = new Tema("1", "Test Assignment", 15, 5);
        ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> service.addTema(assignment2)
        );
        assertEquals("Deadlineul trebuie sa fie intre 1-14.", exception2.getMessage());

        verify(temaRepository, never()).save(any()); // Verify save was never called
    }

    @Test
    void testAddAssignmentWithInvalidReceiveWeek() {
        // Too early receive week
        Tema assignment1 = new Tema("1", "Test Assignment", 10, 0);
        ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> service.addTema(assignment1)
        );
        assertEquals("Saptamana primirii trebuie sa fie intre 1-14.", exception1.getMessage());

        // Too late receive week
        Tema assignment2 = new Tema("1", "Test Assignment", 10, 15);
        ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> service.addTema(assignment2)
        );
        assertEquals("Saptamana primirii trebuie sa fie intre 1-14.", exception2.getMessage());

        verify(temaRepository, never()).save(any()); // Verify save was never called
    }

    @Test
    void testAddAssignmentWithRepositoryError() {
        // Arrange
        Tema assignment = new Tema("1", "Test Assignment", 10, 5);
        when(temaRepository.save(assignment)).thenReturn(null); // Repository returns null (failure)

        // Act
        Tema result = service.addTema(assignment);

        // Assert
        assertNull(result, "Result should be null when repository fails to save");
        verify(temaRepository, times(1)).save(assignment);
    }
}