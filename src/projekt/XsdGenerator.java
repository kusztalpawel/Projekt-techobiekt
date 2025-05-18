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
        String typeString = "\" type=\"";
        String endingTagString = "\"/>\n";

        indent.append(indentUnit);

        if ((xmlElement.getAttributes().isEmpty()) && (xmlElement.getChildren().isEmpty())) {
            writer.write(indent + "<xs:element name=\"" + xmlElement.getTag() + typeString + XmlTypes.detectType(xmlElement.getContent()) + endingTagString);
            indent.delete(indent.length() - 2, indent.length());
            return;
        }

        writer.write(indent + "<xs:element name=\"" + xmlElement.getTag() + "\">\n");
        indent.append(indentUnit);
        writer.write(indent + "<xs:complexType>\n");

        if (!xmlElement.getChildren().isEmpty()) {
            indent.append(indentUnit);
            writer.write(indent + "<xs:sequence>\n");
            for (XmlElement child : xmlElement.getChildren()) {
                xsdElementGenerator(child, writer, indent);
            }
            writer.write(indent + "</xs:sequence>\n");
        } else {
            indent.append(indentUnit);
            writer.write(indent + "<xs:simpleContent>\n");
            indent.append(indentUnit);
            writer.write(indent + "<xs:extension base=\"" + XmlTypes.detectType(xmlElement.getContent()) + "\">\n");
            if (!xmlElement.getAttributes().isEmpty()){
                indent.append(indentUnit);
                for (Map.Entry<String, String> attribute : xmlElement.getAttributes().entrySet()){
                    writer.write(indent + "<xs:attribute name=\"" + attribute.getKey() + typeString + XmlTypes.detectType(attribute.getValue()) + "\"" + " use=\"required" + endingTagString);
                }
            }
            indent.delete(indent.length() - 2, indent.length());
            writer.write(indent + "</xs:extension>\n");
            indent.delete(indent.length() - 2, indent.length());
            writer.write(indent + "</xs:simpleContent>\n");
        }

        if (!xmlElement.getChildren().isEmpty() && !xmlElement.getAttributes().isEmpty()) {
            for (Map.Entry<String, String> attribute : xmlElement.getAttributes().entrySet()){
                writer.write(indent + "<xs:attribute name=\"" + attribute.getKey() + typeString + XmlTypes.detectType(attribute.getValue()) + "\"" + " use=\"required" + endingTagString);
            }
        }
        indent.delete(indent.length() - 2, indent.length());
        writer.write(indent + "</xs:complexType>\n");
        indent.delete(indent.length() - 2, indent.length());
        writer.write(indent + "</xs:element>\n");
    }
}

