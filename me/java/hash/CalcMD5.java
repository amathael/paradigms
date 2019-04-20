package java.hash;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CalcMD5 {

    public static void main(String[] args) {
        try {
            MessageDigest hasher = MessageDigest.getInstance("MD5");
            for (String name: Files.readAllLines(Paths.get(args[0]))) {
                    System.out.println(DatatypeConverter.printHexBinary(
                            hasher.digest(Files.readAllBytes(Paths.get(name)))));
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Not enough command line arguments");
        }
    }

}
