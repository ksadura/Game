import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Klasa reprezentujaca przyciski widoczne podczas gry */
public class Button extends Sprite {
    /** Wczytana grafika z pliku*/
    private  BufferedImage image;
    /** Przeskalowana grafika*/
    private Image icon;
    /** Nazwa przycisku*/
    protected String name;

    /** Konstruktor klasy
     * @param x wspolrzedna X
     * @param y wspolrzedna Y
     * @param name nazwa przycisku*/
    public Button (int x, int y, String name)
    {
        super(x,y);
        this.name = name;
        loadImage();
        uploadImage();
    }
    /** Nadpisana metoda z klasy Sprite*/
    @Override
    public int getX() {
        return (int) Math.round((double)(this.x*MyPanel.windowW)/540);
    }

    /** Nadpisana metoda z klasy Sprite*/
    @Override
    public int getY() {
        return (int) Math.round((double)(this.y*MyPanel.windowH)/540);
    }
    /** Metoda sluzaca do zaladowania grafiki z pliku config.txt
     * @return Grafika przycisku*/
    public void loadImage() {
        File file = new File(Config.cfg.getProperty(name+"Button"));
        try{
            image = ImageIO.read(file); //
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Skalowanie ikony*/
    public void uploadImage() {
        if (!name.equals("back"))
            icon = image.getScaledInstance((int)((double)MyPanel.windowW)/3,(int)((double)MyPanel.windowH)/12, Image.SCALE_SMOOTH);
        else
            icon = image.getScaledInstance((int)((double)MyPanel.windowW)/6,(int)((double)MyPanel.windowH)/24, Image.SCALE_SMOOTH);
    }

    /** Metoda sluzaca do rysowania przyciskow
     * @param g Zmienna sluzaca do wywolywania funkcji rysujacych
     * @param x,y Informacja o miejscu w ktorym zostana narysowane guziki
     */
    public void paintBlock(Graphics g, int x, int y) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(icon, x, y, null);

    }
    /** Zwracanie wysokosci grafiki guzika
     * @return wysokosc*/
    public int getH()
    {
        return icon.getHeight(null);
    }
    /** zwracanie szerokosci grafiki guzika
     * @return szerokosc*/
    public int getW()
    {
        return icon.getWidth(null); // zwracanie szerokosci grafiki guzika
    }
}
