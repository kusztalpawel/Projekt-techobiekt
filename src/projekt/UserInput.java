package projekt;

import java.io.FileNotFoundException;
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

    public void getInput() throws FileNotFoundException {
        int fileIndexInput;
        while(choice != 5){
            System.out.println("Wybierz co chcesz zrobić: \n1. Otwórz plik\n2. Zobacz pliki\n3. Wyświetl wybrany plik\n4. Generuj\n5. Zakończ program ");
            Scanner keyboardInput = new Scanner(System.in);
            choice = keyboardInput.nextInt();

            switch (choice){
                case 1:
                    filesList.add(new FileHandler());
                    filesList.get(fileIndex).openFile();
                    fileIndex++;
                    break;
                case 2:
                    for(int i = 0; i < fileIndex; i++){
                        System.out.println((i + 1) + ". " + filesList.get(i).getFile());
                    }
                    break;
                case 3:
                    System.out.println("Podaj plik, który chcesz zobaczyć: ");
                    for(int i = 0; i < fileIndex; i++){
                        System.out.println((i + 1) + ". " + filesList.get(i).getFile());
                    }
                    fileIndexInput = keyboardInput.nextInt() - 1;
                    filesList.get(fileIndexInput).printFileContent();
                    break;
                case 4:
                    System.out.println("Podaj plik, który chcesz generować: ");
                    for(int i = 0; i < fileIndex; i++){
                        System.out.println((i + 1) + ". " + filesList.get(i).getFile());
                    }
                    fileIndexInput = keyboardInput.nextInt() - 1;

                    new XmlFile(filesList, fileIndexInput).createXml();
                    break;
                default:
                    break;
            }

        }
    }
}
