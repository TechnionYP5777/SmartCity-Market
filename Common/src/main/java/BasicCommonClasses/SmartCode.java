package BasicCommonClasses;

import org.joda.time.LocalDate;

/**
 * SmartCode - basic class represent the SmartCode product: A combination of
 * barcode and expiration date.
 * 
 * @author Lior Ben Ami
 * @since 2016-12-09
 */
public class SmartCode {
	long barcode;
	LocalDate expirationDate;

	public SmartCode(long barcode, LocalDate expirationDate) {
		this.barcode = barcode;
		this.expirationDate = expirationDate;
	}

	public SmartCode(long barcode, java.time.LocalDate expirationDate, String justToSolveAmbiguity) {
		this(barcode, new LocalDate(expirationDate.getYear(), expirationDate.getMonthValue(),
				expirationDate.getDayOfMonth()));
	}

	public void setBarcode(long newBarCode) {
		barcode = newBarCode;
	}

	public long getBarcode() {
		return barcode;
	}

	public void setExpirationDate(LocalDate newExpirationDate) {
		expirationDate = newExpirationDate;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	@Override
	public int hashCode() {
		return 31 * ((int) (barcode ^ (barcode >>> 32)) + 31)
				+ ((expirationDate == null) ? 0 : expirationDate.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SmartCode other = (SmartCode) o;
		if (barcode != other.barcode)
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		return true;
	}

	public boolean isValid() {
		return barcode >= 0;
	}
}
