package projekt;

public class ParsingErrorHandler {
    private ParsingErrorHandler() {
        throw new IllegalStateException();
    }

    public static void error(){
        System.out.println("\u001B[31mError!\u001B[0m");
        System.exit(-1);
    }
}
