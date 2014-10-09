/*
 * This class provides support for computing the checksums that are used at our
 * system. It will attempt to compute them from different sources and provide
 * a consistent and uniform result.
 */

package utils;

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
 * @author Nuno Brito
 */
public class Checksum {

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
            Logger.getLogger(Checksum.class.getName()).log(Level.SEVERE, null, ex);
                //log("error", "No Scanner_Checksum performed for " + filename);
                return "";
        } catch (NoSuchAlgorithmException ex) {
           if(debug == true)
            Logger.getLogger(Checksum.class.getName()).log(Level.SEVERE, null, ex);
                //log("error", "No Scanner_Checksum performed for " + filename);
                return "";
        } finally {
            try {
                if(fis!=null)
                    fis.close();
            } catch (IOException ex) {
             if(debug == true)
               Logger.getLogger(Checksum.class.getName()).log(Level.SEVERE, null, ex);
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
            } catch (IOException ex) {
             if(debug == true)
               Logger.getLogger(Checksum.class.getName()).log(Level.SEVERE, null, ex);
             return "";
            } finally {
            // return our output
            return Long.toString(checksum);
            }
        }
    }


     public static String generateStringSHA256(String content){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Checksum.class.getName()).log(Level.SEVERE, null, ex);
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
