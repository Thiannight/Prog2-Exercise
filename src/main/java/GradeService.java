import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class GradeService {
    private List<Grade> grades;

    public GradeService(List<Grade> grades) {
        this.grades = grades;
    }

    public double getExamGrade(Exam exam, Student student, Instant t) {
        return grades.stream()
                .filter(grade -> grade.getEtudiant().equals(student) && grade.getExamen().equals(exam))
                .findFirst()
                .map(grade -> {
                    Double valeur = grade.getValeurA(t);
                    return valeur != null ? valeur : 0.0;
                })
                .orElse(0.0);
    }

    public double getCourseGrade(Course course, Student student, Instant t) {
        List<Grade> gradesDuCours = grades.stream()
                .filter(grade -> grade.getEtudiant().equals(student) &&
                        grade.getExamen().getCours().equals(course))
                .toList();

        if (gradesDuCours.isEmpty()) {
            return 0.0;
        }

        double sommePonderee = 0.0;
        int sommeCoefficients = 0;

        for (Grade grade : gradesDuCours) {
            Double note = grade.getValeurA(t);
            if (note != null) {
                int coefficient = grade.getExamen().getCoefficient();
                sommePonderee += note * coefficient;
                sommeCoefficients += coefficient;
            }
        }

        return sommeCoefficients > 0 ? sommePonderee / sommeCoefficients : 0.0;
    }

    public void ajouterNote(Grade grade) {
        this.grades.add(grade);
    }

    public void modifierNote(Student student, Exam exam, Double nouvelleValeur, String motif) {
        grades.stream()
                .filter(grade -> grade.getEtudiant().equals(student) && grade.getExamen().equals(exam))
                .findFirst()
                .ifPresent(grade -> grade.modifierNote(nouvelleValeur, motif));
    }
}