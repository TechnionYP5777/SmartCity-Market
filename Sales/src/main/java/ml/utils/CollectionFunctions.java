package ml.utils;

import java.util.Collection;

/**
 * This class contains helper functions to use in the Sales module
 *  
 * @author noam
 *
 */
public class CollectionFunctions {

	@SuppressWarnings("unchecked")
	public static <T, E extends T>  E findInCollection(Collection<? extends T> set, E toFind){
		for (T element : set)
			if (element.equals(toFind))
				return (E) element;
		
		return null;
	}
}
