import java.io.*;
import java.util.*;

public class SongCatalog {
    private static final String FILE_NAME ="songs.bin";
    private final UndoRedoManger history;

    public SongCatalog(UndoRedoManger undoRedoManger) {
        this.history = undoRedoManger;
    }

    @SuppressWarnings("unchecked")
    private List<Song> getAllSongs(){
        File file = new File(FILE_NAME);
        if(!file.exists()){
            return new ArrayList<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            return (List<Song>) ois.readObject();
        }
        catch(IOException|ClassNotFoundException e){
            return new ArrayList<>();
        }
    }

    private void saveAll(List<Song> songs){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))){
            oos.writeObject(songs);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void showSongs() {
        List<Song> songs = getAllSongs();

        IO.println("╔════════════════ YOUR CATALOG ═══════════════╗");

        if (songs.isEmpty()) {
            IO.println("║ There are no songs in the catalog.           ║");
            IO.println("╚══════════════════════════════════════════════╝");
            return;
        }

        for (int i = 0; i < songs.size(); i++) {
            IO.println("  " + (i + 1) + ". " + songs.get(i));
        }

        IO.println("╚══════════════════════════════════════════════╝");
    }

    public boolean addSongs(Song song){
        List<Song> songs = getAllSongs();
        if(songs.stream().anyMatch(s->s.getId().equals(song.getId()))){
            return false;
        }
        history.run(new AddCommand(song),songs);
        saveAll(songs);
        return true;
    }

    public List<Song> searchSongs(String keyword){
        List<Song> songs = getAllSongs();
        if(keyword==null || keyword.isBlank()){
            return songs;
        }
        //fush buffer
        String normalizedKeyword = keyword.toLowerCase().trim();
        return  songs.stream()
                .filter(s-> s.getTitle().toLowerCase()
                        .contains(normalizedKeyword)
                        || s.getArtist().toLowerCase()
                        .contains(normalizedKeyword))
                        .toList();

    }



    public boolean removeSongs(Song song){
        List<Song> songs = getAllSongs();
        Optional<Song> target = songs.stream()
                .filter(s -> s.getId().equals(song.getId()))
                .findFirst();
        if(target.isEmpty()){
            return  false;
        }
        history.run(new RemoveCommand(target.get()),songs);
        saveAll(songs);
        return true;
    }

    public void sortByTitle(boolean descending) {
        List<Song> songs = getAllSongs();
        if(songs.isEmpty()){IO.println("""
                ╔════════════════ SORT BY TITLE ═══════════════╗
                ║ There are no songs in the catalog.          ║
                ╚══════════════════════════════════════════════╝
                """);}
        Comparator<Song> comparator =
                Comparator.comparing(Song::getTitle, String.CASE_INSENSITIVE_ORDER);

        songs.sort(descending ? comparator.reversed() : comparator);
        saveAll(songs);
    }

    public void sortByArtist(boolean descending) {
        List<Song> songs = getAllSongs();
        if(songs.isEmpty()){IO.println("""
                ╔════════════════ SORT BY TITLE ═══════════════╗
                ║ There are no songs in the catalog.          ║
                ╚══════════════════════════════════════════════╝
                """);}
        Comparator<Song> comparator =
                Comparator.comparing(Song::getArtist, String.CASE_INSENSITIVE_ORDER);

        songs.sort(descending ? comparator.reversed() : comparator);
        saveAll(songs);
    }

    public void sortByRate(boolean descending) {
        List<Song> songs = getAllSongs();
        if(songs.isEmpty()){IO.println("""
                ╔════════════════ SORT BY TITLE ═══════════════╗
                ║ There are no songs in the catalog.          ║
                ╚══════════════════════════════════════════════╝
                """);}
        Comparator<Song> comparator = Comparator.comparingInt(Song::getRate);
        songs.sort(descending ? comparator.reversed() : comparator);
        saveAll(songs);
    }

    public void mergeCatalogs(File newCatalog) {
        if (!newCatalog.exists()) {
            IO.println("""
                ╔════════════════ IMPORT SONGS ═══════════════╗
                ║ Catalog file does not exist.               ║
                ╚══════════════════════════════════════════════╝
                """);
            return;
        }

        List<Song> songs = getAllSongs();
        List<Song> newSongs = new ArrayList<>();
        List<Command> subCommands = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(newCatalog))) {
            newSongs = (List<Song>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            IO.println("""
                ╔════════════════ IMPORT SONGS ═══════════════╗
                ║ Merge failed: could not read the catalog.   ║
                ╚═════════════════════════════════════════════╝
                """);
            IO.println("Reason: " + e.getMessage());
            return;
        }

        for (Song newSong : newSongs) {
            if (songs.stream().noneMatch(s -> s.getId().equals(newSong.getId()))) {
                subCommands.add(new AddCommand(newSong));
            }
        }

        if (!subCommands.isEmpty()) {
            history.run(new MergeCommand(subCommands), songs);
            saveAll(songs);
        }

        IO.println("╔════════════════ IMPORT COMPLETE ═══════════════╗");
        IO.println("  Added: " + subCommands.size());
        IO.println("  Already present: " + (newSongs.size() - subCommands.size()));
        IO.println("╚════════════════════════════════════════════════╝");
    }

    public void undo() {
        List<Song> songs = getAllSongs();

        if (history.undo(songs)) {
            saveAll(songs);
            IO.println("""
                ╔════════════════ UNDO ════════════════╗
                ║ Last action was undone.              ║
                ╚══════════════════════════════════════╝
                """);
        } else {
            IO.println("""
                ╔════════════════ UNDO ════════════════╗
                ║ Nothing to undo.                     ║
                ╚══════════════════════════════════════╝
                """);
        }
    }

    public void redo() {
        List<Song> songs = getAllSongs();

        if (history.redo(songs)) {
            saveAll(songs);
            IO.println("""
                ╔════════════════ REDO ════════════════╗
                ║ Last undone action was restored.     ║
                ╚══════════════════════════════════════╝
                """);
        } else {
            IO.println("""
                ╔════════════════ REDO ════════════════╗
                ║ Nothing to redo.                     ║
                ╚══════════════════════════════════════╝
                """);
        }
    }
    public void exportCatalogs(String fileName) {
        File importsFolder = new File("imports");

        if (!importsFolder.exists() || !importsFolder.isDirectory()) {
            boolean created = importsFolder.mkdir();
            if (!created) {
                IO.println("""
                    ╔════════════════ EXPORT SONGS ═══════════════╗
                    ║ Error: could not create the imports folder. ║
                    ╚═════════════════════════════════════════════╝
                    """);
                return;
            }
        }

        if (!fileName.endsWith(".bin"))
        {
            fileName += ".bin";
        }

        File exportsFile = new File(importsFolder, fileName);
        List<Song> songs = getAllSongs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(exportsFile))) {
            oos.writeObject(songs);
            IO.println("""
                ╔════════════════ EXPORT COMPLETE ═══════════════╗
                ╚════════════════════════════════════════════════╝
                """);
            IO.println("Saved to: " + exportsFile.getAbsolutePath());
        } catch (IOException e) {
            IO.println("""
                ╔════════════════ EXPORT FAILED ═════════════════╗
                ╚════════════════════════════════════════════════╝
                """);
            IO.println("Reason: " + e.getMessage());
        }
    }

}
