import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Klasa reprezentujaca portal przejscia do kolejnego poziomu */
public class Portal extends Sprite{
    /** Wczytana grafika z pliku */
    private static BufferedImage image;
    /** Przeskalowny portal */
    private static Image icon;
    /** Konstruktor */
    public Portal(int x, int y)
    {
        super(x,y);
        width = Integer.parseInt(Config.cfg.getProperty("portalW"));
        height = Integer.parseInt(Config.cfg.getProperty("portalH"));
        loadImage();
        uploadImage();
    }
    public void paintBlock(Graphics g,int x,int y){
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(icon,x,y,null);
    }
    /** Metoda sluzaca do zaladowania grafiki z pliku config.txt */
    public static void loadImage() {
        File file = new File(Config.cfg.getProperty("portalIMG"));
        try{
            image = ImageIO.read(file); //
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Metoda sluzaca do skalowania ikony */
    public static void uploadImage() {
        int w = Integer.parseInt(Config.cfg.getProperty("portalW"));
        int h = Integer.parseInt(Config.cfg.getProperty("portalH"));
        icon = image.getScaledInstance((int)((double)w*MyPanel.windowW)/360,(int)((double)h*MyPanel.windowH)/360, Image.SCALE_SMOOTH);
    }

    @Override
    public int getX() {
        return (int) Math.round((double)(this.x*MyPanel.windowW)/360);
    }
    @Override
    public int getY() {
        return (int) Math.round((double)(this.y*MyPanel.windowH)/360);
    }

    /** Zwracanie szerokosci portalu
     * @return szerokosc*/
    public int getWidth(){
        return icon.getWidth(null);
    }
    /** Zwracanie wysokosci portalu
     * @return wysokosc*/
    public int getHeight(){
        return icon.getHeight(null);
    }
}
