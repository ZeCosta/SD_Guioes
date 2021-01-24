import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;


class ContactList {
	private Lock l=new ReentrantLock();
    private List<Contact> contacts;

    public ContactList() {
        contacts = new ArrayList<>();

        contacts.add(new Contact("John", 20, 253123321, null, new ArrayList<>(Arrays.asList("john@mail.com"))));
        contacts.add(new Contact("Alice", 30, 253987654, "CompanyInc.", new ArrayList<>(Arrays.asList("alice.personal@mail.com", "alice.business@mail.com"))));
        contacts.add(new Contact("Bob", 40, 253123456, "Comp.Ld", new ArrayList<>(Arrays.asList("bob@mail.com", "bob.work@mail.com"))));
    }

    // @TODO
    public boolean addContact (DataInputStream in) throws IOException {
        Contact c = Contact.deserialize(in);
        l.lock();
        try{
        	contacts.add(c);
        	System.out.println(c.toString());
        }finally{
        	l.unlock();
        }
        return true;
    }

    // @TODO
    //public void getContacts (...) throws IOException { }
    
}

class ServerWorker implements Runnable {
    private final Socket socket;
    private final ContactList contactList;

    public ServerWorker (Socket socket, ContactList contactList) {
        this.socket = socket;
        this.contactList=contactList;
    }

    // @TODO
    @Override
    public void run() {
    	try{
	        DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	    	for(;;){
	    		contactList.addContact(in);
	    	}
	    	
    	}catch(IOException e){
    		System.out.println("aaac");
    	}finally{
    		try{socket.close();}catch(IOException e){}
    	}
    }
}


class Thread1 implements Runnable {
    private final ContactList contactList;

    public Thread1 (ContactList contactList) {
        this.contactList=contactList;
    }
	
    public void run (){
    	try{
        	ServerSocket serverSocket = new ServerSocket(12345);

	        while (true) {
	            Socket socket = serverSocket.accept();
	            Thread worker = new Thread(new ServerWorker(socket, this.contactList));
	            worker.start();
	        }
        }catch(IOException e){
        	System.out.println("erro");
        }
    }

}
class Thread2 implements Runnable {
    private final ContactList contactList;

    public Thread2 (ContactList contactList) {
        this.contactList=contactList;
    }
	
    public void run (){
    	try{
	        ServerSocket serverSocket = new ServerSocket(12346);

	        while (true) {
	            Socket socket = serverSocket.accept();
	            Thread worker = new Thread(new ServerWorker(socket, this.contactList));
	            worker.start();
	        }
        }catch(IOException e){
        	System.out.println("erro");
        }
    }

}

public class Server {
	
    public static void main (String[] args) throws IOException {
        ContactList contactList = new ContactList();


        Thread thread1 = new Thread(new Thread1(contactList));
        thread1.start();

        Thread thread2 = new Thread(new Thread2(contactList));
        thread2.start();
    }

}
