package BasicCommonClasses;

import java.lang.String;

import api.contracts.IManufacturer;

/** Manufacturer - represents manufacturer of a product.
 * 
 * @author Lior Ben Ami
 * @since 2016-12-09 */
public class Manufacturer implements IManufacturer{
	long id;
	String name;
	
	public Manufacturer(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public void setId(long newId) {
		id  = newId;
	}
	
	public long getId() {
		return id;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32)) + 31;
	}

	@Override
	public boolean equals(Object ¢) {
		return ¢ == this || (¢ != null && getClass() == ¢.getClass() && id == ((Manufacturer) ¢).id);
	}

	@Override
	public boolean isEqualTo(IManufacturer other) {
		return this.equals(other);
	}
}
