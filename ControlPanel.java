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
    @Override
    public void keyTyped(KeyEvent e) {

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
            if (Game.isRunning)
                panel.pause();
            else
                panel.resume();
        }
        if (c == KeyEvent.VK_SPACE && Game.isRunning){
            panel.dynaBlaster.fire();
            Bomb.counter += 1;
        }

    }

    /**Nadpisana metoda wywolywana po puszczeniu przycisku klawiaturzu
     * @param e Zmienna typu KeyEvent*/
    @Override
    public void keyReleased(KeyEvent e) {
//        int key = e.getKeyCode();
//        if (key == KeyEvent.VK_LEFT)
//            panel.dynaBlaster.bomber.setVel(0,0);
//        if (key == KeyEvent.VK_RIGHT)
//            panel.dynaBlaster.bomber.setVel(0,0);
//        if (key == KeyEvent.VK_UP)
//            panel.dynaBlaster.bomber.setVel(0,0);
//        if (key == KeyEvent.VK_DOWN)
//            panel.dynaBlaster.bomber.setVel(0,0);
    }

    /**Nadpisana metoda wywolywana, gdy wystepuje klikniecie myszy
     * @param e Zmienna typu MouseEvent*/
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(Game.isRunning) {
            if (Handler.currentState == Handler.STATES.MENU) {
                if (isStartClicked(x,y))
                    Handler.currentState = Handler.STATES.GAME;
            }
            else {
                Handler.currentState = Handler.STATES.MENU;
                Handler.toReset = true;
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {


    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    /** Metoda isStartClicked sprawdzajaca czy zostal wcisniety przycisk Start
     * @param x wspolrzedna X
     * @param y wspolrzedna Y
     * @return prawda/falsz*/
    public boolean isStartClicked(int x, int y)
    {
        String id = "start";
        return x <= m.getButton(id).getX() + m.getButton(id).getW() && x >= m.getButton(id).getX() && y >= m.getButton(id).getY() && y <= m.getButton(id).getY() + m.getButton(id).getH();
    }
}
