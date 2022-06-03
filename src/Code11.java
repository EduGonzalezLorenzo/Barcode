// Consultar taula https://en.wikipedia.org/wiki/Barcode#Linear_barcodes
// Code11: https://en.wikipedia.org/wiki/Code_11

// Generadors de codis:
//     https://barcode.tec-it.com/en/Code11
//     https://www.free-barcode-generator.net/code-11/
//     https://products.aspose.app/barcode/generate

import java.util.*;

public class Code11 {
    ///////////////////////
    //Herramientas globales
    ///////////////////////
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

    private static String getKeyByValue(String fragment) {
        //Esta función se utliza para obtener de un hashmap una clave mediante su valor, cosa que podemos hacer gracias
        //a que en este caso tanto la clave como el valor son valores únicos. De lo contrario podriamos tener varias
        //respuestas y este método no serviría.
        String result = null;
        //Si el fragmento esta en la lista de codigos
        if (codification.containsValue(fragment)) {
            //Recorremos las claves hasta encontrar cual coincide con el fragmento
            for (Map.Entry<String, String> entry : codification.entrySet()) {
                if (Objects.equals(entry.getValue(), fragment)) {
                    result = entry.getKey();
                }
            }
        }
        return result;
    }


    /////////////////////////////////
    // Codifica un String amb Code11
    /////////////////////////////////
    static String encode(String s) {
        //Divido el string en un arraylist de todos los caracters del codigo
        ArrayList<String> characters = new ArrayList<>(List.of(s.split("")));
        StringBuilder result = new StringBuilder();

        //Si el primer caracter decodificado no coincide con * entonces el formato no es correcto
        if (!characters.get(0).equals("*") || !characters.get(characters.size() - 1).equals("*")) return null;

        for (String character : characters) {
            //Se va mirando cada segmento del codigo y se obtiene su equivalente en codigo de barras.
            result.append(toBarcode(codification.get(character))).append(" ");
        }
        //Se borra el último espacio ya que sobra
        return result.deleteCharAt(result.length() - 1).toString();
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


    /////////////////////////
    // Decodifica amb Code11
    /////////////////////////
    static String decode(String s) {
        Barcode11 barcode11 = new Barcode11(s);
        //Si el formato no es valido no es procesable.
        if (barcode11.barcode == null) return null;
        if ((barcode11.barcode.length() + 1) % 6 != 0) return null;

        //Se intenta decodificar el código tomando como longitud mínima para ser tomado como 1 la barra más ancha encontrada
        //y en caso de fallar se ira bajando el limite hasta llegar al tamaño de la barra más pequeña.
        String answer = null;
        int limitTries = barcode11.smallestSyze;
        int tries = barcode11.biggestSyze;

        while ((tries >= limitTries) && (answer == null || answer.contains("null"))) {
            //Se decodifica un barcode con el indicador de barra pequeña y grande que hemos obtenido antes, además del
            //modificador del margen de error que decrementará en cada ciclo.
            Barcode11 barcodeTries = new Barcode11(s, barcode11.smallestSyze, barcode11.biggestSyze, tries);
            answer = getValue(splitBarcode(barcodeTries.barcode));
            tries--;
        }
        return answer;
    }

    private static String splitBarcode(String barcode) {
        //Se analiza el string caracter a caracter
        ArrayList<String> barcodeSplitedTemp = new ArrayList<>(List.of(barcode.split("")));
        StringBuilder barcodeSplited = new StringBuilder();

        //Divido el string en "paquetes" de 5 caracters, ya que es lo que mide cada segmento de un codigo de barras.
        for (int i = 0; i < barcodeSplitedTemp.size(); i = i + 6) {
            for (int j = i; j < i + 5; j++) {
                barcodeSplited.append(barcodeSplitedTemp.get(j));
            }
            //Añado un espacio entre segmentos para separarlos
            barcodeSplited.append(" ");
        }
        return barcodeSplited.toString();
    }

    private static String getValue(String segmentsListToBits) {
        //Ir cogiendo segmentos separados por espacios y añadir a un string su equivalente.
        StringBuilder value = new StringBuilder();
        ArrayList<String> fragmets = new ArrayList<String>(List.of(segmentsListToBits.split(" ")));

        for (String fragmet : fragmets) {
            value.append(getKeyByValue(fragmet));
        }

        //Si el codigo no empieza y acaba por * no es valido.
        if (value.charAt(0) == '*' && value.charAt(value.length() - 1) == '*') return value.toString();
        else return null;
    }


    ///////////////////////////////////////////////////////////////
    // Decodifica una imatge. La imatge ha d'estar en format "ppm"
    ///////////////////////////////////////////////////////////////
    public static String decodeImage(String str) {
        //Se separa el texto por saltos de linea para poder analizar su contenido.
        BarcodeImage b11 = new BarcodeImage(str);
        //Se intenta decodificar la imagen con un margen de error estandar (1)
        String answer = BarcodeImage.decodeImageLoop(b11, "code11");

        //Si no se obtiene respueta valida se vuelve a intentar bajando de 0.1 en 0.1 el margen
        if (answer == null) {
            answer = BarcodeImage.marginErrorModifierRetryer(str, "code11");
        }
        if (answer == null) answer = BarcodeImage.verticalImage11(b11);
        if (answer.equals("*")) return BarcodeImage.rotatedImage(b11);
        else return answer;
    }

    // Genera imatge a partir de codi de barres
    // Alçada: 100px
    // Marges: vertical 4px, horizontal 8px
    public static String generateImage(String s) {
        //Se obtiene el string codificado del codigo de barras
        String encodedString = Code11.encode(s);
        //si el resultado fuera null no se puede generar imagen
        if (encodedString == null) return null;

        //Se genera una linea de pixeles con su respectivo margen, además de la longitud de esta linea.
        String[] lineAndWidth = getLine(encodedString);
        String line = lineAndWidth[0];
        int width = Integer.parseInt(lineAndWidth[1]);

        //Se calculan las lineas que formaran el margen vertical superior e inferior
        String verticalMargin = UtilCreateImage.getVerticalMarginLines(width,4);
        //Se genera la imagen compuesta por 100 lineas de pixeles
        String barcodeImage = UtilCreateImage.getBarcodeImage(line, 100);

        //Se añaden las 3 lineas iniciales de información, margen superior, barcode y margen inferor y se devuelven.
        return "P3\n" + width + " 108\n255\n" + verticalMargin + barcodeImage + verticalMargin.substring(0, verticalMargin.length() - 1);
    }

    static String[] getLine(String encodedString) {
        //Se añade margen horizontal izquierdo
        String margin = "255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n";
        StringBuilder line = new StringBuilder(margin);

        //Se guarda el primer caracter y se inicia longitud en 1 y ancho de la imagen en 16 (de base se tienen en cuenta los 16 pixeles de los margenes).
        char pastChar = encodedString.charAt(0);
        int longitud = 1;
        int width = 16;

        //Se recorre el string barcode y se añaden los pixeles que correspondan
        for (int i = 1; i < encodedString.length(); i++) {
            char actualChar = encodedString.charAt(i);
            //si el caracter actual es como el anterior se suma uno a longitud
            if (actualChar == pastChar) longitud++;
            else {
                //se añaden la cantidad y tipo de pixeles correspondientes y se reinicia longitud.
                width += UtilCreateImage.getPixels(pastChar, longitud, line);
                longitud = 1;
            }
            //se guarda como caracter anterior el actual, ya que en el siguiente ciclo será asi
            pastChar = actualChar;
        }
        //Se hace un último ciclo despues del for para no dejarnos el último pixel por revisar
        width += UtilCreateImage.getPixels(pastChar, longitud, line);

        //Se añade el margen horizontal derecho y se devuelve el resultado
        return new String[]{(line + margin), "" + width};
    }

}
