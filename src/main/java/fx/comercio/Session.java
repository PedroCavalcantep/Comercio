package fx.comercio;

public class Session {
    //no momento só to guardando o nome, lembrar de arrumar isso
    private static String usuario;
    public static String getUsuario() {
        return usuario;
    }
    public static void setUsuario(String userName) {
        Session.usuario = userName;
    }
}
