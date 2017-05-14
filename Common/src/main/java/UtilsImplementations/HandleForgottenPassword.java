package UtilsImplementations;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import BasicCommonClasses.ForgotPasswordData;
import BasicCommonClasses.Login;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import SMExceptions.SMException;
import UtilsContracts.IClientRequestHandler;

/**
 * @author idan atias
 *
 * @since May 14, 2017
 * 
 *        This class implements the recovering of a forgotten password. Will be
 *        used in Employee & Customer modules.
 */

public class HandleForgottenPassword {

	public static class NoSuchUserName extends SMException {
		private static final long serialVersionUID = 2466826542889809933L;

	}

	public static class WrongAnswer extends SMException {
		private static final long serialVersionUID = 6436535079238432387L;

	}

	public static class CriticalError extends SMException {
		private static final long serialVersionUID = -7126259923544905291L;
	}

	private static Logger log = Logger.getLogger(HandleForgottenPassword.class.getName());

	private int senderId;
	private IClientRequestHandler clientRequestHandler;
	private int port;
	private String host;
	private int timeout;
	private String username;
	private String question;

	@Inject
	public HandleForgottenPassword(int senderId, IClientRequestHandler clientRequestHandler, int port, String host,
			int timeout) {
		this.senderId = senderId;
		this.clientRequestHandler = clientRequestHandler;
		this.port = port;
		this.host = host;
		this.timeout = timeout;
	}

	private void establishCommunication() throws CriticalError {
		log.info("Establish communication with server: port: " + port + " host: " + host);
		try {
			clientRequestHandler.createSocket(port, host, timeout);
		} catch (UnknownHostException | RuntimeException e) {
			log.fatal("Creating communication with the server encounter severe fault: " + e.getMessage());

			throw new CriticalError();
		}
	}

	private void terminateCommunication() {
		if (clientRequestHandler != null)
			clientRequestHandler.finishRequest();
	}

	private String sendRequestWithRespondToServer(String request) throws CriticalError {

		establishCommunication();
		log.info("Sending command to server");
		try {
			String $ = this.clientRequestHandler.sendRequestWithRespond(request);
			terminateCommunication();
			return $;
		} catch (IOException e) {
			log.fatal("Sending logout command to server encounter sever fault : " + e.getMessage());
			terminateCommunication();
			throw new CriticalError();
		}
	}

	private CommandWrapper getCommandWrapper(String serverResponse) {
		try {
			return CommandWrapper.deserialize(serverResponse);
		} catch (Exception e) {
			log.fatal("Critical bug: failed to desirealize server respond: " + serverResponse);

			throw new RuntimeException();
		}
	}

	private void resultDescriptorHandler(ResultDescriptor d) throws WrongAnswer, NoSuchUserName, CriticalError {
		switch (d) {
		case SM_ERR:
			throw new CriticalError();
		case SM_FOROGT_PASSWORD_WRONG_ANSWER:
			throw new WrongAnswer();
		case SM_USERNAME_DOES_NOT_EXIST:
			throw new NoSuchUserName();
		default:
			break;
		}
	}

	public String getAuthenticationQuestion(String username) throws CriticalError, WrongAnswer, NoSuchUserName {
		if (username == null || username.isEmpty())
			throw new CriticalError();
		this.username = username;
		CommandWrapper cmdwrppr = null;
		log.info("Creating 'get authentication question' command wrapper for username: " + username);

		String serverResponse = sendRequestWithRespondToServer(new CommandWrapper(senderId,
				CommandDescriptor.FORGOT_PASSWORD_GET_QUESTION, Serialization.serialize(username)).serialize());

		cmdwrppr = getCommandWrapper(serverResponse);
		resultDescriptorHandler(cmdwrppr.getResultDescriptor());

		log.info("Successfully returned authentication question from server for username: " + username);
		return question = Serialization.deserialize(cmdwrppr.getData(), String.class);
	}

	public boolean sendAnswerWithNewPassword(String ans, String pass) throws CriticalError, WrongAnswer, NoSuchUserName {
		if (username == null || question == null)
			throw new CriticalError();
		CommandWrapper cmdwrppr = null;
		log.info("Creating 'send answer and password' command wrapper for username: " + username);

		ForgotPasswordData forgotPassData = new ForgotPasswordData(question, ans);
		Login ansAndPassContainer = new Login(username, pass, forgotPassData);

		String serverResponse = sendRequestWithRespondToServer(
				new CommandWrapper(senderId, CommandDescriptor.FORGOT_PASSWORD_SEND_ANSWER_WITH_NEW_PASSWORD,
						Serialization.serialize(ansAndPassContainer)).serialize());

		cmdwrppr = getCommandWrapper(serverResponse);
		resultDescriptorHandler(cmdwrppr.getResultDescriptor());

		log.info("Successfully recovered forgotten password for username: " + username);
		return Serialization.deserialize(cmdwrppr.getData(), Boolean.class);
	}

}
