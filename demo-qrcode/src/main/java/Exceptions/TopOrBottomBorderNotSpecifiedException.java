package Exceptions;

public class TopOrBottomBorderNotSpecifiedException extends RuntimeException{
    public TopOrBottomBorderNotSpecifiedException(){

    }

    public TopOrBottomBorderNotSpecifiedException(String message){
        super(message);
    }
}
