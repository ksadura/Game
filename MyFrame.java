import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**Klasa MyFrame dziedziczaca po JFrame sluzaca do tworzenia okna gry.To do niej zostaje dodany komponent typu JPanel
 * @see javax.swing.JFrame  */
public class MyFrame extends JFrame {
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
        this.getContentPane().addComponentListener(new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
            Component c = (Component) e.getSource();
            MyPanel.windowW = c.getWidth();
            MyPanel.windowH = c.getHeight();
            Game.getResizedImage();
        }
    });
    }
}
