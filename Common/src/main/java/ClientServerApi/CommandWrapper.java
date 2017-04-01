package ClientServerApi;

import UtilsImplementations.Serialization;

public class CommandWrapper implements Cloneable {
	/**
	 * Command wrapper for sending packages between client and server:
	 * Fields:
	 * 		senderID - sender id of the client (worker, manager, customer, etc. )
	 * 		commandDescriptor - the command to exec on server side.
	 * 		resultDescriptor - result code delivered from server to client.
	 * 		data - a String (in json format) that holds the cmd's arguments in case of a client-to-server package
	 * 			   a String (in json format) that holds the cmd's return value in case of a server-to-client package
	 * 
	 * @author idan atias
	 */

	private int senderID;
	private CommandDescriptor commandDescriptor;
	private ResultDescriptor resultDescriptor;
	private String data;
	
	//Client c'tors:
	public CommandWrapper(int senderID, CommandDescriptor commandDescriptor) {
		this.senderID = senderID;
		this.commandDescriptor = commandDescriptor;
	}
	
	public CommandWrapper(int senderID, CommandDescriptor commandDescriptor, String args){
		this(senderID, commandDescriptor);
		this.data = args;
	}
	
	//Server c'tors:
	public CommandWrapper(ResultDescriptor resultDescriptor, String retVal) {
		this.resultDescriptor = resultDescriptor;
		this.data = retVal;
	}
	
	public CommandWrapper(ResultDescriptor resultDescriptor) {
		this.resultDescriptor = resultDescriptor;
	}

	public String serialize() {
		return Serialization.serialize(this);
	}
	
	public static CommandWrapper deserialize(String cmdWrap) {
		return Serialization.deserialize(cmdWrap, CommandWrapper.class);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		CommandWrapper $ = (CommandWrapper)super.clone();
		   
		$.setSenderID(this.senderID);
		$.setCommandDescriptor(this.commandDescriptor);
		$.setResultDescriptor(this.resultDescriptor);
		$.setData(this.data);

		return $;
	}
	
	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public CommandDescriptor getCommandDescriptor() {
		return commandDescriptor;
	}

	public void setCommandDescriptor(CommandDescriptor ¢) {
		this.commandDescriptor = ¢;
	}

	public ResultDescriptor getResultDescriptor() {
		return resultDescriptor;
	}

	public void setResultDescriptor(ResultDescriptor ¢) {
		this.resultDescriptor = ¢;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
