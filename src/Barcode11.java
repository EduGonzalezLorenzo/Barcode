public class Barcode11 {
    String barcode;
    int biggestBar = 2;
    int biggestSpace = 2;

    Barcode11(String barcodeDraw) {
        this.barcode = getBarcode(barcodeDraw.trim());
    }

    private String getBarcode(String barcodeDraw) {
        getSizes(barcodeDraw);
        return aplySizes(barcodeDraw.toCharArray());
    }

    private String aplySizes(char[] barcodeDrawCharArray) {
        String barcodeBits = "";
        char pastChar = barcodeDrawCharArray[0];
        int counter = 1;
        int marginErrorBar = (int) ((float) this.biggestBar *0.1f);
        int marginErrorSpace = (int) ((float) this.biggestSpace * 0.1f);
        //recorrer barcodeDraw, asignar 1 cuando longitud es igual que biggest y 0 cuando no.
        for (int i = 1; i < barcodeDrawCharArray.length; i++) {
            char actualChar = barcodeDrawCharArray[i];
            if (actualChar == pastChar) counter++;
            else {
                if (barcodeBits.length() % 2 == 0) {
                    if (counter >= (this.biggestBar-marginErrorBar))
                        barcodeBits += "1";
                    else barcodeBits += "0";
                } else {
                    if (counter >= (this.biggestSpace-marginErrorSpace))
                        barcodeBits += "1";
                    else barcodeBits += "0";
                }
                counter = 1;
            }
            pastChar = actualChar;
        }
        barcodeBits = lastLoop(barcodeBits, counter);

        return barcodeBits;
    }

    private String lastLoop(String barcodeBits, int counter) {
        if (barcodeBits.length() % 2 == 0) {
            if (counter < this.biggestBar) barcodeBits += "0";
            else barcodeBits += "1";
        } else {
            if (counter < this.biggestSpace) barcodeBits += "0";
            else barcodeBits += "1";
        }
        return barcodeBits;
    }

    private void getSizes(String barcodeDraw) {
        int tempBarSize = 1;
        int tempSpaceSize = 1;
        char[] barcodeDrawCharArray = barcodeDraw.toCharArray();
        char pastChar = barcodeDrawCharArray[0];

        for (int i = 1; i < barcodeDrawCharArray.length; i++) {
            char actualChar = barcodeDrawCharArray[i];
            if (pastChar == actualChar) {
                if (pastChar == '█') tempBarSize++;
                if (pastChar == ' ') tempSpaceSize++;
            } else {
                if (tempBarSize > this.biggestBar) this.biggestBar = tempBarSize;
                if (tempSpaceSize > this.biggestSpace) this.biggestSpace = tempSpaceSize;
                tempBarSize = 1;
                tempSpaceSize = 1;
            }
            if (tempBarSize > this.biggestBar) this.biggestBar = tempBarSize;
            if (tempSpaceSize > this.biggestSpace) this.biggestSpace = tempSpaceSize;
            pastChar = actualChar;
        }
    }

}
