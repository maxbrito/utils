/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2015-02-22T21:49:01Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileCopyrightText: <text> Copyright 2015 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Converts a Java object to text and vice-versa </text> 
 */

package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Nuno Brito, 22nd of February 2015 in Darmstadt, Germany
 */
public class serialize {

    /**
     * Transforms a given (serializable) object to a Base64 string. If the
     * object is not serializable for some reason then an exception is raised.
     * If nothing was transformed, the result is null.
     * @param obj
     * @return 
     */
    @SuppressWarnings("ConvertToTryWithResources")
    public static String objectToString(Object obj){
        String output = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(obj);
            so.flush();
            output = DatatypeConverter.printBase64Binary(bo.toByteArray());
            so.close();
            bo.close();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return output;
    }
    
    
    public static Object StringToObject(final String text){
        Object output = null;
        try {
            // convert from base 64 to a byte sequence
            byte b[] = DatatypeConverter.parseBase64Binary(text);
            // move the object from text to binary form
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            output = si.readObject();
            si.close();
            bi.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
    
    
}
