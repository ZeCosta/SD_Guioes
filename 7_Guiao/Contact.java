import java.util.*;
import java.io.*;

class Contact {
    private String name;
    private int age;
    private long phoneNumber;
    private String company;     // Pode ser null
    private List<String> emails;

    public Contact (String name, int age, long phone_number, String company, List<String> emails) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phone_number;
        this.company = company;
        this.emails = new ArrayList<>(emails);
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name).append(";");
        builder.append(this.age).append(";");
        builder.append(this.phoneNumber).append(";");
        builder.append(this.company).append(";");
        builder.append("{");
        for (String s : this.emails) {
            builder.append(s).append(";");
        }
        builder.append("}");
        return builder.toString();
    }

    public void serialize(DataOutputStream out)throws IOException{
    	out.writeUTF(this.name);
    	out.writeInt(this.age);
    	out.writeLong(this.phoneNumber);
    	if (this.company!=null){
    		out.writeBoolean(true);
    		out.writeUTF(this.company);
    	}else out.writeBoolean(false);
    	out.writeInt(this.emails.size());
    	for (String e:this.emails) {
    		out.writeUTF(e);
    	}
    }

    public static Contact deserialize(DataInputStream in)throws IOException{
    	String s = in.readUTF();
    	int i = in.readInt();
    	long l = in.readLong();
		String c = null;
    	if(in.readBoolean())c=in.readUTF();
    	List<String> e = new ArrayList<>();
    	for(int j=0;j<in.readInt();j++){
    		e.add(in.readUTF());
    	}
    	return new Contact(s,i,l,c,e);
    }

    /*
    public static void main (String[] args){
        Contact c = new Contact("ola",1,99999,"cenas",new ArrayList<>());
        //new ArrayList<>(Arrays.asList("john@mail.com")
        DataOutputStream d = new DataOutputStream();
        c.serialize(d);
        Contact cl = deserialize(d);
        System.out.println(cl.toString());
    }*/
}
