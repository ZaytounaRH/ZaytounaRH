package tn.esprit.controllers;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
public class DatePickerDialog  extends Dialog<LocalDate>{
    private DatePicker datePicker;

    public DatePickerDialog(LocalDate date) {
        setTitle("Sélectionner une date");
        setHeaderText("Choisissez une date");

        datePicker = new DatePicker(date);
        datePicker.setPromptText("Sélectionnez une date");

        VBox vbox = new VBox(datePicker);
        getDialogPane().setContent(vbox);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return datePicker.getValue();
            }
            return null;
        });
    }
}
