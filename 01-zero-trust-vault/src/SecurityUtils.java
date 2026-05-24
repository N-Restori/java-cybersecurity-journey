import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class SecurityUtils {

    private static final String ALGORITMO = "AES"; 

    /**
     * Gera uma chave criptográfica de 256 bits a partir da Senha Mestre.
     * 
     * MITIGAÇÃO DE RISCO (Memory Dumping):
     * Objetos do tipo String são imutáveis e permanecem retidos no String Pool da RAM. 
     * O uso estrito de char[] permite a sobrescrita manual dos dados binários na memória 
     * através do método Arrays.fill() imediatamente após a geração do hash SHA-256, 
     * impossibilitando a extração de credenciais por meio de dumps de memória.
     */
    private static SecretKeySpec gerarChave(char[] senhaMestre) throws Exception {
        byte[] senhaBytes = new byte[senhaMestre.length];
        for (int i = 0; i < senhaMestre.length; i++) {
            senhaBytes[i] = (byte) senhaMestre[i];
        }

        MessageDigest sha = MessageDigest.getInstance("SHA-256"); 
        byte[] chaveBytes = sha.digest(senhaBytes); 

        // Higienização imediata dos dados sensíveis na memória RAM
        Arrays.fill(senhaBytes, (byte) 0); 

        return new SecretKeySpec(chaveBytes, ALGORITMO); 
    }

    /**
     * Criptografa o texto plano em formato Base64 utilizando o algoritmo AES-256.
     */
    public static String criptografar(String textoLimpo, char[] senhaMestre) throws Exception {
        SecretKeySpec chave = gerarChave(senhaMestre);
        
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, chave);
        
        byte[] textoCriptografadoBytes = cipher.doFinal(textoLimpo.getBytes("UTF-8"));
        
        return Base64.getEncoder().encodeToString(textoCriptografadoBytes);
    }

    /**
     * Decriptografa o texto cifrado Base64 retornando à String estruturada original.
     */
    public static String decriptografar(String textoCifrado, char[] senhaMestre) throws Exception {
        SecretKeySpec chave = gerarChave(senhaMestre);
        
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, chave);
        
        byte[] bytesCriptografados = Base64.getDecoder().decode(textoCifrado);
        byte[] textoDecriptografadoBytes = cipher.doFinal(bytesCriptografados);
        
        return new String(textoDecriptografadoBytes, "UTF-8");
    }
}

