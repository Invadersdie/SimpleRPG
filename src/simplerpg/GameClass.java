package simplerpg;

public class GameClass {


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

        // Задаем шаблоны монстров

        monsterPattern[0] = new Monster("Goblin", 30, 10, 30);
        monsterPattern[1] = new Monster("Orc", 20, 15, 35);
//        monsterPattern[2] = new Monster(objTank, "Troll");
        currentRound = 1;
        map = new GameMap();
        shop = new InGameShop();
    }

    public void mainGameLoop() {
        inpInt = 0;
        System.out.println("Игра началась!");
        selectHero();

        while (true) {
            if (!mainHero.isAlive()) break;
            int x = Utils.getAction(0, 5, "Что Вы хотите делать дальше 1. Посмотреть карту; 2. Открыть инвентарь; 3. Информация о герое; 4. Отдохнуть; 5. Померяться силами с монстром; 0. Завершить игру;");
            whatsNext(x);
            if (x == 0) break;
        }
        endGame();
    }

    public void selectHero() {
        System.out.println("Введите имя героя: ");
        String name = Utils.sc.next();
        int x = Utils.getAction(1, 3, "Выберите класс: 1.Воин 2.Убийца 3.Танк ");
        switch (x) {
            case 1: {
                mainHero = new Warrior(name, 25, 10, 25);
                break;
            }
            case 2: {
                mainHero = new Assassin(name, 25, 10, 25);
                break;
            }
            case 3: {
                mainHero = new Tank(name, 25, 10, 25);
                break;
            }
            default: {
                mainHero = new Warrior(name, 25, 10, 25);
                break;
            }
        }


        mainHero.setXY(10, 3);
        map.buildDangMap(10, 3);
        System.out.println(mainHero.getName() + " начал свое путешествие");
    }

    public void battle(Hero h, Monster m) {
        currentRound = 1;
        System.out.println("Бой между игроком " + h.getName() + " и монстром " + m.getName() + " начался");
        m.makeNewRound();
        do {
            System.out.println("Текущий раунд: " + currentRound);
            h.showInfo();
            m.showInfo();
            h.makeNewRound(); // Вызываем метод сброса параметров героя на начало раунда            
            inpInt = Utils.getAction(0, 3, "Ход игрока: 1. Атака 2. Защита 3. Покопаться в сумке 0. Попытаться сбежать");
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
                h.enableBlockStance(); // Вызывем метод включения защитной стойки
            }
            if (inpInt == 3) {
                h.myInv.showAllItems();
                int invInput = Utils.getAction(0, h.myInv.getSize(), "Выберите предмет для использования");
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
                    m.enableBlockStance();
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
            monsterPattern[1] = new Monster("Goblin", 25, 15, 20);
        }
        if (!h.isAlive()) {
            System.out.println("Победил " + m.getName());
        }
    }

    public void shopAction() //Покупка в магазине
    {
        shop.showItems();
        System.out.println("Для выхода из магазина нажмите 0");
        int x = Utils.getAction(0, shop.ITEMS_COUNT, "Введите номер покупаемого товара");
        if (x == 0) return;
        shop.buyByHero(x - 1, mainHero);
    }

    public void mapMove() {
        {
            int y = 1;
            while (y != 0) {
                if (!mainHero.isAlive()) {
                    break;
                }
                if (Utils.rand.nextInt(100) < 99)                             //Невероятный рандом 3% нападения монстра
                {
                    System.out.println("На Вас внезапно напали!!!");
                    initBattle();
                }
                map.updateMap(mainHero.getX(), mainHero.getY());
                if (map.getObstValue(mainHero.getX(), mainHero.getY()) == 'S')  //Открытие магазина по достижению "S"
                    shopAction();
                map.showMap();
                y = Utils.getAction(0, 7, "Ваши дальнейшие действия: 1. Пойти влево; 2. Пойти вправо; 3. Пойти вверх; 4. Пойти вниз; 5. Найти монстров; 6. Восстановить силы; 0. Назад;");
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
                        initBattle();
                        break;
                    case 6:
                        mainHero.fullHeal();
                        System.out.println("Ваш герой отдохнул и снова готов в бой");
                        mainHero.showInfo();
                        break;
                    case 0:
                        break;

                }
            }
        }
    }

    public void whatsNext(int x) {
        switch (x) {
            case 1:
                mapMove();
                break;
            case 2:
                showInventory();
                break;
            case 3:
                mainHero.showFullInfo();
                break;
            case 4:
                mainHero.fullHeal();
                System.out.println("Ваш герой отдохнул и снова готов в бой");
                mainHero.showInfo();
                break;
            case 5:
                initBattle();
                break;
            case 0:
                break;
        }

    }

    public void initBattle() {
        currentMonster = (Monster) monsterPattern[1].clone();  // Создаем монстра путем копирования из шаблона
        currentMonster.lvlUp(mainHero.getLevel());
        battle(mainHero, currentMonster);
    }

    public void showInventory() {
        mainHero.myInv.showAllItems();
        int invInput = Utils.getAction(0, mainHero.myInv.getSize(), "Выберите предмет для использования");
        String usedItem = mainHero.myInv.useItem(invInput);
        if (usedItem != "") {
            System.out.println(mainHero.getName() + " использовал " + usedItem);
            mainHero.useItem(usedItem);
        }
    }

    public void endGame() {
        System.out.println("Игра завершена");
        System.out.println("За время игры вы достигли: " + mainHero.level + " уровня");
        System.out.println("Убили: " + mainHero.killCounter() + " монстров");
        System.out.println("Заработали: " + mainHero.myInv.currentGold() + " золота");
    }
}

