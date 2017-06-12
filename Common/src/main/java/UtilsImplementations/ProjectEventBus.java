package UtilsImplementations;

import javax.inject.Singleton;

import com.google.common.eventbus.EventBus;

import UtilsContracts.IEventBus;

@Singleton
public class ProjectEventBus implements IEventBus {

	private EventBus eventbus;

	@Override
	public void register(Object obj) {
		if (eventbus == null) {
			eventbus = new EventBus();
		}
		eventbus.register(obj);

	}

	@Override
	public void unRegister(Object obj) {
		if (eventbus == null) {
			eventbus = new EventBus();
		}
		eventbus.unregister(obj);

	}

	@Override
	public void post(Object obj) {
		if (eventbus == null) {
			eventbus = new EventBus();
		}
		eventbus.post(obj);
	}

}
