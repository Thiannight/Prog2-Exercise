import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {
    private Integer id;
    private String nom;

    @Builder.Default
    private Set<String> groupes = new HashSet<>();

    public void ajouterGroupe(String groupe) {
        this.groupes.add(groupe);
    }

    public void supprimerGroupe(String groupe) {
        this.groupes.remove(groupe);
    }

    public boolean contientGroupe(String groupe) {
        return this.groupes.contains(groupe);
    }
}