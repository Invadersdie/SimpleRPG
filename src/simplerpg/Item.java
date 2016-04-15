package simplerpg;


public class Item {
    
    public static enum ItemType { Consumables, InfConsumables, Quest, Armor, Weapon };
    
    private String name;
    private ItemType type;
    

    public Item(String _name, ItemType _type)
    {
        name = _name;
        type = _type;
    }
    public ItemType getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }



}
