package BasicCommonClasses;

/** Location - the SmartMarket is mapped by the locations of its product.
 * 
 * @see main.BasicCommonClasses.PlaceInMarket
 * @author Lior Ben Ami
 * @since 2016-12-09 */
public class Location {
	int x;
	int y;
	PlaceInMarket placeInMarket;
	
	public Location(int x, int y, PlaceInMarket placeInMarket) {
		this.x = x;
		this.y = y;
		this.placeInMarket = placeInMarket;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public PlaceInMarket getPlaceInMarket() {
		return placeInMarket;
	}

	public void setPlaceInMarket(PlaceInMarket ¢) {
		this.placeInMarket = ¢;
	}

	@Override
	public String toString() {
		return "(x,y)=" + "(" + x + "," + y + ")" + ", in " + placeInMarket;
	}
	@Override
	public int hashCode() {
		return 31 * (x + 31 * (y + 31)) + ((placeInMarket == null) ? 0 : placeInMarket.hashCode());
	}
	
	@Override
	public boolean equals(Object ¢) {
		return ¢ == this || (¢ != null && getClass() == ¢.getClass() && y == ((Location) ¢).y && x == ((Location) ¢).x
				&& placeInMarket == ((Location) ¢).placeInMarket);
	}

}
