import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**Klasa MyFrame dziedziczaca po JFrame sluzaca do tworzenia okna gry.To do niej zostaje dodany komponent typu JPanel
 * @see javax.swing.JFrame  */
public class MyFrame extends JFrame implements WindowStateListener {
    /** Konstruktor domyslny*/
    public MyFrame()
    {
        super("Dyna Blaster");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new MyPanel());
        pack();
        this.setVisible(true);
        setLocationRelativeTo(null);
        setResizable(true);
        this.addWindowStateListener(this);
        this.getContentPane().addComponentListener(new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
            Component c = (Component) e.getSource();
            MyPanel.windowW = c.getWidth();
            MyPanel.windowH = c.getHeight();
            Game.getResizedImage();
            MyPanel.getResizedLabels(MyPanel.windowW, MyPanel.windowH);
            Loading.uploadImage();
            Ranking.getResizedScreen(MyPanel.windowW,MyPanel.windowH);
        }
    });
    }

    /** Napisana metoda sluzaca do zapobiegania przejmowania calego okna przez aplikacje */
    @Override
    public void windowStateChanged(WindowEvent e) {
        if (e.getNewState() == JFrame.MAXIMIZED_BOTH){
            this.setExtendedState(JFrame.NORMAL);
            Toolkit.getDefaultToolkit().beep();
        }

    }
}
