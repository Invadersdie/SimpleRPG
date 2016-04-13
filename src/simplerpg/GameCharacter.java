package simplerpg;

public class GameCharacter implements Cloneable  {

    protected String name;
    public String getName()
    {
        return name;
    }    
    protected String charClass;
    
    protected int base_strength; // Primary Stats
    protected int base_dexterity;
    protected int base_endurance;
    
    protected int strength; // Primary Stats
    protected int dexterity;
    protected int endurance;    
    
    protected int hpMax; // Secondary stats
    public int getHpMax()
    {
        return hpMax;
    }    
    protected int attack; 
    protected int defense;
    protected int critChance;
    protected float critMultiplier;
    protected int avoidChance;
    
    protected int level;
    protected int hp;
    protected boolean blockStance;
    protected boolean life;
    public boolean isAlive()
    {
        return life;
    }
    
    protected Inventory myInv;
    
    public GameCharacter(String _charClass, String _name, int _strength, int _dexterity, int _endurance)
    {
        name = _name;
        charClass = _charClass;
        strength = _strength;
        dexterity = _dexterity;
        endurance = _endurance;     
        base_strength = _strength;
        base_dexterity = _dexterity;
        base_endurance = _endurance; 
        calculateSecondaryParameters();
        level = 1;
        hp = hpMax;
        life = true;
        blockStance = false;
    }
    
    public void calculateSecondaryParameters()
    {
        attack = strength * 4;
        hpMax = endurance * 50;
        defense = (int)((strength + dexterity) / 4.0f);
        critChance = dexterity * 1;
        critMultiplier = 1.2f + (float)(dexterity / 20.0f);
        avoidChance = 8 + (int)(dexterity / 5.0f);
    }
    
    public void showInfo() // Вывод инфо по персонажу
    {
        System.out.println("Имя: " + name + " Здоровье: " + hp + "/" + hpMax);
    }

    public void setBlockStance() // Включение защитной стойки
    {
        blockStance = true;
        System.out.println(name + " стал в защитную стойку");
    }
    
    public void cure(int _val)
    {
        hp += _val;
        if(hp > hpMax) hp = hpMax;
    }
    
    public Object clone() // Копирование объектов 
    {  
        try
        {
            return super.clone();
        } 
        catch (CloneNotSupportedException e)
        {
            System.out.println("Клонирование невозможно");
            return this;
        }                
    } 
    
    public void makeNewRound() // Действия на начало нового раунда
    {
        blockStance = false; // На начало раунда сбрасываем защитную стойку
    }
    
    public int makeAttack() // Метод атаки
    {
        int minAttack = (int)(attack * 0.8f);
        int deltaAttack = (int)(attack * 0.4f);
        int currentAttack = minAttack + Utils.rand.nextInt(deltaAttack); // Делаем разброс атаки 80-120%
        if(Utils.rand.nextInt(100) < critChance) // Проверяем условие на срабатывание критического удара
        {            
            currentAttack = (int)(currentAttack * critMultiplier); // Если крит сработал, умножаем атаку на 2
            System.out.println(name + " провел критический удар в размере " + currentAttack + " ед. урона");
        }
        else
            System.out.println(name + " провел атаку на " + currentAttack + " ед. урона");
        return currentAttack; // возвращаем полученное значение атаки
    }
    
    public void getDamage(int _inputDamage) // Метод получения урона
    {   
        if(Utils.rand.nextInt(100) < avoidChance)
        {
            System.out.println(name + " увернулся от атаки");  
        }
        else
        {
            _inputDamage -= Utils.rand.nextInt(defense); // из входящего урона вычитается значение защиты
            if (blockStance) // если включена защитная стойка - снижаем входящий урон еще раз
            {            
                System.out.println(name + " заблокировал часть урона");
                _inputDamage -= Utils.rand.nextInt(defense);
            }
            if (_inputDamage < 0) _inputDamage = 0; // делаем прверку на отрицательный урон, для предотвращения эффекта лечения
            System.out.println(name + " получил " + _inputDamage + " ед. урона");
            hp -= _inputDamage; // снижаем уровень здоровья
            if(hp < 1) // если здоровье опускается ниже 0
                life = false; // переключаем life = false
        }
    }
    
    public void useItem(String _item)
    {
        switch(_item)
        {
            case "Слабое зелье лечения":
                cure(120);
                System.out.println(name + " пополнил здоровье на 120 ед.");
                break;
            case "Слабый камень здоровья":
                cure(60);
                System.out.println(name + " пополнил здоровье на 60 ед.");
                break;  
        }
    }
    
    public void fullHeal()
    {
        hp = hpMax;
    } //Исцеление
}
