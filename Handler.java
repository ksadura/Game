/** Klasa sluzaca do sterowania stanami gry i ustawiania flag*/
public class Handler {
    /** Zbior typu enum przechowujacy mozliwe stany gry */
    public enum STATES {
        GAME, MENU, HELP
    }
    /** Aktualny stan gry */
    public static STATES currentState = STATES.MENU;
    /** Zmienna pelniaca funkcje flagi. Informuje czy gra ma ulec zresetowaniu*/
    public static boolean toReset = false;
}
