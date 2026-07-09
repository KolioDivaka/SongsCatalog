import java.io.File;
import java.util.*;

public class ClientHandler {
    Scanner sc = new Scanner(System.in);
    private final UndoRedoManger undoRedoManger = new UndoRedoManger();
    private final SongCatalog service = new SongCatalog(undoRedoManger);

    public void info(){
        IO.println("Your Catalog:");
        service.showSongs();
    }

    private static void flush(){
        System.out.flush();
    }


    public void handleAddingSong() {
        IO.println("╔════════════════ Add Song════════════════╗");

        String title = null, artist = null;
        while(true){
            IO.print("Title: ");
            title = IO.readln();
            flush();
            if(title.isEmpty()){
                IO.println("Adding Canceled!!");
                return;
            }
            if(title.length() > 50){
                IO.println("Title must be less than 50 characters!!");
            }else {
                break;
            }
        }



        while(true) {
            IO.print("Artist: ");
            artist = IO.readln();
            flush();
            if(artist.isEmpty()){
                IO.println("Adding Canceled!!");
                return;
            }
            if(artist.length() > 50){
                IO.println("Artist name must be less than 50 characters!!");
            }else {
                break;
            }

        }
        int rating;
        while (true) {
            IO.print("Rating (1-5): ");
            try {
                rating = Integer.parseInt(sc.nextLine().trim());
                flush();
                if (rating >= 1 && rating <= 5) {
                    break;
                }
                IO.println("Rating must be between 1 and 5.");
            } catch (NumberFormatException|InputMismatchException e) {
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
        flush();

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


        int choice;
        while (true){try {
            IO.println("╠═════════════════════════════════════════════╣");
            IO.print(" Choose a song to delete (0 to cancel): ");
            choice = Integer.parseInt(sc.nextLine().trim());
            flush();
            if (choice == 0) {
                IO.println("║ Removal cancelled.                          ║");
                IO.println("╚═════════════════════════════════════════════╝");
                return;
            }

            if (choice < 1 || choice > matches.size()) {
                IO.println("║ Invalid choice.                             ║");
                IO.println("╚═════════════════════════════════════════════╝");
            }
            else {
                break;
            }
            } catch (NumberFormatException | InputMismatchException e) {
                IO.println("║ Invalid number.                             ║");
                IO.println("╚═════════════════════════════════════════════╝");

            }

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
        flush();
        if(keyword.isEmpty()){
            IO.println("╠═════════════════════════════════════════════╣");
            IO.println(" Search Canceled!!!");
            IO.println("╚═════════════════════════════════════════════╝");
            return;
        }

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

        int choice;
        while (true) {
            try {
                 IO.print("➜ Enter choice: ");
                 choice = Integer.parseInt(sc.nextLine().trim());
                 flush();
                if (choice < 1 || choice > 3) {
                    IO.println("Invalid choice.");
                }
                else if (choice == 0) {
                    IO.println("Sorting Cancelled.");
                    return;
                }
                else {
                    break;
                }
            }catch (NumberFormatException|InputMismatchException e) {
                IO.println("Please enter a valid number.");

            }
        }

        IO.println("""
            ╔════════════════ SORT ORDER ════════════════╗
            ║ What order would you like to sort by:      ║
            ║                                            ║
            ║  (1) Asc                                   ║
            ║  (2) Desc                                  ║
            ║  (0) Cancel                                ║
            ╚════════════════════════════════════════════╝
            """);
        int order;
        while (true) {
            try {
                IO.print("➜ Enter order: ");
                order = Integer.parseInt(sc.nextLine().trim());
                flush();
                if (order < 0 || order > 2) {
                    IO.println("Invalid order!!!");
                }
                else if (order == 0) {
                    IO.println("Sorting Cancelled.");
                    return;
                }
                else  {
                    break;
                }
            }catch (NumberFormatException|InputMismatchException e) {
                IO.println("Please enter a valid number.");
            }
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

        int choice;
        while (true) {
            try {
                IO.print("➜ Enter choice: ");
                choice = sc.nextInt();
                flush();
                if (choice == 0) {
                    IO.println("Import canceled.");
                    return;
                }
                else if (choice<0 || choice > files.length) {
                    IO.println("Invalid choice.");
                }
                else {
                    break;
                }
            }catch (NumberFormatException|InputMismatchException e) {
                IO.println("Please enter a valid number.");
            }
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
        String fileName = null;
        while (true) {
            IO.print("➜ File name: ");
            fileName = IO.readln().trim();
            flush();
            if (fileName.isEmpty()) {
                IO.println("Please enter a valid file name.");
            }
            else {
                break;
            }
        }
        service.exportCatalogs(fileName);
    }
}
