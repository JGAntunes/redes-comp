/**
 * 
 */
package commands;

import java.io.IOException;

import socketwrappers.ClientUDP;
import socketwrappers.MessageUDP;
import user.User;
import utils.Errors;
import utils.Protocol;

/**
 * @author Joao Antunes
 *
 */
public class List extends Command{

	private ClientUDP _user;
	private String _CSName;
	private int _CSPort;
	private MessageUDP _bufferUDP;
	
	public List(ClientUDP user, String CSName, int CSPort, String[] arguments){
		_code = Protocol.LIST_COMMAND;
		_arguments = arguments;
		_user = user;
		_CSName = CSName;
		_CSPort = CSPort;
	}
	
	/* (non-Javadoc)
	 * @see commands.Command#run()
	 */
	@Override
	public void run() {
		try{
			_user.sendToServer(new MessageUDP(_CSName, _CSPort, Protocol.LIST + "\n"));
			System.out.println(">> Sent list");
			_bufferUDP = _user.receiveFromServer();
			_arguments = _bufferUDP.getMessage().split(" ");
			if(_arguments[0].equals(Protocol.LIST_RESPONSE)){
				if(_arguments.length > 4){
					System.out.println(_arguments[1]);
					System.out.println(Integer.parseInt(_arguments[2]));
					User.setSS(_arguments[1], Integer.parseInt(_arguments[2]));
					int numFiles = Integer.parseInt(_arguments[3]);
					System.out.println("Available files for download:");
					for(int i = 1; i <= numFiles; i++){
						System.out.println(i + " " + _arguments[3+i]);
					}
				}
			}
			else if(_arguments[0].startsWith(Protocol.ERROR)){
				System.out.println(Errors.INVALID_PROTOCOL);
				System.exit(-1);
			}
			else if(_arguments[0].startsWith(Protocol.EOF)){
				System.out.println(Errors.SERVER_BUSY);
			}
			else{
				System.out.println(Errors.INVALID_COMMAND);
			}
		} catch (IOException e){
			System.err.println(Errors.IO_INPUT);
			System.exit(-1);
		} finally{
			try {
				_user.close();
			} catch (IOException e) {
				System.err.println(Errors.IO_INPUT);
				System.exit(-1);
			}
		}
		
	}
	

}