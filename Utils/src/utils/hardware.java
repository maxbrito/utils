/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2015-02-26T11:49:01Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Code related to the underlying hardware where the JVM
    is running. </text> 
 */

package utils;

/**
 *
 * @author Nuno Brito, 26th of February 2015 in Berlin, Germany
 */
public class hardware {

    /**
     * Returns the number of physical CPU cores available for the JVM.
     * For example, a typical Intel i7 processor will report 4 cores.
     * @return 
     */
    public static int numberCPU(){
        return Runtime.getRuntime().availableProcessors();
    }
    
}
