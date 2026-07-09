import java.io.Serializable;
import java.util.List;

public interface Command extends Serializable {
    void execute(List<Song> catalog);
    void undo (List<Song> catalog);
    String describe();

}
