package CommonDefs;

import UtilsImplementations.Serialization;

/** CLIENT_TYPE - The client type which use the login command.
 * 
 * @author Aviad Cohen
 * @since 2016-12-28 */

public enum CLIENT_TYPE {

	/* Username matches worker type */
	WORKER,

	/* Username matches manager type */
	MANAGER;
	
	public String serialize() {
		return Serialization.serialize(this);
	}
	
	public static CLIENT_TYPE deserialize(String type) {
		return Serialization.deserialize(type, CLIENT_TYPE.class);
	}
}
