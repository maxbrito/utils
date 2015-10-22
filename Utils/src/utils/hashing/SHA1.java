/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-05-29T17:27:35Z
 * LicenseName: AGPL-3.0+
 * FileName: SHA1.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Methods related to SHA1 file signatures</text> 
 */

package utils.hashing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Brito, 29th of May 2014 in Paris, France.
 */
public class SHA1 {
    
    static final String fileNameIndex = "filenames.txt";
    
    /**
     * Given a SHA1 signature of a file, this method will get the respective 
     * folder. If the folder does not exist, it will be created. 
     * @param SHA1  The SHA1 signature for a given file
     * @return      The pointer to the folder
     */
    public static File createOrGetFolder(final String SHA1){
        final String folderPath = getSHA1Folder(SHA1);
        File folder = new File("./files",folderPath);
        utils.files.mkdirs(folder);
        return folder;
    }
    
    
    /**
     * Converts a given SHA1 signature to a set of folders. For example, this
     * signature: 0dd27d1c6a8e3a52ac0cf395c5425b8bc6f9dc31
     * becomes: /0dd2/7d1c/6a8e/3a52/ac0c/f395/c542/5b8b/c6f9/dc31
     * @param SHA1
     * @return 
     */
    public static String getSHA1Folder(final String SHA1){
        String result = "";
        int jump= 0;
        while(jump != 40){
            result = result.concat("/").concat(SHA1.substring(jump, jump + 4));
            jump += 4;
        }
        return result;
    }

    /**
     * Given a specific SHA1 signature folder, we will add the information
     * about the file name on its index folder
     * @param folder    The last folder where the SHA1 index data related to
     *                  this file will be stored
     * @param file      The file that we want to gather information about
     */
    public static void writeFileName(File folder, File file) {
        File indexFile = new File(folder, fileNameIndex);
        addNameToFile(indexFile, file);
    }

    /**
     * Add this file name to the file name index. If the name is already indexed
     * then exit without action, if the name is new then add it up.
     * We use the unix path separator "/" by default when adding the respective
     * path information
     * @param indexFile
     * @param file 
     */
    private static void addNameToFile(File indexFile, File file) {
        // get the file name for this file
        final String fileName = file.getName();
        try {
            // if the index file doesn't exist, create one
            if(indexFile.exists() == false){
                // we can directly save this entry to save time
                utils.files.SaveStringToFile(indexFile, fileName);
                // no need to continue
                return;
            }
            BufferedReader reader;
            // first step is reading the older files
            reader = new BufferedReader(new FileReader(indexFile));
            // skip the header line
//            reader.readLine();
            // declare the variables used in the loop
            String line;
            // go through each line on our large index
            while((line = reader.readLine()) != null){
                if(utils.text.equals(line, fileName)){
                    //System.out.println("Found: " + id);
                    reader.close();
                    // this means no need to write anything
                    return;
                }
            }
            reader.close();
            // if we got this far, it means we don't have this signature yet
            BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile, true));
            writer.append("\n" + fileName);
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SHA1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SHA1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the names that were indexed for a given SHA1 signature. We will read
     * this information from the file knowledge base when available. On this
     * result we include the path names as part of the file name.
     * @param idSHA1    A SHA1 to be read
     * @return          A list of the filenames that were indexed.
     */
    public static String getFileNames(String idSHA1) {
        // get the folder
        File folder = createOrGetFolder(idSHA1);
        // now get the respective file
        File indexFile = new File(folder, fileNameIndex);
        final String result = utils.files.readAsString(indexFile);
        return result;
    }
    
}
