import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class ContactList {
    private List<Contact> contacts;
    Lock l = new ReentrantLock();

    public ContactList() {
        contacts = new ArrayList<>();

        contacts.add(new Contact("John", 20, 253123321, null, new ArrayList<>(Arrays.asList("john@mail.com"))));
        contacts.add(new Contact("Alice", 30, 253987654, "CompanyInc.", new ArrayList<>(Arrays.asList("alice.personal@mail.com", "alice.business@mail.com"))));
        contacts.add(new Contact("Bob", 40, 253123456, "Comp.Ld", new ArrayList<>(Arrays.asList("bob@mail.com", "bob.work@mail.com"))));
    }

    // @TODO
    public boolean addContact (DataInputStream in) throws IOException {
        Contact newContact = Contact.deserialize(in);
        System.out.println("Recebi um contacto:\n" + newContact.toString());
        l.lock();
        try{
            contacts.add(newContact);
        } finally {
            l.unlock();
        }
        return true;
    }

    // @TODO
    public List<Contact> getContacts () throws IOException {
        l.lock();
        try{
            return new ArrayList<>(contacts);
        } finally {
            l.unlock();
        }
    }

}

class ServerWorker implements Runnable {
    private final Socket socket;
    private final ContactList contactList;

    public ServerWorker (Socket socket, ContactList contactList) {
        this.socket = socket;
        this.contactList = contactList;
    }

    // @TODO
    @Override
    public void run(){
        try(socket){ // try with resource, se o socket tiver de fechar fecha automaticamente
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            for(;;){
                contactList.addContact(in);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}


public class Server {

    public static void main (String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        ContactList contactList = new ContactList();

        while (true) {
            Socket socket = serverSocket.accept();
            Thread worker = new Thread(new ServerWorker(socket, contactList));
            worker.start();
        }
    }

}
