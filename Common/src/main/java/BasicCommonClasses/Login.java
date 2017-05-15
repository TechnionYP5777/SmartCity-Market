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
	
	public ForgotPasswordData getForgetPassword() {
		return forgotPasswordData;
	}

	public void setForgetPassword(ForgotPasswordData d) {
		this.forgotPasswordData = d;
	}

	ForgotPasswordData forgotPasswordData;
	
	public Login(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public Login(String userName, String password, ForgotPasswordData forgotPasswordData) {
		this.userName = userName;
		this.password = password;
		this.forgotPasswordData = forgotPasswordData;
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
				forgotPasswordData.equals(((Login) ¢).forgotPasswordData));
	}
}
