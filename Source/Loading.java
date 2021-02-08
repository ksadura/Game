import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Klasa pomocnicza sluzaca do oblsugi animacji np. ladowanie miedzy poziomami */
public class Loading implements ActionListener {

    /** Zmienna typu Timer.swing reprezentujaca czas ladowania */
    private Timer timer = new Timer(2500,this);
    /** Zmienna przechowujaca tlo aplikacji*/
    private Background background;
    /** Wczytana grafika z pliku */
    private static BufferedImage image;
    /** Przeskalowny portal */
    private static Image icon;

    /** Konstruktor*/
    public Loading(){
        this.background = new Background(0,0);
        loadImage();
        uploadImage();
    }
    /** Metoda sluzaca do rysowania efektu ladowania
     * @param g Zmienna sluzaca do wywolywania funkcji rysujacych
     */
    public void render(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        background.paintBlock(g2D,background.getX(),background.getY());
        g2D.drawImage(icon, 0,0,null);
    }

    /** Rozpoczynanie ladowania */
    public void startLoading(){
        Handler.currentState = Handler.STATES.LOADING;
        if(timer.isRunning())
            timer.restart();
        else
            timer.start();
    }

    /** Metoda wywolujaca sie po 4 sekundach od uruchomienia timera.
     * @param e zmienna typu ActionEvent */
    @Override
    public void actionPerformed(ActionEvent e) {
        Handler.currentState = Handler.STATES.GAME;
        timer.stop();
        Clock.restartClock();
    }

    /** Metoda sluzaca do zaladowania grafiki z pliku config.txt */
    public static void loadImage() {
        File file = new File(Config.cfg.getProperty("loadingIMG"));
        try{
            image = ImageIO.read(file); //
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Metoda sluzaca do skalowania ikony */
    public static void uploadImage() {
        icon = image.getScaledInstance(MyPanel.windowW,MyPanel.windowH, Image.SCALE_SMOOTH);
    }

}
