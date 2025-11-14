import lombok.Data;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Data
@SuperBuilder
public class Exam {
    private Integer id;
    private String titre;
    private Course cours;
    private LocalDateTime dateHeure;
    private Integer coefficient;
}