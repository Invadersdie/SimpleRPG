package simplerpg;

public class GameClass {

    private Hero[] heroPattern = new Hero[3];
    private Monster[] monsterPattern = new Monster[3];
    private Hero mainHero;
    private Monster currentMonster;
    private GameMap map;
    private InGameShop shop;
    private int currentRound;

    private int inpInt;

    public GameClass() {
        initGame();
    }

    public void initGame() // Инициализируем начальное состояние игры
    {
        // Задаем шаблоны героев и монстров
        heroPattern[0] = new Hero("Knight", "Lancelot", 16, 8, 12);
        heroPattern[1] = new Hero("Barbarian", "Konan", 16, 8, 12);
        heroPattern[2] = new Hero("Dwarf", "Gimli", 16, 8, 12);
        monsterPattern[0] = new Monster("Humanoid", "Goblin", 12, 4, 4);
        monsterPattern[1] = new Monster("Humanoid", "Orc", 18, 6, 6);
        monsterPattern[2] = new Monster("Humanoid", "Troll", 32, 12, 10);
        currentRound = 1;
    }

    public void mainGameLoop() // Метод, отвечающий за игровую логику
    {
        map = new GameMap();
        shop = new InGameShop();
        inpInt = 0;
        System.out.println("Игра началась!");
        selectHero();
        mainHero.setXY(10, 3);
        map.buildDangMap(10, 3);

        while (true) {

            int x = getAction(0, 4, "Что Вы хотите делать дальше 1. Посмотреть карту; 2. Открыть инвентарь; 3. Информация о герое; 4. Отдохнуть; 5. Померяться силами с монстром; 0. Завершить игру;");
            if (x == 1) {
                int y = 1;
                while (y != 0) {
                    if (!mainHero.isAlive()) {
                        break;
                    }
                    map.updateMap(mainHero.getX(), mainHero.getY());
                    if (map.getObstValue(mainHero.getX(), mainHero.getY()) == 'S')  //Открытие магазина по достижению "S"
                        shopAction();
                    map.showMap();
                    y = getAction(0, 7, "Ваши дальнейшие действия: 1. Пойти влево; 2. Пойти вправо; 3. Пойти вверх; 4. Пойти вниз; 5. Найти монстров; 6. Восстановить силы; 0. Назад;");
                    switch (y) {
                        case 1:
                            if (map.isCellEmpty(mainHero.getX() - 1, mainHero.getY()))
                                mainHero.moveHero(-1, 0);
                            break;
                        case 2:
                            if (map.isCellEmpty(mainHero.getX() + 1, mainHero.getY()))
                                mainHero.moveHero(1, 0);
                            break;
                        case 3:
                            if (map.isCellEmpty(mainHero.getX(), mainHero.getY() - 1))
                                mainHero.moveHero(0, -1);
                            break;
                        case 4:
                            if (map.isCellEmpty(mainHero.getX(), mainHero.getY() + 1))
                                mainHero.moveHero(0, 1);
                            break;
                        case 5:
                            currentMonster = (Monster) monsterPattern[1].clone();  // Создаем монстра путем копирования из шаблона
                            currentMonster.lvlUp(map.getDangerous(mainHero.getX(), mainHero.getY()));
                            battle(mainHero, currentMonster);
                            break;
                        case 0:
                            break;

                    }
                    if (Utils.rand.nextInt(100) < 3)                             //Невероятный рандом 3% нападения монстра
                    {
                        System.out.println("На Вас внезапно напали!!!");
                        currentMonster = (Monster) monsterPattern[1].clone();  // Создаем монстра путем копирования из шаблона
                        currentMonster.lvlUp(map.getDangerous(mainHero.getX(), mainHero.getY()));
                        battle(mainHero, currentMonster);
                    }
                }
            }
            if (!mainHero.isAlive()) {
                break;
            }
            if (x == 2) {
                mainHero.myInv.showAllItems();
                int invInput = getAction(0, mainHero.myInv.getSize(), "Выберите предмет для использования");
                String usedItem = mainHero.myInv.useItem(invInput);
                if (usedItem != "") {
                    System.out.println(mainHero.getName() + " использовал " + usedItem);
                    mainHero.useItem(usedItem);
                }
            }


            if (x == 3) {
                mainHero.showFullInfo();
            }
            if (x == 4) {
                mainHero.fullHeal();
                System.out.println("Ваш герой отдохнул и снова готов в бой");
                mainHero.showInfo();
            }
            if (x == 5) {
                currentMonster = (Monster) monsterPattern[1].clone();  // Создаем монстра путем копирования из шаблона
                currentMonster.lvlUp(map.getDangerous(mainHero.getX(), mainHero.getY()));
                battle(mainHero, currentMonster);
            }

        }
        System.out.println("Игра завершена");
    }

    public void selectHero() {
        String s = "Выберите героя: ";
        for (int i = 0; i < 3; i++) {
            s += (i + 1) + ". " + heroPattern[i].getName() + "   ";
        }
        inpInt = getAction(1, 3, s);
        mainHero = (Hero) heroPattern[inpInt - 1].clone(); // Создаем героя путем копирования из шаблона
        System.out.println(mainHero.getName() + " начал свое путешествие");

    } //Связать с БД

    public void battle(Hero h, Monster m) {
        currentRound = 1;
        System.out.println("Бой между игроком " + h.getName() + " и монстром " + m.getName() + " начался");
        do {
            System.out.println("Текущий раунд: " + currentRound);
            h.showInfo();
            m.showInfo();
            h.makeNewRound(); // Вызываем метод сброса параметров героя на начало раунда            
            inpInt = getAction(0, 3, "Ход игрока: 1. Атака 2. Защита 3. Покопаться в сумке 0. Попытаться сбежать");
            System.out.print("\n\n"); // Печатаем два символа перевода строки
            if (inpInt == 1) // Герой атакует
            {
                m.getDamage(h.makeAttack()); // Вызываем метод получения урона монстром
                if (!m.isAlive()) // Делаем проверку жив ли монстр после удара героя
                {
                    System.out.println(m.getName() + " погиб"); // Печатаем сообщение о гибели монстра
                    h.expGain(m.getHpMax() * 2); // Даем герою опыта в размере (Здоровье_монстра * 2)
                    h.addKillCounter(); // Увеличение количества убитых монстров героем
                    m.myInv.transferAllItemsToAnotherInventory(h.myInv);
                    break;
                }
            }
            if (inpInt == 2) // Герой защищается
            {
                h.setBlockStance(); // Вызывем метод включения защитной стойки
            }
            if (inpInt == 3) {
                h.myInv.showAllItems();
                int invInput = getAction(0, h.myInv.getSize(), "Выберите предмет для использования");
                String usedItem = h.myInv.useItem(invInput);
                if (usedItem != "") {
                    System.out.println(h.getName() + " использовал " + usedItem);
                    h.useItem(usedItem);
                } else {
                    System.out.println(h.getName() + " просто закрыл сумку");
                }
            }
            if (inpInt == 0) {
                if (Utils.rand.nextInt(100) < 5) {
                    break;
                }// Уход из боя
                else {
                    System.out.println("Увы, удача не на твоей стороне, монстр догоняет тебя и пинает");
                    h.getDamage(m.makeAttack());
                }
            }


            m.makeNewRound();  // Вызываем метод сброса параметров монстра на начало раунда

            if (inpInt != 0) {
                if (Utils.rand.nextInt(100) < 80) {
                    h.getDamage(m.makeAttack()); // С вероятностью 80% монстр атакует
                } else {
                    m.setBlockStance();
                }
            }
            if (!h.isAlive()) // Если после удара монстра герой погибает - выходим из основного игрового цикла
            {
                break;
            }

            currentRound++;
        } while (true);
        if (m.isAlive() && h.isAlive()) {
            System.out.println(h.getName() + " сбежал с поля боя");
        }
        if (!m.isAlive()) {
            System.out.println("Победил " + h.getName());
        }
        if (!h.isAlive()) {
            System.out.println("Победил " + m.getName());
        }
    } //Неправильная работа после убийства монстра

    public void shopAction() //Покупка в магазине
    {
        shop.showItems();
        System.out.println("Для выхода из магазина нажмите 0");
        int x = Utils.getAction(0, shop.ITEMS_COUNT, "Введите номер покупаемого товара");
        if (x == 0) return;
        shop.buyByHero(x - 1, mainHero);
    }

    public int getAction(int _min, int _max, String _str) { // Защита от неверного ввода
        int x;
        do {
            if (_str != "") {
                System.out.println(_str);
            }
            x = Utils.sc.nextInt();
        } while (x < _min || x > _max);
        return x;
    }
}
