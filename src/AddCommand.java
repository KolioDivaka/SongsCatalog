import java.util.List;

public class AddCommand implements Command {
    private final Song song;
    public AddCommand(Song song){
        this.song = song;
    }

    @Override
    public void execute(List<Song> catalog) {
        catalog.add(song);
    }
    @Override
    public void undo(List<Song> catalog) {
        catalog.removeIf(s->s.getId().equals(song.getId()));
    }
    @Override
    public String describe() {
        return "Add: " + song;
    }
}
