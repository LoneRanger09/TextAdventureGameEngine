package engine;
import model.Room;
import model.Item;

import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GameLoader {

    private static class ItemData { String name; String description; }
    private static class RoomData { String name; String description; Map<String, String> exits; List<String> items; }
    private static class GameData { String playerStart; List<ItemData> items; List<RoomData> rooms; }

    private Map<String,Room>loadedRooms;
    private Map<String,Room>loadedItems;
    private String playerStartRoomName;

    private final Gson gson;

    public GameLoader(){

        this.loadedItems = new HashMap<>();
        this.loadedRooms = new HashMap<>();
        this.gson = new Gson();
        System.out.println("GameLoader initialized");
    }

    public void loadGameData(String filePath) throws IOException,JsonSyntaxException,IllegalArgumentException{
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }
        System.out.println("Attempting to load game data from: " + filePath);
        Path path = Paths.get(filePath);
        String jsonContent;

        GameData gameData;
        try {
            // Read all content from the file specified by the Path object.
            // Files.readString handles opening, reading, and closing the file.
            jsonContent = Files.readString(path);
            System.out.println("Successfully read content from file: " + filePath);
        } catch (IOException e) {
            System.err.println("ERROR: Failed to read game data file at path: " + filePath);
            System.err.println("Reason: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw new IOException("Failed to read game data file: '" + filePath + "'. Check file existence and permissions.", e);
        }

        try {
            processIntermediateData(gameData);
            System.out.println("Intermediate GameData successfully processed into Room and Item objects.");
        } catch (GameDataException e) {
            System.err.println("Error processing game data logic: " + e.getMessage());
            throw e;
        }
    }

    private void processIntermediateData(GameData gameData) throws GameDataException {
        if (gameData.items == null) {
            System.out.println("Warning: 'items' array not found or null in JSON. No items will be loaded.");
            gameData.items = new ArrayList<>(); // Avoid NullPointerException later
        }
        if (gameData.rooms == null) {
            throw new GameDataException("'rooms' array not found or null in JSON. Cannot load game world.");
        }
        if (gameData.playerStart == null || gameData.playerStart.trim().isEmpty()) {
            throw new GameDataException("'playerStart' field not found, null, or empty in JSON. Cannot determine starting room.");
        }

        for (ItemData itemData : gameData.items) {
            if (itemData == null || itemData.name == null || itemData.name.trim().isEmpty()) {
                System.err.println("Warning: Skipping invalid item data (null or missing name).");
                continue;
            }
            String itemName = itemData.name.trim();
            if (loadedItems.containsKey(itemName)) {
                throw new GameDataException("Duplicate item name found in JSON: '" + itemName + "'");
            }
            String itemDesc = (itemData.description != null) ? itemData.description : "An item.";
            Item newItem = new Item(itemName, itemDesc);
            loadedItems.put(itemName, newItem);
            System.out.println("Created Item: " + itemName);
        }

        public Map<String, Room> getLoadedRooms() {
        return loadedRooms;
    }

        for (RoomData roomData : gameData.rooms) {
            if (roomData == null || roomData.name == null || roomData.name.trim().isEmpty()) {
                continue;
            }
            String currentRoomName = roomData.name.trim();
            Room currentRoom = loadedRooms.get(currentRoomName);

            if (loadedRooms.containsKey(roomName)) {
                throw new GameDataException("Duplicate room name found in JSON: '" + roomName + "'");
            }
            String roomDesc = (roomData.description != null) ? roomData.description : "A non-descript location.";
            Room newRoom = new Room(roomName, roomDesc);
            loadedRooms.put(roomName, newRoom);
            System.out.println("Created Room: " + roomName);
        }
        for (RoomData roomData : gameData.rooms) {
            if (roomData == null || roomData.name == null || roomData.name.trim().isEmpty()) {
                continue;
            }
            String currentRoomName = roomData.name.trim();
            Room currentRoom = loadedRooms.get(currentRoomName); // We know it exists from Step 2

    if (roomData.exits != null) {
          for (Map.Entry<String, String> exitEntry : roomData.exits.entrySet()) {

                    String direction = exitEntry.getKey();
                    String destinationRoomName = exitEntry.getValue();

                    if (direction == null || direction.trim().isEmpty() ||
                            destinationRoomName == null || destinationRoomName.trim().isEmpty()) {
                        System.err.println("Warning: Skipping invalid exit data in room '" + currentRoomName + "' (null/empty direction or destination).");
                        continue;
                    }
                        String trimmedDirection = direction.toLowerCase().trim();
                        String trimmedDestinationName = destinationRoomName.trim();
                    // Validate destination room exists
                    if (!loadedRooms.containsKey(trimmedDestinationName)) {
                        throw new GameDataException("Broken exit link in room '" + currentRoomName +
                                "': Destination room '" + trimmedDestinationName +
                                "' (for direction '" + trimmedDirection + "') not found.");
                    }
                    currentRoom.addExit(trimmedDirection, trimmedDestinationName);
                    System.out.println("  Added exit from '" + currentRoomName + "' [" + trimmedDirection + "] to '" + trimmedDestinationName + "'");
                }
                } else {
                    System.out.println("  Room '" + currentRoomName + "' has no exits defined.");
                 }

            // Add Items
            if (roomData.items != null) {
                for (String itemNameFromJson : roomData.items) {
                    if (itemNameFromJson == null || itemNameFromJson.trim().isEmpty()) {
                        System.err.println("Warning: Skipping invalid item name (null/empty) listed in room '" + currentRoomName + "'.");
                        continue;
                    }
                    String itemName = itemNameFromJson.trim();
                    // Look up the actual Item object
                    Item itemToAdd = loadedItems.get(itemName);
                    if (itemToAdd == null) {
                        throw new GameDataException("Item '" + itemName + "' listed in room '" + currentRoomName + "' but not defined in the top-level 'items' array.");
                    }
                    currentRoom.addItem(itemToAdd);
                    System.out.println("  Added item '" + itemName + "' to room '" + currentRoomName + "'");
                }
            } else {
                System.out.println("  Room '" + currentRoomName + "' has no items defined.");
            }
        }

        public Map<String, Room> getLoadedRooms() { return this.loadedRooms; }
        public Map<String, Item> getLoadedItems() { return this.loadedItems; }
        public String getPlayerStartRoomName() { return this.playerStartRoomName; }

        String startRoomName = gameData.playerStart.trim();
        if (!loadedRooms.containsKey(startRoomName)) {
            throw new GameDataException("Player starting room '" + startRoomName + "' specified in 'playerStart' does not exist in the loaded rooms.");
        }
        this.playerStartRoomName = startRoomName; // Store the validated starting room name
        System.out.println("Player starting room set to: " + this.playerStartRoomName);
    }

    public static class GameDataException extends Exception {
        public GameDataException(String message) {
            super(message);
        }
        public GameDataException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public Map<String, Room> getLoadedRooms() {
        return this.loadedRooms;
    }

    public Map<String, Item> getLoadedItems() {
        return this.loadedItems;
    }

    public String getPlayerStartRoomName() {
        return this.playerStartRoomName;
    }
}
