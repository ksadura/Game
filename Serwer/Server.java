import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/** Klasa reprezentujaca serwer. Implementuje interfejs Runnable, aby zepewnic obsluge wielu klientom jendoczesnie*/
public class Server implements Runnable {
    /** Zmienna typu Socket - gniazdo sluzace do komunikacji z klientami*/
    private Socket socket;
    /** Watek reprezentujacy serwer */
    private Thread thread;
    /** Konstruktor
     * @param socket zaakceptowane gniazdo od klienta*/
    public Server(Socket socket)
    {
        this.socket = socket;
        thread = new Thread(this);
        thread.start();
    }

    /** Nadpisana metoda run(). Kod ktory wykonuje serwer podczas swojego dzialania */
    @Override
    public void run() {
        try{
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter textToClient = new PrintWriter(socket.getOutputStream(),true);
            while(true){
                String[] msg = input.readLine().split(": ");
                if (msg[0].equals("EXIT")) {
                    textToClient.println("wylogowano");
                    System.out.println("klient wyszedł");
                    break;
                }
                switch(msg[0]){
                    case "LOGIN":
                        System.out.println("nowy " + msg[1]);
                        textToClient.println("zalogowano");
                        break;
                    case "SAVE_SCORE":
                        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("resultsOnline.txt", true))){
                            fileWriter.write(msg[1] + "-");
                        }
                        catch (IOException e){ e.printStackTrace();}
                        textToClient.println("zapisano wynik");
                        System.out.println(msg[1]);
                        break;
                    case "GET_SCORES":
                        try (Scanner reader = new Scanner(new BufferedReader(new FileReader("resultsOnline.txt")))){
                            String list = reader.nextLine();
                            textToClient.println(list);
                            System.out.println(msg[1]);
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }

                }
            }
        }
        catch (IOException e){
            System.out.println("Wyjątek serwera: " + e.getMessage());;
        }
        finally {
            try {
                socket.close();
            }
            catch(IOException e){
                System.out.println("Wyjątek serwera: " + e.getMessage());
            }
        }

    }
}
