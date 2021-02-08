import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Pomoc- krotka instrukcja gry*/
public class Help extends Sprite {
    /** Wczytana grafika z pliku konfiguracyjnego*/
    private static BufferedImage image;
    /** Przeskalowana grafika */
    private static Image icon;

    /** Konstruktor
     * @param x Wspolrzedna x
     * @param y Wspolrzedna y */
    public Help (int x , int y) {
        super(x , y );
        this.height = Integer.parseInt(Config.cfg.getProperty("helpH"));
        this.width = Integer.parseInt(Config.cfg.getProperty("helpW"));
        loadImage();
        uploadImage();
    }

    /** Metoda sluzaca do rysowania instrukcji
     * @param g Zmienna sluzaca do wywolywania funkcji rysujacych
     * @param x,y Informacja o miejscu w ktorym zostanie narysowana grafika
     */
    public void paintBlock(Graphics g, int x, int y) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(icon, x, y, null);
    }

    /** Metoda sluzaca do zaladowania grafiki z pliku config.txt */
    public static void loadImage() {
        File file = new File(Config.cfg.getProperty("helpIMG"));
        try{
            image = ImageIO.read(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Metoda sluzaca do skalowania grafiki z instrukcja */
    public static void uploadImage() {
        int w = Integer.parseInt(Config.cfg.getProperty("helpW"));
        int h = Integer.parseInt(Config.cfg.getProperty("helpH"));
        icon = image.getScaledInstance((int)((double)w*MyPanel.windowW)/360,(int)((double)h*MyPanel.windowH)/360, Image.SCALE_SMOOTH);
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
}
