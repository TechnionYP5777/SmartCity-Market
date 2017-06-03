package UtilsContracts;

/**
 * this interface helps the caller of confirmation dialog to get indication about click Yes or No button
 * 
 * @author Shimon Azulay
 * @since 3.6.17
 *
 */
public interface IConfiramtionDialog {
	
	void onYes();
	
	void onNo();

}
