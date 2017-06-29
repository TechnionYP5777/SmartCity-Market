package api.types.basic.sales;

import java.time.LocalDate;

public abstract class ASale {

	LocalDate date;
	
	public ASale(){
		date = LocalDate.now();
	}

	public LocalDate getDate() {
		return date;
	}

	public abstract double getTotalDiscount();
}
