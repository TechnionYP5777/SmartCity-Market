package BasicCommonClasses;

/**
 * Login - this class stores the username and password on login commands
 * 
 * @author Aviad Cohen 
 * @since 2017-02-02
 */
public class Login {

	private String userName;
	private String password;
	
	public ForgetPassword getForgetPassword() {
		return forgetPassword;
	}

	public void setForgetPassword(ForgetPassword p) {
		this.forgetPassword = p;
	}

	ForgetPassword forgetPassword;
	
	public Login(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public Login(String userName, String password, ForgetPassword forgetPassword) {
		this.userName = userName;
		this.password = password;
		this.forgetPassword = forgetPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isValid() {
		return !"".equals(userName) && !"".equals(password);
	}
	
	@Override
	public int hashCode() {
		return userName.hashCode();
	}

	@Override
	public boolean equals(Object ¢) {
		return ¢ == this || (¢ != null && getClass() == ¢.getClass() &&
				userName.equals(((Login) ¢).userName) && password.equals(((Login) ¢).password) &&
				forgetPassword.equals(((Login) ¢).forgetPassword));
	}
}
