package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerThread extends Thread{
    private ServerSocket ServSocket;
    private LogHandle servlog;
    private static boolean isAppRunning;

    ServerThread(){
        servlog = new LogHandle();
        try{
            isAppRunning =true;
            ServSocket = new ServerSocket(8080);
            ServSocket.setSoTimeout(60000);
            this.start();
        }catch (IOException ioe){
            Controller.Print(ioe.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            Controller.Print("Starting server\n");

            while (true) {
                Socket Client = null;
                Client = ServSocket.accept();
                Controller.Print(Client.getInetAddress() + " accepted \n");
                new ClientThread(Client, servlog);
            }
        }catch (SocketTimeoutException ste){
            if(isAppRunning){
                Controller.Print("Timeout reached... Restarting");
                this.start();
            }else {
                try{
                    ServSocket.close();
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }catch (IOException ioe){
            Controller.Print(ioe.getMessage() + "\n");
        }
    }
    synchronized static void setAppRunning(boolean iar){
        isAppRunning = iar;
    }
}
