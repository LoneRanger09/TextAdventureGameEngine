package model;
import model.Item;

import java.util.Map;
import java.util.HashMap;
import  java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String currentRoomName;
    private List<Item>inventory;
    public Player(String startingRoomName){

        if (startingRoomName == null || startingRoomName.trim().isEmpty()) {
            throw new IllegalArgumentException("Player starting room name cannot be null or empty.");
        }

        this.currentRoomName = startingRoomName.trim();
        this.inventory = new ArrayList<>();

    }
    public void takeItem(Item item){

        if (item == null) {
            throw new IllegalArgumentException("Cannot add a null item to player inventory.");
        }
        this.inventory.add(item);
    }

    public boolean dropItem(Item item){

        if (item == null) {
            throw new IllegalArgumentException("Cannot drop a null item to player inventory.");
        }
        boolean removed = this.inventory.remove(item);
        return removed;
    }
    public void getInventory(){
        return this.inventory;
    }
    public void getCurrentRoomName(){
        return this.currentRoomName;
    }
    public void setCurrentRoomName(String newRoomName){
        this.currentRoomName = newRoomName.trim();
    }
}

