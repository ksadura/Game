import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Klasa reprezentujaca liste 10 najlepszych wynikow uzyksanych w grze. Wystepuja dwa przypadki -
 * ranking uzytkownikow offline oraz ranking dla tych, ktorzy graja przez siec */
public class Ranking {

    /** Uzyta czcionka */
    private static Font f;
    /** Rozmiar obramowania */
    private static int sizeH, sizeW;
    /** Zmienna pelniaca role uzyskanego rezultatu*/
    private Result result;
    /** Zmienna przechowujaca tlo aplikacji*/
    private Background background;
    /** Wczytana grafika z pliku*/
    private static BufferedImage image;
    /** Przeskalowana grafika */
    private static Image icon;

    /** Konstruktor */
    public Ranking(){
        this.result = new Result();
        sizeH = 540;
        sizeW = 540;
        loadImage();
        uploadImage();
        this.background = new Background(0,0);
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, new File("./Resources/04b_25__.ttf"));
        }
        catch (IOException | FontFormatException e){
            e.printStackTrace();
        }
    }

    /** Rysowanie rankingu
     * @param g parametr typu Graphics*/
    public void render(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        background.paintBlock(g2D,background.getX(),background.getY());
        g2D.drawImage(icon,0,0,null);
        g2D.setFont(f.deriveFont((sizeW+sizeW)/54f));
        g2D.setColor(new Color(0,206,209));
        int i = 0;
        for(String key : result.getResults().keySet())
        {
            if (i == 10)
                break;
            g2D.drawString( i + 1 + ". " + key, (float) (sizeW/3.2), (float) (sizeH/4 +(i*(sizeH/20))));
            g2D.drawString(String.valueOf(result.getResults().get(key)), (float) (sizeW/1.7),(float) (sizeH/4+(i*(sizeH/20))));
            i++;
        }
    }
    /** Metoda sluzaca do zaladowania grafiki z pliku config.txt */
    public static void loadImage() {
        File file = new File(Config.cfg.getProperty("rankingTitle"));
        try{
            image = ImageIO.read(file); //
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Metoda sluzaca do skalowania ekranu */
    public static void uploadImage() {
        icon = image.getScaledInstance((int)((double) MyPanel.windowW),(int)((double)(MyPanel.windowH)), Image.SCALE_SMOOTH);
    }

    /** Aktualizacja wymiarow  */
    public static void getResizedScreen(int cW, int cH){
        sizeH = cH;
        sizeW = cW;
        uploadImage();
    }

    /** Wczytywanie rankingu offline */
    public void loadStandingsOffline(){
        this.result.loadResultOffline();
    }

    /** Wczytywanie rankingu online
     * @param list wyniki wczytane z serwera*/
    public void loadStandingsOnline(String list) { this.result.loadResultOnline(list);}


}
