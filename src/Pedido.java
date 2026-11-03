public class Pedido {

    public static final String[] STATUS = {
            "Recebido", "Em Preparo", "Saiu para Entrega", "Entregue"
    };

    public int numero;
    public ItemSacola[] itens;
    public double total;
    public Endereco endereco;
    public String pagamento;
    public int status = 0;

    public Pedido(int numero, Sacola sacola, Endereco endereco, String pagamento) {
        this.numero = numero;
        this.endereco = endereco;
        this.pagamento = pagamento;
        this.total = sacola.calcularTotal();

        this.itens = new ItemSacola[sacola.itens.length];
        for (int i = 0; i < sacola.itens.length; i++) {
            this.itens[i] = sacola.itens[i];
        }
    }

    public String getStatus() {
        return STATUS[status];
    }

    public boolean avancarStatus() {
        if (status < STATUS.length - 1) {
            status++;
            return true;
        }
        return false; // já foi entregue
    }

    public void mostrarResumo() {
        System.out.printf("Pedido #%d | %s | R$ %.2f%n", numero, getStatus(), total);
    }

    public void mostrarDetalhes() {
        System.out.println("\n-- Pedido #" + numero + " --");
        for (int i = 0; i < itens.length; i++) {
            if (itens[i] != null) {
                System.out.println("- " + itens[i].quantidade + "x " + itens[i].item.nome);
            }
        }
        System.out.printf("Total: R$ %.2f%n", total);
        System.out.println("Endereço: " + endereco.rua + ", " + endereco.numero
                + " - " + endereco.bairro);
        System.out.println("Pagamento: " + pagamento);
        System.out.println("Status: " + getStatus());
    }
}