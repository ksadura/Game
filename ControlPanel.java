import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/** Klasa ContorlPanel odpowiedzialna za obsluge zdarzen i sterowanie */
public class ControlPanel implements KeyListener, MouseListener {

    /**Zmienna reprezentujaca wyswietlany panel gry*/
    private MyPanel panel;
    /**Zmienna reprezentujaca menu gry*/
    private Menu m;

    /** Konstruktor
     * @param panel panel gry */
    public ControlPanel(MyPanel panel)
    {
        this.panel = panel;
        this.m = new Menu();
    }


    /**Nadpisana metoda wywolywana, gdy wystepuje klikniecie przysiku na klawiaturze
     * @param e Zmienna typu KeyEvent*/
    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if(c == KeyEvent.VK_LEFT && Game.isRunning) {
            panel.dynaBlaster.bomber.setVel(-1, 0);
            panel.dynaBlaster.bomber.setDirection(0);
        }
        if(c == KeyEvent.VK_UP && Game.isRunning) {
            panel.dynaBlaster.bomber.setVel(0, -1);
            panel.dynaBlaster.bomber.setDirection(1);
        }
        if(c == KeyEvent.VK_RIGHT && Game.isRunning) {
            panel.dynaBlaster.bomber.setVel(1, 0);
            panel.dynaBlaster.bomber.setDirection(2);
        }
        if(c == KeyEvent.VK_DOWN && Game.isRunning) {
            panel.dynaBlaster.bomber.setVel(0, 1);
            panel.dynaBlaster.bomber.setDirection(3);
        }
        if (c == KeyEvent.VK_ESCAPE && Handler.currentState == Handler.STATES.GAME){
            if (Game.isRunning) {
                panel.dynaBlaster.clk.getTimer().stop();
                panel.pause();
            }
            else {
                panel.resume();
                panel.dynaBlaster.clk.getTimer().restart();
            }
        }
        if (c == KeyEvent.VK_SPACE && Game.isRunning){
            panel.dynaBlaster.fire();
            Bomb.counter += 1;
        }
    }


    /**Nadpisana metoda wywolywana, gdy wystepuje klikniecie myszy
     * @param e Zmienna typu MouseEvent*/
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(Game.isRunning) {
            if (Handler.currentState == Handler.STATES.MENU) {
                if (isStartClicked(x,y)) {
                    Handler.currentState = Handler.STATES.GAME;
                    panel.dynaBlaster.clk.getTimer().restart();
                    Clock.restartClock();
                }
                if (isExitClicked(x,y))
                    System.exit(0);
                if (isRankClicked(x,y)) {
                    Handler.currentState = Handler.STATES.RANIKING;
                    if (Handler.isAppOnline())
                        this.panel.getRanking().loadStandingsOnline(panel.dynaBlaster.client.getScores());
                    else
                        this.panel.getRanking().loadStandingsOffline();
                }
                if (isLoginClicked(x,y)) {
                    if (Handler.isAppOnline()) {
                        panel.dynaBlaster.client.exit();
                    }
                    else {
                        panel.dynaBlaster.client = new Client();
                    }
                }
                if (isHelpClicked(x,y)){
                    Handler.currentState = Handler.STATES.HELP;
                }
            }
            else {
                if (isBackClicked(x,y) && Handler.currentState == Handler.STATES.GAME) {
                    Handler.currentState = Handler.STATES.MENU;
                    Handler.resetLevel();
                    Handler.toReset = true;
                    panel.dynaBlaster.clk.getTimer().stop();
                }
                else if (isBackClicked(x,y) && Handler.currentState != Handler.STATES.GAME)
                    Handler.currentState = Handler.STATES.MENU;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }


    /** Metoda isStartClicked sprawdzajaca czy zostal wcisniety przycisk Start
     * @param x wspolrzedna X
     * @param y wspolrzedna Y
     * @return prawda/falsz*/
    public boolean isStartClicked(int x, int y)
    {
        String id = "start";
        return x <= m.getButton(id).getX() + m.getButton(id).getW() && x >= m.getButton(id).getX() && y >= m.getButton(id).getY() && y <= m.getButton(id).getY() + m.getButton(id).getH();
    }

    /** Metoda isBackClicked sprawdzajaca czy zostal wcisniety przycisk Wroc
     * @param x wspolrzedna X
     * @param y wspolrzedna Y
     * @return prawda/falsz*/
    public boolean isBackClicked(int x, int y){
        String id = "back";
        return x <= m.getButton(id).getX() + m.getButton(id).getW() && x >= m.getButton(id).getX() && y >= m.getButton(id).getY() && y <= m.getButton(id).getY() + m.getButton(id).getH();
    }

    /** Metoda isExitClicked sprawdzajaca czy zostal wcisniety przycisk Wyjdz
     * @param x wspolrzedna X
     * @param y wspolrzedna Y
     * @return prawda/falsz*/
    public boolean isExitClicked(int x, int y){
        String id = "quit";
        return x <= m.getButton(id).getX() + m.getButton(id).getW() && x >= m.getButton(id).getX() && y >= m.getButton(id).getY() && y <= m.getButton(id).getY() + m.getButton(id).getH();

    }

    /** Metoda isRankClicked sprawdzajaca czy zostal wcisniety przycisk Ranking
     * @param x wspolrzedna X
     * @param y wspolrzedna Y
     * @return prawda/falsz*/
    public boolean isRankClicked(int x, int y){
        String id = "rank";
        return x <= m.getButton(id).getX() + m.getButton(id).getW() && x >= m.getButton(id).getX() && y >= m.getButton(id).getY() && y <= m.getButton(id).getY() + m.getButton(id).getH();
    }

    /** Metoda isLoginClicked sprawdzajaca czy zostal wcisniety przycisk Zaloguj/Wyloguj sie
     * @param x wspolrzedna X
     * @param y wspolrzedna Y
     * @return prawda/falsz*/
    public boolean isLoginClicked(int x, int y){
        String id = Handler.isAppOnline() ? "logout" : "login";
        return x <= m.getButton(id).getX() + m.getButton(id).getW() && x >= m.getButton(id).getX() && y >= m.getButton(id).getY() && y <= m.getButton(id).getY() + m.getButton(id).getH();
    }

    /** Metoda isHelpClicked sprawdzajaca czy zostal wcisniety przycisk Pomoc
     * @param x wspolrzedna X
     * @param y wspolrzedna Y
     * @return prawda/falsz*/
    public boolean isHelpClicked(int x, int y){
        String id = "help";
        return x <= m.getButton(id).getX() + m.getButton(id).getW() && x >= m.getButton(id).getX() && y >= m.getButton(id).getY() && y <= m.getButton(id).getY() + m.getButton(id).getH();
    }
}
