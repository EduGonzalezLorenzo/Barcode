import java.util.ArrayList;
import java.util.List;

public class Barcode93 {
    List<String> barcodeValues = new ArrayList<>();
    int smallestBar = 999999999;

    Barcode93(String barcodeDraw) {
        getSizes(barcodeDraw);
        getValues(barcodeDraw.toCharArray());
    }

    private void getSizes(String barcodeDraw) {
        //Se recorre el string y se van guardando los tamaños de barra más grande y más pequeño.
        try {
            int tempSize = 1;
            char[] barcodeDrawCharArray = barcodeDraw.toCharArray();
            char pastChar = barcodeDrawCharArray[0];

            for (int i = 1; i < barcodeDrawCharArray.length; i++) {
                char actualChar = barcodeDrawCharArray[i];
                if (pastChar == actualChar) tempSize++;
                else {
                    if (pastChar == '█') if (tempSize < this.smallestBar) this.smallestBar = tempSize;
                    tempSize = 1;
                }
                pastChar = actualChar;
            }
            if (pastChar == '█') if (tempSize < this.smallestBar) this.smallestBar = tempSize;

        } catch (ArrayIndexOutOfBoundsException a) {
        }
    }

    private void getValues(char[] barcodeDrawCharArray) {
        char pastChar = barcodeDrawCharArray[0];
        int counter = 1;
        for (int i = 1; i < barcodeDrawCharArray.length; i++) {
            char actualChar = barcodeDrawCharArray[i];
            if (actualChar == pastChar) counter++;
            else {
                this.barcodeValues.add(String.valueOf(counter / this.smallestBar));
                counter = 1;
            }
            pastChar = actualChar;
        }
        this.barcodeValues.add(String.valueOf(counter / this.smallestBar));
    }

}
