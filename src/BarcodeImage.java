import java.util.ArrayList;

public class BarcodeImage {
    String[] image;
    double marginError = 1;

    BarcodeImage(String imageString) {
        //Este constructor será el usado la primera vez
        this.image = getImage(imageString);
    }

    BarcodeImage(String imageString, double marginError) {
        //Para los reintentos se usará este constructor, el cual modificará el margen y generará un nuevo código de
        // barras en función a este nuevo margen.
        this.marginError = marginError;
        this.image = getImage(imageString);
    }


    ///////////////////////////////////////////
    //Metodos para obtener el barcode en String
    ///////////////////////////////////////////
    private String[] getImage(String imageString) {
        String[] splitedString = imageString.split("\n");
        //Obtenemos el punto a tomar como referencia para extraer la información
        int startPoint = getStartPoint(splitedString);

        //Usando el punto de inicio obtenido como referencia se obtiene la información necesaria del String.
        String[] widthAndHeight = splitedString[startPoint - 1].split(" ");
        int width = Integer.parseInt(widthAndHeight[0]) * 3;
        int height = Integer.parseInt(widthAndHeight[1]);
        String[] image = new String[height];
        ArrayList<Integer> decompressedString = decompressStr(splitedString, startPoint);

        //Usando el margen de error (en el primer intento 1) se obtiene el limite que delimitará que es espacio y que es barra.
        int trigger = (int) (Integer.parseInt(splitedString[startPoint]) * this.marginError);

        //Se genera un array de string con cada linea de pixeles que forma la imagen convertida en barras y espacios.
        String imageLine = "";
        int startLine = 1;
        for (int i = 0; i < height; i++) {
            for (int j = startLine; j < (startLine + width); j = j + 3) {
                if (getValueGroupOfThree(j, decompressedString) > trigger) imageLine += " ";
                else imageLine += "█";
            }
            startLine = startLine + width;
            image[i] = imageLine;
            imageLine = "";
        }

        return image;
    }

    private static int getStartPoint(String[] splitedString) {
        //Se obtiene donde esta la primera fila con información relevante, ignorando la información no directamente
        //relacionada con la imagen y los posibles comentarios.
        for (int i = 0; i < splitedString.length; i++) {
            String[] widthAndHeightTry = splitedString[i].split(" ");
            if (widthAndHeightTry.length == 2) {
                return i + 1;
            }
        }
        return 0;
    }

    private static ArrayList<Integer> decompressStr(String[] splitedString, int startPoint) {
        //Se crea un array con todos los valores de los pixeles de la imagen.
        ArrayList<Integer> imageValues = new ArrayList<>();

        for (int i = startPoint; i < splitedString.length; i++) {
            imageValues.add(Integer.parseInt(splitedString[i]));
        }
        return imageValues;
    }

    private static int getValueGroupOfThree(int i, ArrayList<Integer> decompressedString) {
        //se cogen los numeros de 3 en tres, ya que cada pixel esta formado por tres valroes.
        int totalValue = 0;
        for (int j = 0; j < 3; j++) {
            totalValue += decompressedString.get(i + j);
        }
        return totalValue;
    }


    ////////////////////////////////////////////////////////////
    //Metodos para trabajar con objetos de la clase BarcodeImage
    ////////////////////////////////////////////////////////////

    //Este metodo intenta decodificar la imagen variando el margen de error en cada reintento
    static String marginErrorModifierRetryer(String str, String barcodeType) {
        String answer = "";
        double tries = 0.9;

        while (tries > 0.4) {
            BarcodeImage barcodeImageError = new BarcodeImage(str, tries);
            answer = decodeImageLoop(barcodeImageError, barcodeType);
            if (answer != null) return answer;
            tries = tries - 0.1;
        }
        return null;
    }

    static String decodeImageLoop(BarcodeImage barcodeImage, String barcodeType) {
        String answer = null;
        //Fila a fila se intenta decodificar la imagen
        for (int i = 0; i < barcodeImage.image.length; i++) {
            answer = decodeImageCicle(barcodeImage, i, barcodeType);
            if (answer != null) return answer;
        }
        return null;
    }

    private static String decodeImageCicle(BarcodeImage barcodeImage, int line, String bardoceType) {
        String actualTopLine = barcodeImage.image[line];
        String answer;

        //Se prueba la primera linea de la imagen y si no funciona se prueba con la misma linea invertida por si la imagen esta al reves.
        if (!actualTopLine.trim().equals("")) {
            answer = decodeImageTry(actualTopLine, bardoceType);
            if (answer != null) return answer;
        }

        //Si no funciona se repiten el proceso anteriore pero empezando por la última fila
        String actualBottomLine = barcodeImage.image[barcodeImage.image.length - line - 1];

        if (!actualBottomLine.trim().equals("")) return decodeImageTry(actualBottomLine, bardoceType);
        else return null;
    }

    //Este metodo intenta decodificar la linea actual mediante el decode 93 o el 11 segun el tipo recibido.
    private static String decodeImageTry(String actualLine, String barcodeType) {
        String answer;
        if (barcodeType.equals("code93")) {
            answer = Code93.decode(actualLine.trim());
            if (answer != null) return answer;
            return Code93.decode(String.valueOf(new StringBuilder(actualLine).reverse()).trim());
        } else if (barcodeType.equals("code11")) {
            answer = Code11.decode(actualLine);
            if (answer != null) return answer;
            return Code11.decode(String.valueOf(new StringBuilder(actualLine).reverse()));
        }
        return null;
    }


    ////////////////////////////////////////////////////////
    //Metodos para manipular la orientación de las imagenes
    ////////////////////////////////////////////////////////
    //Funciones para imagenes Verticales
    static String verticalImage11(BarcodeImage barcodeImage) {
        String answer = "";
        for (int i = 0; i < barcodeImage.image[0].length(); i++) {
            StringBuilder verticalLine = getVerticalLine(i, barcodeImage);
            answer = Code11.decode(verticalLine.toString());
            if (answer != null) return answer;
            verticalLine = verticalLine.reverse();
            answer = Code11.decode(verticalLine.toString());
            if (answer != null) return answer;
        }
        return null;
    }

    static String verticalImage93(BarcodeImage barcodeImage) {
        String answer = null;
        for (int i = 0; i < barcodeImage.image[0].length(); i++) {
            StringBuilder verticalLine = getVerticalLine(i, barcodeImage);
            if (!verticalLine.toString().trim().equals("")) answer = Code93.decode(verticalLine.toString());
            if (answer != null) return answer;
            verticalLine = verticalLine.reverse();
            if (!verticalLine.toString().trim().equals("")) answer = Code93.decode(verticalLine.toString());
            if (answer != null) return answer;
        }
        return null;
    }

    private static StringBuilder getVerticalLine(int i, BarcodeImage barcodeImage) {
        StringBuilder verticalLine = new StringBuilder("");
        for (int j = 0; j < barcodeImage.image.length; j++) {
            verticalLine.append(barcodeImage.image[j].charAt(i));
        }
        return verticalLine;
    }

    //Funciones para imagenes rotadas
    static String rotatedImage(BarcodeImage barcodeImage) {
        //Se toma la altura como referencia ya que se supone que las imagenes son cuadradas, por lo que base=altura
        //Primero se prueba con cogiendo una linea que hace una diagonal de la coordenada 0,0 a la height,height
        String rotatedLine = generateRotatedLine(barcodeImage);
        String answer = Code11.decode(rotatedLine);
        if (answer != null) return answer;
        StringBuilder inverseRotatedLine = new StringBuilder(rotatedLine).reverse();
        answer = Code11.decode(inverseRotatedLine.toString());
        if (answer != null) return answer;
        //Si la respuesta no es valida se prueba otra vez pero haciendo la diagonal de la cordenada height,0 a la 0,height
        String rotatedLineInverse = generateRotatedLineInverse(barcodeImage);
        answer = Code11.decode(rotatedLineInverse);
        if (answer != null) return answer;
        inverseRotatedLine = new StringBuilder(rotatedLineInverse).reverse();
        return Code11.decode(inverseRotatedLine.toString());
    }

    private static String generateRotatedLineInverse(BarcodeImage barcodeImage) {
        //Diagonal de la coordenada 0,0 a la height,height
        String rotatedLine = "";
        int height = barcodeImage.image.length - 1;
        for (int i = 0; i < height; i++) {
            rotatedLine += barcodeImage.image[i].charAt(i);
        }
        return rotatedLine;
    }

    private static String generateRotatedLine(BarcodeImage barcodeImage) {
        //Diagonal de la cordenada height,0 a la 0,height
        String rotatedLine = "";
        int height = barcodeImage.image.length - 1;
        for (int i = 0; i < height; i++) {
            rotatedLine += barcodeImage.image[height - i].charAt(i);
        }
        return rotatedLine;
    }
}
