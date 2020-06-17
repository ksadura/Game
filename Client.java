import java.io.*;
import java.net.Socket;

/** Klasa reprezentujaca klienta, ktory moze pobierac informacje z serwera. Instancja tej klasy
 *  jest uzywana w trybie online */
public class Client implements Runnable{

    /** Watek kliencki*/
    private Thread t;
    /** Zmienna konczaca prace klienta */
    private volatile boolean active;
    /** Zmienna typu BufferedReader sluzaca do wczytywania odpowiedzi plynacych z serwera */
    private BufferedReader textFromServer;
    /** Zmienna typu PrintWriter sluzaca do wysylania zadan serwerowi*/
    private PrintWriter textToServer;
    /** Odpowiedz serwera jako tekst */
    private String response;

    /** Konstruktor*/
    public Client() {
        t = new Thread(this);
        t.start();
        active = true;
    }

    /** Nawiazywanie i podtrzymanie polaczenia TCP pomiedzy serwerem a klientem */
    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", 5000)) {
            textFromServer = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            textToServer = new PrintWriter(socket.getOutputStream(), true);
            textToServer.println("LOGIN: klient");
            response = textFromServer.readLine();
            System.out.println(response);
            Handler.setAppOnline(true);
            new Info(response);
            while (active) {
               try{
                   Thread.sleep(100);
               }
               catch (InterruptedException e){ e.printStackTrace();}
               //Thread.onSpinWait(); //inna opcja zamiast sleep(), lecz wtedy zuzycie procesora w menedzerze zadan
                // jest wyzsze - wynosi ok. 30% przy sleep 8%. Zuzycie energii tez o wiele wyzsze
            }
        }
        catch (IOException e) {
            Handler.setAppOnline(false);
            new Info("nie udalo sie zalogowac!");
        }
    }

    /** Wylogowywanie sie z serwera*/
    public synchronized void exit(){
        textToServer.println("EXIT: wyjscie");
        try {
            response = textFromServer.readLine();
            System.out.println(response);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        active = false;
        new Info(response);
        Handler.setAppOnline(false);
    }

    /** Wysylanie do serwera uzyskanego wyniku wraz z nickiem*/
    public synchronized void sendScore(String score){
        textToServer.println("SAVE_SCORE: " + score);
        try {
            response = textFromServer.readLine();
            System.out.println(response);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /** Pobranie z serwera rezultatow */
    public synchronized String getScores(){
        textToServer.println("GET_SCORES: wczytaj_wyniki");
        try {
            response = textFromServer.readLine();
            return response;
        }
        catch (IOException e) {
            return "Nie-udalo-sie-wczytac-wynikow";
        }

    }
}