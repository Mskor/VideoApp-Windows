package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{
    private ServerSocket ServSocket;
    private LogHandle servlog;

    ServerThread(){
        servlog = new LogHandle();
        try{
            this.setDaemon(true);
            ServSocket = new ServerSocket(8080);
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
        }catch (IOException ioe){
            Controller.Print(ioe.getMessage() + "\n");
        }
    }
}
