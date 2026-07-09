import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


public class UndoRedoManger {
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();
    private final String historyFile = "history.bin";

    public  UndoRedoManger() {

        load();
    }
    public void run(Command cmd, List<Song> catalog) {
        cmd.execute(catalog);
        undoStack.push(cmd);
        redoStack.clear();
        save();
    }

    public boolean undo(List<Song> catalog) {
        if(undoStack.isEmpty()) {return false;}
        Command cmd = undoStack.pop();
        cmd.undo(catalog);
        redoStack.push(cmd);
        save();
        return true;
    }
    public boolean redo(List<Song> catalog) {
        if(redoStack.isEmpty()) {return false;}
        Command cmd = redoStack.pop();
        cmd.execute(catalog);
        undoStack.push(cmd);
        save();
        return true;

    }

    public String peekUndo() {
        return undoStack.isEmpty() ? null : undoStack.peek().describe();
    }

    public String peekRedo() {
        return redoStack.isEmpty() ? null : redoStack.peek().describe();
    }

    @SuppressWarnings("uncheked")
    private void load() {
        File f = new File(historyFile);
        if(!f.exists()) {
            System.out.println("History file does not exist");
            return;
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            List<Command> savedUndo= (List<Command>) ois.readObject();
            List<Command> saveRedo = (List<Command>) ois.readObject();
            undoStack.addAll(savedUndo);
            redoStack.addAll(saveRedo);
        }
        catch(IOException | ClassNotFoundException e) {
            IO.println("History file could not be loaded");
        }
    }
    private void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(historyFile))) {
            out.writeObject(new ArrayList<>(undoStack));
            out.writeObject(new ArrayList<>(redoStack));
        } catch (IOException e) {
            IO.println("Could not save undo history: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
