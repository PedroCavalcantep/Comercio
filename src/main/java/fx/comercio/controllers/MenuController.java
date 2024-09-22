package fx.comercio.controllers;
import fx.comercio.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MenuController {

    @FXML
    private Text userText;

    public void initialize() {

        String userName = Session.getUsuario();
        if (userName != null) {
            userText.setText("Bem-vindo, " + userName);
        }
    }

    public void setUserName(String userName) {
        userText.setText("Bem-vindo, " + userName);
    }

    @FXML
    protected void abrirProdutos(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fx/comercio/Produto.fxml")));
        Parent segundaPagina = loader.load();

        Scene segundaCena = new Scene(segundaPagina, 1280, 720);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(segundaCena);
        stage.show();

    }
}




