package projekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FXMLController {
    List<FileHandler> filesList = new ArrayList<>();
    UserInput userInput = new UserInput(filesList);

    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private MenuItem quitItem;
    @FXML
    private TextArea textArea;

    private void fileChoosing(int showOrGenerate) {
        List<String> files = new ArrayList<>();
        if(!filesList.isEmpty()){
            for(FileHandler file : filesList){
                files.add(file.getFileName());
            }
            ChoiceDialog<String> dialog = new ChoiceDialog<>(files.getFirst(), files);
            dialog.setTitle("Choose File");
            dialog.setContentText("Files:");

            Optional<String> result = dialog.showAndWait();
            if(showOrGenerate == 0){
                result.ifPresent(file -> textArea.setText(filesList.get(files.indexOf(file)).getFileContent()));
            } else if(showOrGenerate == 1){
                result.ifPresent(file -> userInput.generateSchema(files.indexOf(file)));
            }
        } else {
            System.out.println("No files!");
        }
    }

    @FXML
    void addFile(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(((Node) event.getTarget()).getScene().getWindow());
        if(file != null){
            userInput.addFile(file);
        }
    }

    @FXML
    void showFilesList() {
        List<String> fileList = userInput.showFilesList();
        textArea.setText("");
        for(String file : fileList){
            textArea.appendText(file + "\n");
        }
    }

    @FXML
    void showFile() {
        fileChoosing(0);
    }

    @FXML
    void generateSchema() {
        fileChoosing(1);
    }

    @FXML
    void quit() {
        Stage stage = (Stage) quitItem.getParentPopup().getOwnerWindow();
        stage.close();
    }
}
