public class Main {
    public static void main(String[] args) {
        //String barcode = "███████       ██████████           ███████       ███████       ███████       ██████████       ███████       ██████████            ███████";
        //System.out.println(Code11.decode(barcode));
        String img = UtilTests.getImageAsString("code11_6675-981-743.ppm");
        //System.out.println(img);
        System.out.println(Code11.decodeImage(img));

    }
}
