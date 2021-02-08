import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Klasa reprezentujaca przyciski widoczne podczas gry */
public class Bonus extends Sprite implements ActionListener {
    /** Wczytana grafika z pliku*/
    private  BufferedImage image;
    /** Przeskalowana grafika*/
    private Image icon;
    /** Nazwa przycisku*/
    protected String name;
    /** Timer sluzacy do animacji */
    protected Timer timer = new Timer(15000,this);

    /** Konstruktor klasy
     * @param x wspolrzedna X
     * @param y wspolrzedna Y
     * @param name nazwa bonusu*/
    public Bonus (int x, int y, String name)
    {
        super(x,y);
        this.name = name;
        width = Integer.parseInt(Config.cfg.getProperty("portalW"));
        height = Integer.parseInt(Config.cfg.getProperty("portalH"));
        loadImage();
        uploadImage();
    }
    /** Nadpisana metoda z klasy Sprite*/
    @Override
    public int getX() {
        return (int) Math.round((double)(this.x*MyPanel.windowW)/360);
    }

    /** Nadpisana metoda z klasy Sprite*/
    @Override
    public int getY() {
        return (int) Math.round((double)(this.y*MyPanel.windowH)/360);
    }
    /** Metoda sluzaca do zaladowania grafiki z pliku config.txt
     * @return Grafika bonusu*/
    public void loadImage() {
        File file = new File(Config.cfg.getProperty(name+"Bonus"));
        try{
            image = ImageIO.read(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Skalowanie ikony*/
    public void uploadImage() {
        int w = Integer.parseInt(Config.cfg.getProperty("bonusW"));
        int h = Integer.parseInt(Config.cfg.getProperty("bonusH"));
        icon = image.getScaledInstance((int)((double)w*MyPanel.windowW)/360,(int)((double)h*MyPanel.windowH)/360, Image.SCALE_SMOOTH);
    }

    /** Metoda sluzaca do rysowania bonusow
     * @param g Zmienna sluzaca do wywolywania funkcji rysujacych
     * @param x,y Informacja o miejscu w ktorym zostanie narysowana grafika
     */
    public void paintBlock(Graphics g, int x, int y) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(icon, x, y, null);

    }
    /** Zwracanie wysokosci grafiki bonusu
     * @return wysokosc*/
    public int getH()
    {
        return icon.getHeight(null);
    }
    /** zwracanie szerokosci grafiki bonusu
     * @return szerokosc*/
    public int getW()
    {
        return icon.getWidth(null);
    }


    /** Metoda wywolujaca sie co 15 sekundy - czas trwania bonusu. W niej ustawiane sa flagi.
     * @param e zmienna typu ActionEvent */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (Hero.getAcceleration())
            Hero.accelerate(false);
        if (Alien.getStop())
            Alien.stop(false);
        timer.stop();
    }
}