import java.rmi.*;
import java.util.ArrayList;
public interface CNDSFirstParticipant extends Remote{
	ArrayList<CNDSParticipant> registerParicipant(CNDSParticipant participant) throws RemoteException;
}
