public class Main {
    public static void main(String[] args) {
        String img = UtilTests.getImageAsString("code11_0123-4567.ppm");
        System.out.println((Code11.decodeImage(img)));
        //System.out.println(new Barcode11((Code11.decodeImage("code11_0123452.ppm"))));
    }
}
