public class UtilCreateImage {
    static String getBarcodeImage(String marginLine, int nLines) {
        //Se crea un string con las 100 lineas de pixeles que conforman la imagen.
        String barcodeImage = "";
        for (int i = 0; i < nLines; i++) {
            barcodeImage += marginLine;
        }
        return barcodeImage;
    }

    static String getVerticalMarginLines(int width, int lines) {
        //Se crean las filas de pixeles blancos que conforman el margen inferior y superior.
        String verticalMargin = "";
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < width; j++) {
                verticalMargin += "255\n255\n255\n";
            }
        }
        return verticalMargin;
    }

    static int getPixels(char pastChar, int counter, StringBuilder line) {
        int width;
        //En caso de ser un espacio se guardarán 3 o 10 pixelex blancos en función de la longitud
        if (pastChar == ' ') {
            if (counter == 1) {
                generatePixels(line, 3, "255");
                width = 3;
            } else {
                generatePixels(line, 10, "255");
                width = 10;
            }
        } //En caso de ser una barra se guardarán 3 o 10 pixelex negros en función de la longitud
        else {
            if (counter == 1) {
                generatePixels(line, 3, "0");
                width = 3;
            } else {
                generatePixels(line, 10, "0");
                width = 10;
            }
        }
        return width;
    }

    private static void generatePixels(StringBuilder line, int lenght, String value) {
        //se añaden los pixeles del color solicitado la cantidad necesaria de veces.
        for (int j = 0; j < lenght; j++) {
            line.append(value).append("\n").append(value).append("\n").append(value).append("\n");
        }
    }
}