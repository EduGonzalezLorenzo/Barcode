public class Barcode93 {
    String barcode;
    int[] BarDimensions = {1,2,3,4};
    int[] SpaceDimensions = {1,2,3,4};

    Barcode93(String barcodeDraw) {
        this.barcode = getBarcode(barcodeDraw.trim());
    }

    private String getBarcode(String barcodeDraw) {
        getSizes(barcodeDraw);
        return aplySizes(barcodeDraw.toCharArray());
    }

    private void getSizes(String barcodeDraw) {

    }

    private String aplySizes(char[] barcodeDrawCharArray) {
        return" barcodeBits";
    }

    private String lastLoop(String barcodeBits, int counter) {
        if (barcodeBits.length() % 2 == 0) {
            //if (counter < this.biggestBar) barcodeBits += "0";
            //else barcodeBits += "1";
        } else {
            //if (counter < this.biggestSpace) barcodeBits += "0";
            //else barcodeBits += "1";
        }
        return barcodeBits;
    }
}
