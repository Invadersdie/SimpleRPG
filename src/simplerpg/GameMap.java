package simplerpg;

public class GameMap {
    
    private final int msx = 20;
    private final int msy = 6;
    private String[][] map = new String[msy][msx];
    private int[][] dangMap = new int[msy][msx];
    private char[][] obstMap = new char[msy][msx];
    
    public GameMap()
    {        
        obstMap[3][5] = 'S';        
        obstMap[4][8] = 'X';
    }
    
    public char getObstValue(int x, int y)
    {
        return obstMap[y][x];
    }
    
    public int getDangerous(int _x, int _y)
    {
        return dangMap[_y][_x];
    }
    
    public boolean isCellEmpty(int _x, int _y)
    {
        if(_x < 0 || _y < 0 || _x > msx - 1 || _y > msy - 1) return false;
        if(obstMap[_y][_x] == 'X') return false;
        return true;
    }
    
    public void updateMap(int _hx, int _hy) {
        for (int i = 0; i < msy; i++) {
            for (int j = 0; j < msx; j++) {
                map[i][j] = "*";
                if(obstMap[i][j] == 'S') map[i][j] = "S";
                if(obstMap[i][j] == 'X') map[i][j] = "X";
            }
        }
        map[_hy][_hx] = "H";
    }

    public void showMap() {
        for (int i = 0; i < msy; i++) {
            for (int j = 0; j < msx; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public void buildDangMap(int _cx, int _cy) {
        for (int i = 0; i < msy; i++) {
            for (int j = 0; j < msx; j++) {
                int dng = (int) Math.sqrt(Math.pow(_cy - i, 2) + Math.pow(_cx - j, 2));
                dangMap[i][j] = dng;
            }
        }
    }
    
}
