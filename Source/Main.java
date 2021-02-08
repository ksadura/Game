import javax.swing.*;

/**Klasa Main uruchamiajaca cala aplikacje
 * @author Krzysztof Sadura & Dorota Florianczyk */

public class Main {

    /** Funkcja glowna - 'main*/
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
