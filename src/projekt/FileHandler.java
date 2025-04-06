package projekt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileHandler {
    private File file;
    private String fileContent;

    public void openFile() throws FileNotFoundException {
        String filePath;
        System.out.println("Podaj ścieżkę do pliku: ");
        Scanner keyboardInput = new Scanner(System.in);
        filePath = keyboardInput.nextLine();

        file = new File(filePath);
        setFileContent();
    }

    public void setFileContent() throws FileNotFoundException {
        if(file == null){
            System.out.println("Nie otworzono żadnego pliku!");
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
        }
    }

    public File getFile(){
        return file;
    }

    public String getFileContent(){
        return fileContent;
    }

    public void printFileContent(){
        System.out.println(fileContent.length());
        System.out.println(fileContent);
    }
}
