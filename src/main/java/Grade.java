import lombok.Data;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class Grade {
    private Student etudiant;
    private Exam examen;
    private Double valeurActuelle;
    private List<GradeHistory> historique;

    public Grade(Student etudiant, Exam examen, Double valeurInitiale, String motif) {
        this.etudiant = etudiant;
        this.examen = examen;
        this.valeurActuelle = valeurInitiale;
        this.historique = new ArrayList<>();
        this.historique.add(new GradeHistory(valeurInitiale, Instant.now(), motif));
    }

    public void modifierNote(Double nouvelleValeur, String motif) {
        this.valeurActuelle = nouvelleValeur;
        this.historique.add(new GradeHistory(nouvelleValeur, Instant.now(), motif));
    }

    public Double getValeurA(Instant instant) {
        if (historique == null || historique.isEmpty()) {
            return null;
        }

        Double valeur = null;
        Instant derniereDate = Instant.MIN;

        for (GradeHistory history : historique) {
            if (!history.getDateModification().isAfter(instant) &&
                    history.getDateModification().isAfter(derniereDate)) {
                valeur = history.getValeur();
                derniereDate = history.getDateModification();
            }
        }

        return valeur;
    }
}