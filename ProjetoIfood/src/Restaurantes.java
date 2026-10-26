public class Restaurantes {

    public String nome;
    public String categoria;
    public int tempoMedio;
    public ItemCardapio[] cardapio = new ItemCardapio[10];

    public Restaurantes(String nome, String categoria, int tempoMedio) {
        this.nome = nome;
        this.categoria = categoria;
        this.tempoMedio = tempoMedio;
    }

    public void adicionarItem(String nome, String descricao, double preco) {
        for (int i = 0; i < cardapio.length; i++) {
            if (cardapio[i] == null) {
                cardapio[i] = new ItemCardapio(nome, descricao, preco);
                return;
            }
        }
        System.out.println("Cardápio cheio!");
    }

    public void mostrarCardapio() {
        boolean temItem = false;
        for (int i = 0; i < cardapio.length; i++) {
            if (cardapio[i] != null) {
                System.out.println("- " + cardapio[i].nome);
                System.out.println("  " + cardapio[i].descricao);
                System.out.printf("  R$ %.2f%n", cardapio[i].preco);
                temItem = true;
            }
        }
        if (!temItem) {
            System.out.println("Esse restaurante ainda não tem itens no cardápio.");
        }
    }
}
