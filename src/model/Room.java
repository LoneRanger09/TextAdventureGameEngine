package model;
import model.Item;
import java.util.Map;
import java.util.HashMap;
import  java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Room {

    private String name;
    private String description;
    private Map<String, String> exits;
    private List<Item> items;


     public Room(String name,String description){

         if (name == null || name.trim().isEmpty()) {
             throw new IllegalArgumentException("Room name cannot be null or empty.");
         }
         if (description == null) {
             throw new IllegalArgumentException("Room description cannot be null.");
         }


        this.name=name.trim();
        this.description=description;

        this.exits = new HashMap<>();
        this.items = new ArrayList<>();

     }
    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return  this.description;
    }

    public Map<String, String> getExits() {
        return exits;
    }
    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item){
         if (items == null){
             throw new IllegalArgumentException("Cannot add a null item to the room.");
         }
        this.items.add(item);
    }

    public void remove(Item item){
         if (item == null){
             throw new IllegalArgumentException("Cannot remove a null item to the room.");
         }
         this.items.remove(item);
    }
    public void addExit(String direction, String destinationRoomName){

         if (direction == null || direction.trim().isEmpty()) {
            throw new IllegalArgumentException("Exit direction cannot be null or empty.");
        }
        if (destinationRoomName == null || destinationRoomName.trim().isEmpty()) {
            throw new IllegalArgumentException("Exit destination room name cannot be null or empty.");
        }

        String normalizedDirection = direction.trim().toLowerCase();
        String trimmedDestination = destinationRoomName.trim();

        this.exits.put(normalizedDirection, trimmedDestination);

    }

}
