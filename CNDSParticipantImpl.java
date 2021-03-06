import java.rmi.*;
import java.rmi.server.*;
import java.util.Scanner;
import java.util.ArrayList;
public class CNDSParticipantImpl extends UnicastRemoteObject implements CNDSParticipant{
	public ArrayList<CNDSParticipant> participants;
	private ArrayList<Boolean> isSick;
	private ArrayList<Integer> vectorClock;
	private ArrayList<Message> messageQueue;
	private CNDSFirstParticipant firstParticipant;
	private int name = 0;
	protected CNDSParticipantImpl(CNDSFirstParticipant firstParticipant) throws RemoteException{
		this.firstParticipant = firstParticipant;
		messageQueue = new ArrayList<Message>();
		vectorClock = new ArrayList<Integer>(); 
		isSick = new ArrayList<Boolean>();
	}
	public void setName(int name) throws RemoteException{
		this.name = name;
	}
	public void setVectorClock(ArrayList<Integer> vectorClock)throws RemoteException{
		this.vectorClock = vectorClock;
	}
	public void setIsSick(ArrayList<Boolean> isSick) throws RemoteException{
		this.isSick = isSick;
	}
	public void setMessageQueue(ArrayList<Message> messageQueue) throws RemoteException{
		this.messageQueue = messageQueue;
	} 
	public void retriveNotification(Message message) throws RemoteException {
		if(message.getSender() != name)
			messageQueue.add(message);
	}
	public void updateList(CNDSParticipant participant) throws RemoteException {
		this.participants.add(participant);
		this.vectorClock.add(0);
		this.isSick.add(false);
		System.out.println("some one joined");
	}
	public void chat() throws Exception{
		Message message = new Message();
		int fever;
		int cough;
		int contact;
		boolean stillSick = false;
		while(true){
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
					break;
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
		System.exit(0);
	}
	//function to check messages an be delivered right away or the should be delayed!!
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
