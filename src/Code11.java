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
        //Si el formato no es valido no es procesable.
        if (barcode11.barcode == null) return null;
        if ((barcode11.barcode.length() + 1) % 6 != 0) return null;

        //Se intenta decodificar el código tomando como longitud mínima para ser tomado como 1 la barra más ancha encontrada
        //y en caso de fallar se ira bajando el limite hasta llegar al tamaño de la barra más pequeña.
        String answer = null;
        int limitTries = barcode11.smallestSyze;
        int tries = barcode11.biggestSyze;

        while (tries >= limitTries && answer == null) {
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

    private static String getKeyByValue(String fragmet) {
        //Esta función se utliza para obtener de un hashmap una clave mediante su valor, cosa que podemos hacer gracias
        //a que en este caso tanto la clave como el valor son valores únicos. De lo contrario podriamos tener varias
        //respuestas y este método no serviría.
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
        Barcode11Image b11 = new Barcode11Image(str);
        //Se intenta decodificar la imagen con un margen de error estandar (1)
        String answer = decodeImageLoop(b11);

        //Si no se obtiene respueta valida se vuelve a intentar bajando de 0.1 en 0.1 el margen
        if (answer == null) {
            double tries = 0.9;
            while (tries > 0.5) {
                Barcode11Image b11Error = new Barcode11Image(str, tries);
                answer = decodeImageLoop(b11Error);
                if (answer!=null) return answer;
                tries = tries - 0.1;
            }
        }
        return answer;
    }

    private static String decodeImageLoop(Barcode11Image b11) {
        String answer=null;
        //Fila a fila se intenta decodificar la imagen
        for (int i = 0; i < b11.image.length; i++) {
            answer = decodeImageCicle(b11, i);
            if (answer != null) return answer;
        }
        return answer;
    }

    private static String decodeImageCicle(Barcode11Image b11, int i) {
        String actualTopLine = b11.image[i];
        String answer;

        //Se prueba la primera linea de la imagen y si no funciona se prueba con la misma linea invertida por si la imagen esta al reves.
        answer = decodeImageTry(actualTopLine);
        if (answer != null) return answer;

        //Si no funciona se repiten el proceso anteriore pero empezando por la última fila
        String actualBottomLine = b11.image[b11.image.length - i - 1];
        return decodeImageTry(actualBottomLine);
    }

    private static String decodeImageTry(String actualTopLine) {
        String answer;
        answer = Code11.decode(actualTopLine);

        if (answer != null) return answer;

        return Code11.decode(String.valueOf(new StringBuilder(actualTopLine).reverse()));
    }

    // Genera imatge a partir de codi de barres
    // Alçada: 100px
    // Marges: vertical 4px, horizontal 8px
    public static String generateImage(String s) {
        return "";
    }
}
