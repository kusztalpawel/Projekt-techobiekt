package projekt;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class UserInput {
    private final List<FileHandler> filesList;
    private int choice;
    private int fileIndex;

    public UserInput(List<FileHandler> filesList){
        choice = 0;
        fileIndex = 0;
        this.filesList = filesList;
    }

    private void addFile() throws FileNotFoundException {
        filesList.add(new FileHandler());
        if(filesList.get(fileIndex).openFile() == 0) {
            fileIndex++;
        } else {
            filesList.removeLast();
        }
    }

    private void showFilesList(){
        for (int i = 0; i < fileIndex; i++) {
            System.out.println((i + 1) + ". " + filesList.get(i).getFile());
        }
    }

    private void showFile(Scanner keyboardInput){
        int fileIndexInput;
        if(fileIndex != 0){
            System.out.println("Podaj plik, który chcesz zobaczyć: ");
            for (int i = 0; i < fileIndex; i++) {
                System.out.println((i + 1) + ". " + filesList.get(i).getFile());
            }
            if(keyboardInput.hasNextInt()){
                fileIndexInput = keyboardInput.nextInt() - 1;
            } else {
                return;
            }

            filesList.get(fileIndexInput).printFileContent();
        } else {
            System.out.println("Nie wczytano zadnych plikow!");
        }
    }

    private void generateSchema(Scanner keyboardInput){
        int fileIndexInput;
        if(fileIndex != 0){
            System.out.println("Podaj plik, który chcesz generować: ");
            for (int i = 0; i < fileIndex; i++) {
                System.out.println((i + 1) + ". " + filesList.get(i).getFile());
            }
            if(keyboardInput.hasNextInt()){
                fileIndexInput = keyboardInput.nextInt() - 1;
            } else {
                return;
            }

            XmlFile xmlFile = new XmlFile(filesList.get(fileIndexInput));
            if(xmlFile.createXml() == -1){
                return;
            }

            String fileName  = filesList.get(fileIndexInput).getFile().getName().substring(0, filesList.get(fileIndexInput).getFile().getName().indexOf("."));

            try(FileWriter fileWriter = new FileWriter(fileName + "_schema.xsd")) {
                fileWriter.write("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n");
                new XsdGenerator().createXSD(xmlFile.getRootElement(), fileWriter);
                fileWriter.write("</xs:schema>\n");
            } catch (IOException e) {
                System.out.println("An error occurred");
            }
        } else {
            System.out.println("Nie wczytano zadnych plikow!");
        }
    }

    public void getInput() throws IOException {
        Scanner keyboardInput = new Scanner(System.in);

        while(choice != 5){
            System.out.println("Wybierz co chcesz zrobić: \n1. Otwórz plik\n2. Zobacz pliki\n3. Wyświetl wybrany plik\n4. Wygeneruj schemat XML\n5. Zakończ program ");
            if(keyboardInput.hasNextInt()){
                choice = keyboardInput.nextInt();
            } else {
                System.out.println("Podano niepoprwane dane!");
                return;
            }

            switch (choice) {
                case 1:
                    addFile();

                    break;
                case 2:
                    showFilesList();

                    break;
                case 3:
                    showFile(keyboardInput);

                    break;
                case 4:
                    generateSchema(keyboardInput);

                    break;
                default:
                    break;
            }
        }
        keyboardInput.close();
    }
}
