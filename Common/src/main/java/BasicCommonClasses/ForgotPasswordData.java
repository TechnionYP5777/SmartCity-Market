package BasicCommonClasses;

/**
 * ForgotPasswordData - This class will hold and maintain the forget password flow.
 * 
 * @author Aviad Cohen
 * @since 2016-12-09
 */
public class ForgotPasswordData {

	String question;
	String answer;
	
	public ForgotPasswordData(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}
	
	public ForgotPasswordData() {
		this.question = new String();
		this.answer = new String();
	}
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	@Override
	public int hashCode() {
		return question.hashCode() + answer.hashCode();
	}

	@Override
	public boolean equals(Object ¢) {
		return ¢ == this || (¢ != null && getClass() == ¢.getClass() &&
				question.equals(((ForgotPasswordData) ¢).question) && answer.equals(((ForgotPasswordData) ¢).answer));
	}
}
