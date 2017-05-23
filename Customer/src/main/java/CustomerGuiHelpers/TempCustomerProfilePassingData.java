package CustomerGuiHelpers;

import BasicCommonClasses.ICustomerProfile;

/**
 * TempCustomerPassingData - interface to temporary save customer profile data.
 * 
 * @author idan
 * @since 2017-01-16 
 */
public class TempCustomerProfilePassingData {
	public static ICustomerProfile customerProfile;
	public static String password;
	public static void clear() {
		customerProfile = null;
		password = null;
	}
}
