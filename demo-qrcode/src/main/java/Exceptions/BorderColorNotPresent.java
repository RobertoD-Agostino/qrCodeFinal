package Exceptions;

public class BorderColorNotPresent extends RuntimeException{
    public BorderColorNotPresent(){

    }

    public BorderColorNotPresent(String message){
        super(message);
    }
}
