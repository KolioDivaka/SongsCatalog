import java.util.List;

public class MergeCommand implements Command {
    private final List<Command> subCommands;
    public MergeCommand(List<Command> subCommands){
        this.subCommands = subCommands;
    }
    @Override
    public void execute(List<Song> catalog) {
        for (Command command : subCommands){
            command.execute(catalog);
        }
    }
    @Override
    public void undo(List<Song> catalog) {
        for (int i = subCommands.size() - 1; i >= 0; i--){
            subCommands.get(i).undo(catalog);
        }
    }
    public String describe() {
        return "Merge: " + subCommands.size()+ " songs";
    }
}
