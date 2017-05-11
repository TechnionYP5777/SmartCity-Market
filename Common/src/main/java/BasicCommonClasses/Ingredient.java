package BasicCommonClasses;

import java.lang.String;

/** Ingredient - A product can contain special ingredient. 
 * 
 * @author Lior Ben Ami
 * @since 2016-12-09 */
public class Ingredient {
	long id;
	String name;
	
	public Ingredient(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public void setId(long newId) {
		id  = newId;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32)) + 31;
	}

	@Override
	public boolean equals(Object ¢) {
		return ¢ == this || (¢ != null && getClass() == ¢.getClass() && id == ((Ingredient) ¢).id);
	}
	
	@Override
	public String toString() {
		return name;
	}
		
}
