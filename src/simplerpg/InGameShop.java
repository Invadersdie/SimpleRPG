package simplerpg;

public class InGameShop {
    
    public final int ITEMS_COUNT = 3;
    private Item[] itm = new Item[ITEMS_COUNT];
    private int[] itmCost = new int[ITEMS_COUNT];
    private int goldAmout;
    
    public InGameShop()
    {
        goldAmout = 2000;
        itm[0] = new Item("Слабое зелье лечения", Item.ItemType.Consumables);
        itmCost[0] = 100;
        itm[1] = new Item("Полуразбитый щит", Item.ItemType.Armor);
        itmCost[1] = 200;
        itm[2] = new Item("Камень удачи", Item.ItemType.InfConsumables);
        itmCost[2] = 600;
    }
    
    public void showItems()
    {
        System.out.println("Ассортимент:");
        for(int i=0;i<ITEMS_COUNT;i++)
        {
            System.out.println((i + 1) + ". " + itm[i].getName() + " " + itmCost[i]);    
        }
    }
    
    public void buyByHero(int index, Hero h)
    {
        if(h.myInv.isCoinsEnough(itmCost[index]))
        {
            goldAmout += itmCost[index];
            h.myInv.spendCoins(itmCost[index]);
            h.myInv.add(itm[index]);
            System.out.println("Герой купил " + itm[index].getName());
        }
        else
            System.out.println("Не зватает золота");
    }
    
}
