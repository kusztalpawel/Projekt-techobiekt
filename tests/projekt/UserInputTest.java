package projekt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.*;

class UserInputTest {

    private List<FileHandler> filesList;
    private UserInput userInput;
    private FileHandler fileHandler;

    @BeforeEach
    void setUp() {
        filesList = new ArrayList<>();
        fileHandler = new FileHandler();
        userInput = new UserInput(filesList);
    }

    @Test
    void testShowFilesList() throws IOException {
        File file1 = new File("file1.txt");
        fileHandler.setFile(file1);
        filesList.add(fileHandler);

        File file2 = new File("file2.txt");
        FileHandler fileHandler2 = new FileHandler();
        fileHandler2.setFile(file2);
        filesList.add(fileHandler2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String simulatedInput = "2\n5\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        userInput.getInput();
        String output = outputStream.toString();

        assertTrue(output.contains("1. file1.txt"));
        assertTrue(output.contains("2. file2.txt"));
    }

    @Test
    void testInvalidChoice() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String simulatedInput = "costam\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        userInput.getInput();

        String output = outputStream.toString();
        assertTrue(output.contains("Podano niepoprwane dane!"));
    }
}