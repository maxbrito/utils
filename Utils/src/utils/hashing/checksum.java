/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-05-13T17:32:10Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: checksum.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Code for generation checksums </text> 
 */

package utils.hashing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 *
 * @author Nuno Brito, 27th of May 2014 in Paris, France.
 */
public class checksum {

    // should we display debugging messages or not?
    protected static final boolean
            debug = false;

     @SuppressWarnings("empty-statement")
/**
 * This method will generate a SHA-256 Scanner_Checksum of a given file
 *
 * @param hash - possible values: SHA-1, SHA-256, SHA-512 and MD5
 * @param filename - the target filename, we also handle locked files
 * @return - the resulting Scanner_Checksum or empty if not possible to compute
 */
 public static String generateFileChecksum(String hash, File filename){

       String checksum;
       FileInputStream fis = null;

        try {  // SHA-256
            MessageDigest md = MessageDigest.getInstance(hash);
//              try {
                fis = new FileInputStream(filename);
//            } catch (Exception e) {
//                //log("error", "No Scanner_Checksum performed for " + filename);
//
//                return "";
//            }
            byte[] dataBytes = new byte[1024];
            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            };
            //fis.close();


            byte[] mdbytes = md.digest();
            //convert the byte to hex format method 1
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            checksum = sb.toString();
        } catch (IOException ex) {
           if(debug == true)
            Logger.getLogger(checksum.class.getName()).log(Level.SEVERE, null, ex);
                //log("error", "No Scanner_Checksum performed for " + filename);
                return "";
        } catch (NoSuchAlgorithmException ex) {
           if(debug == true)
            Logger.getLogger(checksum.class.getName()).log(Level.SEVERE, null, ex);
                //log("error", "No Scanner_Checksum performed for " + filename);
                return "";
        } finally {
            try {
                if(fis!=null)
                    fis.close();
            } catch (IOException ex) {
             if(debug == true)
               Logger.getLogger(checksum.class.getName()).log(Level.SEVERE, null, ex);
                return "";
            }
        }


   return checksum;
   }

     public static String generateFileCRC32(String fileName) {
        long checksum = 0;
        CheckedInputStream cis = null;

        try {
        //    long fileSize = 0;
            
                // Computer CRC32 Scanner_Checksum
                cis = new CheckedInputStream(
                        new FileInputStream(fileName), new CRC32());
                //fileSize = new File(fileName).length();
//            } catch (FileNotFoundException e) {
//                System.err.println("File not found.");
//            }
            byte[] buf = new byte[128];
            while(cis.read(buf) >= 0) {
            }

            checksum = cis.getChecksum().getValue();
            //System.out.println(Scanner_Checksum + " " + fileSize + " " + fileName);

        } catch (IOException e) {
            return "";
        } finally{
            try {
                cis.close();
            } catch (Exception ex) {
             if(debug == true)
               Logger.getLogger(checksum.class.getName()).log(Level.SEVERE, null, ex);
             return "";
            } finally {
            // return our output
            return Long.toString(checksum);
            }
        }
    }


    /**
     * Generates an hash from a given string
     * @param content
     * @return 
     * @throws java.security.NoSuchAlgorithmException 
    */
    public static String generateStringSHA1(final String content) throws Exception{
        
        // Create the checksum digest
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(content.getBytes());
        // get the converted bytes
        final byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        //convert the byte to hex format
        @SuppressWarnings("StringBufferMayBeStringBuilder")
        final StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	return hexString.toString();
    }
     
    
    /**
     * Generates an hash from a given string
     * @param byteData
     * @return 
    */
    public static String convertHash(final byte byteData[]){
        //convert the byte to hex format method 1
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        //convert the byte to hex format
        @SuppressWarnings("StringBufferMayBeStringBuilder")
        final StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		final String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	return hexString.toString();
    }
    
    
     /**
     * Generates an hash from a given string
     * @param content
     * @return 
     * @throws java.security.NoSuchAlgorithmException 
    */
    public static String generateStringMD5(final String content) throws NoSuchAlgorithmException{
        
        // Create the checksum digest
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(content.getBytes());
        // get the converted bytes
        final byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        //convert the byte to hex format
        @SuppressWarnings("StringBufferMayBeStringBuilder")
        final StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	return hexString.toString();
    }
    
     /**
      * Generates an hash from a given string
      * @param content
      * @return 
      */
     public static String generateStringSHA256(final String content){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(checksum.class.getName()).log(Level.SEVERE, null, ex);
        }
        md.update(content.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        @SuppressWarnings("StringBufferMayBeStringBuilder")
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        //convert the byte to hex format method 2
        @SuppressWarnings("StringBufferMayBeStringBuilder")
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	return hexString.toString();
    }

}
