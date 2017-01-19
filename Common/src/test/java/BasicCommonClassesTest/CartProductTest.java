package BasicCommonClassesTest;

import static org.junit.Assert.fail;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.CartProduct;
/**@author Lior Ben Ami
 * @since 2016-12-11 */
public class CartProductTest {
	
	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	/*@Test public void CartProductTestMethod() {
		Manufacturer man = new Manufacturer(11,"Osem");
		String bambaIm = "https://www.osem.co.il/tm-content/uploads/2015/01/Bamba_classic_80g3.png3-308x308.png";
		CatalogProduct catp = new CatalogProduct(11, "Bamba", null, man, "", 12, bambaIm, null);
		LocalDate ld =  LocalDate.of(2016,12,11);
		CartProduct crp = new CartProduct(catp, ld, 0);
		if (!catp.equals(crp.getCatalogProduct()) || !ld.equals(crp.getExpirationDate()) ||
				crp.getAmount() != 0)
			fail();
		catp.setBarcode(12);
		crp.setCatalogProduct(catp);
		ld.plusDays(1);
		crp.setExpirationDate(ld);
		crp.setAmount(2);
		if (!catp.equals(crp.getCatalogProduct()) || !ld.equals(crp.getExpirationDate()) ||
				crp.getAmount() != 2)
			fail();
		CartProduct crp2 = new CartProduct(catp, ld, 2);
		if (!crp2.equals(crp))
			fail();
		crp2.setAmount(1);
		if (!crp2.equals(crp))
			fail();
		LocalDate ld2 = ld.plusDays(1);
		crp2.setExpirationDate(ld2);
		if (crp2.equals(crp))
			fail();
	}*/
}

