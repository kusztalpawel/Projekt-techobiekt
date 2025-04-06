package projekt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<FileHandler> filesList = new ArrayList<>();    //C:/Users/pawel/Desktop/psk/TO/Projekt/dummy.xml
        UserInput userInput = new UserInput(filesList);     //C:\Users\pawel\Desktop\psk\TO\Projekt\dummy.xml

        userInput.getInput();
    }
}