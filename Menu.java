import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/** Klasa odpowiedzialna za wyglad i logike Menu */
public class Menu {
    /**Zmienna reprezentujaca tlo gry*/
    private Background background;
    /** Wczytana grafika z pliku konfiguracyjnego */
    private static BufferedImage image;
    /**Przeskalowana grafika napisu tytulowego gry*/
    private static Image title;
    /**Zmienna przchowujaca przyciski*/
    public static Vector<Button> buttons;

    /** Konstuktor domyslny*/
    public Menu()
    {
        background = new Background(0,0);
        uploadTitle();
        buttons = new Vector<>();
        loadButtons();
    }
    /** Metoda sluzaca do rysowania wszystkich elementow Menu
     * @param g Zmienna typu Graphics*/
    public void render (Graphics g)
    {
        Graphics2D g2D = (Graphics2D) g;
        background.paintBlock(g2D,background.getX(),background.getY());
        g2D.drawImage(title,0,0,null);
        for (Button b : buttons)
            b.paintBlock(g2D,b.getX(),b.getY());
    }
    /** Metoda sluzaca do zaladowania grafiki z pliku config.txt
     * @return Napis tytulowy gry */
    public static BufferedImage loadTitle() {
        File file = new File(Config.cfg.getProperty("titleIMG"));
        try{
            image = ImageIO.read(file); //
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    /** Skalowanie napisu tytulowego gry */
    public static void uploadTitle() {
        title = loadTitle().getScaledInstance((int)((double)(MyPanel.windowW)),(int)((double)(MyPanel.windowH/2)), Image.SCALE_SMOOTH);
    }

    /** Metoda sluzaca do zaladowania przyciskow */
    public void loadButtons()
    {
        buttons.add(new Button(180,240,"start"));
        buttons.add(new Button(180,295,"rank"));
        buttons.add(new Button(180,350,"help"));
        buttons.add(new Button(180,405,"quit"));
    }
    /** Funkcja pomocnicza, ktora powoduje uaktualnienie uzytych plikow graficznych podczas skalowania Menu */
    public static void uploadMenu()
    {
        for (Button b : buttons)
            b.uploadImage();
        uploadTitle();
    }
    /** Metoda sluzaca do wczytania odpowiedniego przycisku
     * @param name Zmienna typu String
     * @return Przycisk */
    public Button getButton(String name)
    {
        Button button = null;
        for (Button b : buttons) {
            if (b.name.equals(name))
                button = b;
        }
        return button;
    }


}
