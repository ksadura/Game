import javax.swing.*;
import java.awt.*;
import java.util.Vector;

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
    /** Wektor przechowujacy obiekty klasy JLabel wyswietlajace informacje podczas gry */
    private static Vector<Label> labels;
    /** Zmienna reprezentujaca menu gry*/
    private Menu menu;
    /** Opoznienie w milisekundach */
    private final static int DELAY = Integer.parseInt(Config.cfg.getProperty("gameDelay"));
    /** Zmienna reprezentujaca najlepsze wyniki uzytkownikow*/
    private Ranking ranking;
    /** Instrukcja i pomoc dla uzytkownika */
    private Help help;

    /**Konstruktor*/
    public MyPanel()
    {
      this.dynaBlaster = new Game();
      addKeyListener(new ControlPanel(this));
      addMouseListener(new ControlPanel(this));
      setFocusable(true);
      setFocusTraversalKeysEnabled(false);
      setLayout(new FlowLayout(FlowLayout.RIGHT));
      this.loadLabels();
      menu = new Menu();
      ranking = new Ranking();
      help = new Help(0,0);
    }

    /**Metoda wyswietlajaca wszystkie narysowane komponenty
     * @param g Zmienna typu Graphics*/
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (Handler.currentState == Handler.STATES.GAME) {
            dynaBlaster.render(g, this);
            menu.renderBackButton(g);
            showLabels(true);
        }
        else if (Handler.currentState == Handler.STATES.MENU) {
            menu.render(g);
            showLabels(false);
        }
        else if (Handler.currentState == Handler.STATES.LOADING){
            dynaBlaster.loading.render(g);
            showLabels(false);
        }
        else if (Handler.currentState == Handler.STATES.RANIKING){
            ranking.render(g);
            showLabels(false);
            menu.renderBackButton(g);
        }
        else if (Handler.currentState == Handler.STATES.HELP){
            help.paintBlock(g,0,0);
            menu.renderBackButton(g);
            showLabels(false);
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
        while(Thread.currentThread() == this.thread)
        {
            uploadLabelsAndButtons();
            this.dynaBlaster.animate();
            repaint();
            try {
                checkPause();
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
    /**Tworzenie etykiet*/
    public void loadLabels(){
        labels = new Vector<>();
        labels.add(new Label("health"));
        labels.add(new Label("points"));
        labels.add(new Label("bombs"));
        labels.add(new Label("clock"));
        labels.add(new Label("esc"));
        for (Label l : labels)
            this.add(l);

    }
    /** Ukrywanie/zakrywanie etykiet*/
    public void showLabels(boolean b){
        if (b)
            labels.forEach(Label::uncoverAll);
        else
            labels.forEach(Label::coverAll);
    }
    /** Aktualizacja etykiet */
    public void uploadLabelsAndButtons()
    {
        for (Label l : labels) {
            l.uploadLabel(this.dynaBlaster);
            if (l.getName().equals("clock") && Clock.timeLeft() < 15)
                l.setForeground(new Color(243,78,78));
        }
    }
    /** Skalowanie etykiet */
    public static void getResizedLabels(int cW, int cH){
        for (Label l : labels)
            l.resizeFont(cW, cH);
    }
    /** Zwracanie rankingu */
    public Ranking getRanking(){
        return this.ranking;
    }
}
