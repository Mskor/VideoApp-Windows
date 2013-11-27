package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{
    private ServerSocket serverSocket;

    ServerThread(){
        try{
            this.setDaemon(true);
            serverSocket = new ServerSocket(8080);
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
                Client = serverSocket.accept();
                Controller.Print(Client.getInetAddress() + " accepted \n");
                new ClientThread(Client);
            }
        }catch (IOException ioe){
            Controller.Print(ioe.getMessage() + "\n");
        }
    }
}
