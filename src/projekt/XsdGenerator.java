package projekt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class XsdGenerator {
    public void createXSD(Element element, FileWriter fileWriter) throws IOException {
        String indentUnit = " ";
        StringBuilder currentIndent = new StringBuilder();
        currentIndent.append(indentUnit.repeat(Math.max(0, element.getNodeDepth() + 1)));

        if ((element.getAttributes().isEmpty()) && (element.getChildren().isEmpty())) {
            fileWriter.write(currentIndent + "<xs:element name=\"" + element.getTag() + "\" type=\"xs:string\"/>\n");
            return;
        }

        fileWriter.write(currentIndent + "<xs:element name=\"" + element.getTag() + "\">\n");
        fileWriter.write(currentIndent + indentUnit + "<xs:complexType>\n");

        if (!element.getChildren().isEmpty()) {
            fileWriter.write(currentIndent + indentUnit + indentUnit + "<xs:sequence>\n");
            for (Element child : element.getChildren()) {
                createXSD(child, fileWriter);
            }
            fileWriter.write(currentIndent + indentUnit + indentUnit + "</xs:sequence>\n");
        } else {
            fileWriter.write(currentIndent + indentUnit + indentUnit + "<xs:simpleContent>\n");
            fileWriter.write(currentIndent + indentUnit + indentUnit + indentUnit + "<xs:extension base=\"xs:string\">\n");
            if (!element.getAttributes().isEmpty()){
                for (Map.Entry<String, String> attribute : element.getAttributes().entrySet()){
                    fileWriter.write(currentIndent + indentUnit + indentUnit + indentUnit + indentUnit + "<xs:attribute name=\"" + attribute.getKey() + "\" type=\"xs:string\"/>\n");
                }
            }
            fileWriter.write(currentIndent + indentUnit + indentUnit + indentUnit + "</xs:extension>\n");
            fileWriter.write(currentIndent + indentUnit + indentUnit + "</xs:simpleContent>\n");
        }

        if (!element.getChildren().isEmpty() && !element.getAttributes().isEmpty()) {
            for (Map.Entry<String, String> attribute : element.getAttributes().entrySet()){
                fileWriter.write(currentIndent + indentUnit + indentUnit + "<xs:attribute name=\"" + attribute.getKey() + "\" type=\"xs:string\"/>\n");
            }
        }

        fileWriter.write(currentIndent + indentUnit + "</xs:complexType>\n");
        fileWriter.write(currentIndent + "</xs:element>\n");
    }
}

