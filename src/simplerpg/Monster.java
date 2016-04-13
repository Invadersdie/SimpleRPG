package simplerpg;

public class Monster extends GameCharacter {
    
    public Monster(String _charClass, String _name, int _strength, int _dexterity, int _endurance)
    {
        super(_charClass, _name, _strength, _dexterity, _endurance);
        myInv = new Inventory();
        myInv.add(new Item("Слабое зелье лечения", Item.ItemType.Consumables));
        myInv.addSomeCoins(100);
    }
    
    public void lvlUp(int _l)
    {
        for(int i=0;i<_l;i++)
        {
            strength += base_strength * 0.3f;
            dexterity += base_dexterity * 0.3f;
            endurance += base_endurance * 0.3f;            
           
            calculateSecondaryParameters();
            hp = hpMax;
        }
    }
    
}
