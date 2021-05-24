/**
 * InHouse concrete Class extended from part
 * @author Woo Jin An
 * @see Part
 */
public class InHouse extends Part {

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name, price, stock, min, max);
        this.machineId=machineId;
    }

    /**
     * Set Machine ID variable with a new value
     * @param machineId int MachineID unique to In-House instance
     */
    public void setMachineId(int machineId){
        this.machineId=machineId;
    }

    /**
     * Get Machine ID
     * @return int machineID unique to In-House instance
     */
    public int getMachineId() {
        return this.machineId;
    }
}
