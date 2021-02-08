import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/** Klasa Config sluzy do odczytu pliku konfiguracyujnego. Ten odczyt jest zaimplementowany w bloku statycznym
 * klasy*/
public class Config {
    /** Zmienna ktora jest wykorzystywana do przekazywania parametrow z pliku konfiguracyjnego*/
    public static Properties cfg = new Properties();
    static
    {
        try(InputStream input = new FileInputStream("config.txt"))
        {
            cfg.load(input);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
