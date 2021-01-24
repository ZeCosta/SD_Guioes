import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.*;


public class Server {

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(12345);
        Game g=new Game();
        int count=0;

        while(true) {
            Socket s = ss.accept();

            if(count%4==0) g=new Game();
            System.out.println("+1 connected to "+g);;
            count++;
            new ClientHandler(s,g).start();
        }
    }
}
class ClientHandler extends Thread{
    Socket s;
    Game g;
        
    BufferedReader in;
    PrintWriter out;
    
    public ClientHandler(Socket s, Game g) throws IOException{
        this.s=s;
        this.g=g;
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream());
        
    }

    public void run(){try{
            String str=in.readLine();
            //System.out.println(str);
            int n=Integer.parseInt(str);
            int sum=g.submit(n);
            System.out.println(sum);
            out.println(sum>21?"Perdeu":(n>sum%4?"Ganhou":"Perdeu"));
            out.flush();
        }catch(Exception e){}
    }

}

class Game{
    int c=0;
    int soma=0;
    private Lock l=new ReentrantLock();
    private Condition cond= l.newCondition();

    public int submit(int v) throws InterruptedException{
        try{
            l.lock();
            soma+=v;
            c++;
            while(c<4){
                cond.await();
            }
            cond.signalAll();

            return soma;
        }finally{
            l.unlock();
        }
    }
}
