package UtilsContracts;

/**
 * 
 * An XML related exception
 * @author Shimon Azulay
 * @since 2016-03-29 
 */
public class XmlException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public XmlException(String message, Throwable cause) {
		super(message, cause);
	}
}
