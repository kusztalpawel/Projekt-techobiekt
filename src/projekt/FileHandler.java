package projekt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileHandler {
    private FileHandler(){
    }

    public static String setFileContent(File file) throws FileNotFoundException {
        if (file == null) {
            System.out.println("Nie otworzono żadnego pliku!");

            return null;
        } else {
            String fileContent = "";
            StringBuilder stringBuilder = new StringBuilder(fileContent);
            Scanner fileReader = new Scanner(file);
            String temporary;
            while (fileReader.hasNextLine()) {
                temporary = fileReader.nextLine().trim();
                if (!temporary.isEmpty()) {
                    stringBuilder.append(temporary);
                    stringBuilder.append(" ");
                }
            }
            fileContent = stringBuilder.toString();
            System.out.print(fileContent + " ");
            System.out.print("\n");

            fileReader.close();

            return fileContent;
        }
    }
}
