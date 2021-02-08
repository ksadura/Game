import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Blok zniszczalny */
public class Obstacle extends Sprite{
    /** Wczytana grafika z pliku*/
    private static BufferedImage image;
    /** Przeskalowana grafika */
    private static Image icon;

    /** Konstruktor
     * @param x Wspolrzedna x
     * @param y Wspolrzedna y*/
    public Obstacle(int x, int y) {
        super(x,y);
        width = Integer.parseInt(Config.cfg.getProperty("obstacleW"));
        height = Integer.parseInt(Config.cfg.getProperty("obstacleH"));
        loadImage();
        uploadImage();
    }

    public void paintBlock(Graphics g, int x, int y) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(icon, x, y, null);
    }

    /** Metoda sluzaca do zaladowania grafiki z pliku config.txt */
    public static void loadImage() {
        File file = new File(Config.cfg.getProperty("obstacleIMG"));
        try{
            image = ImageIO.read(file); //
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Metoda sluzaca do skalowania ikony przeszkody */
    public static void uploadImage() {
        icon = image.getScaledInstance((int)((double)(MyPanel.windowW/9)),(int)((double)(MyPanel.windowH/9)), Image.SCALE_DEFAULT);
    }

    public int getX() {
        return (int) Math.round((double)(this.x*(MyPanel.windowW/9)));
    }
    public int getY() {
        return (int) Math.round((double)(this.y*(MyPanel.windowH/9)));
    }
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
