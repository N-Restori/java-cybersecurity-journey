import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        

        System.out.println("=====================================");
        System.out.println("  BEM-VINDA AO THE ZERO-TRUST VAULT ");
        System.out.println("=====================================");

        // 1. Solicitando a Senha Mestre que tranca/destranca o cofre
        System.out.print("Digite sua Senha Mestre para iniciar: ");
        String senhaTexto = scanner.nextLine();
        
        // Converte a String para char[] imediatamente e deixa a String original vazia para sair da memória RAM o quanto antes.
        char[] senhaMestre = senhaTexto.toCharArray();
        senhaTexto = "";

        int opcao = 0;

        // 2. Loop principal do menu | do-while repete o bloco de código até o usuário sair do programa
        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Salvar Nova Credencial");
            System.out.println("2. Listar Minhas Credenciais");
            System.out.println("3. Sair do Programa");
            System.out.print("Escolha uma opção: ");
            
            // Lê o número que o usuário digitou
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer do teclado (evita bugs ao ler textos depois)

            switch (opcao) {
                case 1:
                    System.out.print("Digite o nome do Serviço (ex: GitHub, Instagram): ");
                    String servico = scanner.nextLine();
                    
                    System.out.print("Digite o Nome de Usuário / E-mail: ");
                    String usuario = scanner.nextLine();
                    
                    System.out.print("Digite a Senha desse serviço: ");
                    String senhaServico = scanner.nextLine();

                    // Chama o gerenciador de arquivos para salvar os dados criptografados
                    VaultManager.salvarCredencial(servico, usuario, senhaServico, senhaMestre);
                    break;

                case 2:
                    // Chama o gerenciador para ler, decriptografar e listar as senhas
                    VaultManager.listarCredenciais(senhaMestre);
                    break;

                case 3:
                    System.out.println("Encerrando o cofre em segurança. Até logo!");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }

        } while (opcao != 3);

        // Fechamos o scanner para liberar o recurso do sistema operacional
        scanner.close();

    }
}
