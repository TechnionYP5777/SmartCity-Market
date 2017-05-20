package CustomerContracts;

import SMExceptions.SMException;

/**
 * This class define all the Exceptions {@link ACustomer} could throw.
 * 
 * @author Lior Ben Ami
 * @since 2017-01-04
 */
public class ACustomerExceptions extends SMException {

	private static final long serialVersionUID = -0x689D76D5E6CBA540L;

	/**
	 * Thrown when invalid (unsupported) command was sent by worker.
	 */
	public static class InvalidCommandDescriptor extends ACustomerExceptions {
		private static final long serialVersionUID = 0x523B9E9024F512E7L;
	}
	
	/**
	 * Thrown when one of the parameters is illegal.
	 */
	public static class InvalidParameter extends ACustomerExceptions {
		private static final long serialVersionUID = -0x698DD29CB29AF18EL;
	}
	
	/**
	 * Thrown when unexpected error occurred
	 */
	public static class CriticalError extends ACustomerExceptions {
		private static final long serialVersionUID = -0xE10F81C67CDFF9DL;
	}
	
	/**
	 * Thrown when try to login with wrong user id or password.
	 */
	public static class AuthenticationError extends ACustomerExceptions {
		private static final long serialVersionUID = -0x64FE89BEAE615D83L;
	}
	
	/**
	 * Thrown when customer try to do operation before connecting
	 */
	public static class CustomerNotConnected extends ACustomerExceptions {
		private static final long serialVersionUID = 0x4156FFA9E1980971L;
	}

	/**
	 * Thrown when try to remove product that isn't in the customer.
	 */
	public static class ProductNotInCart extends ACustomerExceptions {
		private static final long serialVersionUID = -0x1CD88A909A88D7D9L;
	}

	/**
	 * Thrown when requesting amount bigger than available.
	 */
	public static class AmountBiggerThanAvailable extends ACustomerExceptions {

		private static final long serialVersionUID = 0x1A89F7BDE622A286L;
	}
	
	/**
	 * Thrown when product package does not exist.
	 */
	public static class ProductPackageDoesNotExist extends ACustomerExceptions {
		private static final long serialVersionUID = -0x5F9E0DB543A6BE52L;
	}
	
	/**
	 * Thrown when try to checkout empty grocery list.
	 */
	public static class GroceryListIsEmpty extends ACustomerExceptions {
		private static final long serialVersionUID = 0xD91EC150B7D29E0L;
	}
	
	/**
	 * Thrown when try to view product catalog which not exist.
	 */
	public static class ProductCatalogDoesNotExist extends ACustomerExceptions {
		private static final long serialVersionUID = 0x428D025BC11F7278L;
	}
	
	/**
	 * Thrown when trying to register with existing username
	 */
	public static class UsernameAlreadyExists extends ACustomerExceptions {
		private static final long serialVersionUID = 0x7B732DCB4926401DL;
	}
	
	/**
	 * Thrown when trying to register with existing username
	 */
	public static class ForgotPasswordWrongAnswer extends ACustomerExceptions {
		private static final long serialVersionUID = 0x267A53FF357E07A6L;
	}
}
