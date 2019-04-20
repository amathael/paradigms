package java.hash;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CalcSHA256 {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No cmd arguments given");
            return;
        }
        try {
            MessageDigest hasher = MessageDigest.getInstance("SHA-256");
            for (String name : Files.readAllLines(Paths.get(args[0]))) {
                System.out.println(DatatypeConverter.printHexBinary(
                        hasher.digest(Files.readAllBytes(Paths.get(name)))));
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
