import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/** Klasa, ktorej instancja pelni role zegara odmierzajacego czas rozgrywki */
public class Clock implements ActionListener {

    /** Calkowity czas na jeden poziom*/
    private static long totalTime;
    /** Timer sluzacy do odliczania pozostalego czasu */
    private Timer timer = new Timer(1000,this);

    /** Konstruktor */
    public Clock()
    {
        totalTime = 150000;
        timer.start();
    }

    /** Wyswietlanie pozostalego czasu na ekranie gry
     * @return sformatowany czas w postaci minut i sekund */
    public String elapsedTime()
    {
        return String.format("%d min, %d sek",
                TimeUnit.MILLISECONDS.toMinutes(totalTime),
                TimeUnit.MILLISECONDS.toSeconds(totalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime))
        );
    }

    /** Metoda timeLeft
     * @return pozostaly czas w sekundach */
    public static double timeLeft()
    {
        return (double) totalTime/1000;
    }

    /** Resetowanie czasu */
    public static void restartClock()
    {
        totalTime = 150000;
    }

    /** Dostep do timera dla innych klas
     * @return timer*/
    public Timer getTimer(){
        return this.timer;
    }

    /** Metoda wywolujaca sie co 1 sekunde. Efekt odmierzanego czasu
     * @param e zmienna typu ActionEvent */
    @Override
    public void actionPerformed(ActionEvent e) {
        totalTime -= 1000;
    }
}
