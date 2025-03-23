import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileHandler {
    private File file;

    public void openFile(){
        String filePath;
        System.out.println("Podaj ścieżkę do pliku: ");
        Scanner keyboardInput = new Scanner(System.in);
        filePath = keyboardInput.nextLine();

        file = new File(filePath);
    }

    public void readFile() throws FileNotFoundException {
        if(file == null){
            System.out.println("Nie otworzono żadnego pliku!");
        } else {
            Scanner fileReader = new Scanner(file);
            while(fileReader.hasNextLine()){
                String data = fileReader.nextLine();
                System.out.println(data);
            }
        }
    }

    public File getFile(){
        return file;
    }
}
