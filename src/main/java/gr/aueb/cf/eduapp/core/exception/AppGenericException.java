package gr.aueb.cf.eduapp.core.exception;

public class AppGenericException extends Exception{

    private final String code;

    public AppGenericException(String code, String message){
        super(message);
        this.code = code;
    }

}
