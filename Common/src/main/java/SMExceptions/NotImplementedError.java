package SMExceptions;

/**
 * Use this exception for forcing a derived class to implement a specific base class method
 * @author idan
 * @since  2017-01-02
 */

public class NotImplementedError extends SMException {

	private static final long serialVersionUID = -0x3B317961E01DE97CL;

	@Override
	public void showInfoToUser() {
		/* empty print */
	}

}
