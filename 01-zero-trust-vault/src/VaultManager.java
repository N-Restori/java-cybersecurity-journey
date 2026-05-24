import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class VaultManager {

    private static final String CAMINHO_ARQUIVO = "vault.dat";

    /**
     * Persiste uma nova credencial criptografada no disco rígido.
     * 
     * 🔒 GESTÃO DE RECURSOS (Prevenção de Memory Leaks):
     * Utiliza o padrão try-with-resources para garantir o fechamento automatizado
     * dos descritores de arquivo (Streams de I/O) após a escrita, mitigando a instabilidade
     * do sistema operacional e possíveis corrupções de dados.
     * O parâmetro 'true' no FileWriter ativa o modo Append, preservando os registros anteriores.
     */
    public static void salvarCredencial(String servico, String usuario, String senhaLimpa, char[] senhaMestre) {
        try {
            String senhaCriptografada = SecurityUtils.criptografar(senhaLimpa, senhaMestre);
            String linhaRegistro = servico + ";" + usuario + ";" + senhaCriptografada;
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO, true))) {
                writer.write(linhaRegistro);
                writer.newLine(); 
            }
            
            System.out.println("[OK] Credencial para " + servico + " salva com segurança!");
        } catch (Exception e) {
            System.out.println("[ERRO] Falha ao salvar a credencial: " + e.getMessage());
        }
    }

    /**
     * Lê, processa e renderiza as credenciais persistidas no arquivo de dados.
     */
    public static void listarCredenciais(char[] senhaMestre) {
        File arquivo = new File(CAMINHO_ARQUIVO);
        
        if (!arquivo.exists()) {
            System.out.println("[INFO] O cofre está vazio. Nenhuma credencial cadastrada.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;
            System.out.println("\n--- CREDENCIAIS PROTEGIDAS ---");
            
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                
                // Validação de integridade estrutural do registro (Formato CSV)
                if (partes.length == 3) { 
                    String servico = partes[0];
                    String usuario = partes[1];
                    String senhaCriptografada = partes[2];
                    
                    try {
                        String senhaLimpa = SecurityUtils.decriptografar(senhaCriptografada, senhaMestre);
                        System.out.println("Serviço: " + servico + " | Usuário: " + usuario + " | Senha: " + senhaLimpa);
                    } catch (Exception e) {
                        System.out.println("Serviço: " + servico + " | Usuário: " + usuario + " | [ERRO: Senha Inválida]");
                    }
                }
            }
            System.out.println("-------------------------------------\n");
            
        } catch (IOException e) {
            System.out.println("[ERRO] Falha na leitura física do cofre: " + e.getMessage());
        }
    }
}