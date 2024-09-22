package fx.comercio.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fx.comercio.Session;

public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private TextField senhaField;

    @FXML
    private Text loginErro;

    @FXML
    protected void abrirMenu(ActionEvent event) throws IOException {
        String login = loginField.getText();
        String senha = senhaField.getText();


        String userName = realizarLogin(login, senha);

        if (userName != null) {
            Session.setUsuario(userName);

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fx/comercio/Menu.fxml")));
            Parent segundaPagina = loader.load();

            MenuController menuController = loader.getController();
            menuController.setUserName(userName);

            Scene segundaCena = new Scene(segundaPagina, 1280, 720);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(segundaCena);
            stage.show();
        } else {
            loginErro.setText("Login/senha incorreto");
        }
    }

    private String realizarLogin(String login, String senha) throws IOException {
        String urlString = "http://localhost:8081/usuario/login?login=" + login + "&senha=" + senha;
        URL url = new URL(urlString);


        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");


        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.toString());
            return jsonResponse.get("login").asText();
        } else {
            return null;
        }
    }
}
