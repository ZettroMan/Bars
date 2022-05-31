package ru.zettro.bars;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.zettro.bars.adapters.LocalDateAdapter;
import ru.zettro.bars.model.Contract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableView<Contract> contractTable;
    @FXML
    private TableColumn<Contract, LocalDate> dateColumn;
    @FXML
    private TableColumn<Contract, String> numberColumn;
    @FXML
    private TableColumn<Contract, String> titleColumn;
    @FXML
    private TableColumn<Contract, LocalDate> createdColumn;
    @FXML
    private TableColumn<Contract, LocalDate> modifiedColumn;
    @FXML
    private TableColumn<Contract, CheckBox> isActualColumn;
    @FXML
    private TextField serviceAddress;

    @FXML
    private Label statusLine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set default server address
        serviceAddress.setText("https://estate-mgmt.herokuapp.com/api/contracts");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        createdColumn.setCellValueFactory(new PropertyValueFactory<>("created"));
        modifiedColumn.setCellValueFactory(new PropertyValueFactory<>("modified"));
        isActualColumn.setCellValueFactory(
                arg0 -> {
                    Contract contract = arg0.getValue();
                    CheckBox checkBox = new CheckBox();
                    checkBox.selectedProperty().setValue(contract.isActual());
                    checkBox.setDisable(true);
                    checkBox.setStyle("-fx-opacity: 1");
                    return new SimpleObjectProperty<>(checkBox);
                }
        );
        refreshData();
    }

    public void refreshData() {
        statusLine.setText("Данные загружаются...");
        try {
            contractTable.setItems(getContracts(serviceAddress.getText()));
            dateColumn.setSortType(TableColumn.SortType.ASCENDING);
            contractTable.getSortOrder().add(dateColumn);
            contractTable.sort();
        } catch (IOException e) {
            statusLine.setText("Произошла ошибка загрузки данных с сервера. " + e.getMessage());
            contractTable.getItems().clear();
            //e.printStackTrace();
        }
    }

    private ObservableList<Contract> getContracts(String address) throws IOException {
        ObservableList<Contract> contracts = FXCollections.observableArrayList();
        contracts.addAll(getContractListFromServer(address));
        return contracts;
    }

    private List<Contract> getContractListFromServer(String address) throws IOException {
        URL url = new URL(address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // successfully connected
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
            Contract[] contractArray;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                contractArray = gson.fromJson(in, Contract[].class);
            } catch (Exception e) {
                throw new IOException("Ошибка чтения входного потока.", e);
            }
            // also, sometimes fromJson() not throws an exception but returns null
            if (contractArray == null) {
                throw new IOException("Ошибка чтения входного потока.");
            }
            statusLine.setText("Данные успешно загружены");
            return Arrays.asList(contractArray);
        } else {
            throw new IOException("Код ответа сервера: " + responseCode);
        }

    }

}
