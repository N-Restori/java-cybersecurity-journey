import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("[+] BEM-VINDA AO THE ZERO-TRUST VAULT");
        System.out.println("========================================");

        System.out.print("Digite sua Senha Mestre para iniciar: ");
        String senhaTexto = scanner.nextLine();
        
        // HIGIENIZAÇÃO DE MEMÓRIA: Migração imediata para estrutura de char[]
        char[] senhaMestre = senhaTexto.toCharArray();
        senhaTexto = ""; 

        int opcao = 0;

        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Salvar Nova Credencial");
            System.out.println("2. Listar Minhas Credenciais");
            System.out.println("3. Sair do Programa");
            System.out.print("Escolha uma opção: ");
            
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpeza do buffer do teclado

            switch (opcao) {
                case 1:
                    System.out.print("Digite o nome do Serviço (ex: GitHub, Instagram): ");
                    String servico = scanner.nextLine();
                    
                    System.out.print("Digite o Nome de Usuário / E-mail: ");
                    String usuario = scanner.nextLine();
                    
                    System.out.print("Digite a Senha desse serviço: ");
                    String senhaServico = scanner.nextLine();

                    VaultManager.salvarCredencial(servico, usuario, senhaServico, senhaMestre);
                    break;

                case 2:
                    VaultManager.listarCredenciais(senhaMestre);
                    break;

                case 3:
                    System.out.println("Encerrando o cofre em segurança. Até logo!");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }

        } while (opcao != 3);

        scanner.close();
    }
}

