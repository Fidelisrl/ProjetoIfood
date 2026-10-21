public class SistemaRestaurantes {

    Restaurantes[] restaurantes = new Restaurantes[10];

    public void adicionarDados(){
        restaurantes[0] = new Restaurantes("Mc Donalds", "Fast-Food", 60);
        restaurantes[1] = new Restaurantes("Bob's", "Fast-Food", 40);
        restaurantes[2] = new Restaurantes("Cantinho da Marmita", "Marmita", 90);
        restaurantes[3] = new Restaurantes("The King Açaí", "Doces", 20);
        restaurantes[4] = new Restaurantes("Dominos Pizzaria", "Pizza", 120);
    }

    public void listarTodos() {
        boolean temRestaurante = false;
        for (int i = 0; i < restaurantes.length; i++) {
            if (restaurantes[i] != null) {
                System.out.println("- " + restaurantes[i].nome + " | Categoria: "
                        + restaurantes[i].categoria + " | Tempo: "
                        + restaurantes[i].tempoMedio + " min");
                temRestaurante = true;
            }
        }
        if (!temRestaurante) {
            System.out.println("Nenhum restaurante cadastrado no momento.");
        }
    }

    public int buscarIndicePorNome(String nome) {
        for (int i = 0; i < restaurantes.length; i++) {
            if (restaurantes[i] != null && restaurantes[i].nome.equalsIgnoreCase(nome)) {
                return i;
            }
        }
        return -1;
    }

    public boolean removerRestaurante(String nome) {
        int index = buscarIndicePorNome(nome);
        if (index != -1) {
            restaurantes[index] = null;
            return true;
        }
        return false;
    }
}
