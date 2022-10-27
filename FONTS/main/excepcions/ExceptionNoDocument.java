package main.excepcions;

public class ExceptionNoDocument extends Exception{
    public ExceptionNoDocument(String title, String author){
        super("El document amb títol '" + title + "' i autor '" + author + "' no existeix");
    }
}