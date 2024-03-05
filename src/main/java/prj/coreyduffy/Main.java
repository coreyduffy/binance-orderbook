package prj.coreyduffy;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Please ensure that one argument (the symbol you would like to query) is passed");
        }
        System.out.println("Hello world!");
    }
}