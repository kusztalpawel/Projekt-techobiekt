package projekt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<FileHandler> filesList = new ArrayList<>();
        UserInput userInput = new UserInput(filesList);

        userInput.getInput();
    }
}