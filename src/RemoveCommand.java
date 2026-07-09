import java.util.List;

public class RemoveCommand implements Command {
    private final Song song;
    public RemoveCommand(Song song) {
        this.song = song;
    }

    @Override
    public void execute(List<Song> catalog){
        catalog.removeIf(s-> s.getId().equals(song.getId()));
    }
    @Override
    public void undo(List<Song> catalog){
        catalog.add(song);
    }
    @Override
    public String describe() {
        return "Remove: " + song;
    }
}
