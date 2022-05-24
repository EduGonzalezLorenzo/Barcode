// https://en.wikipedia.org/wiki/Code_93

import java.util.*;

public class Code93 {
    ///////////////////////
    //Herramientas globales
    ///////////////////////
    static HashMap<String, String> IdToCharacter = new HashMap<>() {{
        put("0", "0");
        put("1", "1");
        put("2", "2");
        put("3", "3");
        put("4", "4");
        put("5", "5");
        put("6", "6");
        put("7", "7");
        put("8", "8");
        put("9", "9");

        put("10", "A");
        put("11", "B");
        put("12", "C");
        put("13", "D");
        put("14", "E");
        put("15", "F");
        put("16", "G");
        put("17", "H");
        put("18", "I");
        put("19", "J");
        put("20", "K");
        put("21", "L");
        put("22", "M");
        put("23", "N");
        put("24", "O");
        put("25", "P");
        put("26", "Q");
        put("27", "R");
        put("28", "S");
        put("29", "T");
        put("30", "U");
        put("31", "V");
        put("32", "W");
        put("33", "X");
        put("34", "Y");
        put("35", "Z");

        put("36", "-");
        put("37", ".");
        put("38", " ");
        put("39", "$");
        put("40", "/");
        put("41", "+");
        put("42", "%");
        put("43", "($)");
        put("44", "(%)");
        put("45", "(/)");
        put("46", "(+)");
        put("47", "*");
    }};

    static HashMap<String, String> characterToWidths = new HashMap<>() {{
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

    private static String getKeyByValue(String fragmet) {
        //Esta función se utliza para obtener de un hashmap una clave mediante su valor, cosa que podemos hacer gracias
        //a que en este caso tanto la clave como el valor son valores únicos. De lo contrario podriamos tener varias
        //respuestas y este método no serviría.
        String result = null;
        //Si el fragmento esta en la lista de codigos
        if (IdToCharacter.containsValue(fragmet)) {
            //Recorremos las claves hasta encontrar cual coincide con el fragmento
            for (Map.Entry<String, String> entry : IdToCharacter.entrySet()) {
                if (Objects.equals(entry.getValue(), fragmet)) {
                    result = entry.getKey();
                }
            }
        }
        return result;
    }

    //////////////////////////
    // Codifica emprant Code93
    //////////////////////////
    static String encode(String str) {
        //Divido el string en un arraylist de todos los caracters del codigo
        ArrayList<String> characters = new ArrayList<>(List.of(str.split("")));
        StringBuilder result = new StringBuilder();

        //Si el primer caracter decodificado no coincide con * entonces el formato no es correcto
        if (!characters.get(0).equals("*") || !characters.get(characters.size() - 1).equals("*")) return null;

        //Booleana para ignorar el primere *
        boolean firstSSFound = false;

        for (int i = 0; i < characters.size(); i++) {
            //Se va mirando cada segmento del codigo y se obtiene su equivalente en codigo de barras.
            if (Objects.equals(characters.get(i), "*")) {
                //Si es el primer * se ignora
                if (!firstSSFound) firstSSFound = true;
                    //Si no lo es se obtiene el checksum y se añade
                else result.append(getCheckSum(characters));
            }
            //se procesa el caracter y se añade a la respuesta
            result.append(toBarcode(characterToWidths.get(characters.get(i))));
        }
        //Se añade la barra final.
        return result.append("█").toString();
    }

    private static String getCheckSum(List<String> characters) {
        //Se eleminan los * del principio y del final y se le da la vuelta a la lista para facilitar su procesamiento
        List<String> barcodeContent = characters.subList(1, characters.size() - 1);
        Collections.reverse(barcodeContent);
        //Se obtiene el checksum C, se guarda y se añade al principio de la lista
        String c = checkSumAlgorithm(barcodeContent, 20);
        barcodeContent.add(0, c);
        //Se obtiene el checksum K y se borra el checkSum C de la la lista para no manipular el arrayList original
        String k = checkSumAlgorithm(barcodeContent, 15);
        barcodeContent.remove(0);
        //Se obtienen el barcode de ambos checksums y se devuelven.
        c = toBarcode(characterToWidths.get(c));
        k = toBarcode(characterToWidths.get(k));
        return c + k;
    }

    private static String checkSumAlgorithm(List<String> barcodeContent, int trigger) {
        int totalValue = 0;
        for (int i = 0; i < barcodeContent.size(); i++) {
            //Se obtiene el valor númerico de la letra
            int value = Integer.parseInt(getKeyByValue(barcodeContent.get(i)));
            int position = i + 1;
            //Para el checksum C cuando la posición sea mayor que 20 se vuelve a empezar, mientras que para el K se toma
            //15 como referencia. Este valor se pasa como parametro de entrada a la función.
            while (position > trigger) {
                //Si la posición es mayor que el trigger se le restará el trigger hasta que sea menor que este.
                position -= trigger;
            }
            //Se añade al valor total el valor del caracter por su posición modulo 47
            totalValue += (value * (position)) % 47;
        }
        //Una vez tratados todos los caracteres se hace el modulo 47 del total y se obtiene el caracter asignado a ese valor.
        totalValue = totalValue % 47;
        return IdToCharacter.get("" + totalValue);
    }

    private static String toBarcode(String s) {
        //Se toman todos los bits del segmento y se convierte en codigo de barras.
        char[] bitCode = s.toCharArray();
        StringBuilder barCode = new StringBuilder();

        for (int i = 0; i < bitCode.length; i++) {
            if (i % 2 == 0) {
                //Si es segmento par es barra (si 1 doble si 0 normal)
                if (bitCode[i] == '1') barCode.append("█");
                else if (bitCode[i] == '2') barCode.append("██");
                else if (bitCode[i] == '3') barCode.append("███");
                else barCode.append("████");
            } else {
                //Si es segmento inpar es espacio en blanco (si 1 doble si 0 normal)
                if (bitCode[i] == '1') barCode.append(" ");
                else if (bitCode[i] == '2') barCode.append("  ");
                else if (bitCode[i] == '3') barCode.append("   ");
                else barCode.append("    ");
            }
        }
        return barCode.toString();
    }

    ////////////////////////////
    // Decodifica emprant Code93
    ////////////////////////////
    static String decode(String str) {
        return "";
    }

    //////////////////////////////////////////////////////////////
    // Decodifica una imatge. La imatge ha d'estar en format "ppm"
    //////////////////////////////////////////////////////////////
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
