package de.hsrm.swt02.model;

/**
 * This class represents a RootElement.
 */
public class RootElement {
    // Used for (de)serialization. Do not change.
    protected int id;

    /**
     * Constructor for RootElement.
     */
    public RootElement() {

    }
    
    public void init(RootElement re) {
        this.id = re.id;
    }
    
    public Object clone() throws CloneNotSupportedException {
        final RootElement clone = new RootElement();
        clone.init(this);

        return clone;
    }

    /**
     * Id getter.
     * @return id of the RootElement
     */
    public int getId() {
        return id;
    }

    /**
     * Id setter.
     * @param id of the RootElement
     */
    public void setId(int id) {
        this.id = id;
    }
}
