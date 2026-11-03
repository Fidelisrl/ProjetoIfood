public class SistemaPedidos {

    Pedido[] pedidos = new Pedido[50];
    int contador = 0;

    public int proximoNumero() {
        contador++;
        return contador;
    }

    public void adicionar(Pedido pedido) {
        for (int i = 0; i < pedidos.length; i++) {
            if (pedidos[i] == null) {
                pedidos[i] = pedido;
                return;
            }
        }
        System.out.println("Limite de pedidos atingido!");
    }

    public void listar() {
        boolean vazio = true;
        for (int i = 0; i < pedidos.length; i++) {
            if (pedidos[i] != null) {
                pedidos[i].mostrarResumo();
                vazio = false;
            }
        }
        if (vazio) {
            System.out.println("Você ainda não fez nenhum pedido.");
        }
    }

    public Pedido buscarPorNumero(int numero) {
        for (int i = 0; i < pedidos.length; i++) {
            if (pedidos[i] != null && pedidos[i].numero == numero) {
                return pedidos[i];
            }
        }
        return null;
    }
}