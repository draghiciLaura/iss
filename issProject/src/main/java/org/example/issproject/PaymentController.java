package org.example.issproject;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.issproject.business.Service;

import java.util.Set;

public class PaymentController {
    public TextField expMonthField;
    public TextField expYearField;
    public TextField cardNameField;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField cardNumberField;
    public TextField cvvField;
    private Set<Integer> selectedSeatIds;
    private Service service;
    private Stage stage;
    public void setService(Service service) {
        this.service = service;
    }
    public void setSelectedSeatIds(Set<Integer> selectedSeatIds) {
        this.selectedSeatIds = selectedSeatIds;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
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
    public void handleContinue(ActionEvent actionEvent) {
        if(expMonthField.getText().isEmpty() || expYearField.getText().isEmpty() || cardNameField.getText().isEmpty() || firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || cardNumberField.getText().isEmpty() || cvvField.getText().isEmpty()){
            showErrorMessage("Error!!", "Please fill all the fields.");
            return;
        }
        for (Integer seatId : selectedSeatIds) {
            service.updateSeatTaken(seatId);
        }
        stage.close();
    }
}
