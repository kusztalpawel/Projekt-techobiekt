package projekt;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class XsdGenerator {
    String indentUnit = "  ";

    public void createXsd(Element element, Writer writer) throws IOException {
        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        writer.write("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n");
        xsdElementGenerator(element, writer, new StringBuilder());
        writer.write("</xs:schema>\n");
    }

    private void xsdElementGenerator(Element element, Writer writer, StringBuilder indent) throws IOException {
        String typeString = "\" type=\"";
        String endingTagString = "\"/>\n";

        indent.append(indentUnit);

        if ((element.getAttributes().isEmpty()) && (element.getChildren().isEmpty())) {
            writer.write(indent + "<xs:element name=\"" + element.getTag() + typeString + XmlTypes.detectType(element.getContent()) + endingTagString);
            indent.delete(indent.length() - 2, indent.length());
            return;
        }

        writer.write(indent + "<xs:element name=\"" + element.getTag() + "\">\n");
        indent.append(indentUnit);
        writer.write(indent + "<xs:complexType>\n");

        if (!element.getChildren().isEmpty()) {
            indent.append(indentUnit);
            writer.write(indent + "<xs:sequence>\n");
            for (Element child : element.getChildren()) {
                xsdElementGenerator(child, writer, indent);
            }
            writer.write(indent + "</xs:sequence>\n");
        } else {
            indent.append(indentUnit);
            writer.write(indent + "<xs:simpleContent>\n");
            indent.append(indentUnit);
            writer.write(indent + "<xs:extension base=\"" + XmlTypes.detectType(element.getContent()) + "\">\n");
            if (!element.getAttributes().isEmpty()){
                indent.append(indentUnit);
                for (Map.Entry<String, String> attribute : element.getAttributes().entrySet()){
                    writer.write(indent + "<xs:attribute name=\"" + attribute.getKey() + typeString + XmlTypes.detectType(attribute.getValue()) + "\"" + " use=\"required" + endingTagString);
                }
            }
            indent.delete(indent.length() - 2, indent.length());
            writer.write(indent + "</xs:extension>\n");
            indent.delete(indent.length() - 2, indent.length());
            writer.write(indent + "</xs:simpleContent>\n");
        }

        if (!element.getChildren().isEmpty() && !element.getAttributes().isEmpty()) {
            for (Map.Entry<String, String> attribute : element.getAttributes().entrySet()){
                writer.write(indent + "<xs:attribute name=\"" + attribute.getKey() + typeString + XmlTypes.detectType(attribute.getValue()) + "\"" + " use=\"required" + endingTagString);
            }
        }
        indent.delete(indent.length() - 2, indent.length());
        writer.write(indent + "</xs:complexType>\n");
        indent.delete(indent.length() - 2, indent.length());
        writer.write(indent + "</xs:element>\n");
    }
}

