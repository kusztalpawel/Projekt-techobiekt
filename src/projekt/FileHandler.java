package projekt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileHandler {
    private File file;
    private String fileContent;

    public FileHandler(File file){
        this.file = file;
    }

    public boolean setFileContent() throws FileNotFoundException {
        if(file == null){
            System.out.println("Nie otworzono żadnego pliku!");

            return false;
        } else {
            fileContent = "";
            StringBuilder stringBuilder = new StringBuilder(fileContent);
            Scanner fileReader = new Scanner(file);
            String temporary;
            while(fileReader.hasNextLine()) {
                temporary = fileReader.nextLine().trim();
                if(!temporary.isEmpty()){
                    stringBuilder.append(temporary);
                    stringBuilder.append(" ");
                }
            }
            fileContent = stringBuilder.toString();
            System.out.print(fileContent + " ");
            System.out.print("\n");

            fileReader.close();

            return true;
        }
    }

    public void setFile(File file){
        this.file = file;
    }

    public File getFile(){
        return file;
    }

    public String getFileName(){
        return file.getName();
    }

    public String getFileContent(){
        return fileContent;
    }
}
