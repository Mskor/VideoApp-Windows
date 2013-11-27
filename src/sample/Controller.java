package sample;

import java.io.IOException;
import java.net.*;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javax.swing.*;

/**
 * Handles the JavaFX GUI events.
 */

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    static private TextArea LogText;

    @FXML
    static private TextField DirField;

    @FXML
    private AnchorPane MainFrame;

    @FXML
    static private TextArea OutText;

    @FXML
    private Button QuitButton;

    @FXML
    private ToggleButton RunButton;

    /**
     * Handles the left-click on the "Quit" button.
     *
     * Initiates program exit process.
     * @param event Object that contains event type info
     *              e.g. is it a left-click or right-click
     */
    @FXML
    void ExitProgram(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            SingletonLogHandle.GetInstance().EndSession();
            System.exit(0);
        }
    }

    /**
     * Handles the left-click on the "Run" button.
     *
     * Launches the sever accept listening cycle.
     * For every accepted client creates an instance of {@link ClientThread}.
     * @param event Object that contains event type info
     *              e.g. is it a left-click or right-click
     */
    @FXML
    void RunServer(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            ServerThread serverThread = new ServerThread();
        }
    }

    /**
     * Opens current accumulator folder using explorer.exe
     * @param event Object that contains event type info
     *              e.g. is it a left-click or right-click
     */
    @FXML
    void OpenVideoFolder(MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY){
            if (DirField.getText().isEmpty()){
                OutText.appendText("Please, specify folder to write videos\n");
            }else{
                try {
                    Runtime.getRuntime().exec("explorer.exe /root," + DirField.getText());
                }catch(IOException ioe){
                    OutText.appendText("Error launching explorer.exe\n");
                }
            }
        }
    }

    /**
     * Opens last video that has been downloaded is this exact session, using wmplayer.exe.
     */
    @FXML
    void OpenLastFile(){
        if(SingletonLogHandle.GetInstance().getLastVideoDownloaded() == null){
            OutText.appendText("No video downloaded yet\n");
        }else{
            if(!SingletonLogHandle.GetInstance().getLastVideoDownloaded().exists()){
                OutText.appendText("It seems that video doesn't exist already");
            }else{
                try{
                    Runtime.getRuntime().exec("C:\\Program Files\\Windows Media Player\\wmplayer.exe " +
                            SingletonLogHandle.GetInstance().getLastVideoDownloaded());
                }catch (IOException ioe){
                    Controller.Print("Error opening video");
                }

            }
        }
    }

    /**
     * Handles the file chooser button that is next to the {@link Controller#DirField}
     *
     * Opens {@link JFileChooser} to choose the accumulator folder for videos.
     *
     * @param event Object that contains event type info
     *              e.g. is it a left-click or right-click
     */
    @FXML
    void GetFolder (MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY){
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                DirField.setText(chooser.getSelectedFile().getPath());
            }
        }
    }

    @FXML
    void initialize() {
        assert MainFrame != null : "fx:id=\"MainFrame\" was not injected: check your FXML file 'sample.fxml'.";
        assert OutText != null : "fx:id=\"OutText\" was not injected: check your FXML file 'sample.fxml'.";
        assert QuitButton != null : "fx:id=\"QuitButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert RunButton != null : "fx:id=\"RunButton\" was not injected: check your FXML file 'sample.fxml'.";
    }


    /**
     * Designed for external usage
     * @return A {@link String} from {@link Controller#DirField} field
     */
    static String GetDir(){
        if(DirField.getText().isEmpty()) return "C:\\";
        return DirField.getText();
    }

    /**
     * Designed for external usage
     * @param foo A {@link String} to print out in {@link Controller#OutText}
     */
    static void LogPrint(String foo){
        LogText.appendText(foo);
    }

    /**
     * Designed for external usage
     * @param foo A {@link String} to print out in {@link Controller#OutText}
     */
    static void Print(String foo){
        OutText.appendText(foo);
    }
}
