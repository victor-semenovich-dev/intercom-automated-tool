package by.geth;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Utils {
    public static int byteArrayToInt(byte[] b) {
        int result = 0;
        for (int i = 0; i < b.length; i++) {
            result = result | ((b[i] & 0xFF) << 8*i);
        }
        return result;
    }

    public static int readInt(String message) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(message);
        try {
            String str = reader.readLine();
            return Integer.parseInt(str);
        } catch (Exception e) {
            return -1;
        }
    }
}
