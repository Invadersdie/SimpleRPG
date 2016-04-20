package simplerpg;

import java.util.Random;
import java.util.Scanner;

public class Utils {
    
    public static Random rand = new Random();
    public static Scanner sc = new Scanner(System.in);
    public static int getAction(int _min, int _max, String _str) { // Защита от неверного ввода
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
    
