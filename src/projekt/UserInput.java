package projekt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserInput {
    private final List<FileHandler> filesList;

    public UserInput(List<FileHandler> filesList){
        this.filesList = filesList;
    }

    public void addFile(File file) throws FileNotFoundException {
        filesList.add(new FileHandler(file));
        if(!(filesList.getLast().setFileContent())) {
            filesList.removeLast();
        }
    }

    public List<String> showFilesList(){
        ArrayList<String> files = new ArrayList<>();
        for (FileHandler fileHandler : filesList) {
            files.add(fileHandler.getFileName());
        }

        return files;
    }

    private XsdElement createXsdRoot(FileHandler xmlFile){
        return new XsdElement(xmlFile.getXmlFile().getRootElement().getTag(), XmlTypes.detectType(xmlFile.getXmlFile().getRootElement().getTag()),0);
    }

    public void generateSchema(int fileIndex){
        FileHandler xmlFile = filesList.get(fileIndex);

        xmlFile.setXmlFile();
        if(xmlFile.getXmlFile().createXml() == -1){
            return;
        }

        XsdElement xsdRootElement = createXsdRoot(xmlFile);
        xsdRootElement.createXsdObject(xmlFile.getXmlFile().getRootElement());

        System.out.println(xsdRootElement);

        String fileName = xmlFile.getFileName().substring(0, xmlFile.getFileName().indexOf("."));

        try(FileWriter fileWriter = new FileWriter(fileName + "_schema.xsd")) {
            new XsdGenerator().createXsd(xmlFile.getXmlFile().getRootElement(), fileWriter);
        } catch (IOException e) {
            System.out.println("An error occurred");
        }
    }
}
