package projekt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class XmlFileTest {
    private XmlFile xmlFile;

    @BeforeEach
    void setUp() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();
        xmlFile = new XmlFile(tempFile);
        String content = "<Element rodzaj=\"miasto\">";
        Files.write(tempFile.toPath(), content.getBytes());

        xmlFile.setFileContent(FileHandler.setFileContent(tempFile));
    }

    @Test
    void testSingleAttribute() {
        Map<String, String> attributes = new HashMap<>();
        String input = "rodzaj=\"miasto\"";

        xmlFile.insertAttributes(attributes, input);

        assertEquals(1, attributes.size());
        assertEquals("miasto", attributes.get("rodzaj"));
    }

    @Test
    void testMultipleAttributes() {
        Map<String, String> attributes = new HashMap<>();
        String input = "rodzaj=\" miasto\" wielkosc =\"duze\" poczta=\"25-315\"";

        xmlFile.insertAttributes(attributes, input);

        assertEquals(3, attributes.size());
        assertEquals(" miasto", attributes.get("rodzaj"));
        assertEquals("duze", attributes.get("wielkosc"));
        assertEquals("25-315", attributes.get("poczta"));
    }

    @Test
    void testExtraWhitespace() {
        Map<String, String> attributes = new HashMap<>();
        String input = "   rodzaj =  \"miasto\"   wielkosc   =\"duze\"   ";

        xmlFile.insertAttributes(attributes, input);

        assertEquals(2, attributes.size());
        assertEquals("miasto", attributes.get("rodzaj"));
        assertEquals("duze", attributes.get("wielkosc"));
    }

    @Test
    void testEmptyString() {
        Map<String, String> attributes = new HashMap<>();
        String input = "";

        xmlFile.insertAttributes(attributes, input);

        assertTrue(attributes.isEmpty());
    }

    @Test
    void testInsertRootElement() {
        List<XmlElement> stack = new ArrayList<>();

        xmlFile.insertElement(stack, true, 0);

        XmlElement root = xmlFile.getRootElement();
        assertNotNull(root);
        assertEquals("Element", root.getTag());
        assertEquals("miasto", root.getAttributes().get("rodzaj"));
        assertTrue(stack.contains(root));
    }

    @Test
    void testInsertSelfClosingChild() {
        List<XmlElement> stack = new ArrayList<>();
        XmlElement parent = new XmlElement("root", 0);
        stack.add(parent);

        xmlFile.insertElement(stack, false, 0);

        List<XmlElement> children = parent.getChildren();
        assertEquals(1, children.size());
        XmlElement child = children.getFirst();
        assertEquals("Element", child.getTag());
        assertEquals("miasto", child.getAttributes().get("rodzaj"));
        assertTrue(stack.contains(child));
    }
}