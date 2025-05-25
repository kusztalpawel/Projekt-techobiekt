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
    private final List<XmlFile> xmlFilesList = new ArrayList<>();
    private final List<XsdFile> xsdFilesList = new ArrayList<>();
    private final UserInput userInput = new UserInput(xmlFilesList, xsdFilesList);
    private int fileIndex;

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

    private String prepareFileContentToShow(String fileContent){
        StringBuilder result = new StringBuilder();
        boolean insideBrackets = false;

        for (int i = 0; i < fileContent.length(); i++) {
            char c = fileContent.charAt(i);

            if (c == '<') {
                insideBrackets = true;
                result.append(c);
            } else if (c == '>') {
                insideBrackets = false;
                result.append(c);
            } else if (c == ' ' && !insideBrackets) {
                result.append('\n');
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private boolean fileChoosing() {
        List<String> files = new ArrayList<>();
        if(!xmlFilesList.isEmpty()){
            for(XmlFile file : xmlFilesList){
                files.add(file.getFile().getName());
            }
            ChoiceDialog<String> dialog = new ChoiceDialog<>(files.getFirst(), files);
            dialog.setTitle("Choose File");
            dialog.setContentText("Files:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(file -> fileIndex = files.indexOf(file));

            return true;
        } else {
            System.out.println("No files!");

            return false;
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
        if(fileChoosing()){
            textArea.setText(prepareFileContentToShow(xmlFilesList.get(fileIndex).getFileContent()));
        }
    }

    @FXML
    void generateSchema() {
        if(fileChoosing()){
            userInput.generateSchema(fileIndex);
        }
    }

    @FXML
    void validateFile(ActionEvent event) {
        if(fileChoosing()){
            System.out.println(Validation.validateXml(xmlFilesList.get(fileIndex).getRootElement(), xsdFilesList.getLast().getRootElement()));
        }
    }

    @FXML
    void quit() {
        Stage stage = (Stage) quitItem.getParentPopup().getOwnerWindow();
        stage.close();
    }
}
