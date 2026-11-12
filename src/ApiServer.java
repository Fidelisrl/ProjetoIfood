import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.regex.*;

/** Expõe as classes do backend via HTTP (só usa o JDK, sem bibliotecas). */
public class ApiServer {
    static SistemaRestaurantes rest = new SistemaRestaurantes();
    static SistemaPedidos pedidos = new SistemaPedidos();
    static SistemaUsuarios usuarios = new SistemaUsuarios();
    static Sacola sacola = new Sacola(); // uma sacola global (demo de um usuário por vez)

    public static void main(String[] args) throws Exception {
        rest.adicionarDados();
        usuarios.adicionarDados();
        HttpServer s = HttpServer.create(new InetSocketAddress(8080), 0);
        s.createContext("/", ApiServer::handle);
        s.start();
        System.out.println("Rodando em http://localhost:8080");
    }

    static void handle(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath(), m = ex.getRequestMethod();
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        if (m.equals("OPTIONS")) {
            ex.sendResponseHeaders(204, -1);
            ex.close();
            return;
        }
        int code = 200;
        String out;
        String ctype = "application/json; charset=utf-8";
        try {
            if (!path.startsWith("/api")) {
                ctype = "text/html; charset=utf-8";
                out = Files.readString(Path.of("index.html"));
            } else {
                out = route(m, path, ex);
            }
        } catch (IllegalArgumentException e) {
            code = 400;
            out = "{\"erro\":" + j(e.getMessage()) + "}";
        } catch (Exception e) {
            code = 500;
            out = "{\"erro\":" + j(String.valueOf(e)) + "}";
        }
        byte[] b = out.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", ctype);
        ex.sendResponseHeaders(code, b.length);
        ex.getResponseBody().write(b);
        ex.close();
    }

    static String route(String m, String path, HttpExchange ex) throws IOException {
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String r = m + " " + path;
        switch (r) {
            case "POST /api/login": {
                Usuario u = usuarios.autenticar(f(body, "login"), f(body, "senha"));
                if (u == null) throw new IllegalArgumentException("Login ou senha incorretos.");
                return "{\"login\":" + j(u.login) + ",\"vendedor\":" + u.ehVendedor() + "}";
            }
            case "GET /api/restaurantes": {
                StringBuilder sb = new StringBuilder("[");
                for (Restaurantes x : rest.restaurantes)
                    if (x != null) sb.append(sb.length() > 1 ? "," : "").append(rj(x));
                return sb.append("]").toString();
            }
            case "POST /api/restaurantes": {
                for (int i = 0; i < rest.restaurantes.length; i++)
                    if (rest.restaurantes[i] == null) {
                        rest.restaurantes[i] = new Restaurantes(req(body, "nome"), req(body, "categoria"), Integer.parseInt(req(body, "tempo")));
                        return "{\"ok\":true}";
                    }
                throw new IllegalArgumentException("Limite de restaurantes atingido.");
            }
            case "PUT /api/restaurantes": {
                int i = rest.buscarIndicePorNome(qp(ex, "nome"));
                if (i == -1) throw new IllegalArgumentException("Restaurante não encontrado.");
                rest.restaurantes[i].nome = req(body, "nome");
                rest.restaurantes[i].categoria = req(body, "categoria");
                rest.restaurantes[i].tempoMedio = Integer.parseInt(req(body, "tempo"));
                return "{\"ok\":true}";
            }
            case "DELETE /api/restaurantes":
                if (!rest.removerRestaurante(qp(ex, "nome"))) throw new IllegalArgumentException("Restaurante não encontrado.");
                return "{\"ok\":true}";
            case "POST /api/cardapio": {
                int i = rest.buscarIndicePorNome(req(body, "restaurante"));
                if (i == -1) throw new IllegalArgumentException("Restaurante não encontrado.");
                rest.restaurantes[i].adicionarItem(req(body, "nome"), f(body, "descricao"), Double.parseDouble(req(body, "preco")));
                return "{\"ok\":true}";
            }
            case "GET /api/sacola":
                return sj();
            case "POST /api/sacola": {
                int i = rest.buscarIndicePorNome(req(body, "restaurante"));
                if (i == -1) throw new IllegalArgumentException("Restaurante não encontrado.");
                int q = Integer.parseInt(req(body, "quantidade"));
                if (q <= 0) throw new IllegalArgumentException("Quantidade inválida.");
                for (ItemCardapio it : rest.restaurantes[i].cardapio)
                    if (it != null && it.nome.equalsIgnoreCase(req(body, "item"))) {
                        sacola.adicionar(it, q);
                        return sj();
                    }
                throw new IllegalArgumentException("Item não encontrado.");
            }
            case "PUT /api/sacola":
                if (!sacola.alterarQuantidade(req(body, "item"), Integer.parseInt(req(body, "quantidade"))))
                    throw new IllegalArgumentException("Item não está na sacola.");
                return sj();
            case "GET /api/pedidos": {
                StringBuilder sb = new StringBuilder("[");
                for (Pedido p : pedidos.pedidos)
                    if (p != null) sb.append(sb.length() > 1 ? "," : "").append(pj(p));
                return sb.append("]").toString();
            }
            case "POST /api/pedidos": {
                if (sacola.estaVazia()) throw new IllegalArgumentException("A sacola está vazia.");
                String pg = req(body, "pagamento");
                Endereco e = new Endereco(req(body, "rua"), req(body, "numero"), req(body, "bairro"), f(body, "complemento"));
                Pedido p = new Pedido(pedidos.proximoNumero(), sacola, e, pg);
                pedidos.adicionar(p);
                sacola.limpar();
                return pj(p);
            }
            case "POST /api/pedidos/avancar": {
                Pedido p = pedidos.buscarPorNumero(Integer.parseInt(req(body, "numero")));
                if (p == null) throw new IllegalArgumentException("Pedido não encontrado.");
                if (!p.avancarStatus()) throw new IllegalArgumentException("Esse pedido já foi entregue.");
                return pj(p);
            }
        }
        // cardápio: GET /api/cardapio?nome=...
        if (r.equals("GET /api/cardapio")) {
            int i = rest.buscarIndicePorNome(qp(ex, "nome"));
            if (i == -1) throw new IllegalArgumentException("Restaurante não encontrado.");
            StringBuilder sb = new StringBuilder("[");
            for (ItemCardapio it : rest.restaurantes[i].cardapio)
                if (it != null)
                    sb.append(sb.length() > 1 ? "," : "").append("{\"nome\":" + j(it.nome) + ",\"descricao\":" + j(it.descricao) + ",\"preco\":" + it.preco + "}");
            return sb.append("]").toString();
        }
        throw new IllegalArgumentException("Rota não existe: " + r);
    }

    // ---------- JSON ----------
    static String rj(Restaurantes x) {
        return "{\"nome\":" + j(x.nome) + ",\"categoria\":" + j(x.categoria) + ",\"tempo\":" + x.tempoMedio + "}";
    }

    static String sj() {
        StringBuilder sb = new StringBuilder("{\"itens\":[");
        boolean first = true;
        for (ItemSacola it : sacola.itens) {
            if (it == null) continue;
            if (!first) sb.append(",");
            first = false;
            sb.append("{\"nome\":" + j(it.item.nome) + ",\"quantidade\":" + it.quantidade + ",\"preco\":" + it.item.preco + "}");
        }
        return sb.append("],\"total\":" + sacola.calcularTotal() + "}").toString();
    }

    static String pj(Pedido p) {
        StringBuilder sb = new StringBuilder("{\"numero\":" + p.numero + ",\"status\":" + j(p.getStatus()) + ",\"entregue\":" + (p.status == Pedido.STATUS.length - 1)
                + ",\"total\":" + p.total + ",\"pagamento\":" + j(p.pagamento)
                + ",\"endereco\":" + j(p.endereco.rua + ", " + p.endereco.numero + " - " + p.endereco.bairro) + ",\"itens\":[");
        boolean first = true;
        for (ItemSacola it : p.itens) {
            if (it == null) continue;
            if (!first) sb.append(",");
            first = false;
            sb.append(j(it.quantidade + "x " + it.item.nome));
        }
        return sb.append("]}").toString();
    }

    static String j(String s) {
        if (s == null) s = "";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ") + "\"";
    }

    static String f(String json, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*(?:\"((?:\\\\.|[^\"\\\\])*)\"|(-?[\\d.]+))").matcher(json);
        if (!m.find()) return "";
        String v = m.group(1) != null ? m.group(1).replace("\\\"", "\"").replace("\\\\", "\\") : m.group(2);
        return v.trim();
    }

    static String req(String json, String key) {
        String v = f(json, key);
        if (v.isEmpty()) throw new IllegalArgumentException("Campo obrigatório: " + key);
        return v;
    }

    static String qp(HttpExchange ex, String key) throws IOException {
        String q = ex.getRequestURI().getRawQuery();
        if (q != null)
            for (String p : q.split("&"))
                if (p.startsWith(key + "="))
                    return URLDecoder.decode(p.substring(key.length() + 1), StandardCharsets.UTF_8);
        return "";
    }
}