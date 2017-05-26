package GuiUtils;

/**
 * SecurityQuestions class - defines the system's security questions for registrations
 *  
 * @author Lior Ben Ami
 * @since 2017-04
 */
public class SecurityQuestions {
	static String[] questions = {
			"What was your childhood nickname?",
			"What is the name of your favorite childhood friend?",
			"What street did you live on in third grade?",
			"What school did you attend for sixth grade?",
			"In what city or town did your mother and father meet?",
			"What is the first name of the boy or girl that you first kissed?",
			"What is your maternal grandmother's maiden name?"
		};
			
	public static String[] getQuestions() { return questions;}
}
