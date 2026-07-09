# Songs Catalog

## Overview

Songs Catalog is a Java console application for managing a personal song library. It allows users to add, remove, search, sort, import, and export songs through a menu-driven interface. Each song is uniquely identified with a UUID, and the application supports undo/redo so changes can be reversed across actions.

## Features

- Menu-based console interface
- Add songs with title, artist, and rating
- Remove songs from the catalog
- Search songs by title or artist
- Sort songs by title, artist, or rating
- Undo and redo previous actions
- Import songs from `.bin` catalog files
- Export the current catalog to `.bin` files
- UUID-based song identification to avoid collisions and simplify merging

## How to Run

1. Open the project in a Java IDE such as IntelliJ IDEA or Eclipse.
2. Make sure a JDK is installed and configured correctly.
3. Build the project.
4. Run the class that contains the `main` method.
5. Use the console menu to manage the catalog.

## Project Structure

The application is organized around a few main responsibilities:

- `Song` stores the song data.
- `SongCatalog` handles catalog operations and persistence.
- `ClientHandler` manages user interaction through the console.
- `Command` and its implementations support undo/redo using the Command pattern.
- `UndoRedoManager` keeps track of action history.

## Design Decisions

### UUID identifiers

Each song uses a UUID instead of relying only on title or artist. This makes every song uniquely identifiable and helps avoid conflicts when importing, exporting, or merging catalogs.

### Undo/redo with Command pattern

Undo and redo are implemented using the Command pattern. Each change to the catalog is wrapped as a command so it can be executed, undone, and redone in a structured way.

### Import/export support

The catalog can be exported into `.bin` files and imported back later. This makes the project easier to test, easier to demonstrate, and more practical for saving or merging song collections.

## Assumptions

- The application is run locally in a Java development environment.
- The user interacts with the application through the console.
- Song data is stored and transferred using serialized `.bin` files.
- Input is provided in the format requested by the menu prompts.
- The `imports` folder is used for import/export file handling.

## AI Usage Note

AI tools were used during development for brainstorming, design support, and code structuring. They were helpful for exploring the UUID-based ID approach, understanding how undo/redo could be implemented with the Command pattern, shaping the import/export functionality, and improving the presentation and readability of the console application.

## Where AI Was Overridden

AI suggestions were not followed blindly. Final implementation decisions, method behavior, menu flow, validation details, and overall project structure were adjusted manually to better fit the project requirements and the intended user experience.
