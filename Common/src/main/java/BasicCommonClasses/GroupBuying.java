package BasicCommonClasses;

/**
 * GroupBuying - This class represents a group buying.
 * 
 * @author Aviad Cohen
 * @since 2017-06-09
 */
public class GroupBuying {
	Integer id;
	Sale sale;
	Integer currentBuyers;
	Integer maxBuyers;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Sale getSale() {
		return sale;
	}
	public void setSale(Sale sale) {
		this.sale = sale;
	}
	public Integer getCurrentBuyers() {
		return currentBuyers;
	}
	public void setCurrentBuyers(Integer currentBuyers) {
		this.currentBuyers = currentBuyers;
	}
	public Integer getMaxBuyers() {
		return maxBuyers;
	}
	public void setMaxBuyers(Integer maxBuyers) {
		this.maxBuyers = maxBuyers;
	}
}
