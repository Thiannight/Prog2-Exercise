import lombok.Data;
import lombok.AllArgsConstructor;
import java.time.Instant;

@Data
@AllArgsConstructor
public class GradeHistory {
    private Double valeur;
    private Instant dateModification;
    private String motif;
}