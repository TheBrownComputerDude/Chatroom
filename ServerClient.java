import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class ServerClient extends Thread{
    private Socket socket;
    private ObjectInputStream input = null;
    private ObjectOutputStream out = null;
    private ServerClient other = null;
    private boolean running = true;
    private LinkedList<ServerClient> list;

    public ServerClient(Socket socket, ObjectInputStream input, ObjectOutputStream out){

        this.socket = socket;
        this.input = input;
        this.out = out;
    }
    public void startThread(LinkedList<ServerClient> list){
        this.list = list;
        //this.other = other;
        this.start();
    }
    public void stopRunning(){
        running = false;
    }
    public void run() {
        while (running) {
            try {
                Message x = (Message) getInput().readObject();
                if(x.amClosing()){
                    list.remove(this);
                    break;
                }
                for(int i = 0; i < list.size(); i ++){
                    if(!list.get(i).equals(this)){
                        list.get(i).getOut().writeObject(x);
                    }
                }
                //other.getOut().writeObject(x);
            } catch (Exception e) {
                stopRunning();
                System.out.println("stop");
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public ObjectOutputStream getOut() {
        return out;
    }
}
