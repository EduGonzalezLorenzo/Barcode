
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
        if (!characters.get(0).equals("*")) return null;

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
        //Divido el string en un arraylist de todos los segmentos del codigo
        return getValue(splitBarcode(new Barcode11(s).barcode));
    }

    private static String getValue(String segmentsListToBits) {
        //Si el codigo de barras no fue procesable return null
        if (segmentsListToBits == null) return null;
        //Ir cogiendo segmentos separados por espacios y añadir a string su equivalente.
        StringBuilder value = new StringBuilder();
        ArrayList<String> fragmets = new ArrayList<String>(List.of(segmentsListToBits.split(" ")));
        for (String fragmet : fragmets) {
            value.append(getKeyByValue(fragmet));
        }
        if (value.charAt(0)== '*' && value.charAt(value.length()-1)=='*') return value.toString();
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

    private static String splitBarcode(String barcode) {
        //Si el formato no es valido no es procesable.
        if ((barcode.length() + 1) % 6 != 0) return null;

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

    // Decodifica una imatge. La imatge ha d'estar en format "ppm"
    public static String decodeImage(String str) {
        //Se separa el texto por saltos de linea para poder analizar su contenido.
        String[] splitedString = str.split("\n");

        //Se extrae el texto en formato barcode y se convierte en un objeto barcode para analizarlo con la funcion decode.
        return Code11.decode(getBarcode(splitedString));
    }

    private static String getBarcode(String[] splitedString) {
        //Se extrae y separa la información deseada del string.
        ArrayList<Integer> analizedString = decompressStr(splitedString);
        int trigger = (Integer.parseInt(splitedString[3])*3)/2;

        StringBuilder barcodeString = new StringBuilder();

        for (int i = 0; i < analizedString.size(); i = i+3) {
            if (getValueGroupOfThree(i, analizedString)>trigger) barcodeString.append(" ");
            else barcodeString.append("█");
        }

        return barcodeString.toString();
    }

    private static int getValueGroupOfThree(int i, ArrayList<Integer> analizedString) {
        int totalValue = 0;
        for (int j = 0; j < 3; j++) {
            totalValue+= analizedString.get(i + j);
        }
        return totalValue;
    }

    private static ArrayList<Integer> decompressStr(String[] splitedString) {
        // Se obtiene el ancho y alto del texto.
        String[] WidthAndHeight = splitedString[2].split(" ");

        //La longitud es el ancho por 3 ya que cada fragmento de información esta compuesto por tres numeros.
        int arrayLength = ((Integer.parseInt(WidthAndHeight[0])) * 3);
        ArrayList<Integer> imageValues = new ArrayList<>();

        // Se suman 4 a arrayLenght para compensar que i se inicia en 4
        for (int i = 4; i < arrayLength + 4; i++) {
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
