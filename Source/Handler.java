/** Klasa sluzaca do sterowania stanami gry i ustawiania flag*/
public class Handler {

    /** Zbior typu enum przechowujacy mozliwe stany gry */
    public enum STATES {
        GAME, MENU, HELP, LOADING, RANIKING
    }

    /** Aktualny stan gry */
    public static STATES currentState = STATES.MENU;

    /** Zmienna pelniaca funkcje flagi. Informuje czy gra ma ulec zresetowaniu*/
    public static boolean toReset = false;

    /** Zmienna pelniaca funkcje flagi. Informuje czy ma zostac wczytany nowy poziom*/
    public static boolean raiseUp = false;

    /** Zbior typu enum przechowujacy mozliwe poziomy trudnosci*/
    private enum LEVELS{
        LEVEL1, LEVEL2, LEVEL3
    }

    /** Aktualny poziom gry */
    private static LEVELS currentLevel = LEVELS.LEVEL1;

    /** Metoda sluzaca do awansowania na wyzszy poziom*/
    public static void levelUP(boolean advance){
        if (currentLevel == Handler.LEVELS.LEVEL1)
            currentLevel = LEVELS.LEVEL2;
        else
            currentLevel = LEVELS.LEVEL3;
        raiseUp = advance;
    }

    /** Zwracanie aktualnego poziomu
     * @return aktualny poziom*/
    public static String getCurrentLevel(){
        return currentLevel.toString();
    }

    /** Resetowanie poziomu i powrot do 1 */
    public static void resetLevel(){
        if (currentLevel != LEVELS.LEVEL1)
            currentLevel = Handler.LEVELS.LEVEL1;
    }
    /** Zmienna sprawdzajaca czy gra jest polaczona z siecia*/
    private static boolean online = false;

    /** Ustawianie gry na tryb online/offline */
    public static void setAppOnline(boolean b){
        online = b;
    }
    /** Sprawdzanie czy gra jest w trybie online
     * @return prawda/falsz */
    public static boolean isAppOnline(){
        return online;
    }
}
