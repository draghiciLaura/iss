package org.example.issproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.issproject.business.Service;
import org.example.issproject.domain.Seat;
import org.example.issproject.repo.LodgeRepo;
import org.example.issproject.repo.SeatsRepo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HelloController implements Observer{
    @FXML
    private Label welcomeText;

    @FXML
    private Label seatPriceLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private ChoiceBox<String> lodgeChoiceBox;

    @FXML
    private GridPane gridPane;

    private Set<Integer> selectedSeatIds;
    private Service service;

    @Override
    public void update() {
        loadSeats(lodgeChoiceBox.getValue());
    }

    @FXML
    protected void initialize() {
        String url = "jdbc:sqlite:identifier.sqlite";
        String username = "";
        String password = "";
        LodgeRepo lodgeRepo = new LodgeRepo(url, username, password);
        SeatsRepo seatsRepo = new SeatsRepo(url, username, password);
        service = new Service(lodgeRepo, seatsRepo);
        service.addObserver(this);
        selectedSeatIds = new HashSet<>();

        // Populate the ChoiceBox with lodge names
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

            if (seat.isReserved()) {
                button.getStyleClass().add("taken-button");
                button.setDisable(true);
            } else {
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
                    updateTotalPrice();
                });
            }

            gridPane.add(button, seat.getNumber()-1, seat.getRow()-1);
            gridPane.setAlignment(Pos.CENTER);
            for (javafx.scene.Node node : gridPane.getChildren()) {
                GridPane.setHalignment(node, HPos.CENTER);
                GridPane.setValignment(node, VPos.CENTER);
            }
        }
    }

    private void updateTotalPrice() {
        List<Integer> seatIdsList = new ArrayList<>(selectedSeatIds);
        float totalPrice = service.getSeatPricesTotal(seatIdsList);
        totalPriceLabel.setText("Total Price: $" + totalPrice);
    }

    public void handleAdmin(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-view.fxml"));
            AnchorPane root = fxmlLoader.load();
            Stage adminStage = new Stage();
            adminStage.setScene(new Scene(root));
            AdminController adminController = fxmlLoader.getController();
            adminController.setStage(adminStage);
            adminController.setService(service);
            adminStage.initModality(Modality.APPLICATION_MODAL);
            adminStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleContinue(ActionEvent actionEvent) {
        if (selectedSeatIds.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Error");
            alert.setHeaderText("No Seats Selected");
            alert.setContentText("Please select at least one seat to continue.");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();

            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("payment-view.fxml"));
            AnchorPane root = fxmlLoader.load();
            Stage paymentStage = new Stage();
            paymentStage.setScene(new Scene(root));
            PaymentController paymentController = fxmlLoader.getController();
            paymentController.setStage(paymentStage);
            paymentController.setService(service);
            paymentController.setSelectedSeatIds(selectedSeatIds);
            paymentStage.initModality(Modality.APPLICATION_MODAL);
            paymentStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
