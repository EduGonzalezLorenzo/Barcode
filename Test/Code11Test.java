import org.junit.Test;
import static org.junit.Assert.*;

public class Code11Test {
    @Test
    public void encodeTest1() {
        assertEquals("█ ██  █ █ █ ██ █ ██  █", Code11.encode("*0*"));
        assertEquals("█ ██  █ █ █ ██ ██ █ ██ █  █ ██ ██  █ █ █ ██ ██ ██ ██ █ █  █ ██ █ ██  █",
                Code11.encode("*0123452*"));
        assertEquals("█ ██  █ ██ █ ██ █  █ ██ ██ █ ██ █ ██ █ ██ █ █ █ █ ██ ██ █  █ █ █  ██ ██ ██ █ █ ██ ██ █ █  ██ █ ██  █",
                Code11.encode("*121-9087547*"));
        assertEquals("█ ██  █ █  █ ██ ██ ██ █ █  █ ██ ██ █ ██ █ ██ ██ █ █  ██ ██ █ ██ █ ██ █ ██ █  █ ██ ██ █ █ █  ██ █ ██ ██ █ ██ █ ██ █ █ █  ██ █ ██ ██ █ ██ █ ██ █  █ ██ ██ █ ██ █ █ ██ █ █ ██ █ █ ██ █ ██ █ █ █  ██ ██ █  █ ██ ██ █ █ ██  █",
                Code11.encode("*2521471-8574-965121000-785*"));
    }

    @Test
    public void decodeTest1() {
        assertEquals("*0*", Code11.decode("█ ██  █ █ █ ██ █ ██  █"));
        assertEquals("*121-9087547*", Code11.decode("█ ██  █ ██ █ ██ █  █ ██ ██ █ ██ █ ██ █ ██ █ █ █ █ ██ ██ █  █ █ █  ██ ██ ██ █ █ ██ ██ █ █  ██ █ ██  █"));
        assertEquals("*0*", Code11.decode("  █ ██  █ █ █ ██ █ ██  █     "));
    }

    @Test
    public void decodeTest2() {
        assertEquals("*0*", Code11.decode("█ ███   █ █ █ ███ █ ███   █"));
        assertEquals("*0*", Code11.decode("██ ███   █ █ █ ███ █ ███   █"));

        assertEquals("*0*", Code11.decode("███   █████     ███   ███   ███   █████   ███   █████     ███"));
        assertEquals("*0*", Code11.decode("███████       ██████████           ███████       ███████       ███████       ██████████       ███████       ██████████            ███████"));
    }

    @Test
    public void invalidCodes() {
        assertNull(Code11.decode("█ ███  ██ █ █ █ █ ███ █ ███   █"));
        assertNull(Code11.decode("█ █      ██   █ "));
        assertNull(Code11.decode("█ ███   █ █ █ █A█ █ █T█   ██"));
    }

    @Test
    public void imageTest() {
        assertEquals("*0123452*", Code11.decodeImage(UtilTests.getImageAsString("code11_0123452.ppm")));
        assertEquals("*0123-4567*", Code11.decodeImage(UtilTests.getImageAsString("code11_0123-4567.ppm")));
        assertEquals("*7867865835409892-654423523452341109-564*", Code11.decodeImage(UtilTests.getImageAsString("code11_buf.ppm")));
//        assertEquals("*6675-981-743*", Code11.decodeImage(UtilTests.getImageAsString("code11_6675-981-743.ppm")));
        assertEquals("*6675-981-743*", Code11.decodeImage(UtilTests.getImageAsString("code11_6675-981-743_bis.ppm")));
//        assertEquals("*3576-989*", Code11.decodeImage(UtilTests.getImageAsString("code11_3576-989.ppm")));
//        assertEquals("*446688120*", Code11.decodeImage(UtilTests.getImageAsString("code11_446688120.ppm")));
//        assertEquals("*9854-9812*", Code11.decodeImage(UtilTests.getImageAsString("code11_9854-9812.ppm")));
    }

    @Test
    public void reverseImageTest() {
        assertEquals("*5*", Code11.decodeImage(UtilTests.getImageAsString("code11_5_.ppm")));
        assertEquals("*789-12-98*", Code11.decodeImage(UtilTests.getImageAsString("code11_789-12-98_.ppm")));
    }

    @Test
    public void rotatedImageTest() throws Exception {
        assertEquals("*3659-542*", Code11.decodeImage(UtilTests.getImageAsString("code11_3659-542.ppm")));
        assertEquals("*77-98-4*", Code11.decodeImage(UtilTests.getImageAsString("code11_77-98-4.ppm")));
        assertEquals("*17-567-38*", Code11.decodeImage(UtilTests.getImageAsString("code11_17-567-38.ppm")));
        assertEquals("*68-5578-1*", Code11.decodeImage(UtilTests.getImageAsString("code11_68-5578-1.ppm")));
    }

    @Test
    public void generateImageTest() throws Exception {
        assertEquals("f8ad17776135622759a91b6e7e7105ab",
                UtilTests.getHexDigest("code11_01234.ppm",
                        Code11.generateImage("*01234*")));

        assertEquals("2aa6fd5a9b6a176dbb7ff2c7604ab572",
                UtilTests.getHexDigest("code11_125691.ppm",
                        Code11.generateImage("*12-56-91*")));

        assertEquals("85c296102cc72cee7cbf80ec6fdf8b56",
                UtilTests.getHexDigest("code11_0123452.ppm",
                        Code11.generateImage("*0123452*")));
    }
}