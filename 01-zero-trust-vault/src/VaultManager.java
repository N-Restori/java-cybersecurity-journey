import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class VaultManager {
// Define o nome do arquivo físico onde as senhas criptografadas serão salvas no disco
    private static final String CAMINHO_ARQUIVO = "vault.dat";
        public static void salvarCredencial(String servico, String usuario, String senhaLimpa, char[] senhaMestre) {
        try {
        // 1. Criptografa a senha usando o SecurityUtils
            String senhaCriptografada = SecurityUtils.criptografar(senhaLimpa, senhaMestre);
            
            // 2. Monta a linha que será salva no arquivo
            String linhaRegistro = servico + ";" + usuario + ";" + senhaCriptografada;
            
            // 3. Abre o arquivo em modo "Append" (acrescentar dados sem apagar o que já existe)
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO, true))) {
            // Garante que o arquivo seja fechado automaticamente assim que a gravação terminar, mesmo se ocorrer um erro inesperado no meio do processo.
            // Um arquivo aberto na memória RAM (Memory Leak), pode deixar o sistema operacional instável ou o arquivo pode ser corrompido.
            // O parâmetro true em new FileWriter(..., true): Adiciona uma nova linha no arquivo vault.dat já existente.
            // IMPORTANTE: Sem este parâmetro, o Java apaga todo o arquivo anterior sempre que uma senha nova for salva!
                writer.write(linhaRegistro);
                writer.newLine(); // Pula para a próxima linha para o próximo registro
            }
            
            System.out.println("Credencial para " + servico + " salva com segurança!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar a credencial: " + e.getMessage());
        }
    }

    public static void listarCredenciais(char[] senhaMestre) {
        File arquivo = new File(CAMINHO_ARQUIVO);
        
        // 1. Defesa Inicial: Se o arquivo nem existe ainda, não há o que ler
        if (!arquivo.exists()) {
            System.out.println("O cofre está vazio. Nenhuma credencial salva.");
            return;
        }
        // 2. Abre o arquivo para leitura usando o BufferedReader (eficiente para ler linha por linha)
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;
            System.out.println("\n--- SUAS CREDENCIAIS PROTEGIDAS ---");
            
            // Loop que lê o arquivo até que ele chegue ao fim (null)
            while ((linha = reader.readLine()) != null) {
                // O método split(";") formato CSV (valores separados por um delimitador) divide a linha onde tiver ponto e vírgula, gerando um array 'partes'
                String[] partes = linha.split(";");
                
                if (partes.length == 3) { // Verifica se o array 'partes' tem 3 pedaços de texto dentro dele
                    String servico = partes[0];
                    String usuario = partes[1];
                    String senhaCriptografada = partes[2];
                    
                    try {
                        // 3. Tenta decriptografar a senha usando a senha mestre fornecida
                        String senhaLimpa = SecurityUtils.decriptografar(senhaCriptografada, senhaMestre);
                        System.out.println("Serviço: " + servico + " | Usuário: " + usuario + " | Senha Mestre: " + senhaLimpa);
                    } catch (Exception e) {
                        // Se a criptografia falhar nesta linha, significa que a senha mestre está incorreta
                        System.out.println("Serviço: " + servico + " | Usuário: " + usuario + " | [ERRO: Senha Inválida]");
                    }
                }
            }
            System.out.println("-------------------------------------\n");
            
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo do cofre: " + e.getMessage());
        }
    }
}
