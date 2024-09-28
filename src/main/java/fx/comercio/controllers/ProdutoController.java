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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fx.comercio.Produto;

public class ProdutoController {

    @FXML
    private TableView<Produto> productTable;

    @FXML
    private TableColumn<Produto, String> colCodProd;
    @FXML
    private TableColumn<Produto, String> colDescricao;
    @FXML
    private TableColumn<Produto, Integer> qtd;
    @FXML
    private TableColumn<Produto, Double> colPrecoCompra;
    @FXML
    private TableColumn<Produto, Double> colPrecoVenda;
    @FXML
    private TableColumn<Produto, String> colCodBarra;
    @FXML
    private TableColumn<Produto, String> colNcm;

    @FXML
    private TextField descricaoField;
    @FXML
    private TextField precoCompraField;
    @FXML
    private TextField precoVendaField;

    private ObservableList<Produto> productList;

    @FXML
    public void initialize() {
        // Configuração das colunas da tabela
        colCodProd.setCellValueFactory(new PropertyValueFactory<>("codProd"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colPrecoCompra.setCellValueFactory(new PropertyValueFactory<>("precoCompra"));
        colPrecoVenda.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        colCodBarra.setCellValueFactory(new PropertyValueFactory<>("codBarra"));
        colNcm.setCellValueFactory(new PropertyValueFactory<>("ncm"));
        qtd.setCellValueFactory(new PropertyValueFactory<>("qtd"));

        productList = FXCollections.observableArrayList();
        productTable.setItems(productList);
        loadProducts(); // Carrega os produtos da API
    }

    private void loadProducts() {
        new Thread(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://localhost:8081/bazinga"))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                ObjectMapper objectMapper = new ObjectMapper();
                List<Produto> produtos = objectMapper.readValue(response.body(), new TypeReference<List<Produto>>() {});
                Platform.runLater(() -> productList.setAll(produtos));
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Erro ao carregar produtos: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    public void onCadastrar(ActionEvent event) {
        String descricao = descricaoField.getText();
        String precoCompraStr = precoCompraField.getText();
        String precoVendaStr = precoVendaField.getText();

        if (descricao.isEmpty() || precoCompraStr.isEmpty() || precoVendaStr.isEmpty()) {
            showAlert("Preencha todos os campos");
            return;
        }

        double precoCompra;
        double precoVenda;

        try {
            precoCompra = Double.parseDouble(precoCompraStr);
            precoVenda = Double.parseDouble(precoVendaStr);
        } catch (NumberFormatException e) {
            showAlert("Preço Compra e Preço Venda devem ser numéricos.");
            return;
        }

        Produto novoProduto = new Produto(descricao, precoCompra, precoVenda);

        // Enviar o novo produto para a API
        new Thread(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                ObjectMapper objectMapper = new ObjectMapper();
                String produtoJson = objectMapper.writeValueAsString(novoProduto);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://localhost:8081/bazinga"))  // URL da API
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(produtoJson))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Verificar o status da resposta
                if (response.statusCode() == 200 || response.statusCode() == 201) {
                    // Produto cadastrado com sucesso
                    Platform.runLater(() -> {
                        productList.add(novoProduto);  // Adicionar o novo produto na tabela
                        descricaoField.clear();
                        precoCompraField.clear();
                        precoVendaField.clear();
                    });
                } else {
                    Platform.runLater(() -> showAlert("Erro ao cadastrar produto: " + response.body()));
                    System.out.println("Status da resposta: " + response.statusCode());
                }
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Erro ao enviar produto: " + e.getMessage()));
            }
        }).start();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
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

    @FXML
    private void cadastrarProduto(ActionEvent event) {
        try {
            // Certifique-se de que o caminho para o FXML está correto
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/comercio/CadastrarProduto.fxml"));

            Parent cadastrarPage = loader.load();

            // Criando um novo Stage para exibir a tela de cadastro
            Stage stage = new Stage();
            stage.setScene(new Scene(cadastrarPage));
            stage.setTitle("Cadastrar Produto");
            stage.initModality(Modality.APPLICATION_MODAL); // Modal impede interação com outras janelas
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();  // Para ver o erro completo no console
            showAlert("Erro ao abrir a tela de cadastro: " + e.getMessage());
        }
    }

    @FXML
    public void deleteProduct(ActionEvent event) {
        Produto selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            // Implementar a lógica para excluir o produto
            productList.remove(selectedProduct);
        } else {
            showAlert("Selecione um produto para excluir.");
        }
    }
}
