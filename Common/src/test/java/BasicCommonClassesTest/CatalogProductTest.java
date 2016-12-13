package BasicCommonClassesTest;

import static org.junit.Assert.fail;
import org.junit.Test;

import BasicCommonClasses.Location;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Manufacturer;;


/**@author Lior Ben Ami
 * @since 2016-12-11 */
public class CatalogProductTest {
	@Test public void CatalogProductTestMethod() {
		Manufacturer man = new Manufacturer(11,"Osem");
		CatalogProduct cp = new CatalogProduct(11, "Bamba", null, man, "", 12, null);
		if (cp.getBarcode() != 11 || !"Bamba".equals(cp.getName()) || cp.getIngredients() != null ||
				!cp.getManufacturer().equals(man) || !"".equals(cp.getDescription()) || 
				cp.getPrice() != 12 || cp.getLocations() != null)
			fail();
		cp.setBarcode(111);
		cp.setName("Bisli");
		Ingredient gluten = new Ingredient(5, "gluten");
		cp.addIngredient(gluten);
		String description = "A popular snack with different flavours";
		cp.setDescription(description);
		Location lo = new Location(1,1,PlaceInMarket.WAREHOUSE);
		cp.addLocation(lo);
		cp.setPrice(7.35);
		if (cp.getBarcode() != 111 || !"Bisli".equals(cp.getName()) || cp.getIngredients().size() != 1 ||
				!cp.getIngredients().contains(gluten) || !cp.getManufacturer().equals(man) || 
				!description.equals(cp.getDescription()) || cp.getPrice() != 7.35 || 
				cp.getLocations().size() != 1 || !cp.getLocations().contains(lo))
			fail();
		CatalogProduct cp2 = new CatalogProduct(111, "Bamba", null, man, "", 12, null);
		if (!cp2.equals(cp))
			fail();
		cp2.setBarcode(11);
		if (cp2.equals(cp))
			fail();
	}
}
