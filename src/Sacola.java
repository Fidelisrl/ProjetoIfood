public class Sacola {
    ItemSacola[] itens = new ItemSacola[20];

    public void adicionar(ItemCardapio item, int quantidade){
        for(int i=0; i < itens.length; i++){
            if(itens[i] != null && itens[i].item == item){
                itens[i].quantidade += quantidade;
                return;
            }
        }
        for(int i=0; i < itens.length; i++){
            if(itens[i] == null){
                itens[i] = new ItemSacola(item, quantidade);
                return;
            }
        }
        System.out.println("Sacola cheia.");
    }

    public boolean alterarQuantidade(String nome, int novaQuantidade){
        for(int i=0; i < itens.length; i++){
            if(itens[i] != null && itens[i].item.nome.equalsIgnoreCase(nome)){
                if(novaQuantidade <= 0){
                    itens[i] = null;
                } else {
                    itens[i].quantidade = novaQuantidade;
                }
                return true;
            }
        }
        return false;
    }
    public double calcularTotal() {
        double total = 0;
        for (int i = 0; i < itens.length; i++) {
            if (itens[i] != null) {
                total += itens[i].item.preco * itens[i].quantidade;
            }
        }
        return total;
    }

    public void mostrarItem() {
        boolean vazio = true;
        for (int i = 0; i < itens.length; i++) {
            if (itens[i] != null) {
                double subtotal = itens[i].item.preco * itens[i].quantidade;
                System.out.printf("- %dx %s | R$ %.2f%n",
                        itens[i].quantidade, itens[i].item.nome, subtotal);
                vazio = false;
            }
        }
        if (vazio) {
            System.out.println("A sua sacola está vazia.");
        } else {
            System.out.printf("TOTAL: R$ %.2f%n", calcularTotal());
        }
    }
}
