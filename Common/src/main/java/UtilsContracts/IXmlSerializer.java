package UtilsContracts;

import java.io.IOException;

/**
 * 
 * Represents a serializer of a specific type.
 * The interface provides ability to serialize and deserialize an object of a given type.
 * This service could not be injected by a user an on order to work with the serializer,
 * the service that should be used in @IXmlSerializerProvider
 * @author Shimon Azulay
 * @since 2016-03-29 
 * @param <Type>
 */
public interface IXmlSerializer<Type> {

	/**
	 * serializes an object to a file
	 * 
	 * @param obj the object to be serialized
	 * @param fileName the name of the file
	 * 
	 * @throws XmlException
	 * @throws IOException
	 */
	void serializeToFile(Object obj, String fileName) throws XmlException, IOException;
	
	/**
	 * deserializes an object of type <Type> from a file
	 * 
	 * @param fileName the name of the file
	 * 
	 * @return an instance of the deserialized object from type <Type>
	 * 
	 * @throws XmlException
	 */
	Type deserializeFromFile(String fileName) throws XmlException;
	
	/**
	 * serializes an object to a string
	 * 
	 * @param obj the object to be serialized
	 * 
	 * @return a string which represents the serialized object
	 * 
	 * @throws XmlException
	 */
	String serializeToString(Object obj) throws XmlException;
	
	/**
	 * deserializes an object of type <Type> to a string
	 * 
	 * @param content the string that contains the serialized object
	 * 
	 * @return an instance of the deserialized object from type <Type>
	 * 
	 * @throws XmlException
	 */
	Type deserializeFromString(String content) throws XmlException;
}
