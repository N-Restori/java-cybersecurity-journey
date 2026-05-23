# The Zero-Trust Vault 🔒

Um gerenciador de segredos e credenciais via linha de comando (CLI) desenvolvido em Java puro. O projeto foi desenhado sob a filosofia de segurança **Zero-Trust**, onde nenhuma informação de validação global é armazenada no disco, mitigando pontos únicos de falha.

## 🛠️ Problema Real Resolvido
Desenvolvedores e empresas frequentemente vazam chaves de API, tokens e senhas em repositórios públicos por não utilizarem cofres de credenciais locais seguros. Este projeto resolve esse problema permitindo o armazenamento seguro e persistente de segredos no disco rígido através de criptografia forte.

## 🧠 Conceitos de Cybersecurity Aplicados
* **Criptografia Simétrica AES-256:** Utilização do padrão mundial AES com chaves de 256 bits geradas dinamicamente a partir da Senha Mestre do usuário através de funções de hash (SHA-256).
* **Prevenção contra Memory Dumping (Char vs String):** Uso estrito de estruturas de `char[]` para manipulação da Senha Mestre. Diferente de objetos `String` (que sofrem com a imutabilidade e retenção no *String Pool* da memória RAM), os arrays de caracteres são limpos explicitamente com `Arrays.fill()` imediatamente após o uso, impedindo a extração de credenciais por técnicas de varredura de memória.
* **Persistência Segura com Tratamento de Recursos:** Implementação de persistência em arquivo estruturado utilizando `try-with-resources` para garantir o fechamento seguro de descritores de arquivo, evitando *Memory Leaks*.

## 🚀 Como Executar
1. Certifique-se de possuir o Java JDK instalado em sua máquina.
2. Abra o arquivo `Main.java` e execute a aplicação através da sua IDE de preferência ou compile diretamente via terminal.
