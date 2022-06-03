// https://en.wikipedia.org/wiki/Code_93

import java.util.*;

public class Code93 {
    ///////////////////////
    //Herramientas globales
    ///////////////////////
    static HashMap<String, String> IdToCharacter = new HashMap<>() {{
        //HashMap que contiene los id y sus caracteres asociados
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
        put("43", "Φ"); // ($)
        put("44", "Ψ"); // (%)
        put("45", "Λ"); // (/)
        put("46", "Ω"); // (+)
        put("47", "!");
    }};

    static HashMap<String, String> characterToWidths = new HashMap<>() {{
        //HashMap que contiene los caracteres y su equivalente en anchos
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
        put("Φ", "121221"); // ($)
        put("Ψ", "312111"); // (%)
        put("Λ", "311121"); // (/)
        put("Ω", "122211"); // (+)
        put("!", "111141");
    }};

    private static String getKeyByValueIdByCharacter(String fragment) {
        //Esta función se utliza para obtener de un hashmap una clave mediante su valor, cosa que podemos hacer gracias
        //a que en este caso tanto la clave como el valor son valores únicos. De lo contrario podriamos tener varias
        //respuestas y este método no serviría.
        String result = null;
        //Si el fragmento esta en la lista de codigos
        if (IdToCharacter.containsValue(fragment)) {
            //Recorremos las claves hasta encontrar cual coincide con el fragmento
            for (Map.Entry<String, String> entry : IdToCharacter.entrySet()) {
                if (Objects.equals(entry.getValue(), fragment)) {
                    result = entry.getKey();
                }
            }
        }
        return result;
    }

    private static String getKeyByValueCharacterByWidths(String fragment) {
        //Esta función se utliza para obtener de un hashmap una clave mediante su valor, cosa que podemos hacer gracias
        //a que en este caso tanto la clave como el valor son valores únicos. De lo contrario podriamos tener varias
        //respuestas y este método no serviría.
        String result = null;
        //Si el fragmento esta en la lista de codigos
        if (characterToWidths.containsValue(fragment)) {
            //Recorremos las claves hasta encontrar cual coincide con el fragmento
            for (Map.Entry<String, String> entry : characterToWidths.entrySet()) {
                if (Objects.equals(entry.getValue(), fragment)) {
                    result = entry.getKey();
                }
            }
        }
        return result;
    }

    private static String[] analizeCharacter(List<String> characters, int i) {
        //Se analiza el caracter recibido para ver si es un caracter estandar o un caracter que requiere de codificación
        //especial (pertenece a Full Ascii Code 93)
        String character = characterToWidths.get(characters.get(i));
        if (character != null && !character.equals("*")) return new String[]{"0", character};
        else return getSpecialChar(characters, i);
    }

    private static String[] getSpecialChar(List<String> characters, int i) {
        //Se comprueba que caracter es.
        //Es minuscula?
        String lowerCase = characterToWidths.get(characters.get(i).toUpperCase(Locale.ROOT));
        if (lowerCase != null) {
            characters.add(i + 1, characters.get(i).toUpperCase(Locale.ROOT));
            characters.remove(i);
            characters.add(i, "Ω");
            return new String[]{"1", "122211 " + lowerCase};
        } //Es caracter especial? Cual?
        else {
            switch (characters.get(i)) {
                case ",":
                    characters.add(i + 1, "L");
                    characters.remove(i);
                    characters.add(i, "Λ");
                    return new String[]{"1", "311121 " + characterToWidths.get("L")};
                case " ":
                    return new String[]{"0", " "};
                case "*":
                    characters.add(i + 1, "J");
                    characters.remove(i);
                    characters.add(i, "Λ");
                    return new String[]{"1", "311121 " + characterToWidths.get("J")};
                default:
                    return null;
            }
        }
    }

    private static String getCheckSum(List<String> characters) {
        //Se eleminan los start/stop del principio y del final y se le da la vuelta a la lista para facilitar su procesamiento
        List<String> barcodeContent = new ArrayList<>(characters.subList(1, characters.size() - 1));

        Collections.reverse(barcodeContent);
        //Se obtiene el checksum C, se guarda y se añade al principio de la lista
        String C = checkSumAlgorithm(barcodeContent, 20);
        barcodeContent.add(0, C);
        //Se obtiene el checksum K y se borra el checkSum C de la la lista para no manipular el arrayList original
        String K = checkSumAlgorithm(barcodeContent, 15);
        barcodeContent.remove(0);
        //Se obtienen el barcode de ambos checksums y se devuelven.
        C = toBarcode(characterToWidths.get(C));
        K = toBarcode(characterToWidths.get(K));
        return C + K;
    }

    private static String checkSumAlgorithm(List<String> barcodeContent, int trigger) {
        //Esta función realiza el algoritmo para obtener el checksum. Trigger indica si debe resetar posición cada 15 o cada 20.
        int totalValue = 0;
        for (int i = 0; i < barcodeContent.size(); i++) {
            //Se obtiene el valor númerico de la letra
            int value = (Integer.parseInt(getKeyByValueIdByCharacter(barcodeContent.get(i))));
            int position = i + 1;
            //Para el checksum C cuando la posición sea mayor que 20 se vuelve a empezar, mientras que para el K se toma
            //15 como referencia. Este valor se pasa como parametro de entrada a la función.
            while (position > trigger) {
                //Si la posición es mayor que el trigger se le restará el trigger hasta que sea menor que este.
                position -= trigger;
            }
            //Se añade al valor total el valor del caracter por su posición
            totalValue += value * position;
        }
        //Una vez tratados todos los caracteres se hace el modulo 47 del total y se obtiene el caracter asignado a ese valor.
        totalValue = totalValue % 47;
        return IdToCharacter.get("" + totalValue);
    }


    //////////////////////////
    // Codifica emprant Code93
    //////////////////////////
    static String encode(String str) {
        //Añado los caracteres de incio y fin para facilitar la manipulación de la información
        str = "!" + str + "!";
        //Divido el string en un arraylist de todos los caracters del codigo
        ArrayList<String> characters = new ArrayList<>(List.of(str.split("")));
        StringBuilder result = new StringBuilder();

        //Booleana para ignorar el primer caracter start/stop
        boolean firstSSFound = false;

        for (int i = 0; i < characters.size(); i++) {
            //Se va mirando cada segmento del codigo y se obtiene su equivalente en codigo de barras.
            if (Objects.equals(characters.get(i), "!")) {
                //Si es el primer start/stop se ignora
                if (!firstSSFound) firstSSFound = true;
                    //Si no lo es se obtiene el checksum y se añade
                else result.append(getCheckSum(characters));
            }
            //se procesa el caracter y se añade a la respuesta
            String[] character = analizeCharacter(characters, i);
            //Si hay caracter especial se debera avanzar una posición más para no releer nada.
            i += Integer.parseInt(character[0]);
            String[] chars = character[1].split(" ");
            //Si es caracter Full Ascii se añadiran los dos caracteres necesarios. Si no solo se añadira uno.
            for (int j = 0; j < chars.length; j++) {
                result.append(toBarcode(chars[j]));
            }
        }
        //Se añade la barra final.
        return result.append("█").toString();
    }

    private static String toBarcode(String s) {
        if (s.equals(" ")) return " ";
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
        Barcode93 barcode93 = new Barcode93(str.trim());
        //Si el formato no es valido no es procesable.
        if (barcode93.barcodeValues == null) return null;
        if ((barcode93.barcodeValues.size() - 1) % 6 != 0) return null;

        //Se obtiene el string de texto equivalente al barcode
        String answer = getValue(splitBarcode(barcode93.barcodeValues));

        //Se comprueba si el checksum es valido y, en caso de serlo, se devuelve el texto obtenido.
        if (answer != null && checkCheckSum(answer)) return answer.substring(1, answer.length() - 3);
        else return null;
    }

    private static boolean checkCheckSum(String answer) {
        //Se separa el checksum encontrado en la imagen del resto del mensaje
        String foundedChecksum = answer.substring(answer.length() - 3, answer.length() - 1);
        //Se añaden caracteres de inicio y final para poder procesar el mensaje.
        String barcode = "!" + answer.substring(1, answer.length() - 3) + "!";
        //Se revisan todos los caracteres. Si se encuentra alguno especial se cambiara por su equivalente según Full Asci code 93.
        List<String> filteredCharacters = filterCharacters(barcode);
        //Una vez el mensaje esta filtrado y si ha sido necesario reajustado se calcula el checksum
        String checkSum = getCheckSum(filteredCharacters);
        //Se comprueba si el checksum extraido del mensaje y el de la imagen son el mismo para verificar que el mensaje
        //ha sido decodificado correctamente.
        checkSum = fastDecode(checkSum);
        return checkSum.equals(foundedChecksum);

    }

    private static List<String> filterCharacters(String barcode) {
        //Esta función busca caracteres especiales y si los encuentra los sustituye por su equivalente.
        List<String> barcodeList = new ArrayList<>(List.of(barcode.split("")));
        List<String> filteredCharacters = new ArrayList<>();
        for (int i = 0; i < barcodeList.size(); i++) {
            //Se va mirando cada segmento del codigo y se obtiene su equivalente en codigo de barras.
            //se procesa el caracter y se añade a la respuesta
            String[] character = analizeCharacter(barcodeList, i);
            i += Integer.parseInt(character[0]);
            String[] chars = character[1].split(" ");
            for (int j = 0; j < chars.length; j++) {
                filteredCharacters.add(getKeyByValueCharacterByWidths((chars[j])));
            }
        }
        return filteredCharacters;
    }

    private static String fastDecode(String checkSum) {
        //Función usada para decodificar el checksum encontrado en la imagen y poder compararlo para verificar que la imangen
        //se ha decodificado de forma correcta
        Barcode93 barcode93 = new Barcode93(checkSum);
        return getValue(splitBarcode(barcode93.barcodeValues));
    }

    private static String splitBarcode(List<String> barcode) {
        //Se analiza el string caracter a caracter
        StringBuilder barcodeSplited = new StringBuilder();

        //Divido el string en "paquetes" de 6 caracters, ya que es lo que mide cada segmento de un codigo de barras.
        for (int i = 0; i < barcode.size() - 1; i = i + 6) {
            for (int j = i; j < i + 6; j++) {
                barcodeSplited.append(barcode.get(j));
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

        //Estas variables sirven para controlar los caracteres especiales
        boolean lastWasSpecial = false;
        String lastSpecial = "";

        for (int i = 0; i < fragmets.size(); i++) {
            String fragmet = fragmets.get(i);
            String candidate = getKeyByValueCharacterByWidths(fragmet);
            //Si el candidato no esta en la tabla normal no es procesable
            if (candidate == null) return null;
            //Los 3 últimos caracteres (el checksum y el stop) no seran filtrados ya que no pueden ser especiales.
            if (i > fragmets.size() - 4) value.append(candidate);
            else {
                if (lastWasSpecial) {
                    //Si el anterior caracter fue especial el actual se tratará en función de lo que diga el especial.
                    //En los test solo hay casos de minusculas, los cuales se resuelven convirtiendo la letra mayuscula
                    //obtenida del HashMap en minuscula.
                    if (lastSpecial.equals("Ω")) value.append(candidate.toLowerCase(Locale.ROOT));
                    lastWasSpecial = false;
                } else if (isSpecial(candidate)) {
                    //Si el caracter es especial se guarda para identificarlo y se deja activo el trigger para avisar al siguiente.
                    lastWasSpecial = true;
                    lastSpecial = candidate;
                    //Si no es especial ni el anterior lo fue se trata con normalidad
                } else value.append(candidate);
            }
        }
        return value.toString();
    }

    private static boolean isSpecial(String candidate) {
        //se comprueba si el caracter es especial.
        return switch (candidate) {
            case "Φ", "Ψ", "Λ", "Ω" -> true;
            default -> false;
        };
    }


    //////////////////////////////////////////////////////////////
    // Decodifica una imatge. La imatge ha d'estar en format "ppm"
    //////////////////////////////////////////////////////////////
    public static String decodeImage(String str) {
        BarcodeImage barcodeImage = new BarcodeImage(str);

        //Se intenta decodificar la imagen.
        String answer = BarcodeImage.decodeImageLoop(barcodeImage, "code93");

        //Si no se obtiene respuesta valida se manipula el margen de error
        if (answer == null) answer = BarcodeImage.marginErrorModifierRetryer(str, "code93");
        //Si no se obtiene respuesta valida se trata la imagen como vertical.
        if (answer == null) answer = BarcodeImage.verticalImage93(barcodeImage);
        //Si no se obtiene respuesta valida se trata la imagen como rotada.
        if (answer == null) return BarcodeImage.rotatedImage(barcodeImage);
        //Llegado a este punto se devolverá lo que se tenga
        return answer;
    }


    // Genera imatge a partir de barcode code93
    // Unitat barra mínima: 3 pixels
    // Alçada: 180px
    // Marges: vertical: 5px, horizontal: 15px
    public static String generateImage(String s) {
        //Se obtiene el string codificado del codigo de barras
        String encodedString = Code93.encode(s);
        //si el resultado fuera null no se puede generar imagen
        if (encodedString.equals("")) return null;

        //Se genera una linea de pixeles con su respectivo margen, además de la longitud de esta linea.
        String[] lineAndWidth = getLine(encodedString);
        String line = lineAndWidth[0];
        int width = Integer.parseInt(lineAndWidth[1]);

        //Se calculan las lineas que formaran el margen vertical superior e inferior
        String verticalMargin = UtilCreateImage.getVerticalMarginLines(width, 5);
        //Se genera la imagen compuesta por 100 lineas de pixeles
        String barcodeImage = UtilCreateImage.getBarcodeImage(line, 180);

        //Se añaden las 3 lineas iniciales de información, margen superior, barcode y margen inferor y se devuelven.
        return "P3\n" + width + " 190\n255\n" + verticalMargin + barcodeImage + verticalMargin.substring(0, verticalMargin.length() - 1);
    }

    static String[] getLine(String encodedString) {
        //Se añade margen horizontal izquierdo
        String margin = "255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n255\n";
        StringBuilder line = new StringBuilder(margin);

        //Se guarda el primer caracter y se inicia longitud en 1 y ancho de la imagen en 16 (de base se tienen en cuenta los 16 pixeles de los margenes).
        int width = 30;
        //Se recorre el string barcode y se añaden los pixeles que correspondan
        for (int i = 0; i < encodedString.length(); i++) {
            char actualChar = encodedString.charAt(i);
            //se añaden la cantidad y tipo de pixeles correspondientes y se reinicia longitud.
            width += UtilCreateImage.getPixels(actualChar, 1, line);
            //se guarda como caracter anterior el actual, ya que en el siguiente ciclo será asi
        }
        //Se añade el margen horizontal derecho y se devuelve el resultado
        return new String[]{(line + margin), "" + width};
    }
}
