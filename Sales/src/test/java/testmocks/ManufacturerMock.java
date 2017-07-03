package testmocks;

import api.contracts.IManufacturer;

public class ManufacturerMock implements IManufacturer {

	String name;
	
	public ManufacturerMock(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isEqualTo(IManufacturer other) {
		return equals(other);
	}

	@Override
	public int hashCode() {
		return 31 + ((name == null) ? 0 : name.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ManufacturerMock other = (ManufacturerMock) o;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
