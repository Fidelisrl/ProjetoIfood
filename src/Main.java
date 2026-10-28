import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        SistemaRestaurantes sistema = new SistemaRestaurantes();
        sistema.adicionarDados();

        Scanner scanner = new Scanner(System.in);
        Sacola sacola = new Sacola();

        while (true) {
            System.out.println("\nPROJETO IFOOD");

            System.out.println("1 - MENU");
            System.out.println("2 - Cadastro de Restaurante");
            System.out.println("3 - Editar Restaurante");
            System.out.println("4 - Deletar Restaurante");
            System.out.println("5 - Sacola");
            System.out.println("0 - Sair");
            System.out.println("Digite o caminho desejado: ");
            int escolhaMenu = scanner.nextInt();
            scanner.nextLine();

            switch (escolhaMenu){
                case 1:
                    menu(sistema, scanner, sacola);
                    break;
                case 2:
                    cadastroRestaurante(sistema, scanner);
                    break;
                case 3:
                    editarRestaurante(sistema, scanner);
                    break;
                case 4:
                    deletarRestaurante(sistema, scanner);
                    break;
                case 5:
                    verSacola(sacola, scanner);
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }


    }

    static void menu(SistemaRestaurantes sistema, Scanner scanner, Sacola sacola) {
        System.out.println("-- MENU --");
        System.out.println("- Categorias -");
        System.out.println("1 - Marmita \n2 - Pizza \n3 - Fast-Food \n4 - Doces");
        System.out.println("O que você irá querer hoje? : ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        String categoria;
        switch (escolha) {
            case 1:
                categoria = "Marmita";
                break;
            case 2:
                categoria = "Pizza";
                break;
            case 3:
                categoria = "Fast-Food";
                break;
            case 4:
                categoria = "Doces";
                break;
            default:
                System.out.println("Opção inválida!");
                return;
        }

        boolean achou = false;
        for (int i = 0; i < sistema.restaurantes.length; i++) {
            if (sistema.restaurantes[i] != null && sistema.restaurantes[i].categoria.equals(categoria)) {
                System.out.println("- " + sistema.restaurantes[i].nome + " (" + sistema.restaurantes[i].tempoMedio + " min)");
                achou = true;
            }
        }

        if (!achou) {
            System.out.println("Nenhum restaurante nessa categoria.");
            return;
        }

        System.out.print("\nDigite o nome do restaurante para ver o cardápio (ou Enter para voltar): ");
        String nomeBusca = scanner.nextLine();

        if (nomeBusca.isEmpty()) {
            return;
        }

        int index = sistema.buscarIndicePorNome(nomeBusca);
        if (index == -1 || !sistema.restaurantes[index].categoria.equals(categoria)) {
            System.out.println("Restaurante não encontrado nessa categoria!");
            return;
        }

        System.out.println("\n-- Cardápio: " + sistema.restaurantes[index].nome + " --");
        sistema.restaurantes[index].mostrarCardapio();

        adicionarNaSacola(sistema.restaurantes[index], sacola, scanner);
    }

    static void cadastroRestaurante(SistemaRestaurantes sistema, Scanner scanner){
        System.out.println("= CADASTRO DE RESTAURANTE =");
        System.out.println("Nome: ");
        String nome = scanner.nextLine();
        System.out.println("Categoria: ");
        String categoria = scanner.nextLine();
        System.out.println("Tempo Médio de Entrega: ");
        int tempoMedio = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < sistema.restaurantes.length; i++) {
            if (sistema.restaurantes[i] == null) {
                sistema.restaurantes[i] = new Restaurantes(nome, categoria, tempoMedio);
                System.out.println("Restaurante adicionado!");
                break;
            }
        }
    }

    static void editarRestaurante(SistemaRestaurantes sistema, Scanner scanner) {
        System.out.println("\n= EDITAR RESTAURANTE =");
        sistema.listarTodos();

        System.out.print("\nDigite o nome exato do restaurante que deseja alterar: ");
        String nomeBusca = scanner.nextLine();

        int index = sistema.buscarIndicePorNome(nomeBusca);
        if (index == -1) {
            System.out.println("Restaurante não encontrado!");
            return;
        }

        System.out.println("Restaurante encontrado! Digite os novos dados:");
        System.out.print("Novo Nome: ");
        String novoNome = scanner.nextLine();
        System.out.print("Nova Categoria: ");
        String novaCategoria = scanner.nextLine();
        System.out.print("Novo Tempo Médio de Entrega: ");
        int novoTempo = scanner.nextInt();
        scanner.nextLine();

        sistema.restaurantes[index].nome = novoNome;
        sistema.restaurantes[index].categoria = novaCategoria;
        sistema.restaurantes[index].tempoMedio = novoTempo;

        System.out.println("Restaurante atualizado com sucesso!");
    }

    static void deletarRestaurante(SistemaRestaurantes sistema, Scanner scanner) {
        System.out.println("\n= DELETAR RESTAURANTE =");
        sistema.listarTodos();

        System.out.print("\nDigite o nome do restaurante que deseja remover: ");
        String nomeBusca = scanner.nextLine();

        boolean removido = sistema.removerRestaurante(nomeBusca);
        if (removido) {
            System.out.println("Restaurante removido com sucesso!");
        } else {
            System.out.println("Restaurante não encontrado!");
        }
    }

    static void adicionarNaSacola(Restaurantes restaurantes, Sacola sacola, Scanner scanner){
        System.out.println("\nDigite o nome do item que deseja adicionar, ou enter para voltar: ");
        String nomeItem = scanner.nextLine();

        if(nomeItem.isEmpty()){
            return;
        }

        ItemCardapio escolhido = null;

        for(int i=0; i < restaurantes.cardapio.length; i++){
            if(restaurantes.cardapio[i] != null && restaurantes.cardapio[i].nome.equalsIgnoreCase(nomeItem)){
                escolhido = restaurantes.cardapio[i];
                break;
            }
        }
        if(escolhido == null){
            System.out.println("Item não encontrado.");
            return;
        }
        System.out.println("Quantidade: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        if(quantidade <= 0){
            System.out.println("Quantidade inválida.");
            return;
        }

        sacola.adicionar(escolhido, quantidade);
        System.out.println("Adicionado à sacola.");
    }

    static void alterarQuantidade(Sacola sacola, Scanner scanner){
        System.out.println("Alterar Quantidade");
        sacola.mostrarItem();

        System.out.println("\nNome do item: ");
        String nome = scanner.nextLine();

        System.out.println("Nova quantidade (0 para remover): ");
    }

    static void verSacola(Sacola sacola, Scanner scanner){
        System.out.println("\n= SACOLA =");
        sacola.mostrarItem();
        System.out.println("Escreva o nome do item para alterar sua quantidade, ou enter para voltar: ");
        String nome = scanner.nextLine();

        if(nome.isEmpty()){
            return;
        }

        System.out.println("Nova quantidade (0 para remover): ");
        int novaQuantidade = scanner.nextInt();
        scanner.nextLine();

        if(sacola.alterarQuantidade(nome, novaQuantidade)){
            System.out.println("Sacola atualizada.");
        } else {
            System.out.println("Este item não foi adicionado à sacola.");
        }
    }
}
