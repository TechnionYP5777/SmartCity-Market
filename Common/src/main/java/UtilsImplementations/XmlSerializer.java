package UtilsImplementations;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import UtilsContracts.IXmlSerializer;
import UtilsContracts.XmlException;


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

@Singleton
public class XmlSerializer<IClass> implements IXmlSerializer<IClass> {
	
	private Class<? extends IClass> mClassType;
	
	
	public XmlSerializer(Class<? extends IClass> classType) {
		mClassType = classType;
	}
	
	@Override
	public void serializeToFile(Object o, String fileName) throws XmlException, IOException {
		try {
			File file = new File(fileName);
		    if (!file.exists()) {
		    	File parentFile = file.getParentFile();
		    	if (parentFile != null)
					file.getParentFile().mkdirs();
		    	file.createNewFile();
		    }
		    
			getMarshaller(o).marshal(o, file);
		} 
		catch (JAXBException e) {
			throw new XmlException("Failed to serialize to file", e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public IClass deserializeFromFile(String fileName) throws XmlException {
		try {
			Object retObj = getUnmarshaller(mClassType).unmarshal(new File(fileName));
			return (IClass)retObj;
		}
		catch (JAXBException e) {
			throw new XmlException("Failed to deserialize from file: " + fileName, e); 
		}
	}

	@Override
	public String serializeToString(Object o) throws XmlException {
		try {
			StringWriter sw = new StringWriter();
			getMarshaller(o).marshal(o, sw);
			return sw + "";
		}
		catch (JAXBException e) {
			throw new XmlException("Failed to serialize to string", e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public IClass deserializeFromString(String content) throws XmlException {
		try {	
			StringReader sr = new StringReader(content);
			Object retObj = getUnmarshaller(mClassType).unmarshal(sr);
			return (IClass)retObj;
		}
		catch (JAXBException e) {
			throw new XmlException("Failed to deserialize from string", e); 
		}
	}
	
	private Marshaller getMarshaller(Object o) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(o.getClass());
		Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    return m;
	}
	
	private Unmarshaller getUnmarshaller(Class<?> classType) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(classType);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    return jaxbUnmarshaller;
	}
	
}
