import java.io.Serializable;

public class Message implements Serializable{
    private String msg;
    private String from;
    private boolean amClosing = false;
    public Message(Boolean x){
        amClosing = x;
    }
    public boolean amClosing(){
        return amClosing;
    }
    public Message(String msg,String from){
        this.msg = msg;
        this.from = from;
    }
    public String toString(){
        return msg;
    }

    public String getFrom() {
        return from;
    }
}
