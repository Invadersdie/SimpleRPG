package simplerpg;

/**
 * Created by Михаил on 20.04.2016.
 */
public class Warrior extends Hero {
    public Warrior(String _name, double strength, double dexterity, double endurance) {
        super(_name, strength, dexterity, endurance, 2, 1.5, 5);
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
}