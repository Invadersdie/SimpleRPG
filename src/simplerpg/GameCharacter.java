package simplerpg;

public class GameCharacter implements Cloneable {

    protected String name;

    public String getName() {
        return name;
    }

    protected Hero charClass;

    protected int hpMax; // Secondary stats

    public int getHpMax() {
        return hpMax;
    }

    protected double strength;
    protected double dexterity;
    protected double endurance;
    protected double strMulti;
    protected double dexMulti;
    protected double endMulti;

    protected int attack;
    protected int defense;
    protected int critChance;
    protected double critMultiplier;
    protected int avoidChance;
    protected int lucky;

    protected int tDefense;
    protected int tAvoidChance;

    public int getLevel() {
        return level;
    }

    protected int level;
    protected int hp;
    protected boolean blockStance;
    protected boolean attackAvoided;
    protected boolean life;
    protected boolean stun;

    public boolean isAlive() {
        return life;
    }

    protected Inventory myInv;

    public GameCharacter(String _name, double _strength, double _dexterity, double _endurance, double strMulti, double dexMulti, double endMulti) {
        this.strMulti = strMulti;
        this.dexMulti = dexMulti;
        this.endMulti = endMulti;
        name = _name;
        strength = _strength;
        dexterity = _dexterity;
        endurance = _endurance;
        calculateParameters();
        level = 1;
        hp = hpMax;
        life = true;
        blockStance = false;
    }

    public void calculateParameters() {
        attack = (int) (strength * 5);
        hpMax = (int) (endurance * 30);
        defense = (int) (strength / 4.0 + endurance / 2.0);
        critChance = (int) (100 - 10000 / (100 + dexterity));
        critMultiplier = 2 + (0.3 * Math.log(dexterity / 20 + 1));
        avoidChance = (int) (10 * Math.log(0.085 * dexterity + 1));
    }

    public void addStrength(double amount) {
        strength += amount * strMulti;
    }

    public void addDexterity(double amount) {
        dexterity += amount * dexMulti;
    }

    public void addEndurance(double amount) {
        endurance += amount * endMulti;
    }


    public void showFullInfo() // Вывод инфо по персонажу
    {
        System.out.println("Имя: " + name + " Здоровье: " + hp + "/" + hpMax + " Уровень: " + level);
        System.out.println("Атака: " + attack + " Защита: " + defense + " Шанс крита(множитель): " + critChance + "(" + critMultiplier + ") " + "Уклонение%: " + avoidChance);
        System.out.println(strength + " " + dexterity + " " + endurance);
    }

    public void showInfo() // Вывод инфо по персонажу
    {
        System.out.println("Имя: " + name + " Здоровье: " + hp + "/" + hpMax);
    }

    public void enableBlockStance() { // Включение защитной стойки
        blockStance = true;
        tAvoidChance = avoidChance * 2;
        tDefense = defense * 2;
        System.out.println(name + " стал в защитную стойку");
    }

    public void disableBlockStance() {
        blockStance = false;
        tAvoidChance = avoidChance;
        tDefense = defense;
    }

    public void setAttackAvoided() {
        attackAvoided = true;
        System.out.println(name + " успешно увернулся от атаки и готов контратаковать(крит шанс=100%)");
    }

    public void gotLucky(int _val) {
        lucky = _val;
        System.out.println(name + " познал дзен и стал нереально удачным(" + _val + "% к удаче");
    }

    public void gotStunned() {
        stun = true;
        System.out.println(name + "оглушен");
    }

    public void cure(int _val) {
        hp += _val;
        if (hp > hpMax) {
            int x = hpMax - hp;
            hp = hpMax;
            System.out.println(name + "не смог восполнить здоровье выше максимального и исцелился всего на " + x + " ед.");
        }
        System.out.println(name + " пополнил здоровье на " + _val + " ед.");
    }

    public Object clone() // Копирование объектов
    {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("Клонирование невозможно");
            return this;
        }
    }

    public void makeNewRound() // Действия на начало нового раунда
    {
        disableBlockStance(); // На начало раунда сбрасываем защитную стойку
    }

    public int makeAttack() {
        int minAttack = (int) (attack * 0.8f);
        int deltaAttack = (int) (attack * 0.4f);
        int currentAttack = minAttack + Utils.rand.nextInt(deltaAttack); // Делаем разброс атаки 80-120%
        if (attackAvoided) {
            currentAttack = (int) (currentAttack * critMultiplier); // Если крит сработал, умножаем атаку на 2
            System.out.println(name + " провел критический удар в размере " + currentAttack + " ед. урона");
        } else if (Utils.rand.nextInt(100) < critChance) // Проверяем условие на срабатывание критического удара
        {
            currentAttack = (int) (currentAttack * critMultiplier); // Если крит сработал, умножаем атаку на 2
            System.out.println(name + " провел критический удар в размере " + currentAttack + " ед. урона");
        } else
            System.out.println(name + " провел атаку на " + currentAttack + " ед. урона");
        return currentAttack;
    }

    public void getDamage(int _inputDamage) // Метод получения урона
    {
        if (Utils.rand.nextInt(100) < tAvoidChance) {
            System.out.println(name + " увернулся от атаки");
            setAttackAvoided();
        } else {
            _inputDamage = (int) (_inputDamage * (100.0 / (100.0 + tDefense)));
            System.out.println(name + " получил " + _inputDamage + " ед. урона");

            hp -= _inputDamage; // снижаем уровень здоровья
            if (hp < 1) // если здоровье опускается ниже 0
                life = false; // переключаем life = false
        }
    }

    public void useItem(String _item) {
        switch (_item) {
            case "Слабое зелье лечения":
                cure(120);
                break;
            case "Слабый камень здоровья":
                cure(60);
                break;
            case "Малый камень удачи":
                gotLucky(30);
                break;
        }
    }

    public void fullHeal() {
        hp = hpMax;
    } //Исцеление
}
