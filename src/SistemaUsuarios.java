public class SistemaUsuarios {

    Usuario[] usuarios = new Usuario[10];

    public void adicionarDados() {
        usuarios[0] = new Usuario("cliente", "1234", "COMUM");
        usuarios[1] = new Usuario("vendedor", "1234", "VENDEDOR");
    }

    public Usuario autenticar(String login, String senha) {
        for (int i = 0; i < usuarios.length; i++) {
            if (usuarios[i] != null
                    && usuarios[i].login.equals(login)
                    && usuarios[i].senha.equals(senha)) {
                return usuarios[i];
            }
        }
        return null;
    }
}