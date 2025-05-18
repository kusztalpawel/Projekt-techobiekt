package projekt;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.*;

class XsdGeneratorTest {

    private XsdGenerator xsdGenerator;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        xsdGenerator = new XsdGenerator();
        outputStream = new ByteArrayOutputStream();
    }

    private String getOutput() {
        return outputStream.toString();
    }

    @Test
    void testEmptyElement() throws IOException {
        XmlElement xmlElement = new XmlElement("testElement", 0);

        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        xsdGenerator.createXsd(xmlElement, writer);
        writer.flush();

        String expectedOutput = """
                <?xml version="1.0" encoding="utf-8"?>
                <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">
                  <xs:xmlElement name="testElement" type="xs:string"/>
                </xs:schema>
                """;

        assertEquals(expectedOutput, getOutput());
    }

    @Test
    void testWithChildren() throws IOException {
        XmlElement child = new XmlElement("childElement", 1);
        List<XmlElement> children = List.of(child);
        XmlElement xmlElement = new XmlElement("parentElement", 0);

        xmlElement.setChildren(children);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        xsdGenerator.createXsd(xmlElement, writer);
        writer.flush();

        String expectedOutput = """
                <?xml version="1.0" encoding="utf-8"?>
                <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">
                  <xs:xmlElement name="parentElement">
                    <xs:complexType>
                      <xs:sequence>
                        <xs:xmlElement name="childElement" type="xs:string"/>
                      </xs:sequence>
                    </xs:complexType>
                  </xs:xmlElement>
                </xs:schema>
                """;

        assertEquals(expectedOutput, getOutput());
    }

    @Test
    void testWithAttributes() throws IOException {
        XmlElement xmlElement = new XmlElement("testElement", 0);
        xmlElement.setAttributes(new HashMap<>());
        xmlElement.addAttribute("attr1", "value1");

        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        xsdGenerator.createXsd(xmlElement, writer);
        writer.flush();

        String expectedOutput = """
                <?xml version="1.0" encoding="utf-8"?>
                <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">
                  <xs:xmlElement name="testElement">
                    <xs:complexType>
                      <xs:simpleContent>
                        <xs:extension base="xs:string">
                          <xs:attribute name="attr1" type="xs:string" use="required"/>
                        </xs:extension>
                      </xs:simpleContent>
                    </xs:complexType>
                  </xs:xmlElement>
                </xs:schema>
                """;

        assertEquals(expectedOutput, getOutput());
    }
}