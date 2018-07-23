import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class codeGen_Controller implements Initializable{
    @FXML
    private Button btn_reloadtxt;
    @FXML
    private Button btn_generate;
    @FXML
    private Label lbl_output;
    @FXML
    private ChoiceBox<String> cb_passwordLen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        .setItems(FXCollections.observableArrayList(
        cb_passwordLen.getItems().addAll("SHORT", "MEDIUM","LONG", "SUPER", "CRAZY");
        cb_passwordLen.setValue("LONG");
    }

    /**
     * Handles all button presses on the page
     * @param e
     */
    @FXML
    public void buttonAction(ActionEvent e){
        // Hitting the reloadtxt button
        if(e.getSource() == btn_reloadtxt){
            NameBase.getInstance().ReloadDB();
        }
        // Hitting the generate button
        else if(e.getSource() == btn_generate){
            String passwordLen = cb_passwordLen.getValue();
            if(passwordLen.equals("SHORT")){
                Generator.getInstance().setPasswordLen(0);
            }
            else if(passwordLen.equals("MEDIUM")){
                Generator.getInstance().setPasswordLen(1);
            }
            else if(passwordLen.equals("LONG")){
                Generator.getInstance().setPasswordLen(2);
            }
            else if(passwordLen.equals("SUPER")){
                Generator.getInstance().setPasswordLen(3);
            }
            else {
                Generator.getInstance().setPasswordLen(4);
            }
            lbl_output.setText(Generator.getInstance().generateCode());
        }
    }
}
