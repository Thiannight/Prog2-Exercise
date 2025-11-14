import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Course {
    private Integer id;
    private String label;
    private Integer credits;
    private Teacher enseignant;
}