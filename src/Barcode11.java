public class Barcode11 {
    String barcode;
    int biggestSyze = 0;
    int smallestSyze = 999999999;
    int margin = 0;

    Barcode11(String barcodeDraw) {
        //Este constructor será el usado la primera vez
        this.barcode = getBarcode(barcodeDraw.trim());
    }

    Barcode11(String barcodeDraw, int smallestSyze, int biggestSyze, int margin) {
        //Para los reintentos se usará este constructor, el cual mantendra el tamaño de barra pequeña y grande mientras
        //que modificará el margen y generará un nuevo código de barras en función a este nuevo margen.
        this.smallestSyze = smallestSyze;
        this.biggestSyze = biggestSyze;
        this.margin = margin;
        this.barcode = getBarcode(barcodeDraw.trim());
    }

    private String getBarcode(String barcodeDraw) {
        //Si no se ha dado margen en el constructor se obtienen la barra pequeña y la grande y se genera el margen incial.
        if (this.margin == 0) getSizes(barcodeDraw);
        return applySizes(barcodeDraw.toCharArray());
    }

    private String applySizes(char[] barcodeDrawCharArray) {
        try {
            String barcodeBits = "";
            char pastChar = barcodeDrawCharArray[0];
            int counter = 1;

            //recorrer barcodeDraw, cuando el caracter actual es diferente del anterior sabemos que se cambia de barra
            // a espacio o viceversa. Se asignar 1 cuando longitud mayor que el margen y 0 cuando menor. Si es barra o
            // espacio se define por la posición, los pares son barras y los impares espacios.
            for (int i = 1; i < barcodeDrawCharArray.length; i++) {
                char actualChar = barcodeDrawCharArray[i];
                if (actualChar == pastChar) counter++;
                else {
                    if (counter > this.margin) barcodeBits += "1";
                    else barcodeBits += "0";
                    counter = 1;
                }
                pastChar = actualChar;
            }
            if (counter > this.margin) barcodeBits += "1";
            else barcodeBits += "0";
            return barcodeBits;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }

    }

    private void getSizes(String barcodeDraw) {
        //Se recorre el string y se van guardando los tamaños de barra más grande y más pequeño.
        try {
            int tempSize = 1;
            char[] barcodeDrawCharArray = barcodeDraw.toCharArray();
            char pastChar = barcodeDrawCharArray[0];

            for (int i = 1; i < barcodeDrawCharArray.length; i++) {
                char actualChar = barcodeDrawCharArray[i];
                //Si el caracter actual y el anterior son iguales se sigue leyendo.
                if (pastChar == actualChar) tempSize++;
                    //De lo contrario si es una barra se comprobará si el tamaño actual es menor o mayor que el mínimo y máximo respectivamente
                else {
                    if (actualChar == '█') {
                        trySyze(tempSize);
                    }
                    //Se resetea el contador y se sigue leyendo
                    tempSize = 1;
                }
                pastChar = actualChar;
            }
            trySyze(tempSize);
            //El margen inical es media entre la barra grande y la pequeña
            this.margin = (this.biggestSyze + this.smallestSyze) / 2;

        } catch (ArrayIndexOutOfBoundsException a) {
        }
    }

    private void trySyze(int tempSyze) {
        //Se comprueba si el tamaño es menor que el menor o mayor que el mayor y si se cumpla alguna sustituye al actual.
        if (tempSyze > this.biggestSyze) this.biggestSyze = tempSyze;
        else if (tempSyze < this.smallestSyze) this.smallestSyze = tempSyze;
    }

}
