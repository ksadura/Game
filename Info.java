import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/** Klasa reprezentujaca wyskakujace okno, na ktorym jest indormacja o wyniku proby polaczenia z serwerem */
public class Info extends JDialog {
    /** Czcionka tekstu wyswietlajacego sie w oknie */
    private Font f;
    /** Przycisk OK*/
    private JButton button = new JButton("OK");
    /** Tekst w sytuacji sukcesywnej proby polaczenia TCP*/
    private JLabel connected = new JLabel();
    /** Tekst w sytuacji niepowodzenia w probie laczenia z serwerem */
    private JLabel disconnected = new JLabel();

    /** Konstruktor
     * @param info tekst jaki ma sie wyswietlic w okienku*/
    public Info(String info){
        super();
        setTitle("localhost, port = 5000");
        setIconImage(Toolkit.getDefaultToolkit().createImage("server.png"));
        this.setModalityType(DEFAULT_MODALITY_TYPE);
        setFont();
        setSize(250,140);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(47,79,79));
        setLocationRelativeTo(null);
        setLayout(null);
        button.addActionListener(actionEvent -> dispose());
        setComponents(info);
        setVisible(true);
    }
    /** Pobieranie i ustawianie czcionki*/
    public void setFont(){
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, new File("./Resources/04b_25__.ttf"));
        }
        catch (IOException | FontFormatException e){
            e.printStackTrace();
        }
    }
    /** Modyfikacja i dodanie komponentow zawierajacych sie w oknie dialogowym*/
    public void setComponents(String msg){
        connected.setText(msg);
        disconnected.setText(msg);
        connected.setForeground(new Color(0,206,209));
        disconnected.setForeground(new Color(0,206,209));
        connected.setFont(f.deriveFont(17f));
        disconnected.setFont(f.deriveFont(17f));
        button.setFont(f.deriveFont(14f));
        connected.setBounds(70,15,140,30);
        disconnected.setBounds(25,15,200,30);
        button.setBounds(65,55,100,30);
        add(connected);
        add(disconnected);
        add(button);
        if (Handler.isAppOnline())
            disconnected.setVisible(false);
        else
            connected.setVisible(false);
    }
}
