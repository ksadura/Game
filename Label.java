import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/** Klasa reprezentujaca tekst ktory wyswielta sie w czasie gry wraz z informacja */
public class Label extends JLabel {

    /** Nazwa etykiety */
    private String name;
    /** Czcionka */
    private Font f;
    /** Rozmiar etykiety*/
    private static int size = MyPanel.windowH;

    /** Konstruktor
     * @param name nadany typ*/
    public Label(String name){
        this.name = name;
        this.setForeground(Color.WHITE);
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, new File("04b_25__.ttf"));
        }
        catch (IOException | FontFormatException e){
            e.printStackTrace();
        }
        this.setFont(f.deriveFont(size/30f));
    }

    /** Aktualizacja etykiet w czasie rozgrywki
     * @param dynaBlaster gra w ktorej sie wyswietlaja*/
    public void uploadLabel(Game dynaBlaster) {
        switch(name){
            case "esc":
                this.setText("esc - pauza");
                break;
            case "health":
                this.setText("zycia: " + dynaBlaster.bomber.getHealth() + " ");
                break;
            case "bombs":
                if (Bomb.counter <= 15)
                    this.setText("liczba bomb: " + (15 - Bomb.counter) + " ");
                else
                    this.setText("liczba bomb: 0" + " ");
                break;
            case "clock":
                this.setText("czas: " + dynaBlaster.clk.elapsedTime() + " ");
                break;
            case "points":
                this.setText("punkty: " + Result.getScore() + " ");
                break;
        }
    }

    /** Zakrywanie wszystkich etykiet */
    public void coverAll(){
        this.setVisible(false);
    }

    /** Odkrywanie wszystkich etykiet */
    public void uncoverAll(){
        this.setVisible(true);
    }

    /** Skalowanie etykiet */
    public void resizeFont(int cW, int cH){
        int size = Math.min(cW,cH);
        this.setFont(f.deriveFont(size/30f));
    }

    /** Zwracanie typu (nazwy) etykiety
     * @return nazwa*/
    public String getName(){
        return this.name;
    }
}
