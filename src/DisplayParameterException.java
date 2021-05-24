/**
 * Custom Exception for Part Form Class when parameters for methods are invalid
 * @author Woo Jin An
 */
public class DisplayParameterException extends Exception{
    private String parameterValue;

    /**
     * Exception Constructor with message parameter
     * @param message a String to store in the exception as a message
     */
    public DisplayParameterException(String message){
        super(message);
    }

    /**
     * Exception Constructor with message and parameterValue parameter
     * @param message a String to store in the exception as a message
     * @param parameterValue the String parameter value passed to raise the exception
     */
    public DisplayParameterException(String message, String parameterValue){
        super(message);
        this.setParameterValue(parameterValue);
    }

    /**
     * Set the passed parameterValue String to the class private variable
     * @param parameterValue the String parameter value passed to raise the exception
     */
    public void setParameterValue(String parameterValue){
        this.parameterValue = parameterValue;
    }

    /**
     * Get the value of the String, parameterValue
     * @return the String parameter value passed to raise the exception
     */
    public String getParameterValue(){
        return this.parameterValue;
    }
}
