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
    private FileHandler fileHandler2;

    @BeforeEach
    void setUp() {
        filesList = new ArrayList<>();
        File file1 = new File("file1.txt");
        fileHandler = new FileHandler(file1);
        filesList.add(fileHandler);
        File file2 = new File("file2.txt");
        fileHandler2 = new FileHandler(file2);
        userInput = new UserInput(filesList);
    }

    @Test
    void testShowFilesList() {
        //create test
        assertEquals("file1.txt", filesList.getFirst().getFileName());
    }
}