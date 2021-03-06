import java.rmi.*;
public class Participant{
	public static void main(String[] args) throws Exception {
		String url = "rmi://localhost/firstParticipant";
		CNDSFirstParticipant chatServer = (CNDSFirstParticipant) Naming.lookup(url);
		CNDSParticipantImpl participant = new CNDSParticipantImpl(chatServer);
		participant.participants = chatServer.registerParicipant(participant);
		participant.chat(); 
	}
}
