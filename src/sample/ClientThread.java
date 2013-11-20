package sample;

import java.io.*;
import java.net.Socket;

/**
 * Handles file transfer from any client in a separate thread.
 * For a new client should be created a new instance of this class.
 * All critical sections are synchronized appropriately.
 * If the app will end its work in unusual way, the data already been accepted could be found in
 * a file with *.out extension named uniquely for each client instance
 * (it uses Thread ID to name a temporary file).
 *
 * Uses {@link LogHandle} type to write down information about downloading
 *
 * @author Yakovlev Oleg
 * @since 0.2
 * @version 0.4
 */
public class ClientThread extends Thread{
    /**
     * Client socket variable.
     * It is obtained via constructor.
     */
    public final Socket socket;

    /**
     * An instance of {@link LogHandle} which will do the logs
     * for the process of download.
     */
    private final LogHandle logHandle;

    /**
     * Name that is assigned to the downloaded file
     * right after the process of download is ended.
     *
     * It is assigned a <b>hash-code</b> of last packet
     * of this file to ensure that the name will be unique
     * for each file.
     */
    public File HCodeName;

    /**
     * A folder where all downloaded files are accumulated.
     * Thread gets it directly from the UI, invoking {@link sample.Controller#GetDir()}
     */
    private final File DirectoryToWrite;

    /**
     * Constructor starts the downloading process according to the arguments it gets.
     * @param Client Defines the connection between server and client <b>this</b> exact thread handles
     * @param lh Defines the instance of {@link LogHandle} to do the logging
     */
    ClientThread(Socket Client, LogHandle lh){
        this.socket = Client;
        this.logHandle = lh;
        this.DirectoryToWrite = new File(Controller.GetDir());
        this.start();
    }

    /**
     * Defines the actual process of download.
     *
     * It ensures that file is successfully downloaded
     * and applied a unique name.
     *
     * This name has the form of return-value of {@link Object#hashCode()}
     * All the files can be renamed manually by the though.
     * The purpose of this temporary name is to ensure
     * that there is no name conflict
     */
    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            FileMetadata fileMetadata = (FileMetadata)objectInputStream.readObject();
            File Temp = new File(DirectoryToWrite, this.getId() + ".out");
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Temp));

            int in;
            byte[] byteArray = new byte[8192];
            while ((in = bis.read(byteArray)) != -1) {
                bos.write(byteArray, 0, in);
            }
            Controller.Print(socket.getInetAddress() + "Finished \n");

            bos.close();
            bis.close();
            objectInputStream.close();
            HCodeName = new File(DirectoryToWrite, byteArray.hashCode() + "." + fileMetadata.Extension);
            if(!(Temp.renameTo(HCodeName))){
                Controller.Print("Error, renaming file, named " + Temp.getPath());
            }

            LogHandle.setLastVideoDownloaded(Temp);

            logHandle.ClientThreadReport(this);
        }catch (Exception e){
            Controller.Print(socket.getInetAddress() + " has the following error " + e.getMessage());
        }
    }
}
