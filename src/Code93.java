// https://en.wikipedia.org/wiki/Code_93

import java.util.HashMap;

public class Code93 {
    static HashMap<String, String> codification = new HashMap<>() {{
        put("0", "131112");
        put("1", "111213");
        put("2", "111312");
        put("3", "111411");
        put("4", "121113");
        put("5", "121212");
        put("6", "121311");
        put("7", "111114");
        put("8", "131211");
        put("9", "141111");

        put("A", "211113");
        put("B", "211212");
        put("C", "211311");
        put("D", "221112");
        put("E", "221211");
        put("F", "231111");
        put("G", "112113");
        put("H", "112212");
        put("I", "112311");
        put("J", "122112");
        put("K", "132111");
        put("L", "111123");
        put("M", "111222");
        put("N", "111321");
        put("O", "121122");
        put("P", "131121");
        put("Q", "212112");
        put("R", "212211");
        put("S", "211122");
        put("T", "211221");
        put("U", "221121");
        put("V", "222111");
        put("W", "112122");
        put("X", "112221");
        put("Y", "122121");
        put("Z", "123111");

        put("-", "121131");
        put(".", "311112");
        put(" ", "311211");
        put("$", "321111");
        put("/", "112131");
        put("+", "113121");
        put("%", "211131");
        put("($)", "121221");
        put("(%)", "312111");
        put("(/)", "311121");
        put("(+)", "122211");
        put("*", "111141");
    }};

    // Codifica emprant Code93
    static String encode(String str) {
        return "";
    }

    // Decodifica emprant Code93
    static String decode(String str) {
        return "";
    }

    // Decodifica una imatge. La imatge ha d'estar en format "ppm"
    public static String decodeImage(String str) {
        return "";
    }

    // Genera imatge a partir de barcode code93
    // Unitat barra mínima: 3 pixels
    // Alçada: 100px
    // Marges: vertical: 5px, horizontal: 15px
    public static String generateImage(String s) {
        return "";
    }
}
