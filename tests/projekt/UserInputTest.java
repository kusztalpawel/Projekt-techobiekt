package projekt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.*;

class UserInputTest {

    private List<XmlFile> filesList;

    @BeforeEach
    void setUp() {
        filesList = new ArrayList<>();
        File file1 = new File("file1.txt");
        XmlFile xmlFile = new XmlFile(file1);
        filesList.add(xmlFile);
    }

    @Test
    void testShowFilesList() {
        //create test
        assertEquals("file1.txt", filesList.getFirst().getFile().getName());
    }
}