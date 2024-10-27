import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;

public class SecretKeyGenerator {
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256); // 使用256位密钥
        SecretKey secretKey = keyGen.generateKey();
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated Key: " + base64Key);
    }
}
