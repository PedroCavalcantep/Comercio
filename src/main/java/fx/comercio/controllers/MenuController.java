package fx.comercio.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MenuController {

    @FXML
    private Text userText; // Referência ao elemento de texto

    // Método para atualizar o texto com o nome do usuário
    public void setUserName(String userName) {
        userText.setText("Bem-vindo, " + userName);
    }
}
