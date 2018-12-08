import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Server{
    ServerSocket server;
    LinkedList<ServerClient> clients = new LinkedList<>();
    private boolean running = true;
    public void startServer(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter port");
        int port = scanner.nextInt();
        try {
            server = new ServerSocket(port);
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Server is up and waiting...");

        Runnable run2 = new Runnable() {
            @Override
            public void run() {
                try {
                    while(running) {
                        System.out.println("Waiting for new Client");
                        Socket socket = server.accept();
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        ServerClient sc = new ServerClient(socket, in, out);
                        clients.push(sc);
                        sc.startThread(clients);
                        System.out.println("Connected Client");
                    }
                }catch(Exception e){
                    stopAll();
                    e.printStackTrace();
                }
            }
        };
        Thread t1 = new Thread(run2);
        t1.start();

    }
    public void stopAll(){
        running  = false;
        try {
        for(int j = 0; j < clients.size(); j++) {
                clients.get(j).stopRunning();
                clients.get(j).getSocket().close();
        }
        server.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main (String[] args){
        new Server().startServer();
    }
}
