import java.io.*;
import java.util.*;

/** Klasa reprezentujaca rezultat uzyskany podczas gry */
public class Result  {

    /** Liczba punktow */
    private static double score = 0.0;
    /** Slownik przechowujacy nick i punkty w trybie offline */
    private Map<String, Double> resultDictOff = new LinkedHashMap<>();
    /** Slownik przechowujacy nick i punkty w trybie online  */
    private Map<String, Double> resultDictOn = new LinkedHashMap<>();

    /** Zapisywanie rezultatu
     * @return wpisany nick do okna dialogowego wraz ze zdobycza punktowa */
    public String saveResult(){
        String nick = new Notification(null,"Twoj wynik: " + score).displayUI();
        if (!Handler.isAppOnline()) {
            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("results.txt", true))) {
                if (nick != null && nick.length() != 0)
                    fileWriter.write(nick + " " + score + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return nick + "=" + score;
    }

    /** Liczenie punktow
     * @param points dodatkowe punkty*/
    public static void countScore(double points){
        score += points;
    }

    /** Zwracanie ilosci punktow
     * @return rezultat */
    public static double getScore() {
        return score;
    }

    /** Resetowanie wyniku */
    public static void resetScore() {
        score = 0.0;
    }

    /** Wczytywanie wynikow z pliku .txt w trybie offline */
    public void loadResultOffline() {
        try (Scanner reader = new Scanner(new BufferedReader(new FileReader("results.txt")))) {
            while (reader.hasNextLine()) {
                String[] array = reader.nextLine().split(" ");
                resultDictOff.put(array[0], Double.parseDouble(array[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Wczytywanie wynikow w trybie online
     * @param list wyniki z serwera */
    public void loadResultOnline(String list){
        String [] array = list.split("-");
        for (String s : array){
            String [] nickAndScore = s.split("=");
            resultDictOn.put(nickAndScore[0], Double.parseDouble(nickAndScore[1]));
        }
    }

    /** Zwracanie slownika z nickiem i wynikiem
     * @return posortowany slownik z wynikami */
    public Map<String,Double> getResults(){
        if (Handler.isAppOnline())
            return sortByValue(resultDictOn);
        else
            return sortByValue(resultDictOff);
    }

    /** Sortowanie slownika po ilosci punktow
     * @param map slownik do posortowania
     * @return posortowany parametr 'map' */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
