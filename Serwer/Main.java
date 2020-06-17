import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** Klasa reprezentujaca uruchomienie programu
 * @author Dorota Florianczyk & Krzysztof Sadura
 * */
public class Main {

    /** Funkcja main uruchamiajaca serwer
     * @param args nieuzywany parametr*/
    public static void main(String[] args) {
	System.out.println("Uruchomiono serwer, mozesz go zminimalizowac");
	System.out.println("Aby go wylaczyc zamknij okno terminala");
	    try(ServerSocket serverSocket = new ServerSocket(5000)){
	        while(true) {
                Socket socket = serverSocket.accept();
                new Server(socket);
            }
        }
	    catch (IOException e){
            System.out.println("Server exception " + e.getMessage());;
        }
    }
}
