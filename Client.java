import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    public Client(String ip, int port){
        try {
            socket = new Socket(ip, port);
            System.out.println("connected");
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Done.");
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Connected to Server.");
    }

    public Socket getSocket() {
        return socket;
    }

    public Message getMessage(){
        try {
            while(true) {
                Message x = (Message) in.readObject();
                System.out.println("got msg");
                return x;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void sendMessage(Boolean x){
        Message msg = new Message(x);
        try {
            out.writeObject(msg);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void sendMessage(String msg, String name){
        Message x = new Message(msg,name);
        try {
            out.writeObject(x);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
