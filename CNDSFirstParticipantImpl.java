import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
public class CNDSFirstParticipantImpl extends UnicastRemoteObject implements CNDSFirstParticipant, CNDSParticipant{
	private ArrayList<CNDSParticipant> participants;
	public ArrayList<Boolean> isSick;
	public ArrayList<Message> messageQueue;
	public ArrayList<Integer> vectorClock;
	private int name;
	private int s = 0;
	private boolean isDead = false; 
	protected CNDSFirstParticipantImpl() throws RemoteException{
		setName(s);
		participants = new ArrayList<CNDSParticipant>();
		setVectorClock(new ArrayList<Integer>());
		setIsSick(new ArrayList<Boolean>());
		setMessageQueue(new ArrayList<Message>());
		participants.add(this);
	}
	public void setName(int name) throws RemoteException{
		this.name = name;
	}
	public void setIsSick(ArrayList<Boolean> isSick) throws RemoteException{
		this.isSick = isSick;
		this.isSick.add(false);
	}
	public void setMessageQueue(ArrayList<Message> messageQueue) throws RemoteException{
		this.messageQueue = messageQueue;
	}
	public void setVectorClock(ArrayList<Integer> vectorClock)throws RemoteException{
		this.vectorClock = vectorClock;
		this.vectorClock.add(0);
	}
	public void retriveNotification(Message message) throws RemoteException {
			if(message.getSender() != name)
				messageQueue.add(message);
	}
	public ArrayList<CNDSParticipant> registerParicipant(CNDSParticipant participant) throws RemoteException{
		updateList(participant);
		int i = 1;		
		while (i < participants.size() - 1){
			try{
				participants.get(i).updateList(participant);
				i++;
			}
			catch(RemoteException e){
				participants.remove(i);
			}
			
		}
		participant.setName(++s);
		participant.setVectorClock(this.vectorClock);
		participant.setIsSick(this.isSick);
		participant.setMessageQueue(this.messageQueue);		
		return participants;
	}
	public void updateList(CNDSParticipant participant) throws RemoteException {
		this.participants.add(participant);
		this.vectorClock.add(0);
		this.isSick.add(false);
	}
	public void chat() throws Exception{
		Message message = new Message();
		int fever;
		int cough;
		int contact;
		boolean stillSick = false;
		while(true){
			if(!isDead){
				if(!stillSick){
					fever = (int)(10*Math.random());
					cough = (int)(10*Math.random());
					contact = (int)(isSick.size()*Math.random());
					if(amISick(fever,cough,contact)){
						stillSick = true;
						vectorClock.set(name,vectorClock.get(name)+1);
						message.setSender(name);
						message.setNotification("i am sick");
						message.setTimestamp(vectorClock);
						int i = 0;
						while (i < participants.size()){
							try{
								participants.get(i).retriveNotification(message);
								Thread.sleep((int)(500*Math.random()));
								i++;
							}
							catch(RemoteException e){
								participants.remove(i);
							}
			
						}
					}
				}
				else{
					int random = (int)(10*Math.random());
					if(random > 8){
						isDead = true;
						System.out.println("just died");
						vectorClock.set(name,vectorClock.get(name)+1);
						message.setSender(name);
						message.setNotification("just died");
						message.setTimestamp(vectorClock);
						int i = 0;
						while (i < participants.size()){
							try{
								participants.get(i).retriveNotification(message);
								Thread.sleep((int)(500*Math.random()));
								i++;
							}
							catch(RemoteException e){
								participants.remove(i);
							}
			
						}
					}
					else if(random < 4){
						stillSick = false;
						vectorClock.set(name,vectorClock.get(name)+1);
						message.setSender(name);
						message.setNotification("i am fine ");
						message.setTimestamp(vectorClock);
						int i = 0;
						while (i < participants.size()){
							try{
								participants.get(i).retriveNotification(message);
								Thread.sleep((int)(500*Math.random()));
								i++;
							}
							catch(RemoteException e){
								participants.remove(i);
							}
			
						}
					}
					else
						stillSick = true;
				}
			}
			int j = 0;
			while(j < messageQueue.size()){
				if(delay(messageQueue.get(j))){
					j++;
				}
				else{
					System.out.println(messageQueue.get(j).getSender()+" says "+messageQueue.get(j).getNotification());
					vectorClock.set(messageQueue.get(j).getSender(),vectorClock.get(messageQueue.get(j).getSender())+1);
					if(messageQueue.get(j).getNotification().equals("i am sick"))
						isSick.set(messageQueue.get(j).getSender(),true);
					else
						isSick.set(messageQueue.get(j).getSender(),false);
					messageQueue.remove(j);
				}
			}
			Thread.sleep(500);
		}
		
	}
	public boolean delay(Message message){
		for(int i = 0; i < message.getTimestamp().size(); i++){
			if(i == message.getSender()){
				if(message.getTimestamp().get(i) != vectorClock.get(i) + 1)
					return true;
			}
			else{
				if(message.getTimestamp().get(i) > vectorClock.get(i))
					return true;
			}
		}
		return false;
	}
	public boolean amISick(int fever, int cough, int contact){
		if(fever > 8){
			return true;
		}
		else if(fever > 6 && cough > 8){
			return true;
		}
		else if(cough > 6 && isSick.get(contact)){
			System.out.println("from contact with"+contact);
			return true;
		}
		else{
			return false;
		}
	} 
	
}
