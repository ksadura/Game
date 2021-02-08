import javax.swing.*;
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
    protected Hero bomber;
    /**Zmienna reprezentujaca tlo gry*/
    private Background background;
    /** Zmienna pelniaca role flagi informujacej czy gra jest wlaczona */
    public static boolean isRunning;
    /** Zmienna przechowujaca bomby */
    private static Vector<Bomb> bombs;
    /** Zmienna odmierzajaca czas*/
    protected Clock clk;
    /** Zmienna reprezentujaca portal */
    private Portal portal;
    /** Zmienna reprezentujaca efekt przejscia miedzy poziomami*/
    protected Loading loading;
    /** Zmienna przechowujaca uzyskany wynik*/
    private Result score;
    /** Tablica z bonusami */
    private static Vector<Bonus> bonuses;
    /** Reprezentacja klienta, ktory moze komunikowac sie z serwerem*/
    protected Client client;

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
        clk = new Clock();
        int x1 = Integer.parseInt(Config.cfg.getProperty("portalX"));
        int y1 = Integer.parseInt(Config.cfg.getProperty("portalY"));
        portal = new Portal(x1,y1);
        loading = new Loading();
        score = new Result();
        bonuses = new Vector<>();
        loadBonuses();

    }

    /** Funkcja sluzaca do rysowania wszystkich elementow gry
     * @param g Zmienna typu Graphics*/
    public void render(Graphics g, JPanel panel)
    {
        background.paintBlock(g,background.getX(),background.getY());
        if(portal.isVisible)
            portal.paintBlock(g,portal.getX(),portal.getY());
        //if (bomber.getHealth() != 0)
        bomber.paintBlock(g,bomber.getX(),bomber.getY());
        for (Bonus bon: bonuses) {
            if (bon.isVisible)
                bon.paintBlock(g, bon.getX(), bon.getY());
        }
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
            for (Bomb bomb : bombs) {
                if (bomb.isVisible)
                    bomb.paintBlock(g, bomb.getX(), bomb.getY(),panel);
            }
        }
        catch (ConcurrentModificationException e){
            System.out.println("slow down");
        }
        if (bomber.flag || bomber.wasNearby)
            bomber.drawEffects(g);
    }
    /**Funkcja loadMap inicjalizujaca i parsujaca plansze*/
    public void loadMap()
    {
        if(blocks.size() != 0)
            this.blocks.clear();
        String[] array = Config.cfg.getProperty("map-" + Handler.getCurrentLevel()).split(",");
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
        String[] array = Config.cfg.getProperty("aliensPosition-" + Handler.getCurrentLevel()).split(",");
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
        for(Bomb b : bombs)
            b.uploadImage();
        Portal.uploadImage();
        for (Bonus bonus: bonuses)
            bonus.uploadImage();
        Help.uploadImage();
    }
    /** Funkcja reprezentujaca animacje, czyli ruch oraz kolizje*/
    public void animate()
    {
        if (Handler.raiseUp){
            resetForNextLevel();
            Handler.raiseUp = false;
        }
        if(Handler.toReset) {
            resetGame();
            Handler.toReset = false;
            //System.out.println("zresetowano gre");
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
        checkClock();
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
        for (Bonus b: bonuses) {
            if (b.getBounds().intersects(bomber.getHeart()) && b.isVisible) {
                if (b.name.equals("speed")) {
                    Hero.accelerate(true);
                    b.setVisible(false);
                }
                if (b.name.equals("power")) {
                    b.setVisible(false);
                    bomber.heal();
                }
                if (b.name.equals("slow")) {
                    Alien.stop(true);
                    b.setVisible(false);
                }
                b.timer.start();
            }
        }
    }
    /** Funkcja sprawdzajaca czy potwor zderzyl sie z przeszkoda, badz bohaterem
     * @param alien pojedynczy potwor*/
    public void checkAlienCollision(Alien alien)
    {
        if (bomber.getHeart().intersects(portal.getBounds()) && portal.isVisible){
            if (Handler.getCurrentLevel().equals("LEVEL1")) {
                portal.setVisible(false);
                loading.startLoading();
                Handler.levelUP(true);
                Result.countScore(Clock.timeLeft()/2 + 15 + bomber.getHealth()*5);
            }
            else if (Handler.getCurrentLevel().equals("LEVEL2")){
                portal.setVisible(false);
                loading.startLoading();
                Handler.levelUP(true);
                Result.countScore(Clock.timeLeft()/2 + 30 + bomber.getHealth()*5);
            }
            else {
                portal.setVisible(false);
                Result.countScore(Clock.timeLeft()/2 + 45 + bomber.getHealth()*5);
                closeAndSave();
            }
        }
        if (alien.getHeart().intersects(bomber.getHeart()) && alien.isVisible && !bomber.flag){
            bomber.flag = true;
            bomber.reduceHealth();
            bomber.timer.start();
            if (bomber.getHealth() == 0 && Handler.currentState == Handler.STATES.GAME)
            {
                closeAndSave();
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
                if (b.getBounds().intersects(s.getBounds()) && b.isReady && s instanceof Obstacle && s.isVisible){
                    s.setVisible(false);
                    Result.countScore(1.0);
                }
            }
            for (Alien a : aliens){
                if(b.getBounds().intersects(a.getBounds()) && b.isReady && a.isVisible) {
                    a.setVisible(false);
                    Result.countScore(10);
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
                bomber.wasNearby = true;
                bomber.timer.start();
                if (bomber.getHealth() == 0 && Handler.currentState == Handler.STATES.GAME) {
                    closeAndSave();
                }
                break;
            }
        }
    }
    /** Rzucanie bomb */
    public void fire()
    {
        if (Bomb.counter < 15)
            bombs.add(new Bomb(bomber.x - bomber.width/12 ,bomber.y + bomber.height/3));
    }
    /** Resetowanie rozgrywki do stanu poczatkowego */
    public void resetGame(){
        loadMap();
        loadAliens();
        bombs.clear();
        bomber.restoreHero();
        Bomb.counter = 0;
        portal.setVisible(true);
        Result.resetScore();
        loadBonuses();
    }
    /** Przygotowanie planszy na nastepny poziom*/
    public void resetForNextLevel(){
        loadMap();
        loadAliens();
        bombs.clear();
        bomber.restoreHero();
        Bomb.counter = 0;
        portal.setVisible(true);
        loadBonuses();
    }

    /** Sprawdzanie stanu zegara gry */
    public void checkClock(){
        if(Clock.timeLeft() == 0 && Handler.currentState == Handler.STATES.GAME){
            String nick;
            nick = this.score.saveResult();
            if (Handler.isAppOnline() && nick != null)
                client.sendScore(nick);
            Handler.currentState = Handler.STATES.MENU;
            Handler.toReset = true; // gra gotowa do zresetowania
            Handler.resetLevel(); // ustawianie poziomu na level pierwszy
        }
    }
    /** Metoda konczaca kazda rozgrywke*/
    public void closeAndSave(){
        String nick;
        nick = this.score.saveResult();
        if (Handler.isAppOnline() && nick != null)
            client.sendScore(nick);
        Handler.currentState = Handler.STATES.MENU;
        Handler.toReset = true;
        Handler.resetLevel();
    }
    /** Wczytanie dostepnych w grze bonusow */
    public void loadBonuses()
    {
        if (bonuses.size() != 0)
            bonuses.clear();
        int speedBonusX= Integer.parseInt(Config.cfg.getProperty("speedX-"+Handler.getCurrentLevel()));
        int speedBonusY= Integer.parseInt(Config.cfg.getProperty("speedY-"+Handler.getCurrentLevel()));
        int powerBonusX= Integer.parseInt(Config.cfg.getProperty("powerX-"+Handler.getCurrentLevel()));
        int powerBonusY= Integer.parseInt(Config.cfg.getProperty("powerY-"+Handler.getCurrentLevel()));
        int slowBonusX= Integer.parseInt(Config.cfg.getProperty("slowX-"+Handler.getCurrentLevel()));
        int slowBonusY= Integer.parseInt(Config.cfg.getProperty("slowY-"+Handler.getCurrentLevel()));
        bonuses.add(new Bonus(speedBonusX, speedBonusY, "speed"));
        bonuses.add(new Bonus(powerBonusX, powerBonusY, "power"));
        bonuses.add(new Bonus(slowBonusX, slowBonusY, "slow"));
    }
}

