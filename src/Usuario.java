public class Usuario {

    public String login;
    public String senha;
    public String tipo;

    public Usuario(String login, String senha, String tipo) {
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
    }

    public boolean ehVendedor() {
        return tipo.equals("VENDEDOR");
    }
}