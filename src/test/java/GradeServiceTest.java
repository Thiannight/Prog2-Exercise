import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GradeServiceTest {

    private Student student;
    private Course course;
    private GradeService service;

    @BeforeEach
    public void setUp() {
        Tutor tutor = Tutor.builder()
                .id(1)
                .nom("Raniriharinosy")
                .prenom("Mami")
                .dateNaissance(LocalDate.of(1965, 8, 11))
                .email("mami@gmail.com")
                .telephone("03400")
                .lienAvecEtudiant("Père")
                .build();

        student = Student.builder()
                .id(1)
                .nom("Raniriharinosy")
                .prenom("Thiann")
                .dateNaissance(LocalDate.of(2007, 5, 16))
                .email("hei.thiann.7@gmail.com")
                .telephone("03700")
                .groupe("G1")
                .tuteur(tutor)
                .build();

        Teacher teacher = Teacher.builder()
                .id(2)
                .nom("Ramarozaka")
                .prenom("Toky")
                .dateNaissance(LocalDate.of(1980, 2, 2))
                .email("tokyramarozaka@gmail.com")
                .telephone("03800")
                .specialite("Java")
                .build();

        course = Course.builder()
                .id(1)
                .label("PROG1")
                .credits(5)
                .enseignant(teacher)
                .build();

        List<Grade> grades = new ArrayList<>();
        service = new GradeService(grades);
    }

    @Test
    public void testGetExamGrade() {
        Exam exam = Exam.builder()
                .id(1)
                .titre("Examen 1")
                .cours(course)
                .dateHeure(LocalDateTime.now())
                .coefficient(2)
                .build();

        Grade grade = new Grade(student, exam, 12.0, "Note initiale");
        service.ajouterNote(grade);

        double result = service.getExamGrade(exam, student, Instant.now());
        assertEquals(12.0, result);
    }

    @Test
    public void testGetCourseGrade() {
        Exam exam1 = Exam.builder()
                .id(1)
                .titre("Examen 1")
                .cours(course)
                .dateHeure(LocalDateTime.now())
                .coefficient(2)
                .build();

        Exam exam2 = Exam.builder()
                .id(2)
                .titre("Examen 2")
                .cours(course)
                .dateHeure(LocalDateTime.now())
                .coefficient(3)
                .build();

        Grade grade1 = new Grade(student, exam1, 10.0, "Note initiale");
        Grade grade2 = new Grade(student, exam2, 15.0, "Note initiale");

        service.ajouterNote(grade1);
        service.ajouterNote(grade2);

        double result = service.getCourseGrade(course, student, Instant.now());
        assertEquals(13.0, result);
    }

    @Test
    public void testGetExamGradeWithHistory() throws InterruptedException {
        Exam exam = Exam.builder()
                .id(1)
                .titre("Examen 1")
                .cours(course)
                .dateHeure(LocalDateTime.now())
                .coefficient(2)
                .build();

        Grade grade = new Grade(student, exam, 12.0, "Note initiale");
        service.ajouterNote(grade);

        Thread.sleep(100);
        Instant avantModification = Instant.now();

        Thread.sleep(100);

        service.modifierNote(student, exam, 14.0, "Réévaluation");

        Thread.sleep(100);
        Instant apresModification = Instant.now();

        double resultAvant = service.getExamGrade(exam, student, avantModification);
        assertEquals(12.0, resultAvant);

        double resultApres = service.getExamGrade(exam, student, apresModification);
        assertEquals(14.0, resultApres);
    }

    @Test
    public void testGetCourseGradeWithDifferentCoefficients() {
        Exam exam1 = Exam.builder()
                .id(1)
                .titre("Examen 1")
                .cours(course)
                .dateHeure(LocalDateTime.now())
                .coefficient(1)
                .build();

        Exam exam2 = Exam.builder()
                .id(2)
                .titre("Examen 2")
                .cours(course)
                .dateHeure(LocalDateTime.now())
                .coefficient(4)
                .build();

        Grade grade1 = new Grade(student, exam1, 10.0, "Note initiale");
        Grade grade2 = new Grade(student, exam2, 20.0, "Note initiale");

        service.ajouterNote(grade1);
        service.ajouterNote(grade2);

        double result = service.getCourseGrade(course, student, Instant.now());
        assertEquals(18.0, result);
    }

    @Test
    public void testPromotion() {
        Promotion promotion = Promotion.builder()
                .id(1)
                .nom("Promotion 2024")
                .build();

        promotion.ajouterGroupe("G1");
        promotion.ajouterGroupe("G2");
        promotion.ajouterGroupe("G3");

        assertEquals(3, promotion.getGroupes().size());
        assertTrue(promotion.contientGroupe("G1"));

        promotion.supprimerGroupe("G3");
        assertEquals(2, promotion.getGroupes().size());
        assertFalse(promotion.contientGroupe("G3"));
    }

    @Test
    public void testPromotionWithInitialGroups() {
        Set<String> groupesInitiaux = new HashSet<>();
        groupesInitiaux.add("G1");
        groupesInitiaux.add("G2");

        Promotion promotion = Promotion.builder()
                .id(1)
                .nom("Promotion 2024")
                .groupes(groupesInitiaux)
                .build();

        assertEquals(2, promotion.getGroupes().size());
        assertTrue(promotion.contientGroupe("G1"));
        assertTrue(promotion.contientGroupe("G2"));
    }

    @Test
    public void testPromotionEmpty() {
        Promotion promotion = new Promotion();
        promotion.setId(1);
        promotion.setNom("Promotion Vide");

        assertEquals(0, promotion.getGroupes().size());
        assertFalse(promotion.contientGroupe("G1"));

        promotion.ajouterGroupe("NouveauGroupe");
        assertEquals(1, promotion.getGroupes().size());
        assertTrue(promotion.contientGroupe("NouveauGroupe"));
    }

    @Test
    public void testPromotionWithoutBuilder() {
        Promotion promotion = new Promotion();
        promotion.setId(1);
        promotion.setNom("Promotion 2024");

        promotion.ajouterGroupe("G1");
        promotion.ajouterGroupe("G2");
        promotion.ajouterGroupe("G3");

        assertEquals(3, promotion.getGroupes().size());
        assertTrue(promotion.contientGroupe("G1"));

        promotion.supprimerGroupe("G3");
        assertEquals(2, promotion.getGroupes().size());
        assertFalse(promotion.contientGroupe("G3"));
    }

    @Test
    public void testGetExamGradeNoGrade() {
        Exam exam = Exam.builder()
                .id(1)
                .titre("Examen 1")
                .cours(course)
                .dateHeure(LocalDateTime.now())
                .coefficient(2)
                .build();

        double result = service.getExamGrade(exam, student, Instant.now());
        assertEquals(0.0, result);
    }

    @Test
    public void testGetCourseGradeNoExams() {
        double result = service.getCourseGrade(course, student, Instant.now());
        assertEquals(0.0, result);
    }
}