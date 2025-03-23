import java.util.List;

public class XSDGenerator {
    public void createXSD(List<FileHandler> filesList){
        for (FileHandler fileHandler : filesList) {
            System.out.println(fileHandler.getFile());
        }
    }
}
