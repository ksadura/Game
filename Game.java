import java.awt.*;
import java.util.Vector;
/** Klasa Game odpowiedzalna za wyglad i logike gry*/
public class Game {
    /**Zmienna przechowujaca elementy planszy*/
    private Vector<Sprite> blocks;
    /**Zmienna przechowujaca potwory*/
    private Vector<Alien> aliens;
    /**Zmienna reprezentujaca glownego bohatera*/
    private Hero bomber;
    /**Zmienna reprezentujaca tlo gry*/
    private Background background;

    /**Konstruktor*/
    public Game(){
        loadAliens();
        loadMap();
        int w = Integer.parseInt(Config.cfg.getProperty("bomberX"));
        int h = Integer.parseInt(Config.cfg.getProperty("bomberY"));
        bomber = new Hero(w,h);
        background = new Background(0,0);
    }

    /** Funkcja sluzaca do rysowania wszystkich elementow gry
     * @param g Zmienna typu Graphics*/
    public void render(Graphics g)
    {
        background.paintBlock(g,background.getX(),background.getY());
        bomber.paintBlock(g,bomber.getX(),bomber.getY());
        for(Sprite b : this.blocks)
        {
            if(b instanceof Block) {
                Block block = (Block) b;
                block.paintBlock(g,block.getX(),block.getY());
            }
            else {
                Obstacle obs = (Obstacle) b;
                obs.paintBlock(g,obs.getX(),obs.getY());
            }
        }
        for(Alien ali : this.aliens)
        {
            ali.paintBlock(g,ali.getX(),ali.getY());
        }
    }
    /**Funkcja loadMap inicjalizujaca i parsujaca plansze*/
    public void loadMap()
    {
        this.blocks = new Vector<>();
        String[] array = Config.cfg.getProperty("map-Level1").split(",");
        int[][] map = new int[array.length][array.length];
        int c = 0;
        for(String str : array)
        {
            c += 1;
            char[] chars = str.toCharArray();
            for(int i=0;i<chars.length;i++)
            {
                map[c-1][i] = Integer.parseInt(String.valueOf(chars[i]));
            }
        }
        for(int i = 0;i<map.length;i++) {
            for(int j = 0;j<map.length;j++)
            {
                switch(map[i][j]){
                    case 1:
                        this.blocks.add(new Block(i,j));
                        break;
                    case 2:
                        this.blocks.add(new Obstacle(i,j));
                        break;
                }
            }
        }
    }
    /**Funkcja loadAliens inicjalizujaca i parsujaca powtory */
    public void loadAliens()
    {
        this.aliens = new Vector<>();
        String[] array = Config.cfg.getProperty("aliensPosition-Level1").split(",");
        for(int i =0; i<array.length; i+=2)
        {
            int i1 = Integer.parseInt(array[i]); //x
            int i2 = Integer.parseInt(array[i+1]); //y
            this.aliens.add(new Alien(i1,i2));
        }
    }
    /** Funkcja pomocnicza, ktora powoduje uaktualnienie uzytych plikow graficznych podczas skalowania gry */
    public static void getResizedImage()
    {
        Obstacle.uploadImage();
        Block.uploadImage();
        Alien.uploadImage();
        Hero.uploadImage();
        Background.uploadImage();
    }
}

