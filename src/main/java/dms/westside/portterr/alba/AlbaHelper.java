package dms.westside.portterr.alba;

public class AlbaHelper {

    public static boolean isDoorman(String address) {
        return address.replaceAll("\\s", "").toUpperCase().matches(".+>DOORMAN<.+");
    }
}
