import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Przeszkadzajacy potwor */
public class Alien extends Sprite {
    /** Wczytana grafika z pliku konfiguracyjnego */
    private static BufferedImage image;
    /** Przeskalowana grafika */
    private static Image icon;

    /** Konstruktor
     * @param x Wspolrzedna x
     * @param y Wspolrzedna y*/
    public Alien(int x, int y){
        super(x,y);
        this.height = Integer.parseInt(Config.cfg.getProperty("alienH"));
        this.width = Integer.parseInt(Config.cfg.getProperty("alienW"));
        uploadImage();
    }

    public void paintBlock(Graphics g, int x, int y) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(icon, x, y, null);
    }

    /** Metoda sluzaca do zaladowania grafiki z pliku config.txt
     * @return Grafika potwora */
    public static BufferedImage  loadImage() {
        File file = new File(Config.cfg.getProperty("alienIMG"));
        try{
            image = ImageIO.read(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /** Skalowanie ikony potwora */
    public static void uploadImage() {
        int w = Integer.parseInt(Config.cfg.getProperty("alienW"));
        int h = Integer.parseInt(Config.cfg.getProperty("alienH"));
        icon = loadImage().getScaledInstance((int)((double)w*MyPanel.windowW)/360,(int)((double)w*MyPanel.windowH)/360, Image.SCALE_SMOOTH);
    }

    public int getX() {
        return (int) Math.round((double)(this.x*MyPanel.windowW)/360);
    }

    public int getY() {
        return (int) Math.round((double)(this.y*MyPanel.windowH)/360);
    }
}
