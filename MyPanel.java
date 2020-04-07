import javax.swing.*;
import java.awt.*;

/** Klasa MyPanel dziedziczaca po klasie JPanel
 *  odpowiedzialna za wyswietlanie panelu wraz z wygladem gry
 * @see javax.swing.JPanel
 */
public class MyPanel extends JPanel {
    /** Zmienna reprezentujaca dana gre*/
    private Game dynaBlaster;
    /** Zmienna przechowujaca wysokosc okna gry*/
    public static int windowH = Integer.parseInt(Config.cfg.getProperty("windowH"));
    /** Zmienna przechowujaca szerokosc okna gry*/
    public static int windowW = Integer.parseInt(Config.cfg.getProperty("windowW"));

    /**Konstruktor*/
    public MyPanel()
    {
      this.dynaBlaster = new Game();
    }

    /**Metoda wyswietlajaca wszystkie narysowane komponenty
     * @param g Zmienna typu Graphics*/
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        dynaBlaster.render(g);
    }
    /**Nadpisana metoda zwracajaca preferowane wymiary planszy
     * @return  Obiekt typu Dimension*/
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(windowW,windowH);
    }
}
