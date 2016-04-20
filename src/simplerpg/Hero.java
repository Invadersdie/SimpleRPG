package simplerpg;

public class Hero extends GameCharacter { // Класс "герой" наследуется от класса "игровой персонаж"

    private int currentExp;
    private int expToNextLevel;
    private int killedMonsters;
    private int currentZone;
    private int posX;
    private int posY;

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public void setXY(int _x, int _y) {
        posX = _x;
        posY = _y;
    }

    public void moveHero(int _vx, int _vy) {
        posX += _vx;
        posY += _vy;
    }

//    public int getZoneDangerous() {
//        return currentZone;
//    }

//    public void goToDangerousZone() {
//        currentZone++;
//        System.out.println("Герой перешел в зону опасности " + currentZone);
//    }

    public Hero(String _name, double strength, double dexterity, double endurance, double strMulti, double dexMulti, double endMulti) {
        super(_name, strength, dexterity, endurance, strMulti, dexMulti, endMulti);
        currentZone = 0;
        currentExp = 0;
        expToNextLevel = 1000;
        killedMonsters = 0;
        myInv = new Inventory();
        myInv.add(new Item("Слабый камень здоровья", Item.ItemType.InfConsumables));
        myInv.add(new Item("Слабое зелье лечения", Item.ItemType.Consumables));
        myInv.addSomeCoins(1000);
    }

    public void expGain(int _exp) // Метод получение опыта
    {
        currentExp += _exp;
        System.out.println(name + " получил " + _exp + " ед. опыта");
        while (currentExp >= expToNextLevel) // Если нарали необходимый уровень опыта, повышаем уровень
        {
            level++;
            System.out.println("Закаленный в боях " + name + " повысил уровень до " + level);
            int points = 6 + level * 2;
            currentExp -= expToNextLevel;
            expToNextLevel *= 1.2;
            do {
                int x = Utils.getAction(1, 3, "Выберите какой из параметров повышать : 1.Сила 2.Ловкость 3.Выносливость; Осталось распределить: " + points);
                int y = Utils.getAction(1, points, "Сколько очков?");
                if (x == 1) addStrength(y);
                if (x == 2) addDexterity(y);
                if (x == 3) addEndurance(y);
                points -= y;
            } while (points > 0);
            calculateParameters();
            hp = hpMax;
        }
    }

    public void addKillCounter() {
        killedMonsters++;
    }

    public int killCounter() {
        return killedMonsters;
    }

    public void showInfo() // Вывод инфо по персонажу
    {
        System.out.println("Имя: " + name + " Здоровье: " + hp + "/" + hpMax + " Уровень: " + level + "[" + currentExp + " / " + expToNextLevel + "]");
    }

}
