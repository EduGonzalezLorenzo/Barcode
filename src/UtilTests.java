import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class UtilTests {

    static void writeToFile(String fname, String result) throws Exception {
        Path rootDir = Paths.get(".").normalize().toAbsolutePath();
        Files.writeString(Paths.get(rootDir + "/src/out/" + fname), result);
    }

    static String getHexDigest(String fname, String result) throws Exception {
        writeToFile(fname, result);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(result.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        BigInteger bi = new BigInteger(1, digest);
        return String.format("%0" + (digest.length << 1) + "x", bi);
    }

    static String getImageAsString(String s) {
        try {
            Path rootDir = Paths.get(".").normalize().toAbsolutePath();
            return new String(Files.readAllBytes(Paths.get(rootDir.toString() + "/src/imgs/" + s)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
