import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Klas reprezentujaca bomby w grze */
public class Bomb extends Sprite implements ActionListener {
    /** Wczytana grafika bomby */
    private static BufferedImage image;
    /** Przeskalowana grafika */
    private static Image icon;
    /** Zmienna typu Timer.swing pelniaca funkcje watku dla Bomby */
    private Timer timer = new Timer(3000,this);
    /** Flaga informujaca czy bomba jest gotowa do detonacji*/
    protected boolean isReady;
    /** Licznik zuzytych bomb*/
    public static int counter;
    /** Konstruktor klasy
     * @param x wspolrzedna X
     * @param y wspolrzedna Y*/
    public Bomb(int x, int y)
    {
        super(x,y);
        width = Integer.parseInt(Config.cfg.getProperty("bombW"));
        height = Integer.parseInt(Config.cfg.getProperty("bombH"));
        uploadImage();
        isReady = false;
        if(timer.isRunning())
            timer.restart();
        else
            timer.start();

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

    @Override
    public void paintBlock(Graphics g, int x, int y) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(icon, x, y, null);

    }
    /** Metoda sluzaca do zaladowania grafiki z pliku config.txt
     * @return Grafika bomby*/
    public static BufferedImage loadImage() {
        File file = new File(Config.cfg.getProperty("bombIMG"));
        try{
            image = ImageIO.read(file); //
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    /** Skalowanie ikony*/
    public static void uploadImage() {
        int w = Integer.parseInt(Config.cfg.getProperty("bombW"));
        int h = Integer.parseInt(Config.cfg.getProperty("bombH"));
        icon = loadImage().getScaledInstance((int)((double)w*MyPanel.windowW)/360,(int)((double)h*MyPanel.windowH)/360, Image.SCALE_SMOOTH);
    }

    /** Metoda wywolujaca sie co 3 sekundy. W niej ustawiane sa widocznosc i gotowosc
     * @param e zmienna typu ActionEvent */
    @Override
    public void actionPerformed(ActionEvent e) {
        isReady = true;
        timer.stop();
        setVisible(false);
    }
    /** Zwracanie wymiarow bomby
     * @return obiekt klasy Rectangle*/
    public Rectangle getBounds()
    {
        return new Rectangle(getX() - getWidth(),getY() - getHeight(),3*getWidth(),3*getHeight());
    }
}
