/*
 * Methods to ease the handling of files
 */

package utils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.security.CodeSource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ReadWrite.FileReadLines;
import utils.thirdparty.MiscMethods;

/**
 *
 * @author Nuno Brito, 6th of June 2011 in Darmstadt, Germany.
 */
public class files {
  
    /**
     * Provides back the folder where the running jar is placed.
     * This is useful for cases where you launch a jar file from some
     * other folder but need that jar file to access specific sub-folders
     * where it is placed.
     * 
     * This method tries to detect when you are running the code from
     * an IDE and attempt to correct the file path accordingly.
     * @return 
     */
    public static File getFolderForJar(){
        try {
            // get the folder where the jar is located
            CodeSource codeSource = files.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            // if we are running this internally from the IDE, fix it to the base folder
            if(jarFile.getParentFile().getPath().endsWith("build")){
                // return the ./run/ folder that is defined by the IDE
                return new File(System.getProperty("user.dir"));
            }else{
                // return the folder where the jar can be found
                return jarFile.getParentFile();
            }
        } catch (URISyntaxException ex) {
            return null;
        }
    }
    
    /**
     * Tries to discover if we are running this code from the netbeans IDE
     * or from a normal .jar mode
     * @return 
     */
    public static boolean weAreInDevMode(){
    try {
            // get the folder where the jar is located
            CodeSource codeSource = files.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            // if we are running this internally from the IDE, fix it to the base folder
            if(jarFile.getParentFile().getPath().endsWith("build")){
                // return the ./run/ folder that is defined by the IDE
                return true;
            }else{
                // return the folder where the jar can be found
                return false;
            }
        } catch (URISyntaxException ex) {
            return false;
        }
    }
    
    
    /** Writes an inputstream back into a file
     * @param inputStream
     * @param outFile 
     */  
    static public void inputFileStreamToFile(InputStream inputStream, 
          File outFile){  
    
      OutputStream out = null;
      try{
          out = new FileOutputStream(outFile);
          byte buf[]=new byte[1024];
          int len;
          while((len=inputStream.read(buf))>0)
              out.write(buf,0,len);
          inputStream.close();
      }
      catch (IOException e){}
      finally {
            try {
                if(out != null)
                    out.close();
            } catch (IOException ex) {
            }
       
      }
    }

  /** Returns the extension of a file on disk
     * @param file
     * @return 
     */
  public static String getExtension(File file){
      String result;
      String filename = file.getName();
      // no dot found? no need to proceed
      if(filename.contains(".")==false){
          return "";
      }
      int mid= filename.lastIndexOf(".");
      result = filename.substring(mid+1, filename.length());  
      // ensure that we always get things in lowercase
      result = result.toLowerCase();
    return result;
  }
  

  /**
   * Returns the extension from a given file name. If the file name has no
   * dots then it will return the whole filename as result.
   * @param filename
   * @return 
   */
  public static String getExtension(final String filename){
      // no dot found? no need to proceed
      if(filename.contains(".") == false){
          return filename;
      }
      final int pos = filename.lastIndexOf(".");
      return filename.substring(pos + 1).toLowerCase();
    }

    /**
     * Retrieves the extension portion from a file name. Attempts to provide
     * an extension for cases where two dots are used. E.g. Archive.tar.gz
     * @param filename
     * @return 
     */
    public static String getExtensionFlexible(final String filename){
      // no dot found? use the same file name then
      if(filename.contains(".") == false){
          return filename;
      }
      // does the file has more than two dots?
      String[] portions = filename.split("\\.");
      if(portions.length > 2){
          return 
                  portions[portions.length-2] 
                  + "."
                  + portions[portions.length-1];
      }
      
      final int pos = filename.lastIndexOf(".");
      return filename.substring(pos + 1);
    }
  

  /**
   * This method will read the contents from a text file onto
   * a text array. It is useful to speed the text reading operation
   * @param file    the file on disk
   * @return        an array with the text lines
   * @date 2014-05-09
   */
  public static ArrayList<String[]> readAsStringArray(File file){
      ArrayList<String[]> result = new ArrayList();
      try {
          InputStreamReader fileReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
          BufferedReader reader = new BufferedReader(fileReader);
          String temp;
          // iterate through all lines
            while ((temp = reader.readLine()) != null) {
                String[] line = new String[]{temp};
                // add the line
                result.add(line);
            }
          
      } catch (IOException ex) {
          Logger.getLogger(files.class.getName()).log(Level.SEVERE, null, ex);
      }
      //System.out.println("files81 - read string array with " + result.size() + " elements");
      return result;
  }
  
  
    public static String readAsString(File file){
        final long length = file.length();
        final byte[] bytes = new byte[(int) length];
        InputStream is = null;
        try{
        is = new FileInputStream(file);
        is.read(bytes);
        }catch(IOException e){}
        finally{ 
            try {
                if(is!=null){
                    is.close();
                }
            } catch (IOException ex) {
                return null;
            }
        }

        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


// Deletes all files and subdirectories under dir.
// Returns true if all deletions were successful.
// If a deletion fails, the method stops attempting to delete and returns false.
public static boolean deleteDir(final File dir) {
    if (dir.isDirectory()) {
        for (String children : dir.list()) {
            boolean success = deleteDir(new File(dir, children));
            if (!success) {
                return false;
            }
        }
    }
    // The directory is now empty so delete it
    return dir.delete();
}

public static void shellDeleteDir(final File dir) {
    
    // get the real path without shortcuts
    final String path = getCanonical(dir);
   
    // needs to be bigger than 10 chars (avoid deleting stuff on root)
    if(path.length() < 10){
        return;
    }
    
    // go ahead, delete stuff without recovery being possible
    MiscMethods.executeCommand("rm -rf " + path);
}



/**
 * Counts the size of all files inside a given folder
 * @param where The folder where we want to count the files
 * @return 
 */
public static long folderSize(File where){
        ArrayList<File> folders = findFiles(where);
        long counter = 0;
        for(File folder : folders){
            counter += folder.length();
        }
        return counter;
}


/**
 * Find all files in a given folder and respective subfolders
 * @param where A file object of the start folder
 * @return An array containing all the found files, returns null if none is
 * found
 */
 public static ArrayList<File> findFiles(File where){
     return findFiles(where, 25);
 }

 /**
 * Find all files in a given folder and respective subfolders
 * @param where A file object of the start folder
 * @param what
 * @param maxDeep How deep is the crawl allowed to proceed
 * @return An array containing all the found files, returns null if none is
 * found
 */
 public static ArrayList<File> findFilesFiltered(File where, final String what, int maxDeep){

    File[] files = where.listFiles();
    ArrayList<File> result = new ArrayList<File>();

    if(files != null)
        for (File file : files) {
            if (file.isFile()){
                if(file.getName().contains(what))
                   result.add(file);}
            else
            if ( (file.isDirectory())
               &&( maxDeep-1 > 0 ) ){
                  // do the recursive crawling
                  ArrayList<File> temp = findFilesFiltered(file, what, maxDeep-1);

                      for(File thisFile : temp)
                              result.add(thisFile);
            }
        }
    return result;
    }
 
   /**
    * Find all files in a given folder and respective subfolders
    * @param where A file object of the start folder
    * @param what
    * @param maxDeep How deep is the crawl allowed to proceed
    * @return An array containing all the found files, returns null if none is
    * found
    */
 public static ArrayList<File> findFilesByExtension(File where, final String what, int maxDeep){

    File[] files = where.listFiles();
    ArrayList<File> result = new ArrayList<File>();

    if(files != null)
        for (File file : files) {
          if (file.isFile()){
              if(file.getName().endsWith(what))
                 result.add(file);}
          else
          if ( (file.isDirectory())
             &&( maxDeep-1 > 0 ) ){
                // do the recursive crawling
                ArrayList<File> temp = findFilesByExtension(file, what, maxDeep-1);

                    for(File thisFile : temp)
                            result.add(thisFile);
          }
        }
    return result;
    }
 
 
/**
 * Find all files in a given folder and respective subfolders
 * @param where A file object of the start folder
 * @param maxDeep How deep is the crawl allowed to proceed
 * @return An array containing all the found files, returns null if none is
 * found
 */
 public static ArrayList<File> findFiles(File where, int maxDeep){

    File[] files = where.listFiles();
    ArrayList<File> result = new ArrayList<File>();

    if(files != null)
        for (File file : files) {
        if (file.isFile())
            result.add(file);
        else
            if ( (file.isDirectory())
                    &&( maxDeep-1 > 0 ) ){
                // do the recursive crawling
                ArrayList<File> temp = findFiles(file, maxDeep-1);
                for(File thisFile : temp)
                    result.add(thisFile);
            }
        }
    return result;
 }


    /** 
     * Get the size of a given folder and respective files inside subfolders
     * @param where     The folder we want to analyse
     * @param maxDeep   How deep in subfolders we can go
     * @return          The number of subfolders
     */
  public static int getFileCount(File where, int maxDeep){
      // output variable
    int result = 0;
    File[] files = where.listFiles();

    if(files != null)
    for (File file : files) {
      // is this a folder?
      if(file.isDirectory()){
         if(maxDeep-1 > 0){
          // do the recursive crawling
          result += getFileCount(file, maxDeep-1);
        }
      }else{
      // then it is file
          result++;
      }
    }
      // output our result
     return result;
    }
 
 
    /**
     * @param where Get the size of a given folder and respective files inside subfolders 
     * @param maxDeep 
     * @return  
     */
    public static long getFolderSize(File where, int maxDeep){
        // output variable
        long result = 0;
        // get all files from the given folder
        ArrayList<File> files = findFiles(where, 25);
        // iterate each file and sum the value
        for(File file : files)
            result += file.length();
        // output our result
    return result;
    }
 
  
  /**
   * Simpler version of find all folders on a given location
   * @param where
   * @return 
   */
  public static ArrayList<File> findFolders(File where){
      return findFolders(where, 25);
  }
  
/**
 * Find all subfolders in a given folder.
 * @param where A file object of the start folder
 * @param maxDeep How deep is the crawl allowed to proceed
 * @return An array containing all the found files, returns null if none is
 * found
 */
 public static ArrayList<File> findFolders(File where, int maxDeep){

    File[] files = where.listFiles();
    ArrayList<File> result = new ArrayList<File>();

    if(files != null)
        for (File file : files) {

          if ( (file.isDirectory())
             &&( maxDeep-1 > 0 ) ){
              result.add(file);
              // do the recursive crawling
              ArrayList<File> temp = findFolders(file, maxDeep-1);

                    for(File thisFile : temp)
                            result.add(thisFile);
          }
        }
    return result;
  }

/**
 * Find all files and subfolders inside a given folder.
 * @param where A file object of the start folder
 * @param maxDeep How deep is the crawl allowed to proceed
 * @return An array containing all the found files, returns null if none is
 * found
 */
 public static ArrayList<File> findAll(File where, int maxDeep){

    File[] files = where.listFiles();
    ArrayList<File> result = new ArrayList<File>();

    if(files != null)
        for (File file : files) {
          if (file.isFile())
             result.add(file);
          else
          if ( (file.isDirectory())
             &&( maxDeep-1 > 0 ) ){
              result.add(file);
              // do the recursive crawling
              ArrayList<File> temp = findAll(file, maxDeep-1);

                    for(File thisFile : temp)
                            result.add(thisFile);
          }
        }
    return result;
  }

/**
 * Find all subfolders inside a given folder.
 * @param where A file object of the start folder
 * @return An array containing all the found files, returns null if none is
 * found
 */
 public static ArrayList<File> findSubFolders(File where){
// 2013-MAY-11 psc added
    File[] files = where.listFiles();
    ArrayList<File> result = new ArrayList<File>();

    if(files != null)
        for (File file : files) {
          if (file.isDirectory()) {
             result.add(file);
          }
        }
    return result;
  }


 
   


   /**
    * Ensures that we can pick on a value and present a readable
    * size of the file instead of plain bytes
    *
    * @param size
    * @return String
    */
   public static String humanReadableSize(Long size){

       long l = size;
       //Long.parseLong(size.trim());
       String output;

                long b;
                long TERABYTE = 1024L * 1024L * 1024L * 1024L;
                long GIGABYTE = 1024L * 1024L * 1024L;
                long MEGABYTE = 1024L * 1024L;
                long KILOBYTE = 1024;
                if (l > TERABYTE){
                    b = l / TERABYTE;
                    output = Long.toString(b)+" Tb";}
                else
                if (l > GIGABYTE){
                    b = l / GIGABYTE;
                    output = Long.toString(b)+" Gb";}
                else
                if (l > MEGABYTE){
                    b = l / MEGABYTE;
                    output = Long.toString(b)+" Mb";}
                else
                if (l > KILOBYTE){
                    b = l / KILOBYTE;
                    output = Long.toString(b)+" Kb";}
                else
                    output = size+" bytes";
    return output;
   }


   /** 
    * get the folder where our user documents are placed
    * @return  
    */
   public static synchronized String getDocumentsDirectory(){

       String result = "";

       java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            javax.swing.JFileChooser jFileChooser1 = new javax.swing.JFileChooser();
            String myresult = jFileChooser1.getFileSystemView().getDefaultDirectory()
                    .getAbsolutePath();
            }
        });
               
            return result;
   }

   /** 
    * get the desktop folder
    * @return  
    */
   public static synchronized String getDesktopDirectory(){
               javax.swing.JFileChooser jFileChooser1 = new javax.swing.JFileChooser();
            return jFileChooser1.getFileSystemView().getHomeDirectory()
                    .getAbsolutePath();
   }

   /** 
    * get our home folder (under windows this value is not certain
    * @return )
    */
   public static synchronized String getHomeDirectory(){
               javax.swing.JFileChooser jFileChooser1
                       = new javax.swing.JFileChooser();
            return jFileChooser1.getFileSystemView().getParentDirectory(
                   jFileChooser1.getFileSystemView().createFileObject
                   (getDesktopDirectory())
                   ).getAbsolutePath();
    }


   /** 
    * create a folder along with respective parent folders if needed
    * @param folder
    * @return  
    */
    public static Boolean mkdirs(String folder){
       boolean result;

        File docs = new File(folder);
        result = docs.mkdirs();

        return result;
    }

  /** 
    * create a folder along with respective parent folders if needed
    * @param docs
    * @return  
    */
   public static Boolean mkdirs(File docs){
       boolean result;
        result = docs.mkdirs();
        return result;
    }

   /**
    * Touch a file, if it does not exist then create an empty file.
     * @param folder
    * @param file
    * @return
    */
   public static boolean touch(File folder, String file){
       File touch = new File(folder, file);
        try {
            touch.createNewFile();
        } catch (IOException ex) {
            return false;
        }
       return true;
   }

    /**
    * Touch a file, if it does not exist then create an empty file.
    * @param file
    * @return
    */
   public static boolean touch(File file){
        try {
            file.createNewFile();
        } catch (IOException ex) {
            return false;
        }
       return true;
   }
   
   
   public static boolean SaveLargeStringToFile(File inputFile, 
           ArrayList<String[]> listLines){
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inputFile), "UTF-8"));
            for(String[] lines : listLines){
                for(String line : lines){
                    out.write(line + "\n");
                }
            }
            out.close();
            }
            catch (IOException e){
                System.out.println(e.getMessage());
                return false;
            }
        return true;
	}
   
    public static boolean SaveLargeStringToFile(File inputFile, String[] lines){
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inputFile), "UTF-8"));
            for(String line : lines){
                out.write(line + "\n");
            }
            out.close();
            }
            catch (IOException e){
                System.out.println(e.getMessage());
                return false;
            }
        return true;
	}
   
   
   /**
     * This method saves the contents from a string to a file
     *
     * @author Nuno Brito
     * @version 1.1
     * @param file     The file that we want to write
     * @param text   The string that will be written to the file
     * @return              True if operation has success, False otherwise
     * @date 2010/06/06
     * @modified 2014-07-11
    */
   public static boolean SaveStringToFile(final File file, 
           final String text){
       OutputStreamWriter fileWriter = null;
       BufferedWriter out = null;
        try {
            fileWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            out = new BufferedWriter(fileWriter);
            out.write(text);
            
            // now close everything
            fileWriter.flush();
            out.flush();
        
        }catch (IOException e){
                System.out.println(e.getMessage());
                return false;
            }
        finally{
           try {
               if(out!=null){
                    out.close();
               }
               if(fileWriter != null){
                fileWriter.close();
               }
            } catch (IOException ex) {
               Logger.getLogger(files.class.getName()).log(Level.SEVERE, null, ex);
           }
            // call the garbage collector, we often on the error:
            // Too many open files
            System.gc();
        
        }
        return true;
   }


   /**
   * Load a text file contents as a <code>String</code>.
   * This method does not perform encoding conversions
   *
   * @param file The input file
   * @return The file contents as a <code>String</code>
   * @exception IOException IO Error
   */
  public static String deserializeString(File file)
      // code copied from http://goo.gl/5qa3y
    throws IOException {
      int len;
      char[] chr = new char[4096];
      final StringBuffer buffer = new StringBuffer();
      final InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
      try {
          while ((len = reader.read(chr)) > 0) {
              buffer.append(chr, 0, len);
          }
      } finally {
          reader.close();
      }
      return buffer.toString();
  }

    /**
     * Returns the last line from a given text file
     * @param file  A file on disk 
     * @return The last line if available or an empty string if nothing
     * was found
     */
    public static String getLastLine(final File file){
        String result = "";
        BufferedReader reader;
        try {
            InputStreamReader fileReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(fileReader);
            String line = "";
            while (line != null) {
                line = reader.readLine();
                if(line != null){
                    result = line;
                }
                // we don't care about the content, just move to the last line
            }
            fileReader.close();
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(files.class.getName()).log(Level.SEVERE, null, ex);
        }
        // all done    
        return result;
    }
  
    /**
     * Returns the last line from a given text file. This method is particularly
     * well suited for very large text files that contain millions of text lines
     * since it will just seek the end of the text file and seek the last line
     * indicator. Please use only for large sized text files.
     * 
     * @param file A file on disk
     * @return The last line or an empty string if nothing was found
     * 
     * @author Nuno Brito
     * @author Michael Schierl
     * @throws java.lang.Exception
     * @license MIT
     * @date 2014-11-01
     */
    public static String getLastLineFast(final File file) throws Exception {
        // file needs to exist
        if (file.exists() == false || file.isDirectory()) {
                return "";
        }

        // avoid empty files
        if (file.length() <= 2) {
                return "";
        }

        // open the file for read-only mode
        RandomAccessFile fileAccess = new RandomAccessFile(file, "r");
        char breakLine = '\n';
        // offset of the current filesystem block - start with the last one
        long blockStart = (file.length() - 1) / 4096 * 4096;
        // hold the current block
        byte[] currentBlock = new byte[(int) (file.length() - blockStart)];
        // later (previously read) blocks
        List<byte[]> laterBlocks = new ArrayList<byte[]>();
        while (blockStart >= 0) {
            fileAccess.seek(blockStart);
            fileAccess.readFully(currentBlock);
            // ignore the last 2 bytes of the block if it is the first one
            int lengthToScan = currentBlock.length - (laterBlocks.isEmpty() ? 2 : 0);
            for (int i = lengthToScan - 1; i >= 0; i--) {
                if (currentBlock[i] == breakLine) {
                    // we found our end of line!
                    StringBuilder result = new StringBuilder();
                    // RandomAccessFile#readLine uses ISO-8859-1, therefore
                    // we do here too
                    result.append(new String(currentBlock, i + 1, currentBlock.length - (i + 1), "UTF-8"));
                    for (byte[] laterBlock : laterBlocks) {
                            result.append(new String(laterBlock, "UTF-8"));
                    }
                    // maybe we had a newline at end of file? Strip it.
                    if (result.charAt(result.length() - 1) == breakLine) {
                            // newline can be \r\n or \n, so check which one to strip
                            int newlineLength = result.charAt(result.length() - 2) == '\r' ? 2 : 1;
                            result.setLength(result.length() - newlineLength);
                    }
                    fileAccess.close();
                    return result.toString();
                }
            }
            // no end of line found - we need to read more
            laterBlocks.add(0, currentBlock);
            blockStart -= 4096;
            currentBlock = new byte[4096];
        }
        fileAccess.close();
        
        // oops, no line break found or some exception happened
        return "";
    }
    
    
    /**
     * Looks inside a text file to discover the line that contains a given
     * keyword.
     * @param file      A file on disk
     * @param keyword   A keyword that must be present on the file
     * @return          The full line that has the keyword. This method
     * only returns the first line that was found. If nothing was found, then it
     * returns empty as result.
     */
    public static String getTextLineEndingWithKeyword(final File file, 
            final String keyword){
        String result = "";
        BufferedReader reader;
        try {
            InputStreamReader fileReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(fileReader);
            String line = "";
            while (line != null) {
                if(line.endsWith(keyword)){
                    result = line;
                    break;
                }
                line = reader.readLine();
            }
            fileReader.close();
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(files.class.getName()).log(Level.SEVERE, null, ex);
        }
        // all done    
        return result;
    }
  
    /**
     * Writes a piece of text onto a specific file on disk. If you need
     * to add a line then you'll need to manually specify the "\n" separator.
     * @param file  The file that we want to write
     * @param text  The text that will be written
     */
    public static void addTextToFile(final File file, final String text){
      try {
          // the object that will write our file
          OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8");
          BufferedWriter writer = new BufferedWriter(fileWriter, 8192);
          // write the expected text
          writer.write(text);
          // now close things up
          writer.flush();
          writer.close();
          fileWriter.close();
      } catch (IOException ex) {
          Logger.getLogger(files.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    
    
    /**
     * Reduces or increases the size of a file as needed
     * @param file      The file to modify
     * @param newSize   The new size to be defined
     */
    public static void changeSize(final File file, final long newSize){
      try {
          // create a random access file object
          RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
          // trim the size as specified
          randomAccessFile.setLength(newSize);
          // close the file to prevent memory leaks 
          randomAccessFile.close();
      } catch (FileNotFoundException ex) {
          Logger.getLogger(files.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
          Logger.getLogger(files.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    /**
     * Reduces or increases the size of a file as needed
     * @param file      The file to modify
     * @param keyword   Cuts a given text file after a given keyword
     */
    public static void cutTextFileAfter(final File file, final String keyword){
      try {
          // create a random access file object
          RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
          // trim the size as specified
          String line = "";
          // read all the lines until we reach end of file
          while(line != null){
              // Houston, we have got a match
              if(line.contains(keyword)){
                  long currentPosition = 
                          randomAccessFile.getFilePointer() // current position
                          - line.length()   // remove the size of this line
                          - 2               // remove the line separator
                          ;
                  // now we cut the position of this file
                  randomAccessFile.setLength(currentPosition);
                  // all done let's get back.
                  break;
              }
              line = randomAccessFile.readLine();
          }
          
          // close the file to prevent memory leaks 
          randomAccessFile.close();
      } catch (FileNotFoundException ex) {
          Logger.getLogger(files.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
          Logger.getLogger(files.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    
    /**
     * On index files we have the need to write the coordinates of a given portion
     * of data within a file large binary files. This method helps to write these
     * coordinates always using the same size. Albeit taking up more disk space
     * due to the zeros not used, this speeds up processing considerably because
     * we don't have discover how big (or small) the coordinates are since they
     * have a fixed position to be written. On the current scheme we can index
     * up to 999 Terabytes worth of information per file.
     * @param value
     * @return 
     */
    public static String getPrettyFileSize(final long value){
        DecimalFormat formatted = new DecimalFormat("000000000000000");
        return formatted.format(value);
    }
    
    
    
    /**
     * Deletes all the files that are marked as hidden in our system.
     * @param where     The folder where we want to delete the hidden files
     * @param maxDeep   How deep in the sub-folder levels should we crawl?
     */
    public static void deleteHiddenFilesAndFolders(final File where, int maxDeep){
        // list the files on the current directory 
        File[] filesFound = where.listFiles();
        
        // no need to continue if nothing was found
        if(filesFound == null){
            return;
        }
        // go through each file
        for (File file : filesFound) {
            if (file.isFile()){
                if(file.getName().startsWith(".")==false){
                    continue;
                }
                // delete the file
                file.delete();
            }
            else
                if ( (file.isDirectory())
                        &&( maxDeep-1 > 0 ) ){
                    // is this folder hidden?
                    if(file.getName().startsWith(".")){
                        // delete it also
                        files.deleteDir(file);
                    }else
                        // do the recursive crawling
                        deleteHiddenFilesAndFolders(file, maxDeep-1);
                }
        }
    }
    
    /**
     * Provides the canonical path for a given file on disk without the
     * need to include the exception handling on the original code, making
     * it more readable.
     * @param file  A file on disk
     * @return      A string representing the canonical path
     */
    public static String getCanonical(final File file){
        try{
          return file.getCanonicalPath();
        }
        catch (Exception e){
            return null;
        }
    }
    
        
    public static File getCanonicalFile(final File file){
        try{
          return new File(file.getCanonicalPath());
        }
        catch (Exception e){
            // something went wrong, just give back the original file
            return file;
        }
    }
    

    /**
     * Removes the path portion of a file when relative to a specific folder
     * that it has as parent
     * @param sourceCodeFolder  A folder on disk
     * @param file A file inside a sub-folder of the sourceCodeFolder
     * @return A string representation of the canonical and relative path
     */
    public static String getRelativePath(final File sourceCodeFolder, final File file) {
      
        String result = null;
        
        try {
          
          String path = sourceCodeFolder.getCanonicalPath();
          String filePath = file.getCanonicalPath();
          
          result = filePath.replaceAll(path, "");
          
      } catch (IOException ex) {
          Logger.getLogger(files.class.getName()).log(Level.SEVERE, null, ex);
      }
        
        return result;
    }

    
       
    /**
     * Does a given folder contains at minimum a single file?
     * @param folder    The target folder
     * @param deep      How deep in the subfolder level do we want to look?
     * @return          True if it has one file (or more), false when it is empty
     */
    public static boolean sourceFolderHasAtLeastOneFile(final File folder, final int deep){
        File[] files = folder.listFiles();
        // iterate the entries
        for(File file: files){
            if(deep == 0){
                return false;
            }
            if(file.isFile()){
                return true;
            }else{
                // it is a folder
                boolean result = sourceFolderHasAtLeastOneFile(file, deep-1);
                if(result){
                    return true;
                }
            }
        }
        // no positive results up to this point? Mark as false then.
        return false;
    }
    
    
    /**
     * This method counts the number of lines inside a text files
     * @param file A file on disk
     * @return 
     * @throws java.io.FileNotFoundException
     */
    public static long countLines(final File file) throws Exception{
        long lineCounter = 0;
        
        // initialise the objects from where to read text
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader fileReader = new InputStreamReader(fileInputStream, "UTF-8");
        BufferedReader reader = new BufferedReader(fileReader);
        
        // count the lines to infinitum
        while(reader.readLine() != null){
            lineCounter++;
        }
        
        // close the streams
        reader.close();
        fileReader.close();
        fileInputStream.close();
        
        return lineCounter;
    }
    
    
    /**
     * Copy one file to another location
     * Based on snippet from: http://stackoverflow.com/a/19974236/1120206
     * Copyright (c) 2013 user1079877 (http://stackoverflow.com/users/1079877/user1079877)
     * @param f1 input file
     * @param f2 output file
     * @return True if copied, False if something went wrong
     */
    public static boolean copy(final File f1, final File f2){
    
      try {
          f2.createNewFile();
          
          final RandomAccessFile file1 = new RandomAccessFile(f1, "r");
          final RandomAccessFile file2 = new RandomAccessFile(f2, "rw");
          
          file2.getChannel().write(file1.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, f1.length()));
          
          file1.close();
          file2.close();
          
      } catch (IOException ex) {
          return false;
      }
      // all ok
      return true;
    }
    
    /**
     * Delete the annoying DS_Store files
     * @param folder 
     * @param showMessage Enable or disable a text message for each
     * file that was deleted.
     */
    public static void deleteDsStoreFiles(File folder, boolean showMessage) {
        if(folder.exists() == false || folder.isFile()){
            return;
        }
        ArrayList<File> files = utils.files
                .findFilesFiltered(folder, ".DS_Store", 25);
        for(File file : files){
            file.delete();
            if(showMessage){
                System.out.println("Deleted: " + file.getPath());
            }
        }
    }

    /**
     * Reads a portion of text from a file
     * @param file
     * @param lineStart Where we start reading
     * @param lineEnd   Where we stop reading
     * @return null when something goes wrong
     */
    public static String readFileBetweenLines(File file, int lineStart, int lineEnd) {
        try {
            // make sure that the file is existing
            if(file == null || file.exists() == false || file.isDirectory()){
                return null;
            }
            if(lineStart >= lineEnd){
                return null;
            }
            FileReadLines fileReader = new FileReadLines(file);
            String output = "";
            StringBuilder line;
            int counter = 0;
            // look for the lines we want
            while((line = fileReader.getNextLine()) != null){
                counter++;
                if(counter >= lineStart && counter <= lineEnd){
                    output = output.concat(line.toString()).concat("\n");
                }
                // no need to keep reading
                if(counter > lineEnd){
                    break;
                }
            }
            fileReader.close();
            return output;
        } catch (IOException ex) {
            Logger.getLogger(files.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
