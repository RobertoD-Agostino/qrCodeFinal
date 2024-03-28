package Exceptions;

public class WidthAndHeightNotEnoughException extends RuntimeException{
    public WidthAndHeightNotEnoughException(){

    }

    public WidthAndHeightNotEnoughException(String message){
        super(message);
    }
}
