import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.*;


public class EchoServer2 {
    public static void main(String[] args) {
        try {
        	ServerSocket ss = new ServerSocket(12345);
        	ValueStore val=new ValueStore();
            while (true) {
                Socket socket = ss.accept();
                new Session2(socket,val).start();

                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ValueStore{
	private float soma=0; //soma entre threads
	private int count=0;

	Lock l=new ReentrantLock();


	public void register(float soma){
		l.lock();
		try{
			this.soma+=soma;
			count+=1;
		}
		finally{
			l.unlock();
		}
	}
	public float media(){
		l.lock();
		try{
			return count>0?this.soma/count:0;
		}
		finally{
			l.unlock();
		}

	}
}

class Session2 extends Thread{
	Socket socket;
	ValueStore val;

	Session2(Socket socket,ValueStore val){
		this.socket=socket;this.val=val;
	}

	public void run(){
		try{
			float soma=0,aux;
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        PrintWriter out = new PrintWriter(socket.getOutputStream());

	        String line;
	        while ((line = in.readLine()) != null) {
	        	
	            	aux=Integer.parseInt(line);
	            	soma+=aux;
	            	val.register(aux);
	                out.println(soma);
	                out.flush();
	        	
	        }


	        out.println(val.media());
	        out.flush();

	        socket.shutdownOutput();
	        socket.shutdownInput();
	        socket.close();

		}catch(IOException e){
            e.printStackTrace();			
		}

	}
}