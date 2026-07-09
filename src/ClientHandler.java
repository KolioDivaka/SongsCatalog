import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ClientHandler {
    Scanner sc = new Scanner(System.in);
    private final UndoRedoManger undoRedoManger = new UndoRedoManger();
    private final SongCatalog service = new SongCatalog(undoRedoManger);

    public void info(){
        IO.println("Your Catalog:");
        service.showSongs();
    }


    public void handleAddingSong() {
        IO.println("╔════════════════ Add Song════════════════╗");
        IO.print("Title: ");
        String title = sc.nextLine().trim();

        IO.print("Artist: ");
        String artist = sc.nextLine().trim();

        int rating;
        while (true) {
            IO.print("Rating (1-5): ");
            try {
                rating = Integer.parseInt(sc.nextLine().trim());
                if (rating >= 1 && rating <= 5) {
                    break;
                }
                IO.println("Rating must be between 1 and 5.");
            } catch (NumberFormatException e) {
                IO.println("Please enter a valid number.");
            }
        }

        Song song = new Song(title, artist, rating);
        boolean added = service.addSongs(song);
        IO.println(added ? "✔ Song added." : "✘ Song already exists.");
    }
    public void handleRemovingSong() {
        IO.println("╔════════════════ Remove Song ════════════════╗");
        IO.print(" Search by title or artist: ");
        String keyword = sc.nextLine().trim();

        List<Song> matches = service.searchSongs(keyword);

        if (matches.isEmpty()) {
            IO.println("╠═════════════════════════════════════════════╣");
            IO.println("║ No matching songs found.                    ║");
            IO.println("╚═════════════════════════════════════════════╝");
            return;
        }

        IO.println("╠════════════════ Matching Songs ════════════╣");
        for (int i = 0; i < matches.size(); i++) {
            IO.println("║ " + (i + 1) + ". " + matches.get(i));
        }
        IO.println("╠═════════════════════════════════════════════╣");
        IO.print(" Choose a song to delete (0 to cancel): ");

        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            IO.println("║ Invalid number.                             ║");
            IO.println("╚═════════════════════════════════════════════╝");
            return;
        }

        if (choice == 0) {
            IO.println("║ Removal cancelled.                          ║");
            IO.println("╚═════════════════════════════════════════════╝");
            return;
        }

        if (choice < 1 || choice > matches.size()) {
            IO.println("║ Invalid choice.                             ║");
            IO.println("╚═════════════════════════════════════════════╝");
            return;
        }

        Song target = matches.get(choice - 1);
        boolean result = service.removeSongs(target);

        IO.println("╠═════════════════════════════════════════════╣");
        IO.println(result
                ? "║ Removed: " + target
                : "║ Failed to remove: " + target);
        IO.println("╚═════════════════════════════════════════════╝");
    }

    public void handleSearching() {
        IO.println("╔════════════════ Search Songs ═══════════════╗");
        IO.print(" Search by title or artist: ");
        String keyword = sc.nextLine().trim();

        List<Song> matches = service.searchSongs(keyword);

        if (matches.isEmpty()) {
            IO.println("╠═════════════════════════════════════════════╣");
            IO.println(" No songs found.");
            IO.println("╚═════════════════════════════════════════════╝");
            return;
        }

        IO.println("╠════════════════ Results ════════════════════╣");
        for (int i = 0; i < matches.size(); i++) {
            IO.println(" " + (i + 1) + ". " + matches.get(i));
        }
        IO.println("╚═════════════════════════════════════════════╝");
    }
    public void handleSorting() {
        IO.println("""
            ╔════════════════ SORT SONGS ════════════════╗
            ║ Choose what part to be sorted by:          ║
            ║                                            ║
            ║  (1) Artist                                ║
            ║  (2) Title                                 ║
            ║  (3) Rating                                ║
            ║  (0) Cancel                                ║
            ╚════════════════════════════════════════════╝
            """);

        IO.print("➜ Enter choice: ");
        int choice = sc.nextInt();
        if (choice < 1 || choice > 3) {
            IO.println("Invalid choice.");
            return;
        }

        IO.println("""
            ╔════════════════ SORT ORDER ════════════════╗
            ║ What order would you like to sort by:      ║
            ║                                            ║
            ║  (1) Asc                                   ║
            ║  (2) Desc                                  ║
            ╚════════════════════════════════════════════╝
            """);

        IO.print("➜ Enter order: ");
        int order = sc.nextInt();
        if (order < 1 || order > 2) {
            IO.println("Invalid order!!!");
            return;
        }

        boolean desc = order == 2;

        switch (choice) {
            case 1:
                service.sortByArtist(desc);
                service.showSongs();
                break;
            case 2:
                service.sortByTitle(desc);
                service.showSongs();
                break;
            case 3:
                service.sortByRate(desc);
                service.showSongs();
                break;
            default:
                IO.println("Invalid choice!!!");
                break;
        }
    }

    //Some empty reused code cuz I am not moving them
    public void handleUndo(){
        service.undo();
    }
    public void handleRedo(){
        service.redo();
    }

    public void handleMerge() {
        File importsFolder = new File("imports");
        if (!importsFolder.exists() || !importsFolder.isDirectory()) {
            IO.println("""
                ╔════════════════ IMPORT SONGS ═══════════════╗
                ║ Import folder does not exist or is invalid. ║
                ╚═════════════════════════════════════════════╝
                """);
            return;
        }

        File[] files = importsFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".bin"));

        if (files == null || files.length == 0) {
            IO.println("""
                ╔════════════════ IMPORT SONGS ═══════════════╗
                ║ No import files found.                      ║
                ╚═════════════════════════════════════════════╝
                """);
            return;
        }

        Arrays.sort(files, Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));

        IO.println("╔════════════════ IMPORT SONGS ═══════════════╗");
        IO.println("║ Choose a catalog file to import:            ║");
        IO.println("║                                             ║");

        for (int i = 0; i < files.length; i++) {
            IO.println("║  " + (i + 1) + ". " + files[i].getName());
        }

        IO.println("║                                              ║");
        IO.println("║  0. Cancel                                   ║");
        IO.println("╚══════════════════════════════════════════════╝");
        IO.print("➜ Enter choice: ");

        int choice = sc.nextInt();
        if (choice == 0 || choice > files.length) {
            IO.println("Import canceled.");
            return;
        }

        File selectedFile = files[choice - 1];
        service.mergeCatalogs(selectedFile);
    }

    public void handleExport() {
        IO.println("""
            ╔════════════════ EXPORT SONGS ═══════════════╗
            ║ Enter a name for the export file:           ║
            ╚═════════════════════════════════════════════╝
            """);
        IO.print("➜ File name: ");
        String fileName = IO.readln();
        service.exportCatalogs(fileName);
    }
}
