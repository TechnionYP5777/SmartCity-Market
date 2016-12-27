package EmployeeDefs;

/**
 * This class define all the Exceptions {@link AEmployee} could throw.
 * 
 * @author Aviad Cohen
 * @since 2016-12-26
 */
public class AEmployeeExceptions extends Exception {

	private static final long serialVersionUID = 8663454971017928972L;

	/**
	 * Thrown when invalid (unsupported) command was sent by worker.
	 *
	 */
	public static class InvalidCommandDescriptor extends AEmployeeExceptions {

		private static final long serialVersionUID = -6013813473192780331L;
	}
	
	/**
	 * Thrown when one of the parameters is illegal.
	 *
	 */
	public static class InvalidParameter extends AEmployeeExceptions {

		private static final long serialVersionUID = 6423454812287539804L;

	}
	
	/**
	 * Thrown when unexpected error occurred
	 *
	 */
	public static class CriticalError extends AEmployeeExceptions {

		private static final long serialVersionUID = -1281316098307233162L;
	}

	/**
	 * Thrown when unexpected sender ID is not recognized by the database.
	 *
	 */
	public static class UnknownSenderID extends AEmployeeExceptions {

		private static final long serialVersionUID = -2842898860811837903L;
	}
	
	/**
	 * Thrown when worker try to do operation before connecting
	 *
	 */
	public static class EmployeeNotConnected extends AEmployeeExceptions {

		private static final long serialVersionUID = 736110846693773042L;	
	}

	/**
	 * Thrown when worker try to connect twice
	 * 
	 */
	public static class EmployeeAlreadyConnected extends AEmployeeExceptions {

		private static final long serialVersionUID = -4880371025977064054L;
	}
	
	/**
	 * Thrown when worker not exist in database or password don't match
	 *
	 */
	public static class AuthenticationError extends AEmployeeExceptions {

		private static final long serialVersionUID = -3035025128800993047L;
	}
	
	/**
	 * Thrown when requesting product that not exist in catalog
	 *
	 */
	public static class ProductNotExistInCatalog extends AEmployeeExceptions {

		private static final long serialVersionUID = -1218770191837502028L;
	}
	
	/**
	 * Thrown when requesting product already exist in catalog
	 *
	 */
	public static class ProductAlreadyExistInCatalog extends AEmployeeExceptions {

		private static final long serialVersionUID = 231336810775816368L;
	}
	
	/**
	 * Thrown when requesting to remove product which still in stock for sale.
	 *
	 */
	public static class ProductStillForSale extends AEmployeeExceptions {

		private static final long serialVersionUID = -6441045369608147333L;
	}
	
	/**
	 * Thrown when requesting amount bigger than available.
	 *
	 */
	public static class AmountBiggerThanAvailable extends AEmployeeExceptions {

		private static final long serialVersionUID = -6977996976396033467L;
	}
	
	/**
	 * Thrown when product package does not exist.
	 *
	 */
	public static class ProductPackageDoesNotExist extends AEmployeeExceptions {

		private static final long serialVersionUID = -1086760036316621806L;
	}
}
