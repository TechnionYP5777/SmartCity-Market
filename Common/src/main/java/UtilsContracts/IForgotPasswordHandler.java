package UtilsContracts;

import UtilsImplementations.ForgotPasswordHandler.NoSuchUserName;
import UtilsImplementations.ForgotPasswordHandler.WrongAnswer;

/** IForgotPasswordHandler - This interface represents the functionality of the client forgot password.
 * @author Shimon Azulay
 * @since 2017-05-25 */

public interface IForgotPasswordHandler {
	
	String getForgotPasswordQuestion(String user) throws NoSuchUserName;

	boolean sendAnswerAndNewPassword(String ans, String pass) throws WrongAnswer, NoSuchUserName;

}
