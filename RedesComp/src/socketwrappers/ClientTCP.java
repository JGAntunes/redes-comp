/**
 * 
 */
package socketwrappers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import utils.FileHandler;
import utils.Protocol;
import utils.StreamProcessors;

/**
 * @author Joao Antunes
 *
 */
public class ClientTCP {
	private Socket _socket;
	private DataOutputStream _output;
	private String _host;
	private int _port;
	
	public ClientTCP(String host, int port) throws UnknownHostException, IOException{
		_host = host;
		_port = port;
		_socket = new Socket(host, port);
		//_socket.setKeepAlive(true);
		if(_socket.isConnected()){
			System.out.println("Successfully connected to: " + _host + ": " + _port);
		}
		else{
			System.out.println("Failded to connect to server.");
		}
		_output = new DataOutputStream(_socket.getOutputStream());
	}
	
	public int getPort() {
		return _port;
	}

	public void setPort(int port) throws IOException {
		_port = port;
		reconnect();
	}
	
	public String getHost() {
		return _host;
	}

	public void setHost(String host) throws IOException {
		_host = host;
		reconnect();
	}

	public Socket getSocket() {
		return _socket;
	}
	
	public boolean isConnected() {
		return _socket.isConnected();
	}

	public void setServerSocket(Socket socket) {
		_socket = socket;
	}
	
	public void reconnect() throws IOException{
		_socket.connect(new InetSocketAddress(_host, _port));
	}
	
	public void close() throws IOException{
		_socket.close();
	}
	
	public void sendToServer(String message) throws IOException{
		_output = new DataOutputStream(_socket.getOutputStream());
		_output.writeBytes(message + '\n');
	}
	
	public void sendToServer(byte[] message) throws IOException{
		_output = new DataOutputStream(_socket.getOutputStream());	
		_output.write(message, 0, message.length);
	}
	
/*	public void sendToServer(MessageTCP message) throws IOException{
		String args = new String();
		for(String s : message.getStrParams()){
			args.concat(s + " ");
		}
		if (message.getData() == null || message.getData().length < 1){
			args.s
		}
		byte[] argsBytes = args.getBytes();
		byte[] messageBytes = StreamProcessors.concatByte(message., output);
		_output = new DataOutputStream(_socket.getOutputStream());
		_output.write(messageBytes + '\n');
		_output.flush();
	}*/
	
	public MessageTCP receiveFromServer(int expectedArgs) throws IOException{
		return new MessageTCP(StreamProcessors.getByteArray(new BufferedInputStream(_socket.getInputStream())), expectedArgs);
	}
}