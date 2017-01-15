package BasicCommonClasses;

/** PlaceInMarket - 
 *  A product can be:
 * <p> in </p>
 * <ol> WAREHOUSE </ol>
 * <il>or in </il><p>
 * <ol> STORE </ol>
 * @author Lior Ben Ami
 * @since 2016-12-09 */
public enum PlaceInMarket {
	WAREHOUSE, STORE;
	
	@Override
	public String toString() {
		switch (this) {
			case WAREHOUSE: return "WAREHOUSE";
			case STORE: return "STORE";
			default: return "";
		}
	}
}
