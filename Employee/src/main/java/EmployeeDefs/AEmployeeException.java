package EmployeeDefs;

import SMExceptions.SMException;

/**
 * This class define all the Exceptions {@link AEmployee} could throw.
 * 
 * @author Aviad Cohen
 * @since 2016-12-26
 */
public class AEmployeeException extends SMException {

	private static final long serialVersionUID = 8663454971017928972L;

	/**
	 * Thrown when invalid (unsupported) command was sent by worker.
	 *
	 */
	public static class InvalidCommandDescriptor extends AEmployeeException {

		private static final long serialVersionUID = -6013813473192780331L;
	}
	
	/**
	 * Thrown when one of the parameters is illegal.
	 *
	 */
	public static class InvalidParameter extends AEmployeeException {

		private static final long serialVersionUID = 6423454812287539804L;

	}
	
	/**
	 * Thrown when unexpected error occurred
	 *
	 */
	public static class CriticalError extends AEmployeeException {

		private static final long serialVersionUID = -1281316098307233162L;
	}
	
	/**
	 * Thrown when worker try to do operation before connecting
	 *
	 */
	public static class EmployeeNotConnected extends AEmployeeException {

		private static final long serialVersionUID = 736110846693773042L;	
	}

	/**
	 * Thrown when worker try to connect twice
	 * 
	 */
	public static class EmployeeAlreadyConnected extends AEmployeeException {

		private static final long serialVersionUID = -4880371025977064054L;
	}
	
	/**
	 * Thrown when worker not exist in database or password don't match
	 *
	 */
	public static class AuthenticationError extends AEmployeeException {

		private static final long serialVersionUID = -3035025128800993047L;
	}
	
	/**
	 * Thrown when requesting product that not exist in catalog
	 *
	 */
	public static class ProductNotExistInCatalog extends AEmployeeException {

		private static final long serialVersionUID = -1218770191837502028L;
	}
	
	/**
	 * Thrown when requesting product already exist in catalog
	 *
	 */
	public static class ProductAlreadyExistInCatalog extends AEmployeeException {

		private static final long serialVersionUID = 231336810775816368L;
	}
	
	/**
	 * Thrown when requesting to remove product which still in stock for sale.
	 *
	 */
	public static class ProductStillForSale extends AEmployeeException {

		private static final long serialVersionUID = -6441045369608147333L;
	}
	
	/**
	 * Thrown when requesting amount bigger than available.
	 *
	 */
	public static class AmountBiggerThanAvailable extends AEmployeeException {

		private static final long serialVersionUID = -6977996976396033467L;
	}
	
	/**
	 * Thrown when product package does not exist.
	 *
	 */
	public static class ProductPackageDoesNotExist extends AEmployeeException {

		private static final long serialVersionUID = -1086760036316621806L;
	}
	
	/**
	 * Thrown when failed to receive respond from server.
	 *
	 */
	public static class ConnectionFailure extends AEmployeeException {

		private static final long serialVersionUID = -7116035947026187101L;
	}
}
