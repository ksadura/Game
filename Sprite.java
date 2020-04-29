import java.awt.*;

/** Abstrakcyjna klasa pelniaca role interfejsu dla pozostalych
 klas tworzacych gre, ktore dziedzicza po klasie Sprite*/
public abstract class Sprite {
    /**Wspolrzedna x*/
    protected int x;
    /**Wspolrzedna y*/
    protected int y;
    /** Szerokosc danego obiektu */
    protected int width;
    /** Wysokosc danego obiektu */
    protected int height;
    /**Zmienna informujaca czy dany obiekt jest widoczny w grze*/
    protected boolean isVisible;

    /** Konstruktor klasy z dwoma argumentami
     * @param x Przekazanie zmiennej x
     * @param y Przekazanie zmiennej y*/
    public Sprite(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.isVisible = true;
    }
    /** Metoda skalujaca pozycje na planszy
     * @return Aktualna wspolrzedna x*/
    public abstract int getX();
    /** Metoda skalujaca pozycje na planszy
     * @return Aktualna wspolrzedna y*/
    public abstract int getY();
    /** Metoda sluzaca do rysowania obiektow graficznych danej klasy
     * @param g Zmienna sluzaca do wywolywania funkcji rysujacych
     * @param x Informacja o miejscu w ktorym zostanie narysowana grafika
     * @param y Informacja o mijescu w ktorym zostanie narysowana grafika*/
    public abstract void paintBlock(Graphics g,int x,int y);
    /** Metoda zwracajaca aktualna szerokosc
     * @return szerokosc */
    public int getWidth()
    {
        return (int) Math.round((double)(this.width*MyPanel.windowW)/360);
    }
    /** Metoda zwracajaca aktualna wysokosc
     * @return wysokosc */
    public int getHeight()
    {
        return (int) Math.round((double)(this.height*MyPanel.windowH)/360);
    }
    /** Metoda zwracajaca wymiary obiektu
     * @return zmiennaa typu Rectangle */
    public Rectangle getBounds()
    {
        return new Rectangle(this.getX(),this.getY(),this.getWidth(),this.getHeight());
    }
    /** Metoda ustawiajaca lokalizacje
     * @param z wspolrzedna X */
    public void setX(int z)
    {
        this.x = (int)Math.round(((double)(z*360))/MyPanel.windowW);
    }
    /** Metoda ustawiajaca lokalizacje
     * @param z wspolrzedna Y */
    public void setY(int z)
    {
        this.y = (int)Math.round(((double)(z*360))/MyPanel.windowH);
    }
    /** Metoda sprawdzajca czy obiekt jest widoczny podczas gry
     * @param b prawda/falsz */
    public void setVisible(boolean b)
    {
        isVisible = b;
    }

}
