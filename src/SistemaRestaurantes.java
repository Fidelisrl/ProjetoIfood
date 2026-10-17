public class SistemaRestaurantes {

    Restaurantes[] restaurantes = new Restaurantes[10];

    public void adicionarDados(){
        restaurantes[0] = new Restaurantes("Mc Donalds", "Fast-Food", 60);
        restaurantes[1] = new Restaurantes("Bob's", "Fast-Food", 40);
        restaurantes[2] = new Restaurantes("Cantinho da Marmita", "Marmita", 90);
        restaurantes[3] = new Restaurantes("The King Açaí", "Doces", 20);
        restaurantes[4] = new Restaurantes("Dominos Pizzaria", "Pizza", 120);
    }
}
