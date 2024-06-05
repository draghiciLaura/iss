package org.example.issproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.issproject.business.Service;
import org.example.issproject.domain.Seat;
import org.example.issproject.repo.LodgeRepo;
import org.example.issproject.repo.SeatsRepo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminController {

    public TextField rowField;
    public TextField lodgeField;
    public TextField priceField;
    public TextField updatePriceField;
    @FXML
    private Label seatPriceLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private GridPane gridPane;

    @FXML
    private ChoiceBox<String> lodgeChoiceBox;

    private Set<Integer> selectedSeatIds;
    private Service service;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setService(Service service) {
        this.service = service;
    }

    public void initialize() {
        String url = "jdbc:sqlite:identifier.sqlite";
        String username = "";
        String password = "";
        LodgeRepo lodgeRepo = new LodgeRepo(url, username, password);
        SeatsRepo seatsRepo = new SeatsRepo(url, username, password);
        service = new Service(lodgeRepo, seatsRepo);
        selectedSeatIds = new HashSet<>();

        List<String> lodges = service.getAllLodges();
        lodgeChoiceBox.getItems().addAll(lodges);
        lodgeChoiceBox.setValue("Lodge A");
        loadSeats("Lodge A");
        lodgeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadSeats(newValue);
            }
        });
    }

    private void loadSeats(String lodgeName) {
        gridPane.getChildren().clear();
        selectedSeatIds.clear();
        List<Seat> seats = service.getAllSeatsFromLodge(lodgeName);
        for (Seat seat : seats) {
            Button button = new Button("Seat " + seat.getId());
            button.getStyleClass().add("grid-button");

            button.setOnMouseEntered(event -> {
                float price = service.getSeatPrice(seat.getId());
                seatPriceLabel.setText("Price: $" + price);
            });

            button.setOnMouseExited(event -> seatPriceLabel.setText(""));

            button.setOnAction(event -> {
                if (selectedSeatIds.contains(seat.getId())) {
                    selectedSeatIds.remove(seat.getId());
                    button.getStyleClass().remove("selected-button");
                } else {
                    selectedSeatIds.add(seat.getId());
                    button.getStyleClass().add("selected-button");
                }
            });

            gridPane.add(button, seat.getNumber() - 1, seat.getRow() - 1);
        }
    }
    private void showErrorMessage(String mainMessage, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Error");
        alert.setHeaderText(mainMessage);
        alert.setContentText(message);
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();

    }

    public void handleAdd(ActionEvent actionEvent) {
        if (lodgeField.getText().isEmpty() || rowField.getText().isEmpty() || priceField.getText().isEmpty()){
            showErrorMessage("Error!!", "Please fill all the fields.");
            return;
        }
        boolean isValidLodge = false;
        for (String lodge : lodgeChoiceBox.getItems()) {
            if (lodge.equals(lodgeField.getText())) {
                isValidLodge = true;
                break;
            }
        }

        if (!isValidLodge) {
            showErrorMessage("Error!!", "Lodge name is invalid.");
            return;
        }
        int lodgeId = service.getLodgeId(lodgeField.getText());
        int row = Integer.parseInt(rowField.getText());
        float price = Float.parseFloat(priceField.getText());
        String taken = "false";
        int number = service.getMaxSeatNumberFromLodge(lodgeId, row) + 1;
        service.addSeat(number, lodgeId, price, taken, row);
        loadSeats(lodgeChoiceBox.getValue());
    }

    public void handleUpdate(ActionEvent actionEvent) {
        if (updatePriceField.getText().isEmpty()){
            showErrorMessage("Error!!", "Please fill all the fields.");
            return;
        }
        float price = Float.parseFloat(updatePriceField.getText());
        for (Integer seatId : selectedSeatIds) {
            service.updateSeatPrice(seatId, price);
        }
        loadSeats(lodgeChoiceBox.getValue());
    }

    public void handleDelete(ActionEvent actionEvent) {
        if(selectedSeatIds.isEmpty()){
            showErrorMessage("Error!!", "Please select a seat to delete.");
            return;
        }
        for (Integer seatId : selectedSeatIds) {
            service.deleteSeat(seatId);
        }
        loadSeats(lodgeChoiceBox.getValue());
    }

    public void handleBack(ActionEvent actionEvent) {
        stage.close();
    }
}
