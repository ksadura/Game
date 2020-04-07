import javax.swing.*;

/**Klasa Main uruchamiajaca cala aplikacje*/
public class Main {
    /** Funkcja glowna-'main'
     * @param args Argument nieuzywany*/
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MyFrame();
            }
        });
    }
}
