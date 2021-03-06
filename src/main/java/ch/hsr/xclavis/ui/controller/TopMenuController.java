/*
 * Copyright (c) 2015, Gian Poltéra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1.	Redistributions of source code must retain the above copyright notice,
 *   	this list of conditions and the following disclaimer.
 * 2.	Redistributions in binary form must reproduce the above copyright 
 *   	notice, this list of conditions and the following disclaimer in the 
 *   	documentation and/or other materials provided with the distribution.
 * 3.	Neither the name of HSR University of Applied Sciences Rapperswil nor 
 * 	the names of its contributors may be used to endorse or promote products
 * 	derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.ui.MainApp;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * Shows the top menu.
 *
 * @author Gian Poltéra
 */
public class TopMenuController implements Initializable {

    private MainApp mainApp;
    private ResourceBundle rb;

    @FXML
    private VBox topMenu;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem miOpen;
    @FXML
    private HBox toolBar;
    @FXML
    private Button fileListBtn;
    @FXML
    private Button qrScannerBtn;
    @FXML
    private Button keyManagementBtn;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Marks in the menu the FileSelecter button.
     */
    public void markFileSelecter() {
        fileListBtn.setStyle("-fx-base: #b6e7c9;");
        qrScannerBtn.setStyle("-fx-base: #eaeaea;");
        keyManagementBtn.setStyle("-fx-base: #eaeaea;");
    }

    /**
     * Marks in the menu the CodeReader button.
     */
    public void markCodeReader() {
        fileListBtn.setStyle("-fx-base: #eaeaea;");
        qrScannerBtn.setStyle("-fx-base: #b6e7c9;");
        keyManagementBtn.setStyle("-fx-base: #eaeaea;");
    }

    /**
     * Marks in the menu the KeyManagement button.
     */
    public void markKeyManagement() {
        fileListBtn.setStyle("-fx-base: #eaeaea;");
        qrScannerBtn.setStyle("-fx-base: #eaeaea;");
        keyManagementBtn.setStyle("-fx-base: #b6e7c9;");
    }

    @FXML
    private void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(rb.getString("select_file"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());
        if (selectedFiles != null) {
            selectedFiles.stream().forEach((selectedFile) -> {
                mainApp.showFileSelecter();
                mainApp.getFiles().add(selectedFile);
            });
        }
    }

    @FXML
    private void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void changeLanguageToEnglish(ActionEvent event) {
        mainApp.getProperties().set("language", "en");
        mainApp.changeLanguage(Locale.ENGLISH);
    }

    @FXML
    private void changeLanguageToGerman(ActionEvent event) {
        mainApp.getProperties().set("language", "de");
        mainApp.changeLanguage(Locale.GERMAN);
    }

    @FXML
    private void showKeyLengthSettings(ActionEvent event) {
        List<String> choices = new ArrayList<>();
        choices.add("128");
        choices.add("256");

        ChoiceDialog<String> dialog = new ChoiceDialog<>(mainApp.getProperties().getString("key_size"), choices);
        dialog.setTitle(rb.getString("window_title"));
        dialog.setHeaderText(rb.getString("encryption_key_length"));
        dialog.setContentText(rb.getString("bit_length") + ":");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(choice -> mainApp.getProperties().set("key_size", choice));
    }

    @FXML
    private void showFileList(ActionEvent event) {
        mainApp.showFileSelecter();
    }

    @FXML
    private void showQRScanner(ActionEvent event) {
        mainApp.showCodeReader();
    }

    @FXML
    private void showKeyList(ActionEvent event) {
        mainApp.showKeyManagement();
    }

    @FXML
    private void showKeyStorePasswordSettings(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog(rb.getString("password"));
        dialog.setTitle(rb.getString("window_title"));
        dialog.setHeaderText(rb.getString("password_protection"));
        dialog.setContentText(rb.getString("password") + ":");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(choice -> mainApp.getKeys().updatePassword(choice));
    }

    @FXML
    private void showOutputPathSettings(ActionEvent event) {
        //TBA Check if permissions for write in this folder!!
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(rb.getString("select_folder"));
        if (mainApp.getProperties().getString("output_path").equals("default")) {
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        } else {
            directoryChooser.setInitialDirectory(new File(mainApp.getProperties().getString("output_path")));
        }
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            mainApp.getProperties().set("output_path", selectedDirectory.getAbsolutePath());
            mainApp.showFileSelecter();
        }
    }

    @FXML
    private void showAbout(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(rb.getString("window_title"));
        alert.setHeaderText(rb.getString("about"));
        alert.setContentText(rb.getString("about_text"));

        alert.showAndWait();
    }

    @FXML
    private void showExtendedSecuritySettings(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("window_title"));
        alert.setHeaderText(rb.getString("extended_security"));
        alert.setContentText(rb.getString("activating_extended_security"));

        ButtonType buttonTypeSwitcher;
        if (!mainApp.getProperties().getBoolean("extended_security")) {
            buttonTypeSwitcher = new ButtonType(rb.getString("activate"), ButtonData.OK_DONE);
        } else {
            buttonTypeSwitcher = new ButtonType(rb.getString("deactivate"), ButtonData.OK_DONE);
        }
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeSwitcher, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeSwitcher) {
            if (!mainApp.getProperties().getBoolean("extended_security")) {
                mainApp.getProperties().set("extended_security", true);
            } else {
                mainApp.getProperties().set("extended_security", false);
            }
        }
    }
}
