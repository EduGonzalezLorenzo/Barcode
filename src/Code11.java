
// Consultar taula https://en.wikipedia.org/wiki/Barcode#Linear_barcodes
// Code11: https://en.wikipedia.org/wiki/Code_11

// Generadors de codis:
//     https://barcode.tec-it.com/en/Code11
//     https://www.free-barcode-generator.net/code-11/
//     https://products.aspose.app/barcode/generate


import java.util.*;

public class Code11 {
    static HashMap<String, String> codification = new HashMap<>() {{
        put("0", "00001");
        put("1", "10001");
        put("2", "01001");
        put("3", "11000");
        put("4", "00101");
        put("5", "10100");
        put("6", "01100");
        put("7", "00011");
        put("8", "10010");
        put("9", "10000");
        put("-", "00100");
        put("*", "00110");
    }};

    // Codifica un String amb Code11
    static String encode(String s) {
        //Divido el string en un arraylist de todos los caracters del codigo
        ArrayList<String> characters = new ArrayList<String>(List.of(s.split("")));
        StringBuilder resultado = new StringBuilder();

        //Si el primer caracter decodificado no coincide con * entonces el formato no es correcto
        if (!characters.get(0).equals("*") || !characters.get(characters.size() - 1).equals("*")) return null;

        for (String character : characters) {
            //Se va mirando cada segmento del codigo y se obtiene su equivalente en codigo de barras.
            //resultado.append(getCode(character)).append(" ");
            resultado.append(toBarcode(codification.get(character))).append(" ");
        }
        //Se borra el último espacio ya que sobra
        return resultado.deleteCharAt(resultado.length() - 1).toString();
    }

    private static String toBarcode(String s) {
        //Se toman todos los bits del segmento y se convierte en codigo de barras.
        char[] bitCode = s.toCharArray();
        StringBuilder barCode = new StringBuilder();

        for (int i = 0; i < bitCode.length; i++) {
            if (i % 2 == 0) {
                //Si es segmento par es barra (si 1 doble si 0 normal)
                if (bitCode[i] == '1') barCode.append("██");
                else barCode.append("█");
            } else {
                //Si es segmento inpar es espacio en blanco (si 1 doble si 0 normal)
                if (bitCode[i] == '1') barCode.append("  ");
                else barCode.append(" ");
            }
        }
        return barCode.toString();
    }

    // Decodifica amb Code11
    static String decode(String s) {
        Barcode11 barcode11 = new Barcode11(s);
        int trys = barcode11.biggestSyze;
        //Si el formato no es valido no es procesable.
        if (barcode11.barcode == null) return null;
        if ((barcode11.barcode.length() + 1) % 6 != 0) return null;
        String answer = getValue(splitBarcode(barcode11.barcode));
        int limitTrys = barcode11.smallestSyze;

        while (trys > limitTrys && answer == null) {
            trys--;
            answer = getValue(splitBarcode(new Barcode11(s, barcode11.smallestSyze, barcode11.biggestSyze, trys).barcode));
        }
        if (answer == null) return null;
        if (answer.charAt(0) != '*' || answer.charAt(answer.length() - 1) != '*') return null;
        return answer;
    }

    private static String splitBarcode(String barcode) {
        ArrayList<String> barcodeSplitedTemp = new ArrayList<>(List.of(barcode.split("")));
        StringBuilder barcodeSplited = new StringBuilder();
        for (int i = 0; i < barcodeSplitedTemp.size(); i = i + 6) {
            for (int j = i; j < i + 5; j++) {
                barcodeSplited.append(barcodeSplitedTemp.get(j));
            }
            barcodeSplited.append(" ");
        }
        return barcodeSplited.toString();
    }

    private static String getValue(String segmentsListToBits) {
        //Ir cogiendo segmentos separados por espacios y añadir a string su equivalente.
        StringBuilder value = new StringBuilder();
        ArrayList<String> fragmets = new ArrayList<String>(List.of(segmentsListToBits.split(" ")));
        for (String fragmet : fragmets) {
            value.append(getKeyByValue(fragmet));
        }
        if (value.charAt(0) == '*' && value.charAt(value.length() - 1) == '*') return value.toString();
        else return null;
    }

    private static String getKeyByValue(String fragmet) {
        String result = null;
        //Si el fragmento esta en la lista de codigos
        if (codification.containsValue(fragmet)) {
            //Recorremos las claves hasta encontrar cual coincide con el fragmento
            for (Map.Entry<String, String> entry : codification.entrySet()) {
                if (Objects.equals(entry.getValue(), fragmet)) {
                    result = entry.getKey();
                }
            }
        }
        return result;
    }

    // Decodifica una imatge. La imatge ha d'estar en format "ppm"
    public static String decodeImage(String str) {
        //Se separa el texto por saltos de linea para poder analizar su contenido.
        String[] splitedString = str.split("\n");
        int originalStartPoint = getStartPoint(splitedString) + 2;
        String[] widthAndHeight = splitedString[originalStartPoint - 2].split(" ");
        int width = Integer.parseInt(widthAndHeight[0]);
        int height = Integer.parseInt(widthAndHeight[1]);
        int originalEndPoint = originalStartPoint + (width * 3);

        //Se intenta hasta conseguir un resultado valido hasta conseguirlo o que no quede más imagen por analizar
        return trySinceSuccess(originalStartPoint, originalEndPoint, height, width, splitedString);
    }

    private static String trySinceSuccess(int startPoint, int endPoint, int height, int width, String[] splitedString) {
        int actualRow = 1;
        while (actualRow < (height - 1)) {
            //TODO En la fila 36 encuentra algo que cree que es correcto pero se ha saltado una serie de valores (el 75-) hay que conseguir que se de cuenta
            if (actualRow == 36) {
                System.out.println("stop");
            }
            String barcode = getBarcode(splitedString, startPoint, endPoint);
            String answer = Code11.decode(barcode);
            if (answer != null) {
                return answer;
            }
            StringBuilder reverseBarcode = new StringBuilder(barcode);
            barcode = String.valueOf(reverseBarcode.reverse());
            answer = Code11.decode(barcode);
            if (answer != null) return answer;
            startPoint = endPoint;
            endPoint = (startPoint + (width * 3));
            actualRow++;
        }
        return null;
    }

    private static int getStartPoint(String[] splitedString) {
        for (int i = 0; i < splitedString.length; i++) {
            String[] widthAndHeightTry = splitedString[i].split(" ");
            if (widthAndHeightTry.length == 2) {
                return i;
            }
        }
        return 0;
    }

    private static String getBarcode(String[] splitedString, int startPoint, int endPoint) {
        //Se extrae y separa la información deseada del string.
        ArrayList<Integer> analizedString = decompressStr(splitedString, startPoint, endPoint);
        int trigger = Integer.parseInt(splitedString[startPoint - 1]) * 2;
        StringBuilder barcodeString = new StringBuilder();
        for (int i = 0; i < analizedString.size(); i = i + 3) {
            if (getValueGroupOfThree(i, analizedString) > trigger) barcodeString.append(" ");
            else barcodeString.append("█");
        }
        return barcodeString.toString();
    }

    private static int getValueGroupOfThree(int i, ArrayList<Integer> analizedString) {
        int totalValue = 0;
        for (int j = 0; j < 3; j++) {
            totalValue += analizedString.get(i + j);
        }
        return totalValue;
    }

    private static ArrayList<Integer> decompressStr(String[] splitedString, int startPoint, int endPoint) {
        //La longitud es el ancho por 3 ya que cada fragmento de información esta compuesto por tres numeros.
        ArrayList<Integer> imageValues = new ArrayList<>();
        // Se suman 4 a arrayLenght para compensar que i se inicia en 4
        for (int i = startPoint; i < endPoint; i++) {
            imageValues.add(Integer.parseInt(splitedString[i]));
        }
        return imageValues;
    }

    // Genera imatge a partir de codi de barres
    // Alçada: 100px
    // Marges: vertical 4px, horizontal 8px
    public static String generateImage(String s) {
        return "";
    }
}
