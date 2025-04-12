package projekt;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileHandler {
    private File file;
    private String fileContent;

    public int openFile() throws FileNotFoundException {
        String filePath;
        System.out.println("Podaj ścieżkę do pliku: ");
        Scanner keyboardInput = new Scanner(System.in);
        filePath = keyboardInput.nextLine();

        if (filePath == null || filePath.isEmpty()) {
            System.out.println("Path is null or empty.");
            return -1;
        }

        try {
            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                file = new File(filePath);
                setFileContent();
                return 0;
            } else {
                System.out.println("Podany plik nie istnieje!");
                return -1;
            }
        } catch (InvalidPathException e){
            System.out.println("Niepoprawna sciezka");
            return -1;
        }
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

    public void setFile(File file){
        this.file = file;
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
