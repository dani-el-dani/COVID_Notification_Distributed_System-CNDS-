import java.rmi.*;
import java.util.ArrayList;
public interface CNDSParticipant extends Remote{
	void retriveNotification(Message message)throws RemoteException;
	void updateList(CNDSParticipant participant) throws RemoteException;
	void setName(int name) throws RemoteException;
	void setVectorClock(ArrayList<Integer> vectorClock) throws RemoteException;
	void setIsSick(ArrayList<Boolean> isSick) throws RemoteException;
	void setMessageQueue(ArrayList<Message> messageQueue) throws RemoteException;
	
}
