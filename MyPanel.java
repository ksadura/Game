import javax.naming.ldap.Control;
import javax.swing.*;
import java.awt.*;

/** Klasa MyPanel dziedziczaca po klasie JPanel oraz implementujaca po Runnable
 *  odpowiedzialna za wyswietlanie panelu wraz z wygladem gry i animacja
 * @see javax.swing.JPanel
 * @see java.lang.Runnable
 */
public class MyPanel extends JPanel implements Runnable{
    /** Watek gry*/
    private Thread thread;
    /** Zmienna reprezentujaca dana gre*/
    public Game dynaBlaster;
    /** Zmienna przechowujaca wysokosc okna gry*/
    public static int windowH = Integer.parseInt(Config.cfg.getProperty("windowH"));
    /** Zmienna przechowujaca szerokosc okna gry*/
    public static int windowW = Integer.parseInt(Config.cfg.getProperty("windowW"));
    /** Zmienne typu JLabel wyswietlajace informacje podczas gry */
    private JLabel escLabel, healthLabel, bombsLabel;
    /** Zmienna reprezentujaca menu gry*/
    private Menu menu;
    /** Opoznienie w milisekundach */
    private final static int DELAY = Integer.parseInt(Config.cfg.getProperty("gameDelay"));

    /**Konstruktor*/
    public MyPanel()
    {
      this.dynaBlaster = new Game();
      addKeyListener(new ControlPanel(this));
      addMouseListener(new ControlPanel(this));
      setFocusable(true);
      setFocusTraversalKeysEnabled(false);
      setLayout(new FlowLayout(FlowLayout.RIGHT));
      escLabel = new JLabel("'ESC' - PAUZA/WZNOWIENIE");
      escLabel.setForeground(Color.WHITE);
      healthLabel = new JLabel();
      healthLabel.setForeground(Color.WHITE);
      bombsLabel = new JLabel();
      bombsLabel.setForeground(Color.WHITE);
      add(escLabel);
      add(healthLabel);
      add(bombsLabel);
      menu = new Menu();
    }

    /**Metoda wyswietlajaca wszystkie narysowane komponenty
     * @param g Zmienna typu Graphics*/
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (Handler.currentState == Handler.STATES.GAME) {
            dynaBlaster.render(g);
            escLabel.setVisible(true);
            healthLabel.setVisible(true);
            bombsLabel.setVisible(true);
        }
        else if (Handler.currentState == Handler.STATES.MENU) {
            menu.render(g);
            escLabel.setVisible(false);
            healthLabel.setVisible(false);
            bombsLabel.setVisible(false);
        }

    }
    /**Nadpisana metoda zwracajaca preferowane wymiary planszy
     * @return  Obiekt typu Dimension*/
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(windowW,windowH);
    }

    /** Nadpisana metoda addNotify. Wywoluje sie niejawnie oraz uruchamia watek*/
    @Override
    public void addNotify() {
        super.addNotify();
        this.thread = new Thread(this);
        this.thread.start();
        Game.isRunning = true;
    }

    /** Silnik gry */
    @Override
    public void run() {
        long beforeTime, diffTime, sleep;
        beforeTime = System.currentTimeMillis();
        while(Thread.currentThread() == this.thread)
        {
            uploadLabels();
            this.dynaBlaster.animate();
            repaint();
            diffTime = beforeTime - System.currentTimeMillis();
            sleep = DELAY - diffTime;
            if (sleep < 0)
                sleep = 2;
            try {
                checkPause();
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            beforeTime = System.currentTimeMillis();
        }

    }
    /** Metoda pauzujaca gre*/
    public void pause()
    {
        Game.isRunning = false;
    }
    /** Metoda wznowienia gry*/
    public synchronized void resume()
    {
        Game.isRunning = true;
        notifyAll();
    }
    /** Metoda odpowiedzialna za podtrzymanie gry w stanie pauzy*/
    public synchronized void checkPause()
    {
        while(!Game.isRunning)
        {
            try{
                wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
    /** Aktualizacja etykiet */
    public void uploadLabels()
    {
        healthLabel.setText("ZYCIA: " + dynaBlaster.bomber.getHealth());
        bombsLabel.setText("POZOSTALE BOMBY: " + (15 - Bomb.counter));
    }
}
