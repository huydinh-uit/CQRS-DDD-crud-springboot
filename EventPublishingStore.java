package vn.com.vng.mcrusprofile.util;

/**
 * @author sonnd
 *
 */
public class EventPublishingStore {

	private static final ThreadLocal<Object> tlEvent = new ThreadLocal<>();
	
	public static final void putEvent(Object event) {
		tlEvent.set(event);
	}
	
	public static final Object getEvent() {
		return tlEvent.get();
	}
}
