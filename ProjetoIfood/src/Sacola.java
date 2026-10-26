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

    public void mostrarItem(){
        boolean vazio = true;
        for(int i=0; i < itens.length; i++){
            if(itens[i] != null){
                System.out.println("- " + itens[i].quantidade + "x " + itens[i].item.nome);
                vazio = false;
            }
        }
        if(vazio){
            System.out.println("A sua sacola está vazia.");
        }
    }
}
