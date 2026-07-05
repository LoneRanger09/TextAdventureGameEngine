package model;
public class Item {

    private String name;
    private String description;
    public Item(String name,String description){
        this.name = name;
        this.description = description;
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty.");
        }
        if (description == null) {
            throw new IllegalArgumentException("Item description cannot be null.");
        }
    }
    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return  this.description;
    }
}
