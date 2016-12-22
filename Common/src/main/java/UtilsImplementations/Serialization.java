package UtilsImplementations;

import com.google.gson.Gson;

/**
 * Serialization - This class gives serialization service
 * 
 * @author Shimon Azulay
 * @since 2016-12-19
 */

public class Serialization {
	
	public static String serialize(Object toSerialize) {
		return new Gson().toJson(toSerialize);
	}


	public static <T> T deserialize(String toDeserialize, Class<T> classType) {
		return new Gson().fromJson(toDeserialize, classType);
	}

}
