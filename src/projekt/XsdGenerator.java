package projekt;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class XsdGenerator {
    String indentUnit = "  ";

    public void createXsd(XmlElement xmlElement, Writer writer) throws IOException {
        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        writer.write("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n");
        xsdElementGenerator(xmlElement, writer, new StringBuilder());
        writer.write("</xs:schema>\n");
    }

    private void xsdElementGenerator(XmlElement xmlElement, Writer writer, StringBuilder indent) throws IOException {
        indent.append(indentUnit);

        if (xmlElement.getAttributes().isEmpty() && xmlElement.getChildren().isEmpty()) {
            writeSimpleElement(xmlElement, writer, indent);
            removeIndent(indent);
            return;
        }

        writer.write(indent + "<xs:element name=\"" + xmlElement.getTag() + "\">\n");
        indent.append(indentUnit);
        writer.write(indent + "<xs:complexType>\n");

        if (!xmlElement.getChildren().isEmpty()) {
            writeChildren(xmlElement, writer, indent);
        } else {
            writeSimpleContentWithAttributes(xmlElement, writer, indent);
        }

        if (!xmlElement.getChildren().isEmpty() && !xmlElement.getAttributes().isEmpty()) {
            writeAttributes(xmlElement, writer, indent);
        }

        removeIndent(indent);
        writer.write(indent + "</xs:complexType>\n");
        removeIndent(indent);
        writer.write(indent + "</xs:element>\n");
    }

    private void writeSimpleElement(XmlElement element, Writer writer, StringBuilder indent) throws IOException {
        String type = XmlTypes.detectType(element.getContent());
        writer.write(indent + "<xs:element name=\"" + element.getTag() + "\" type=\"" + type + "\"/>\n");
    }

    private void writeChildren(XmlElement parent, Writer writer, StringBuilder indent) throws IOException {
        indent.append(indentUnit);
        writer.write(indent + "<xs:sequence>\n");
        indent.append(indentUnit);
        for (XmlElement child : parent.getChildren()) {
            xsdElementGenerator(child, writer, indent);
        }
        removeIndent(indent);
        writer.write(indent + "</xs:sequence>\n");
        removeIndent(indent);
    }

    private void writeSimpleContentWithAttributes(XmlElement element, Writer writer, StringBuilder indent) throws IOException {
        indent.append(indentUnit);
        writer.write(indent + "<xs:simpleContent>\n");

        indent.append(indentUnit);
        writer.write(indent + "<xs:extension base=\"" + XmlTypes.detectType(element.getContent()) + "\">\n");

        if (!element.getAttributes().isEmpty()) {
            writeAttributes(element, writer, indent);
        }

        removeIndent(indent);
        writer.write(indent + "</xs:extension>\n");
        removeIndent(indent);
        writer.write(indent + "</xs:simpleContent>\n");
    }

    private void writeAttributes(XmlElement element, Writer writer, StringBuilder indent) throws IOException {
        indent.append(indentUnit);
        for (Map.Entry<String, String> attr : element.getAttributes().entrySet()) {
            String type = XmlTypes.detectType(attr.getValue());
            writer.write(indent + "<xs:attribute name=\"" + attr.getKey() + "\" type=\"" + type + "\" use=\"required\"/>\n");
        }
        removeIndent(indent);
    }

    private void removeIndent(StringBuilder indent) {
        indent.delete(indent.length() - indentUnit.length(), indent.length());
    }
}

