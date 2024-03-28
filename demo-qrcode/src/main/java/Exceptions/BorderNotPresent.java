package Exceptions;

public class BorderNotPresent extends RuntimeException{
    public BorderNotPresent(){

    }

    public BorderNotPresent(String message){
        super(message);
    }
}
