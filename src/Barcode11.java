public class Barcode11 {
    String barcode;
    int biggestSyze;
    int smallestSyze;
    int margin = 0;


    Barcode11(String barcodeDraw) {
        this.barcode = getBarcode(barcodeDraw.trim());
    }

    Barcode11(String barcodeDraw, int smallestSyze, int biggestSyze, int margin) {
        this.smallestSyze = smallestSyze;
        this.biggestSyze = biggestSyze;
        this.margin = margin;
        this.barcode = getBarcode(barcodeDraw.trim());
    }

    private String getBarcode(String barcodeDraw) {
        if (this.margin == 0) getSizes(barcodeDraw);
        return applySizes(barcodeDraw.toCharArray());
    }

    private String applySizes(char[] barcodeDrawCharArray) {
        //TODO si un espacio o barra(sobretodo espacio ya que puede coincidir justo con un espacio real) es más largo que la barra mayor devolver null.
        try {
            String barcodeBits = "";

            char pastChar = barcodeDrawCharArray[0];
            int counter = 1;

            //recorrer barcodeDraw, asignar 1 cuando longitud mayor que el margen y 0 cuando menor.
            for (int i = 1; i < barcodeDrawCharArray.length; i++) {
                char actualChar = barcodeDrawCharArray[i];
                if (actualChar == pastChar) counter++;
                else {
                    //if (counter > this.biggestSyze * 2) return "";

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
        try {
            int tempSize = 1;
            char[] barcodeDrawCharArray = barcodeDraw.toCharArray();
            char pastChar = barcodeDrawCharArray[0];

            for (int i = 1; i < barcodeDrawCharArray.length; i++) {
                char actualChar = barcodeDrawCharArray[i];
                if (actualChar == ' ') continue;
                if (pastChar == actualChar) tempSize++;
                else {
                    if (tempSize > this.biggestSyze) this.biggestSyze = tempSize;
                    else if (tempSize < this.smallestSyze) this.smallestSyze = tempSize;
                    tempSize = 1;
                }
                if (tempSize > this.biggestSyze) this.biggestSyze = tempSize;
                else if (tempSize < this.smallestSyze) this.smallestSyze = tempSize;
                pastChar = actualChar;
            }
            this.margin = (this.biggestSyze + this.smallestSyze) / 2;
        } catch (ArrayIndexOutOfBoundsException a) {

        }

    }

}
