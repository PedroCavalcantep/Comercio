package fx.comercio.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import fx.comercio.Produto;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.Stage;


public class ProdutoController {

    @FXML
    private TableView<Produto> productTable;

    @FXML
    private TableColumn<Produto, Integer> colCodProd;
    @FXML
    private TableColumn<Produto, String> colDescricao;
    @FXML
    private TableColumn<Produto, Double> colPrecoCompra;
    @FXML
    private TableColumn<Produto, Double> colPrecoVenda;
    @FXML
    private TableColumn<Produto, String> colCodBarra;
    @FXML
    private TableColumn<Produto, String> colNcm;

    @FXML
    public void initialize() {

        colCodProd.setCellValueFactory(new PropertyValueFactory<>("codProd"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colPrecoCompra.setCellValueFactory(new PropertyValueFactory<>("precoCompra"));
        colPrecoVenda.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        colCodBarra.setCellValueFactory(new PropertyValueFactory<>("codBarra"));
        colNcm.setCellValueFactory(new PropertyValueFactory<>("ncm"));

        fetchProducts();
    }

    public void fetchProducts() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/bazinga"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::parseProducts)
                .thenAccept(this::populateTable)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }


    private List<Produto> parseProducts(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, new TypeReference<List<Produto>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void populateTable(List<Produto> produtos) {
        ObservableList<Produto> productList = FXCollections.observableArrayList(produtos);


        Platform.runLater(() -> {
            productTable.setItems(productList);
        });
    }
    @FXML
    protected void voltarMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fx/comercio/Menu.fxml")));
        Parent menuPage = loader.load();

        Scene menuScene = new Scene(menuPage, 1280, 720);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(menuScene);
        stage.show();
    }
}
