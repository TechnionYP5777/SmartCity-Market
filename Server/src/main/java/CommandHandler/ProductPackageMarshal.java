package CommandHandler;

import java.time.LocalDate;
import java.util.Map;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import api.contracts.IGroceryPackage;
import api.contracts.IProduct;
import api.contracts.IStorePackage;

public class ProductPackageMarshal implements IGroceryPackage, IStorePackage {

	ProductPackage pack;
	CatalogProduct product;
	
	@Override
	public LocalDate getExpirationDate() {
		return pack.getSmartCode().getExpirationDate();
	}

	@Override
	public PlaceInMarket getPlace() {
		return pack.getLocation().getPlaceInMarket();
	}

	@Override
	public IProduct getProduct() {
		return product;
	}

	@Override
	public int getAmount() {
		return pack.getAmount();
	}

	public ProductPackageMarshal(ProductPackage pack, Map<Long, CatalogProduct> catalog) {
		this.pack = pack;
		this.product = catalog.get(pack.getSmartCode().getBarcode());
	}

}
