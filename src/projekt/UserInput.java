package projekt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserInput {
    private final List<XmlFile> xmlFilesList;
    private final List<XsdFile> xsdFilesList;

    public UserInput(List<XmlFile> xmlFileList, List<XsdFile> xsdFileList){
        this.xmlFilesList = xmlFileList;
        this.xsdFilesList = xsdFileList;
    }

    public void addFile(File file) throws FileNotFoundException {
        xmlFilesList.add(new XmlFile(file));
        xmlFilesList.getLast().setFileContent(FileHandler.setFileContent(file));
        xmlFilesList.getLast().createXmlObject();
    }

    public List<String> showFilesList(){
        ArrayList<String> files = new ArrayList<>();
        for (XmlFile fileHandler : xmlFilesList) {
            files.add(fileHandler.getFile().getName());
        }

        return files;
    }

    public void generateSchema(int fileIndex){
        XmlFile xmlFile = xmlFilesList.get(fileIndex);

        xsdFilesList.add(new XsdFile());
        xsdFilesList.getLast().setRootElement(XsdElement.createXsdElement(xmlFile));
        xsdFilesList.getLast().getRootElement().createXsdTree(xmlFile.getRootElement());

        XsdFile xsdFile = xsdFilesList.get(fileIndex);

        System.out.println(xsdFilesList.getLast().getRootElement());

        String fileName = xmlFile.getFile().getName().substring(0, xmlFile.getFile().getName().indexOf("."));

        try(FileWriter fileWriter = new FileWriter(fileName + "_schema.xsd")) {
            new XsdFileGenerator().createXsd(xsdFile.getRootElement(), fileWriter);
        } catch (IOException e) {
            System.out.println("An error occurred");
        }
    }
}
