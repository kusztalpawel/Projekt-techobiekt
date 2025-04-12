package projekt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {

    private FileHandler handler;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        handler = new FileHandler();
        tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();
    }

    @Test
    void testSetFileContentReadsNonEmptyLines() throws Exception {
        String content = """
                <?xml version="1.0" encoding="UTF-8"?>
                	<Adres>
                		<Miejscowosc rodzaj="miasto">Kielce</Miejscowosc>
                		<Ulica>Kasztanowa</Ulica>
                		<Budynek>44</Budynek>
                		<Mieszkanie>12</Mieszkanie>
                		<Poczta numer="26-200"/>
                	</Adres>
                """;
        Files.write(tempFile.toPath(), content.getBytes());

        handler.setFile(tempFile);
        handler.setFileContent();

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?> <Adres> <Miejscowosc rodzaj=\"miasto\">Kielce</Miejscowosc> <Ulica>Kasztanowa</Ulica> <Budynek>44</Budynek> <Mieszkanie>12</Mieszkanie> <Poczta numer=\"26-200\"/> </Adres> ", handler.getFileContent());
    }

    @Test
    void testSetFileContentWithNullFile() throws Exception {
        handler.setFileContent();

        assertNull(handler.getFileContent());
    }

}