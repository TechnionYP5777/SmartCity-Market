package UtilsContracts;

public interface IEventBus {
	
	void register(Object obj);
	
	void unRegister(Object obj);
	
	void post(Object obj);

}
