/*
 * Some handy routines to help on everyday tasks
 */

package old.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Nuno Brito, 20th of March 2011 at Germany
 */
public class time {

    /**
     * Pauses the current thread for a while
     * @param time_to_wait in seconds
     */
    public static void wait(int time_to_wait){

    try { 
        
        Thread.sleep(time_to_wait * 1000);
    }
    catch (InterruptedException ex) {
            }

    }


    /**
     * Pauses the current thread for a while
     * @param time_to_wait in Milliseconds
     */
    public static void waitMs(long time_to_wait){

    try { 
        
        Thread.sleep(time_to_wait);
    }
    catch (InterruptedException ex) {
            }

    }

    
    
    /**
     * Converts a given quantity of time into a human readable format
     * 
     * @param ms Number of miliseconds to quantify
     * @return A human readable result
     */
    public static String timeNumberToHumanReadable(long ms){

        String time;
        long seconds;
        long minutes;
        long hours;
        long days;


        seconds = ms / 1000;
        minutes = seconds / 60;
        seconds %= 60;
        hours = minutes / 60;
        minutes %= 60;
        days = hours / 24;
        hours %= 24;

        time = "";

        if(days >0) time=days
                +(days == 1 ? " day" : " days")+", ";
        if(hours >0) time=time+hours
                +(hours == 1 ? " hour" : " hours")+", ";
        if(minutes >0) time=time+minutes
                +(minutes == 1 ? " minute" : " minutes")+" and ";
        time=time+seconds
                +(seconds == 1 ? " second" : " seconds");

        return time;
                }

    /**
     * Converts a given date to a unique number
     * @return 
     */
   public static long textDateToMilliseconds(int yearTo, int monthTo, int dayTo){
       return textDateToMilliseconds(""+yearTo, ""+monthTo, ""+dayTo);
   } 
    
   /** Convert from a normal date to correct milliseconds representation */ 
   public static long textDateToMilliseconds(String yearTo, String monthTo, String dayTo){
       
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(
//                    Integer.parseInt(yearTo), 
//                    Integer.parseInt(monthTo), 
//                    Integer.parseInt(dayTo)
//                    );
//             return calendar.getTimeInMillis();   
       
       DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
             Date date;
            try {
                date = dateFormat.parse(
//                          yearTo + "-" + dayTo + "-" + monthTo
                          yearTo + "-" + monthTo + "-" + dayTo
                        );
                return date.getTime();
            } catch (ParseException ex) {
                return -1;
            }
   } 
   
   
   /** Convert from a normal date to correct milliseconds representation */ 
   public static Date getDate(int yearTo, int monthTo, int dayTo){
       DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
       Date date;
            try {
                date = dateFormat.parse(
                          yearTo + "-" + monthTo + "-" + dayTo
                        );
                return date;
            } catch (ParseException ex) {
                return null;
            }
   } 


    /** get the current time in a human readable manner */
   public static String getDateTime() {
       // code adapted from http://goo.gl/rZ716
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
        }
    /** get the current time in a human readable manner */

   public static String getTimeFromLong(long time) {
       // code adapted from http://goo.gl/rZ716
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //Date date = new Date();
        return dateFormat.format(time);
        }

   public static String getTimeFromLongNoDate(long time) {
       // code adapted from http://goo.gl/rZ716
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        //Date date = new Date();
        return dateFormat.format(time);
        }
   

   /** get the current time in a human readable manner */
   public static String getCurrentYear() {
       // code adapted from http://goo.gl/rZ716
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return dateFormat.format(date);
        }

    /** 
     * get the current time in a human readable manner
     * @return 
     */
   public static String getDateTimeISO() {
       // code adapted from http://goo.gl/rZ716
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
   }
   
   /** 
    * Returns the age of a given file using days as value
    * A result of "1" means today, "2" means more than 24 hours and so forth
    */
   public static int getFileAge(File file){
       long fileAge = System.currentTimeMillis() - file.lastModified();
       DateFormat dateFormat = new SimpleDateFormat("dd");
       int dateValue = Integer.parseInt(dateFormat.format(fileAge));
       return dateValue;
   }
   
    /**
     * Get the current time and date in SPDX format
     * @return the properly formatted SPDX time format
     */
    public static String getDateSPDX() {
       // do the time calculation such as 2012-09-03T13:32:12Z
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String textDate = dateFormat.format(date);
        //TODO for some reason "T" and "Z" are not accepted as parameters
        return textDate.replace(" ", "T") + "Z";
    }
    
    /**
     * Get the current time and date in SPDX format
     * @return the properly formatted SPDX time format
     */
    public static String getDate() {
       // do the time calculation such as 2012-09-03T13:32:12Z
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String textDate = dateFormat.format(date);
        //TODO for some reason "T" and "Z" are not accepted as parameters
        return textDate;
    }
    
    
     /**
     * Get the current time and date in SPDX format from a file
     * @param file the file from where we want the date
     * @return the properly formatted SPDX time format
     */
    public static String getFileDateSPDX(File file) {
       // do the time calculation such as 2012-09-03T13:32:12Z
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(file.lastModified());
        String textDate = dateFormat.format(date);
        //TODO for some reason "T" and "Z" are not accepted as parameters
        return textDate.replace(" ", "T") + "Z";
    }
    
    
   
}
