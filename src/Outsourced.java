/**
 * Outsourced concrete Class extended from part
 * @author Woo Jin An
 * @see Part
 */
public class Outsourced extends Part{

    private String companyName;


    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName=companyName;
    }

    /**
     * Set Company Name variable with a new value
     * @param companyName String company name unique to Outsourced instance
     */
    public void setCompanyName(String companyName){
        this.companyName=companyName;
    }

    /**
     * Get Company Name
     * @return String companyName unique to Outsourced instance
     */
    public String getCompanyName() {
        return this.companyName;
    }
}
