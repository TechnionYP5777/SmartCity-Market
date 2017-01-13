package BasicCommonClasses;

/** ProductPackage - represents a package of the same products. 
 * All products have the same Expiration Date and located in the same location in the market.
 * @author Lior Ben Ami
 * @since 2016-12-09 */
public class ProductPackage {
	SmartCode smartCode;
	int amount;
	//TODO: discuss if the location should be under control. maybe we want the package C'tor place it in the warehouse..
	Location location;

	public ProductPackage(SmartCode smartCode, int amount, Location location) {
		this.smartCode = smartCode;
		this.amount = amount;
		this.location = location;
	}

	public SmartCode getSmartCode() {
		return smartCode;
	}

	public void setSmartCode(SmartCode ¢) {
		this.smartCode = ¢;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location ¢) {
		this.location = ¢;
	}

	public void incrementAmount(int ¢) {
		amount += ¢;
	}
	
	public void decreaseOneAmount() {
		amount = amount <= 0 ? 0 : amount - 1;
	}
	
	@Override
	public int hashCode() {
		return 31 * (31 * (amount + 31) + ((location == null) ? 0 : location.hashCode()))
				+ ((smartCode == null) ? 0 : smartCode.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductPackage other = (ProductPackage) o;
		if (amount != other.amount)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (smartCode == null) {
			if (other.smartCode != null)
				return false;
		} else if (!smartCode.equals(other.smartCode))
			return false;
		return true;
	}
	
	public boolean isValid() {
		return smartCode.isValid() && amount > 0;
	}
	
	@Override
	public String toString(){
		//TODO - Lior, plz impelemt this method so we can use it in GUI. Thanks!
		return "prodcut package info in ONE SHORT LINE";
		
	}
}
