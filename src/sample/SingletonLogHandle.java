package sample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * A custom class for simple logging.
 *
 * It creates a *.txt file in root folder of the program.
 * This logfile contains the time mark of the beginning and of the end of all sessions.
 * It also leaves notes about every file downloaded,
 * including address of client which uploaded the file, the name this file was
 * auto-given at the end of process of download and a time mark of the end of this download.
 *
 * @author Yakovlev Oleg
 * @version 0.4
 * @since 0.3
 */
public class SingletonLogHandle {

    /**
     * Currently hardcoded
     */
    private File LogFilePath = new File( "./servlog.txt");

    /**
     * Marks last video for internal usage.
     * @see Controller#OpenLastFile()
     */
    private File LastVideoDownloaded = null;

    public File getLastVideoDownloaded() {
        return LastVideoDownloaded;
    }

    public void setLastVideoDownloaded(File lastVideoDownloaded) {
        LastVideoDownloaded = lastVideoDownloaded;
    }

    private static SingletonLogHandle Instance = null;

    /**
     * For the implementation of singleton pattern
     */
    public static SingletonLogHandle GetInstance(){
        if(Instance == null){
            Instance = new SingletonLogHandle();
        }
        return Instance;
    }

    /**
     * Checks out if the log file not already exists.
     * If it's so constructor creates the file.
     * It also leaves the mark that new session has begun.
     */
    protected SingletonLogHandle(){
        if(!LogFilePath.exists()){
            try{
                LogFilePath.createNewFile();
            }catch (IOException ioe){
                Controller.Print("Error while creating log file\n");
            }
        }
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(LogFilePath, true));
            long CurTime = System.currentTimeMillis();
            bufferedWriter.write(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(CurTime) + " session begun");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.close();
        }catch (IOException ioe){
            Controller.Print("Error while writing log file\n");
        }
    }

    /**
     * Leaves a mark in the logfile.
     * @param instance class instance to be logged,
     * the actual message is defined by overridden {@link sample.Loggable#GetLogEntry()} method
     * @see Loggable
     */
    synchronized void ClientThreadReport(Loggable instance){
        try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(LogFilePath, true));
                long CurTime = System.currentTimeMillis();
                bufferedWriter.write(new SimpleDateFormat("HH:mm :").format(CurTime) + instance.GetLogEntry());
                Controller.LogPrint(instance.GetLogEntry());
                bufferedWriter.newLine();
                bufferedWriter.close();
        }catch (IOException ioe){
                Controller.Print("Error while reporting log file\n");
        }
    }

    /**
     * Leaves a time mark in the logfile about an end of this session
     */
    void EndSession() {
            try{
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(LogFilePath, true));
                long CurTime = System.currentTimeMillis();
                bufferedWriter.newLine();
                bufferedWriter.write(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(CurTime) + " session ends");
                bufferedWriter.newLine();
                bufferedWriter.write("*********************************************************");
                bufferedWriter.newLine();
                bufferedWriter.close();
            }catch (IOException ioe){
                Controller.Print("Error while writing log file\n");
            }
    }
}
