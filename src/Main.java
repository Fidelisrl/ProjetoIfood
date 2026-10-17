import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        SistemaRestaurantes sistema = new SistemaRestaurantes();
        sistema.adicionarDados();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nPROJETO IFOOD");

            System.out.println("1 - MENU");
            System.out.println("2 - Cadastro de Restaurante");
            System.out.println("Digite o caminho desejado: ");
            int escolhaMenu = scanner.nextInt();

            switch (escolhaMenu){
                case 1:
                    menu(sistema, scanner);
                    break;
                case 2:
                    cadastroRestaurante(sistema, scanner);
                    break;
                case 3:
                    break;
            }
        }


    }

    static void menu(SistemaRestaurantes sistema, Scanner scanner) {
        System.out.println("-- MENU --");
        System.out.println("- Categorias -");
        System.out.println("1 - Marmita \n2 - Pizza \n3 - Fast-Food \n4 - Doces");
        System.out.println("O que você irá querer hoje? : ");
        int escolha = scanner.nextInt();

        switch (escolha) {
            case 1:
                for (int i = 0; i < sistema.restaurantes.length; i++) {
                    if (sistema.restaurantes[i] != null && sistema.restaurantes[i].categoria.equals("Marmita")){
                        System.out.println("- " + sistema.restaurantes[i].nome + " (" + sistema.restaurantes[i].tempoMedio + " min)");
                    }
                }
                break;
            case 2:
                for (int i = 0; i < sistema.restaurantes.length; i++) {
                    if (sistema.restaurantes[i] != null && sistema.restaurantes[i].categoria.equals("Pizza")){
                        System.out.println("- " + sistema.restaurantes[i].nome + " (" + sistema.restaurantes[i].tempoMedio + " min)");
                    }
                }
                break;
            case 3:
                for (int i = 0; i < sistema.restaurantes.length; i++) {
                    if (sistema.restaurantes[i] != null && sistema.restaurantes[i].categoria.equals("Fast-Food")){
                        System.out.println("- " + sistema.restaurantes[i].nome + " (" + sistema.restaurantes[i].tempoMedio + " min)");
                    }
                }
                break;
            case 4:
                for (int i = 0; i < sistema.restaurantes.length; i++) {
                    if (sistema.restaurantes[i] != null && sistema.restaurantes[i].categoria.equals("Doces")){
                        System.out.println("- " + sistema.restaurantes[i].nome + " (" + sistema.restaurantes[i].tempoMedio + " min)");
                    }
                }
                break;
        }
    }

    static void cadastroRestaurante(SistemaRestaurantes sistema, Scanner scanner){
        scanner.nextLine();
        System.out.println("= CADASTRO DE RESTAURANTE =");
        System.out.println("Nome: ");
        String nome = scanner.nextLine();
        System.out.println("Categoria: ");
        String categoria = scanner.nextLine();
        System.out.println("Tempo Médio de Entrega: ");
        int tempoMedio = scanner.nextInt();

        for (int i = 0; i < sistema.restaurantes.length; i++) {
            if (sistema.restaurantes[i] == null) {
                sistema.restaurantes[i] = new Restaurantes(nome, categoria, tempoMedio);
                System.out.println("Restaurante adicionado!");
                break;
            }
        }
    }

}
