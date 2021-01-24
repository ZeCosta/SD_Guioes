import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.*;


public class Client{
	public static void main(String[] args) {
        try{

        	Socket socket = new Socket("localhost", 12345);
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        PrintWriter out = new PrintWriter(socket.getOutputStream());
	        
	        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
	        
	        out.println(sysin.readLine());
	        out.flush();
	    
	        String response = in.readLine();    
            System.out.println("Server response: " + response);
   			
   			socket.shutdownInput();
            socket.close();


        }catch(Exception e){

        }
	}
}