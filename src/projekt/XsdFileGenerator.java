package projekt;

import javafx.util.Pair;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class XsdFileGenerator {
    String indentUnit = "  ";
    String newLineClosing = "\"/>\n";
    String newLineOpening = "\">\n";

    public void createXsd(XsdElement xsdElement, Writer writer) throws IOException {
        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        writer.write("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n");
        xsdElementGenerator(xsdElement, writer, new StringBuilder());
        writer.write("</xs:schema>\n");
    }

    private String occursString(XsdElement element){
        String occ = "";
        StringBuilder stringBuilder = new StringBuilder(occ);

        if(!element.getMaxOccur().isEmpty()){
            stringBuilder.append("maxOccurs=\"").append(element.getMaxOccur()).append("\" ");
        }
        if(element.getMinOccur() != 1){
            stringBuilder.append("minOccurs=\"").append(element.getMinOccur()).append("\" ");
        }

        occ = stringBuilder.toString();

        return occ;
    }

    private void xsdElementGenerator(XsdElement xsdElement, Writer writer, StringBuilder indent) throws IOException {
        indent.append(indentUnit);

        if (xsdElement.getAttributes().isEmpty() && xsdElement.getChildren().isEmpty()) {
            writeSimpleElement(xsdElement, writer, indent);
            removeIndent(indent);
            return;
        }

        writer.write(indent + "<xs:element " + occursString(xsdElement) + "name=\"" + xsdElement.getName() + newLineOpening);
        indent.append(indentUnit);
        if(xsdElement.isMixed()){
            writer.write(indent + "<xs:complexType mixed=\"" + xsdElement.isMixed() + newLineOpening);
        } else {
            writer.write(indent + "<xs:complexType>\n");
        }


        if (!xsdElement.getChildren().isEmpty()) {
            writeChildren(xsdElement, writer, indent);
        } else {
            writeSimpleContentWithAttributes(xsdElement, writer, indent);
        }

        if (!xsdElement.getChildren().isEmpty() && !xsdElement.getAttributes().isEmpty()) {
            writeAttributes(xsdElement, writer, indent);
        }

        removeIndent(indent);
        writer.write(indent + "</xs:complexType>\n");
        removeIndent(indent);
        writer.write(indent + "</xs:element>\n");
    }

    private void writeSimpleElement(XsdElement element, Writer writer, StringBuilder indent) throws IOException {
        String type = element.getType();
        if(element.isEmpty()){
            writer.write(indent + "<xs:element " + occursString(element) + "name=\"" + element.getName() + newLineClosing);
        } else {
            writer.write(indent + "<xs:element " + occursString(element) + "name=\"" + element.getName() + "\" type=\"" + type + newLineClosing);
        }
    }

    private void writeChildren(XsdElement parent, Writer writer, StringBuilder indent) throws IOException {
        indent.append(indentUnit);
        writer.write(indent + "<xs:sequence>\n");
        indent.append(indentUnit);
        for (XsdElement child : parent.getChildren()) {
            xsdElementGenerator(child, writer, indent);
        }
        removeIndent(indent);
        writer.write(indent + "</xs:sequence>\n");
        removeIndent(indent);
    }

    private void writeSimpleContentWithAttributes(XsdElement element, Writer writer, StringBuilder indent) throws IOException {
        if(element.isEmpty()){
            if (!element.getAttributes().isEmpty()) {
                writeAttributes(element, writer, indent);
            }
        } else {
            indent.append(indentUnit);
            writer.write(indent + "<xs:simpleContent>\n");

            indent.append(indentUnit);
            writer.write(indent + "<xs:extension base=\"" + element.getType() + newLineOpening);

            if (!element.getAttributes().isEmpty()) {
                writeAttributes(element, writer, indent);
            }

            removeIndent(indent);
            writer.write(indent + "</xs:extension>\n");
            removeIndent(indent);
            writer.write(indent + "</xs:simpleContent>\n");
        }
    }

    private void writeAttributes(XsdElement element, Writer writer, StringBuilder indent) throws IOException {
        indent.append(indentUnit);
        for (Map.Entry<Pair<String, String>, String> attr : element.getAttributes().entrySet()) {
            String type = attr.getKey().getValue();
            writer.write(indent + "<xs:attribute name=\"" + attr.getKey().getKey() + "\" type=\"" + type + "\" use=\"" + attr.getValue() + newLineClosing);
        }
        removeIndent(indent);
    }

    private void removeIndent(StringBuilder indent) {
        indent.delete(indent.length() - indentUnit.length(), indent.length());
    }
}

