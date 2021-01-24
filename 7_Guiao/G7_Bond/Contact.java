import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

class Contact {
    private String name;
    private int age;
    private long phoneNumber;
    private String company;     // Pode ser null
    private List<String> emails;

    private void serializEmails(DataOutputStream out) throws IOException{
        for(String e : this.emails)
            out.writeUTF(e);
    }

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

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(name);
        out.writeInt(age);
        out.writeLong(phoneNumber);
        boolean isCompanyNull = (company == null);
        out.writeBoolean(isCompanyNull);
        if(!isCompanyNull) out.writeUTF(company);
        out.writeInt(emails.size()); //Envia o número de emails a escrever
        serializEmails(out);
    }

    public static Contact deserialize(DataInputStream in) throws IOException{
        String name = in.readUTF();
        int age = in.readInt();
        long phoneNumber = in.readLong();
        boolean isCompanyNull = in.readBoolean();
        String company;
        if(!isCompanyNull) company = in.readUTF();
        else company = null;
        int numEmails = in.readInt();
        List<String> emails = new ArrayList<>();
        for(int i = 0; i < numEmails; i++)
            emails.add(in.readUTF());

        return new Contact(name, age, phoneNumber, company, emails);
    }
}
