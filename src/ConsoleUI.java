public class ConsoleUI {
    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BOLD = "\u001B[1m";

    public static void header() {
        IO.println(CYAN + BOLD + """
                ╔══════════════════════════════════════════════╗
                ║              SONGS CATALOG APP              ║
                ╚══════════════════════════════════════════════╝
                """ + RESET);
    }

    public static void menu() {
        IO.println(YELLOW + """
                ╔══════════════════ MAIN MENU ═════════════════╗
                ║  1. Add Song                                ║
                ║  2. Remove Song                             ║
                ║  3. Search Songs                            ║
                ║  4. Sort Songs                              ║
                ║  5. Import Songs                            ║
                ║  6. Export Songs                            ║
                ║  7. Undo                                    ║
                ║  8. Redo                                    ║
                ║  9. View Songs                              ║
                ║  0. Exit                                    ║
                ╚══════════════════════════════════════════════╝
                """ + RESET);
        IO.print("➜ Enter choice: ");
    }

    public static void success(String message) {
        IO.println(GREEN + "✔ " + message + RESET);
    }

    public static void error(String message) {
        IO.println(RED + "✘ " + message + RESET);
    }

    public static void info(String message) {
        IO.println(CYAN + "➜ " + message + RESET);
    }

    public static void section(String title) {
        IO.println(YELLOW + "════════════ " + title.toUpperCase() + " ════════════" + RESET);
    }
}