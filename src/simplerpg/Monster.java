package simplerpg;

public class Monster extends GameCharacter {

    public Monster(String _name, double strength, double dexterity, double endurance) {
        super(_name, strength, dexterity, endurance, 2, 2, 2);
        myInv = new Inventory();
        myInv.add(new Item("Слабое зелье лечения", Item.ItemType.Consumables));
        myInv.addSomeCoins(100);
    }

    public void lvlUp(int _l) {
        if (_l > 1 && level < _l) {
            int points = (6 + _l * 2);

            int x = Utils.rand.nextInt(points);
            System.out.print(x + " ");
            int y = Utils.rand.nextInt(points - x);
            System.out.print(y + " ");
            int z = points - y - x;
            System.out.print(z + " ");
            charClass.addStrength(x);
            charClass.addDexterity(y);
            charClass.addEndurance(z);
            calculateParameters();
            fullHeal();
            level++;
            showFullInfo();
        } else return;
    }
}


    

