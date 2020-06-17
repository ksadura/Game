import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Blok niezniszczalny */
public class Block extends Sprite {
    /** Wczytana grafika z pliku konfiguracyjnego*/
    private static BufferedImage image;
    /** Przeskalowana grafika */
    private static Image icon;

    /** Konstruktor
     * @param x Wspolrzedna x
     * @param y Wspolrzedna y*/
    public Block(int x, int y) {
        super(x,y);
        this.width = Integer.parseInt(Config.cfg.getProperty("blockW"));
        this.height = Integer.parseInt(Config.cfg.getProperty("blockH"));
        loadImage();
        uploadImage();
    }

    /** Metoda sluzaca do rysowania elementow mapy
     * @param g Zmienna sluzaca do wywolywania funkcji rysujacych
     * @param x,y Informacja o miejscu w ktorym zostanie narysowana grafika
     */
    public void paintBlock(Graphics g, int x, int y) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(icon, x, y, null);
    }

    /** Metoda sluzaca do zaladowania grafiki z pliku config.txt */
    public static void loadImage() {
        File file = new File(Config.cfg.getProperty("blockIMG"));
        try{
            image = ImageIO.read(file); //
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Skalowanie ikony bloku */
    public static void uploadImage() {
        icon = image.getScaledInstance((int)((double)MyPanel.windowW)/9,(int)((double)MyPanel.windowH)/9, Image.SCALE_DEFAULT);
    }

    public int getX() { return (int) Math.round((double)(this.x*(MyPanel.windowW/9))); }
    public int getY() { return (int) Math.round((double)(this.y*(MyPanel.windowH/9))); }
    public int getWidth()
    {
        return (int) Math.round((double)(MyPanel.windowW)/9);
    }
    public int getHeight()
    {
        return (int) Math.round((double)(MyPanel.windowH)/9);
    }
    public Rectangle getBounds()
    {
        return new Rectangle(this.getX(),this.getY(),this.getWidth(),this.getHeight());
    }
}
