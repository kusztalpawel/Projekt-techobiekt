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

    public void generateSchema(int fileIndex){
            XmlFile xmlFile = new XmlFile(filesList.get(fileIndex));
            if(xmlFile.createXml() == -1){
                return;
            }

            String fileName  = filesList.get(fileIndex).getFileName().substring(0, filesList.get(fileIndex).getFileName().indexOf("."));

            try(FileWriter fileWriter = new FileWriter(fileName + "_schema.xsd")) {
                new XsdGenerator().createXSD(xmlFile.getRootElement(), fileWriter);
            } catch (IOException e) {
                System.out.println("An error occurred");
            }
    }
}
