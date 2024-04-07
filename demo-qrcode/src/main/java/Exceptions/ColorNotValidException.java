package Exceptions;

public class ColorNotValidException extends RuntimeException{

    public ColorNotValidException(){

    }

    public ColorNotValidException(String message){
        super(message);
    }
}
