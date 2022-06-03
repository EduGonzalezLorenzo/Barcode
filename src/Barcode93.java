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

            //se recorren los caracteres. Se va acumulando el tamaño y, si es una barra, se compreuna si el tamaño encontrado
            //es el más pequeño. Si lo es se guarda como nuevo valor más pequeño
            for (int i = 1; i < barcodeDrawCharArray.length; i++) {
                char actualChar = barcodeDrawCharArray[i];
                if (pastChar == actualChar) tempSize++;
                else {
                    if (pastChar == '█' && tempSize < this.smallestBar) this.smallestBar = tempSize;
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
        //recorrer barcodeDraw, cuando el caracter actual es diferente del anterior sabemos que se cambia de barra
        // a espacio o viceversa. Se asignar la longitud dividiendo el tamaño encontrado entre el tamaño más pequeño.
        // Si es barra o espacio se define por la posición, los pares son barras y los impares espacios.
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
