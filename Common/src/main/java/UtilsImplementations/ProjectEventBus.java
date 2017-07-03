package UtilsImplementations;

/** PersistenceType - The project event bus
 * @author Shimon Azulay
 * @since 2016-03-30*/

import javax.inject.Singleton;

import com.google.common.eventbus.EventBus;

import UtilsContracts.IEventBus;

@Singleton
public class ProjectEventBus implements IEventBus {

	private EventBus eventbus;

	@Override
	public void register(Object o) {
		if (eventbus == null)
			eventbus = new EventBus();
		eventbus.register(o);

	}

	@Override
	public void unRegister(Object o) {
		if (eventbus == null)
			eventbus = new EventBus();
		eventbus.unregister(o);

	}

	@Override
	public void post(Object o) {
		if (eventbus == null)
			eventbus = new EventBus();
		eventbus.post(o);
	}

}
