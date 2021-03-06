import java.io.Serializable;
import java.util.ArrayList;
public class Message implements Serializable{
    private int sender;
    private ArrayList<Integer> timestamp;
    private String notification;
    
    public int getSender() {
        return sender;
    }
    public void setSender(int sender) {
        this.sender = sender;
    }
    public ArrayList<Integer> getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(ArrayList<Integer> timestamp) {
        this.timestamp = timestamp;
    }
    public String getNotification() {
        return notification;
    }
    public void setNotification(String notification) {
        this.notification = notification;
    }
    
    
}
