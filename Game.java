import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.Vector;
/** Klasa Game odpowiedzalna za wyglad i logike gry*/
public class Game {
    /**Zmienna przechowujaca elementy planszy*/
    private Vector<Sprite> blocks;
    /**Zmienna przechowujaca potwory*/
    private Vector<Alien> aliens;
    /**Zmienna reprezentujaca glownego bohatera*/
    public Hero bomber;
    /**Zmienna reprezentujaca tlo gry*/
    private Background background;
    /** Zmienna pelniaca role flagi informujacej czy gra jest wlaczona */
    public static boolean isRunning;
    /** Zmienn przechowujaca bomby */
    private Vector<Bomb> bombs;


    /**Konstruktor*/
    public Game(){
        this.blocks = new Vector<>();
        this.aliens = new Vector<>();
        loadAliens();
        loadMap();
        int x = Integer.parseInt(Config.cfg.getProperty("bomberX"));
        int y = Integer.parseInt(Config.cfg.getProperty("bomberY"));
        bomber = new Hero(x,y);
        background = new Background(0,0);
        bombs = new Vector<>();

    }

    /** Funkcja sluzaca do rysowania wszystkich elementow gry
     * @param g Zmienna typu Graphics*/
    public void render(Graphics g)
    {
        background.paintBlock(g,background.getX(),background.getY());
        if (bomber.getHealth() != 0)
            bomber.paintBlock(g,bomber.getX(),bomber.getY());
        for(Sprite b : this.blocks)
        {
            if(b instanceof Block && b.isVisible) {
                Block block = (Block) b;
                block.paintBlock(g,block.getX(),block.getY());
            }
            else if (b.isVisible) {
                Obstacle obs = (Obstacle) b;
                obs.paintBlock(g,obs.getX(),obs.getY());
            }
        }
        for(Alien ali : this.aliens)
        {
            if(ali.isVisible)
                ali.paintBlock(g,ali.getX(),ali.getY());
        }
        try {
            for (Bomb bomb : this.bombs) {
                if (bomb.isVisible)
                    bomb.paintBlock(g, bomb.getX(), bomb.getY());
            }
        }
        catch (ConcurrentModificationException e){
            System.out.println("slow down");
        }
    }
    /**Funkcja loadMap inicjalizujaca i parsujaca plansze*/
    public void loadMap()
    {
        if(blocks.size() != 0)
            this.blocks.clear();
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
        if (aliens.size() != 0)
            this.aliens.clear();
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
        Menu.uploadMenu();
        Bomb.uploadImage();
    }
    /** Funkcja reprezentujaca animacje, czyli ruch oraz kolizje*/
    public void animate()
    {
        if(Handler.toReset) {
            reset();
            Handler.toReset = false;
        }
        bomber.move();
        checkBomberCollision();
        checkExplosion();
        isHeroHurt();
        removeBombs();
        for(Alien a : this.aliens) {
            a.move();
            checkAlienCollision(a);
        }
    }
    /** Funkcja sprawdzajaca czy glowny bohater gry zderzyl sie z przeszkoda */
    public void checkBomberCollision()
    {
        for (Sprite sprite : this.blocks) {
            Rectangle blockBounds = sprite.getBounds();
            if (bomber.getBounds().intersects(blockBounds) && sprite.isVisible) {
                bomber.setVel(0, 0);
                if (bomber.getDirection() == 0)
                    bomber.setX(sprite.getX() + sprite.getWidth()+1);
                else if (bomber.getDirection() == 1)
                    bomber.setY(sprite.getY() + sprite.getHeight()+1);
                else if (bomber.getDirection() == 2)
                    bomber.setX(sprite.getX() - bomber.getWidth()-1);
                else
                    bomber.setY(sprite.getY() - bomber.getHeight()-1);
            }
        }
    }
    /** Funkcja sprawdzajaca czy potwor zderzyl sie z przeszkoda, badz bohaterem
     * @param alien pojedynczy potwor*/
    public void checkAlienCollision(Alien alien)
    {
        if (alien.getHeart().intersects(bomber.getHeart()) && alien.isVisible && !bomber.flag){
            bomber.flag = true;
            bomber.reduceHealth();
            if (bomber.timer.isRunning())
                bomber.timer.restart();
            else
                bomber.timer.start();
            if (bomber.getHealth() == 0 && Handler.currentState == Handler.STATES.GAME)
            {
                Handler.currentState = Handler.STATES.MENU;
                Handler.toReset = true;
                //reset();
            }
        }
        for (Sprite sprite: this.blocks)
        {
            if(alien.getBounds().intersects(sprite.getBounds()) && sprite.isVisible)
            {
                if(alien.getDirection() == 0) {
                    alien.setX(sprite.getX() - alien.getWidth()-2);
                    alien.setDirection((int) Math.round(Math.random() * 3));
                }
                else if (alien.getDirection() == 1){
                    alien.setX(sprite.getX() + sprite.getWidth()+2);
                    alien.setDirection((int) Math.round(Math.random() * 3));
                }
                else if (alien.getDirection() == 2){
                    alien.setY(sprite.getY() - alien.getHeight()-2);
                    alien.setDirection((int) Math.round(Math.random() * 3));
                }
                else{
                    alien.setY(sprite.getY() + sprite.getHeight()+2);
                    alien.setDirection((int) Math.round(Math.random() * 3));
                }
            }
        }
    }
    /**Funkcja sprawdzajaca czy wybuchajaca bomba zabila potwora lub zniszczyla przeszkode */
    public void checkExplosion()
    {
        for (Bomb b : bombs) {
            for (Sprite s : blocks) {
                if (b.getBounds().intersects(s.getBounds()) && b.isReady && s instanceof Obstacle){
                    s.setVisible(false);
                }
            }
            for (Alien a : aliens){
                if(b.getBounds().intersects(a.getBounds()) && b.isReady) {
                    a.setVisible(false);
                    //break;
                }
            }
        }
    }
    /** Funkcja pomocnicza usuwajaca zbedne bomby */
    public void removeBombs()
    {
        if (bombs.size() != 0)
            bombs.removeIf(b -> b.getBounds().intersects(new Rectangle(0, 0, MyPanel.windowW, MyPanel.windowH)) && b.isReady);
    }
    /** Funkcja sprawdzajaca czy bohater ucierpial */
    public void isHeroHurt()
    {
        for (Bomb b : bombs) {
            if (b.getBounds().intersects(bomber.getBounds()) && b.isReady) {
                bomber.reduceHealth();
                if (bomber.getHealth() == 0 && Handler.currentState == Handler.STATES.GAME) {
                    Handler.currentState = Handler.STATES.MENU;
                    Handler.toReset = true;
                }
                //System.out.println(bomber.getHealth());
                break;
            }
        }
    }
    /** Rzucanie bomb */
    public void fire()
    {
        if (Bomb.counter < 15)
            this.bombs.add(new Bomb(bomber.x + bomber.width/2,bomber.y + bomber.height/2));
    }
    /** Resetowanie rozgrywki do stanu poczatkowego */
    public void reset(){
        loadMap();
        loadAliens();
        bombs.clear();
        bomber.restoreHero();
        Bomb.counter = 0;
    }

}

