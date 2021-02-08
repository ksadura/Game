import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/** Klas reprezentujaca bomby w grze */
public class Bomb extends Sprite implements ActionListener {
    /** Wczytana i przeskalowana grafika */
    private  Image icon;
    /** Zmienna typu Timer.swing pelniaca funkcje watku dla Bomby */
    private Timer timer = new Timer(4000,this);
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

    /** Metoda sluzaca do rysowania bomb
     * @param g Zmienna sluzaca do wywolywania funkcji rysujacych
     * @param x,y Informacja o miejscu w ktorym zostanie narysowana grafika
     * @param panel ImageObserver */
    public void paintBlock(Graphics g, int x, int y, JPanel panel) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(icon, x, y, panel);

    }

    /** Skalowanie ikony*/
    public void uploadImage() {
        int w = Integer.parseInt(Config.cfg.getProperty("bombW"));
        int h = Integer.parseInt(Config.cfg.getProperty("bombH"));
        icon = Toolkit.getDefaultToolkit().createImage(Config.cfg.getProperty("bombIMG"))
                .getScaledInstance((int)((double)w*MyPanel.windowW)/360,(int)((double)h*MyPanel.windowH)/360, Image.SCALE_REPLICATE);
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
        return new Rectangle(getX(),getY(),icon.getWidth(null),icon.getHeight(null));
    }
}
