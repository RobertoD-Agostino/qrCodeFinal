package Exceptions;

public class UrlNotPresentException extends RuntimeException{
    public UrlNotPresentException(){

    }

    public UrlNotPresentException(String message){
        super(message);
    }
}
