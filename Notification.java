import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/** Klasa reprezentujaca wyskakujace okno, w ktorym zapisujemy swoj wynik i podajemy przyjety 'nick' */
public class Notification extends JDialog {

    /** Tekst widniejacy w oknie */
    private JLabel label = new JLabel("wpisz swoj nick: ");
    /** Miejsce do wpisania tekstu - nicku */
    private JTextArea text = new JTextArea();
    /** Informacja o zbyt malej ilosci znakow */
    private JLabel warning = new JLabel("minimum trzy znaki");
    /** Przycisk zatwierdzajacay i zapisujacy nick i wynik */
    private JButton button = new JButton("ZAPISZ");
    /** Uzyta czcionka*/
    private Font f;
    /** Nick wpisany przez gracza */
    private String typedNick;

    /** Konstruktor
     * @param frame tzw. parent component
     * @param title tytul okna */
    public Notification(JFrame frame, String title){
        super(frame, title);
        this.setIconImage(Toolkit.getDefaultToolkit().createImage("bomber.png"));
        this.setFont();
        this.setLabels();
        this.setModalityType(DEFAULT_MODALITY_TYPE);
        getContentPane().setBackground(new Color(47,79,79));
        setLocationRelativeTo(null);
        setLayout(null);
        button.addActionListener(actionEvent -> {
            typedNick = text.getText();
            if (typedNick.length() <= 2)
                warning.setVisible(true);
            else {
                dispose();
            }
        });
        this.setSize(280,180);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /** Wczytywanie i ustawianie czcionki */
    public void setFont(){
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, new File("04b_25__.ttf"));
        }
        catch (IOException | FontFormatException e){
            e.printStackTrace();
        }
    }

    /** Wyswietlanie okna podczas dzialania gry
     * @return wpisany nick przez gracza*/
    public String displayUI(){
        this.setVisible(true);
        return typedNick;
    }

    /** Modyfikacja i dodanie komponentow do okna */
    public void setLabels(){
        warning.setForeground(new Color(255,140,0));
        label.setForeground(new Color(0,206,209));
        label.setFont(f.deriveFont(17f));
        button.setFont(f.deriveFont(14f));
        text.setFont(f.deriveFont(17f));
        warning.setFont(f.deriveFont(11f));
        label.setBounds(70,0,140,30);
        text.setBounds(60,35,140,19);
        button.setBounds(79,70,100,30);
        warning.setBounds(80,105,140,30);
        add(label);
        add(text);
        add(button);
        add(warning);
        warning.setVisible(false);
    }
}