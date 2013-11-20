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
public class LogHandle {

    /**
     * Customizable file logfile path.
     * Default value is "./servlog.txt"
     */
    public static File LogFilePath = new File( "./servlog.txt");
    /**
     * Marks last video for internal usage.
     * @see Controller#OpenLastFile()
     */
    static File LastVideoDownloaded = null;
    private Object logsync = new Object();
    private long CurTime;

    /**
     * Checks out if the log file not already exists.
     * If it's so constructor creates the file.
     * It also leaves the mark that new session has begun.
     */
    LogHandle(){
    synchronized (logsync){
        if(!LogFilePath.exists()){
            try{
                LogFilePath.createNewFile();
            }catch (IOException ioe){
                Controller.Print("Error while creating log file\n");
            }
        }
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(LogFilePath, true));
            CurTime = System.currentTimeMillis();
            bufferedWriter.write(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(CurTime) + " session begun");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.close();
        }catch (IOException ioe){
            Controller.Print("Error while writing log file\n");
        }
    }
    }

    static synchronized void setLastVideoDownloaded(File newFile){
        LastVideoDownloaded = newFile;
    }

    /**
     * Leaves a mark in the logfile.
     *
     * Mark goes in the form:
     * "Client <b><u>internet address  of client</u></b> successfully uploaded file named
     * <b><u>name that was assigned to downloaded file</u></b>"
     * @param clientThread An instance of {@link ClientThread}
     *                     that has to report info
     *                     about its download     */
    void ClientThreadReport(ClientThread clientThread){
        synchronized (logsync){
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(LogFilePath, true));
                bufferedWriter.write(clientThread.socket.getInetAddress()
                        + " uploaded "
                        + clientThread.HCodeName.getName());
                Controller.LogPrint(new SimpleDateFormat("HH:mm :").format(CurTime) +
                        clientThread.socket.getInetAddress()
                        + " uploaded "
                        + clientThread.HCodeName.getName() + "\n");
                bufferedWriter.newLine();
                bufferedWriter.close();
            }catch (IOException ioe){
                Controller.Print("Error while reporting log file\n");
            }
        }
    }

    /**
     * Leaves a time mark in the logfile about an end of this session
     */
    void EndSession() {
        synchronized (logsync){
            try{
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(LogFilePath, true));
                CurTime = System.currentTimeMillis();
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
}
