import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/** Glowny bohater - Bomber */
public class Hero extends Sprite implements ActionListener {
    /** Predkosc w kierunku x */
    private int velX;
    /** Predkosc w kierunku y */
    private int velY;
    /** Wczytana grafika z pliku konfiguracyjnego */
    private static BufferedImage image;
    /** Przeskalowana grafika*/
    private static Image icon;
    /** Kierunek w jakim porusza sie bohater*/
    private int direction;
    /** Ilosc zyc */
    private int lives;
    /** Pomocnicza flaga uzywana przy kolizjach*/
    protected boolean flag = false;
    /** Timer pelniacy role watku */
    public Timer timer = new Timer(1000,this);

    /** Konstruktor
     * @param x Wspolrzedna x
     * @param y Wspolrzedna y*/
    public Hero(int x, int y) {
        super(x, y);
        this.height = Integer.parseInt(Config.cfg.getProperty("bomberH"));
        this.width = Integer.parseInt(Config.cfg.getProperty("bomberW"));
        uploadImage();
        this.lives = Integer.parseInt(Config.cfg.getProperty("bomberLives"));
    }

    /** Ruch Bombera */
    public void move() {
        x += velX;
        y += velY;
    }
    /** Ustawianie predkosci
     * @param velX predkosc w kierunku X
     * @param velY predkosc w kierunku Y*/
    public void setVel(int velX, int velY)
    {
        this.velX = velX;
        this.velY = velY;
    }

    public void paintBlock(Graphics g, int x, int y) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(icon, x, y, null);
    }

    /** Metoda sluzaca do zaladowania grafiki z pliku config.txt
     * @return Grafika bohatera*/
    public static BufferedImage loadImage() {
        File file = new File(Config.cfg.getProperty("bomberIMG"));
        try{
            image = ImageIO.read(file); //
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /** Skalowanie ikony Bombera */
    public static void uploadImage() {
        int w = Integer.parseInt(Config.cfg.getProperty("bomberW"));
        int h = Integer.parseInt(Config.cfg.getProperty("bomberH"));
        icon = loadImage().getScaledInstance((int)((double)w*MyPanel.windowW)/360,(int)((double)w*MyPanel.windowH)/360, Image.SCALE_SMOOTH);
    }

    public int getX() { return (int) Math.round((double)(this.x*MyPanel.windowW)/360); }
    public int getY() { return (int) Math.round((double)(this.y*MyPanel.windowH)/360); }
    public int getWidth()
    {
        return (int) Math.round((double)(this.width*MyPanel.windowW)/360);
    }
    public int getHeight()
    {
        return (int) Math.round((double)(this.height*MyPanel.windowH)/360);
    }
    public Rectangle getBounds()
    {
        return new Rectangle(this.getX(),this.getY(),this.getWidth(),this.getHeight());
    }
    /** Ustawianie kierunku w jakim sie porusza Bomber
     * @param direct nowy kierunek*/
    public void setDirection(int direct)
    {
        this.direction = direct;
    }
    /** Dostep do aktualnego kierunku Bombera
     * @return kierunek*/
    public int getDirection()
    {
        return this.direction;
    }
    /** Metoda zwracajaca aktualna ilosc zyc
     * @return zmienna typu int - ilosc zyc*/
    public int getHealth()
    {
        return this.lives;
    }
    /** Utrata zycia w wyniku kolizji*/
    public void reduceHealth() {
        this.lives -= 1;
    }
    /** Przywracanie domyslnych parametrow*/
    public void restoreHero()
    {
        this.x = Integer.parseInt(Config.cfg.getProperty("bomberX"));
        this.y = Integer.parseInt(Config.cfg.getProperty("bomberY"));
        this.lives = 3;
        setVel(0,0);

    }

    /** Nadpisana metoda z intefejsu ActionListener. Ustawia pomocnicza flage co 1.5 sekundy*/
    @Override
    public void actionPerformed(ActionEvent e) {
        flag = false;
        timer.stop();
    }
    /** Metoda zwracajaca pomniejszone wymiary potwora. Sluzy do dokladniejszej kolizji z bohaterem
     * @return zmienna typu Rectangle2D.Double*/
    public Rectangle2D.Double getHeart()
    {
        return new Rectangle2D.Double(getX() + 0.25*getWidth(),getY() + 0.25*getHeight(),0.5*getWidth(),0.5*getWidth());
    }
}
