import java.rmi.*;
public class FirstParticipant{
	public static void main(String[] args) throws Exception{
		CNDSFirstParticipantImpl firstParticipant = new CNDSFirstParticipantImpl();
		Naming.rebind("firstParticipant", firstParticipant);
		System.out.println("Server up and runing.....");
		firstParticipant.chat();
	}
}
