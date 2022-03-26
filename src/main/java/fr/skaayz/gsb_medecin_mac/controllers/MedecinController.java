package fr.skaayz.gsb_medecin_mac.controllers;

import fr.skaayz.gsb_medecin_mac.MainController;
import fr.skaayz.gsb_medecin_mac.models.Medecin;
import fr.skaayz.gsb_medecin_mac.models.MedecinAccess;
import fr.skaayz.gsb_medecin_mac.models.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MedecinController implements Initializable {
    @FXML
    public TableView<Medecin> tableView;

    @FXML
    public TableColumn<Medecin, Integer> id, departement_id;

    @FXML
    public TableColumn<Medecin, String> nom, prenom, adresse, tel, specialite;

    @FXML
    public TableColumn<Medecin, String> action;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        tel.setCellValueFactory(new PropertyValueFactory<>("tel"));

        if (!Utilisateur.isConnected()) {
            adresse.setVisible(false);
            tel.setVisible(false);
            //action.setVisible(false);
        }

        specialite.setCellValueFactory(new PropertyValueFactory<>("specialite"));
        departement_id.setCellValueFactory(new PropertyValueFactory<>("departement_id"));

        // Create buttons for every rows
        Callback<TableColumn<Medecin, String>, TableCell<Medecin, String>> cellFactory = (param) -> new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    final Button soft_view_button = new Button("Voir");
                    soft_view_button.getStyleClass().add("button-blue");
                    soft_view_button.setCursor(Cursor.HAND);

                    soft_view_button.setOnAction(event -> {
                        Medecin medecin = getTableView().getItems().get(getIndex());

                        // Create new stage
                        Stage stage = new Stage();
                        Pane scene_window = null;
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fr/skaayz/gsb_medecin_mac/views/main-view.fxml"));

                        try {
                            scene_window = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Set Scene
                        assert scene_window != null; // scene_window might be null, so assert
                        Scene scene = new Scene(scene_window);
                        stage.setScene(scene);

                        MainController.setupFrame(stage, scene, scene_window);

                        stage.show();
                    });

                    setGraphic(soft_view_button);
                    setText(null);
                }
            }
        };

        // Attribute buttons to the column
        action.setCellFactory(cellFactory);

        tableView.refresh();
        tableView.getItems().addAll(MedecinAccess.getAll());
        tableView.setPlaceholder(new Label("Médecins non chargés."));
    }
}