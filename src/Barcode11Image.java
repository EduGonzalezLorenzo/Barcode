import java.util.ArrayList;

public class Barcode11Image {
    String[] image;
    double marginError = 1;

    Barcode11Image(String imageString) {
        this.image = getImage(imageString);
    }

    Barcode11Image(String imageString, double marginError){
        this.marginError=marginError;
        this.image = getImage(imageString);
    }

    private String[] getImage(String imageString) {
        String[] splitedString = imageString.split("\n");
        int startPoint = getStartPoint(splitedString) + 2;

        String[] widthAndHeight = splitedString[startPoint - 1].split(" ");
        int width = Integer.parseInt(widthAndHeight[0]) * 3;
        int height = Integer.parseInt(widthAndHeight[1]);
        int trigger = (int) (Integer.parseInt(splitedString[startPoint])*this.marginError);
        String[] image= new String[height-1];
        ArrayList<Integer> decompressedString = decompressStr(splitedString, startPoint);

        String imageLine ="";
        for (int i = 0; i < height-1; i++) {
            for (int j = startPoint; j < (startPoint+width); j=j+3) {
                if (getValueGroupOfThree(j, decompressedString) > trigger) imageLine+=" ";
                else imageLine+="█";
            }
            startPoint = startPoint+width;
            image[i] = imageLine;
            imageLine= "";
        }

        return image;
    }

    private static int getStartPoint(String[] splitedString) {
        for (int i = 0; i < splitedString.length; i++) {
            String[] widthAndHeightTry = splitedString[i].split(" ");
            if (widthAndHeightTry.length == 2) {
                return i-1;
            }
        }
        return 0;
    }

    private static ArrayList<Integer> decompressStr(String[] splitedString, int startPoint) {
        ArrayList<Integer> imageValues = new ArrayList<>();

        for (int i = startPoint; i < splitedString.length; i++) {
            imageValues.add(Integer.parseInt(splitedString[i]));
        }
        return imageValues;
    }


    private static int getValueGroupOfThree(int i, ArrayList<Integer> decompressedString) {
        int totalValue = 0;
        for (int j = 0; j < 3; j++) {
            totalValue += decompressedString.get(i + j);
        }
        return totalValue;
    }
}
