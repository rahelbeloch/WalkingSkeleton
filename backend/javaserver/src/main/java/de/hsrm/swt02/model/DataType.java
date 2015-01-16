package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Emum for DataTypes in Forms.
 *
 */
public enum DataType {

    STRING(String.class), 
    INT(Integer.class), 
    DOUBLE(Double.class);
    
    private final Class<?> type; 
    
    /**
     * 
     * @param type the java Type
     */
    private DataType(Class<?> type) {
        this.type = type;
        
    }
    /**
     * value getter.
     * @return name()
     */
    public String value() {
        return name();
    }
    /**
     * 
     * @return the type
     */
    public Class<?> getType() {
        return type;
    }
    /**
     * valueof getter.
     * @param v is string corresponding the enum value
     * @return int
     */
    public static DataType fromValue(String v) {
        return valueOf(v);
    }
    
    public static boolean hasType(String type) {
    	for(DataType dt: DataType.values()) {
    		if(type.equals(dt.toString())) {
    			return true;
    		}
    	}
    	return false;
    }
}
