import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class SecurityUtils {
    private static final String ALGORITMO = "AES"; // Advanced Encryption Standard
    private static SecretKeySpec gerarChave(char[] senhaMestre) throws Exception {
    // Os objetos do tipo String são imutáveis e ficam guardados em um espaço da memória RAM chamado String Pool. 
    // Quando terminamos de usar uma String, ela continua na memória até que o sistema decida limpar (o que pode demorar minutos ou horas).
    // Se um atacante fizer um Memory Dump (escanear a memória RAM da máquina alvo), ele consegue ler a senha mestre em texto limpo.
    // O array de char[] nos permite sobrescrever a memória manualmente assim que terminamos de usar, destruindo qualquer rastro da senha.
    
    // 1. Converter o array de char para um array de bytes primitivos
    byte[] senhaBytes = new byte[senhaMestre.length];
    for (int i = 0; i < senhaMestre.length; i++) {
        senhaBytes[i] = (byte) senhaMestre[i];
        }
    // 2. Chamar o SHA-256 para gerar o hash da senha
    MessageDigest sha = MessageDigest.getInstance("SHA-256"); // Classe nativa do Java que calcula hashes de segurança
    byte[] chaveBytes = sha.digest(senhaBytes); // Método .digest() pega os bytes da senha, tritura, mistura e devolve um array único de 32 bytes (chaveBytes).
    // 3. HIGIENIZAÇÃO: Apagar os bytes da senha da memória RAM imediatamente
    Arrays.fill(senhaBytes, (byte) 0); // Substitui todos os números da senha por zeros (0). Se um hacker analisar a memória do computador, só verá os zeros em vez da senha.
    // 4. Montar e retornar o objeto da chave configurado para o algoritmo AES
    return new SecretKeySpec(chaveBytes, ALGORITMO); // Chave pronta
    }

    public static String criptografar(String textoLimpo, char[] senhaMestre) throws Exception {
    // 1. Criando a chave de 256 bits usando o método gerarChave
        SecretKeySpec chave = gerarChave(senhaMestre);
        
        // 2. Inicializando a criptografia (Cipher) ---- modo de Criptografia (ENCRYPT) ----
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, chave);
        // 3. Processando o texto limpo | Retorna bytes trancados e ilegíveis
        byte[] textoCriptografadoBytes = cipher.doFinal(textoLimpo.getBytes("UTF-8"));
        // 4. Transformando o resultado binário em uma String Base64 para poder salvar no arquivo
        return Base64.getEncoder().encodeToString(textoCriptografadoBytes);
    }

    public static String decriptografar(String textoCifrado, char[] senhaMestre) throws Exception {
    // 1. Recriando a chave matemática usando a mesma senha mestre fornecida
        SecretKeySpec chave = gerarChave(senhaMestre);
        // 2. Inicializando o Cipher ---- modo de Decriptografia (DECRYPT) ----
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, chave);
        // 3. Convertendo o texto Base64 de volta para os bytes esquisitos/criptografados originais
        byte[] bytesCriptografados = Base64.getDecoder().decode(textoCifrado);
        // 4. Processando os bytes trancados e devolve os bytes originais em texto limpo
        byte[] textoDecriptografadoBytes = cipher.doFinal(bytesCriptografados);
        // 5. Transformando esses bytes de volta em uma String legível para o usuário
        return new String(textoDecriptografadoBytes, "UTF-8");
    }
}