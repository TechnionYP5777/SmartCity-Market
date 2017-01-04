package CartContracts;

import SMExceptions.SMException;

/**
 * This class define all the Exceptions {@link ACart} could throw.
 * 
 * @author Lior Ben Ami
 * @since 2017-01-04
 */
public class ACartExceptions extends SMException {

	private static final long serialVersionUID = 8663454971017928972L;

	/**
	 * Thrown when invalid (unsupported) command was sent by worker.
	 */
	public static class InvalidCommandDescriptor extends ACartExceptions {

		private static final long serialVersionUID = -6013813473192780331L;
	}
	
	/**
	 * Thrown when one of the parameters is illegal.
	 */
	public static class InvalidParameter extends ACartExceptions {

		private static final long serialVersionUID = 6423454812287539804L;

	}
	
	/**
	 * Thrown when unexpected error occurred
	 */
	public static class CriticalError extends ACartExceptions {

		private static final long serialVersionUID = -1281316098307233162L;
	}
	
	/**
	 * Thrown when worker try to do operation before connecting
	 */
	public static class CartNotConnected extends ACartExceptions {
		private static final long serialVersionUID = 736110846693773042L;	
	}

	/**
	 * Thrown when worker try to connect twice
	 */
	public static class ProductNotInCart extends ACartExceptions {

		private static final long serialVersionUID = -4880371025977064054L;
	}
	
	/**
	 * Thrown when worker not exist in database or password don't match
	 */
	public static class AuthenticationError extends ACartExceptions {

		private static final long serialVersionUID = -3035025128800993047L;
	}

	/**
	 * Thrown when requesting amount bigger than available.
	 */
	public static class AmountBiggerThanAvailable extends ACartExceptions {

		private static final long serialVersionUID = -6977996976396033467L;
	}
	
	/**
	 * Thrown when product package does not exist.
	 */
	public static class ProductPackageDoesNotExist extends ACartExceptions {

		private static final long serialVersionUID = -1086760036316621806L;
	}
	
	/**
	 * Thrown when try to checkout empty grocery list.
	 */
	public static class GroceryListIsEmpty extends ACartExceptions {

		private static final long serialVersionUID = 231336810775816368L;
	}
	
	/**
	 * Thrown when requesting product already exist in catalog
	 */
}
