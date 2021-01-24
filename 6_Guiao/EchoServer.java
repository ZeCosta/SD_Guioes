import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);

            while (true) {
                Socket socket = ss.accept();
                new Session(socket).start();

                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class Session extends Thread{
	Socket socket;

	Session(Socket socket){this.socket=socket;}

	public void run(){
		try{
			int n=0;
			float soma=0,aux;
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        PrintWriter out = new PrintWriter(socket.getOutputStream());

	        String line;
	        while ((line = in.readLine()) != null) {
	        	
	            	aux=Integer.parseInt(line);
	            	soma+=aux;
	            	n+=1;
	                out.println(soma);
	                out.flush();
	        	
	        }

	        n=n>0?n:1;
	        out.println(soma/n);
	        out.flush();

	        socket.shutdownOutput();
	        socket.shutdownInput();
	        socket.close();

		}catch(IOException e){
            e.printStackTrace();			
		}

	}
}