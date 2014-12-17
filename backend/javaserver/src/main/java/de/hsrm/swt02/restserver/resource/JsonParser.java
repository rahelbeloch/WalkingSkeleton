package de.hsrm.swt02.restserver.resource;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.hsrm.swt02.restserver.exceptions.JacksonException;

/**
 * 
 * @author akoen001
 *
 * class responsible for parsing Json Strings to Java Objects and vice versa.
 *
 */
public class JsonParser {

    final static ObjectMapper MAPPER = new ObjectMapper();
    
    /**
     * 
     * @param o the Object to parse
     * @return the given Object as String
     * @throws JacksonException if the Object is malformed
     */
    public static String marshall(Object o) throws JacksonException {
        String objectAsString;
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectAsString = MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new JacksonException();
        }
        return objectAsString;
    }
    
    /**
     * 
     * @param s the String to unmarshall
     * @param o the type of the Object
     * @return the Object
     * @throws JacksonException if the String is malformed
     */
    public static Object unmarshall(String s, Object o) throws JacksonException {
        try {
            o = MAPPER.readValue(s, o.getClass());
        } catch (IOException e) {
            throw new JacksonException();
        }
        return o;
    }
    
}