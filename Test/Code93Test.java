import org.junit.Test;

import static org.junit.Assert.*;

public class Code93Test {
    @Test
    public void encodeTest1() {
        assertEquals("█ █ ████ ██ █ █   ██ █ █   ██  █ ██ █ █ ████ █",
                Code93.encode("*A*"));
        assertEquals("█ █ ████ █ ██  █  ██  █  █ █ █ ██   █ █ ██   █  █ ██  █  ███ █ ██ ██ █  █ █ ████ █",
                Code93.encode("*HELLO*"));
        assertEquals("█ █ ████ ██ █ █   ██ █  █  ██ ██  █ ██ █ █   ██ █   █ ██ █ █   ██  █ █  ██ █ █   ██ █  █  ██ ██  █ ██ █ █   █  █  ██ █ █   █  █ █ ████ █",
                Code93.encode("*ABRACADABRA*"));
        assertEquals("█ █ ████ █   █ █  █ █  █   █ █   █  █ █    █ █  █ █   █  █  █  █  █   █ █ █ █    █   █  █ █    █ █ █   █ █  ██ █ █   ██ █  █  ██ █   █ ██  █ █  ██  █  █ ██   █ █ █ ██ █   █ ██  █  █ ██   █ █  ██ █  █   ██ █ █ █ ██   █ █  ██  █ █   ██ █  █ ██  █   █ ██ ██ ██ █  ██ ██  █ ██ █ ██  ██ █  ██ ██  █ ██ ██  ██ █ █ ██ ██  █ ██  ██ █  ██ ██ █  ███ █ █  █ ███ ███ █ █  ███ █  █ ███  █ █ █ ██ ███ █ ███ ██ ██ █ ███ █   █ ██ █ ███ ██ █ █ ████ █",
                Code93.encode("*01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%*"));
    }

    @Test
    public void decodeTest1() {
        assertEquals("*146-65-AB-59*", Code93.decode("█ █ ████ █ █  █   █  █ █   █  █   █ █  █ ███ █  █   █ █  █  █  █  █ ███ ██ █ █   ██ █  █  █  █ ███ █  █  █  █    █ █ █   ██ █ █ ██  █  █ █ ████ █"));
        assertEquals("*ABRACADABRA*", Code93.decode("█ █ ████ ██ █ █   ██ █  █  ██ ██  █ ██ █ █   ██ █   █ ██ █ █   ██  █ █  ██ █ █   ██ █  █  ██ ██  █ ██ █ █   █  █  ██ █ █   █  █ █ ████ █"));
        assertEquals("*A*", Code93.decode("██████       ██████      █████████████████████████      █████████████      ██████       ██████                   ████████████       ██████      ██████                   █████████████            ██████       ████████████      ███████      ██████      ██████████████████████████      ██████"));
        assertEquals("*HELLO*", Code93.decode("█ █ ████ █ ██  █  ██  █  █ █ █ ██   █ █ ██   █  █ ██  █  ███ █ ██ ██ █  █ █ ████ █"));
        assertEquals("*HELLO*", Code93.decode("██████       ██████      █████████████████████████      ███████      ████████████             ██████             ████████████             ██████            ███████      ██████      ███████      ████████████                   ██████       ██████      █████████████                   ██████            ███████      ████████████             ██████             ██████████████████       ██████      █████████████      ████████████       ██████            ███████      ██████      ██████████████████████████      ██████"));
        assertEquals("*AIGUA*", Code93.decode("█████     █████     ████████████████████     ██████████     █████     █████               █████     ██████████               █████     █████     ██████████     █████               ██████████          █████     ██████████     ██████████     █████     █████               █████          █████          █████          █████               █████     █████          █████     █████     ████████████████████     █████"));
        assertEquals("*ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%*", Code93.decode("█ █ ████ ██ █ █   ██ █  █  ██ █   █ ██  █ █  ██  █  █ ██   █ █ █ ██ █   █ ██  █  █ ██   █ █  ██ █  █   ██ █ █ █ ██   █ █  ██  █ █   ██ █  █ ██  █   █ ██ ██ ██ █  ██ ██  █ ██ █ ██  ██ █  ██ ██  █ ██ ██  ██ █ █ ██ ██  █ ██  ██ █  ██ ██ █  ███ █ █  █ ███ ███ █ █  ███ █  █ ███  █ █ █ ██ ███ █ ███ ██ ██ █ ███ █   █  █ ██ █ █   █ █ ████ █"));
        assertEquals("*01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%*", Code93.decode("█ █ ████ █   █ █  █ █  █   █ █   █  █ █    █ █  █ █   █  █  █  █  █   █ █ █ █    █   █  █ █    █ █ █   █ █  ██ █ █   ██ █  █  ██ █   █ ██  █ █  ██  █  █ ██   █ █ █ ██ █   █ ██  █  █ ██   █ █  ██ █  █   ██ █ █ █ ██   █ █  ██  █ █   ██ █  █ ██  █   █ ██ ██ ██ █  ██ ██  █ ██ █ ██  ██ █  ██ ██  █ ██ ██  ██ █ █ ██ ██  █ ██  ██ █  ██ ██ █  ███ █ █  █ ███ ███ █ █  ███ █  █ ███  █ █ █ ██ ███ █ ███ ██ ██ █ ███ █   █ ██ █ ███ ██ █ █ ████ █"));
    }

    @Test
    public void images() {
        assertEquals("*A*",
                Code93.decodeImage(
                        UtilTests.getImageAsString("code93_A.ppm")));

        assertEquals("*HELLO*",
                Code93.decodeImage(
                        UtilTests.getImageAsString("code93_HELLO.ppm")));

        assertEquals("*LICEU123*",
                Code93.decodeImage(
                        UtilTests.getImageAsString("code93_LICEU123.ppm")));

        assertEquals("*WIKIPEDIA*",
                Code93.decodeImage(
                        UtilTests.getImageAsString("code93_WIKIPEDIA.ppm")));

        assertEquals("*GOODBYE*",
                Code93.decodeImage(
                        UtilTests.getImageAsString("code93_GOODBYE.ppm")));

        assertEquals("*ABC-123$/+ . DEFG*",
                Code93.decodeImage(
                        UtilTests.getImageAsString("code93_ABC_etc.ppm")));
    }

    @Test
    public void rotatedImages() {
        assertEquals("*ALPHA*",
                Code93.decodeImage(
                        UtilTests.getImageAsString("code93_ALPHA.ppm")));

        assertEquals("*OMEGA*",
                Code93.decodeImage(
                        UtilTests.getImageAsString("code93_OMEGA.ppm")));

        assertEquals("*DELTA*",
                Code93.decodeImage(
                        UtilTests.getImageAsString("code93_DELTA.ppm")));

        assertEquals("*CHARLIE*",
                Code93.decodeImage(
                        UtilTests.getImageAsString("code93_CHARLIE.ppm")));
    }

    @Test
    public void rotatedLowQuality() {
        assertEquals("*FOXTROT*",
                Code93.decodeImage(
                        UtilTests.getImageAsString("code93_FOXTROT_lowquality2.ppm")));
    }

    @Test
    public void generateImageTest1() throws Exception {
        String imageStr;

        imageStr = Code93.generateImage("*1234*");
        assertEquals("*1234*", Code93.decodeImage(imageStr));

        imageStr = Code93.generateImage("*AA-BB-CC-17890-PALMA-PARIS-MUNICH*");
        assertEquals("*AA-BB-CC-17890-PALMA-PARIS-MUNICH*",
                Code93.decodeImage(imageStr));
    }


    @Test
    public void generateImageTest2() throws Exception {
        assertEquals("9616570b73764754cc8613259935a407",
                UtilTests.getHexDigest("code93_01234.ppm",
                        Code93.generateImage("*01234*")));

        assertEquals("27bf97c4b49468a2f5d4edd90236d3b0",
                UtilTests.getHexDigest("code93_HELLO-WORLD.ppm",
                        Code93.generateImage("*HELLO-WORLD*")));

        assertEquals("1bc9236c7dc02e58db9c7e1a696139a3",
                UtilTests.getHexDigest("code93_STARWARS.ppm",
                        Code93.generateImage("*STAR WARS 123456789*")));
    }
}