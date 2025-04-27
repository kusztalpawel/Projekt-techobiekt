package projekt;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FXMLController {
    List<FileHandler> filesList = new ArrayList<>();
    @FXML
    private MenuItem quitItem;

    @FXML
    void getFile() throws IOException {

        UserInput userInput = new UserInput(filesList);

        userInput.getInput();
    }

    @FXML
    void quit() {
        Stage stage = (Stage) quitItem.getParentPopup().getOwnerWindow();
        stage.close();
    }
}
