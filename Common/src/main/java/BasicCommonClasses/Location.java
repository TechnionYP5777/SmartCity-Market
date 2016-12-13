package BasicCommonClasses;

//TODO: decide about the logic off placeInMarket -C'tor, set...
/** Location - the SmartMarket is mapped by the locations of its product.
 * @see main.BasicCommonClasses.PlaceInMarket
 * @author Lior Ben Ami
 * @since 2016-12-09 */
public class Location {
	int passage;
	int column;
	PlaceInMarket placeInMarket;
	
	public Location(int passage, int column, PlaceInMarket placeInMarket) {
		this.passage = passage;
		this.column = column;
		this.placeInMarket = placeInMarket;
	}
	
	public int getPassage() {
		return passage;
	}

	public void setPassage(int passage) {
		this.passage = passage;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public PlaceInMarket getPlaceInMarket() {
		return placeInMarket;
	}

	public void setPlaceInMarket(PlaceInMarket ¢) {
		this.placeInMarket = ¢;
	}

	@Override
	public int hashCode() {
		return 31 * (passage + 31 * (column + 31)) + ((placeInMarket == null) ? 0 : placeInMarket.hashCode());
	}
	
	@Override
	public boolean equals(Object ¢) {
		return ¢ == this || (¢ != null && getClass() == ¢.getClass() && column == ((Location) ¢).column && passage == ((Location) ¢).passage
				&& placeInMarket == ((Location) ¢).placeInMarket);
	}

}
