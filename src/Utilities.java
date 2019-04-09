import java.io.IOException;
import java.nio.file.*;;
/**
 * Created by behnam on 12/25/18.*******
 */
public  class Utilities {
    public static  String ReadFileAsString(String path) throws IOException
    {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
