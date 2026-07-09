//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
    Scanner scanner = new Scanner(System.in);
    ClientHandler clientHandler = new ClientHandler();

    ConsoleUI.header();
    clientHandler.info();

    while (true) {
        ConsoleUI.menu();
        IO.print("Choice: ");

        int choice = Integer.parseInt(scanner.nextLine());
        System.out.flush();

        switch (choice) {
            case 1 -> clientHandler.handleAddingSong();
            case 2 -> clientHandler.handleRemovingSong();
            case 3 -> clientHandler.handleSearching();
            case 4 -> clientHandler.handleSorting();
            case 5 -> clientHandler.handleMerge();
            case 6 -> clientHandler.handleExport();
            case 7 -> clientHandler.handleUndo();
            case 8 -> clientHandler.handleRedo();
            case 9 -> clientHandler.info();
            case 0 -> {
                ConsoleUI.success("Goodbye!");
                return;
            }
            default -> ConsoleUI.error("Invalid choice.");
        }

        IO.println();

    }
}
