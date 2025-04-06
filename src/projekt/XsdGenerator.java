package projekt;

import java.util.List;

public class XsdGenerator {
    public void createXSD(List<FileHandler> filesList){
        for (FileHandler fileHandler : filesList) {
            System.out.println(fileHandler.getFile());
        }
    }
}
