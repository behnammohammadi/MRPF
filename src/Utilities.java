import java.io.IOException;
import java.nio.file.*;

public  class Utilities {
    public static  String ReadFileAsString(String path) throws IOException
    {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
