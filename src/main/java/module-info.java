module tn.esprit {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.base;

  requires net.synedra.validatorfx;
  requires java.sql;
  requires java.desktop;

  opens tn.esprit to javafx.fxml;
  exports tn.esprit;
  exports tn.esprit.getionfinanciere.controllers to javafx.fxml;
  opens tn.esprit.getionfinanciere.controllers to javafx.fxml;
  opens tn.esprit.getionfinanciere.models to javafx.base;
  exports tn.esprit.getionfinanciere.models;

}