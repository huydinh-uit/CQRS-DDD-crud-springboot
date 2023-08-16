package vn.com.vng.mcrusprofile.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.DigestUtils;

import vn.com.vng.core.server.enums.AuthStatus;
import vn.com.vng.core.server.enums.RecordStatus;

import vn.com.vng.mcrusprofile.security.SecurityUtils;

public final class MetaDataUtils {
	private MetaDataUtils() {

	}

	public static void forCreate(Object object) {

		try {
			BeanUtils.setProperty(object, "makerDate", new Date());
			BeanUtils.setProperty(object, "makerId", SecurityUtils.getCurrentUserLogin().orElseGet(() -> "system"));
			BeanUtils.setProperty(object, "createDate", new Date());
			BeanUtils.setProperty(object, "recordStatus", RecordStatus.O.name());
			BeanUtils.setProperty(object, "authStatus", AuthStatus.A.name());
			BeanUtils.setProperty(object, "recordStat", RecordStatus.O.name());
			BeanUtils.setProperty(object, "authStat", AuthStatus.A.name());
			BeanUtils.setProperty(object, "updateDate", new Date());
			BeanUtils.setProperty(object, "updateId", SecurityUtils.getCurrentUserLogin().orElseGet(() -> "system"));
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}

	}

	public static void forCreateWithUser(Object object, String username) {

		try {
			BeanUtils.setProperty(object, "makerDate", new Date());
			BeanUtils.setProperty(object, "createDate", new Date());
			BeanUtils.setProperty(object, "makerId", username);
			BeanUtils.setProperty(object, "recordStatus", RecordStatus.O.name());
			BeanUtils.setProperty(object, "authStatus", AuthStatus.A.name());
			BeanUtils.setProperty(object, "recordStat", RecordStatus.O.name());
			BeanUtils.setProperty(object, "authStat", AuthStatus.A.name());
			BeanUtils.setProperty(object, "updateDate", new Date());
			BeanUtils.setProperty(object, "updateId", username);
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}

	}

	public static void forCreateWithoutAuthStat(Object object) {

		try {
			BeanUtils.setProperty(object, "makerDate", new Date());
			BeanUtils.setProperty(object, "makerId", SecurityUtils.getCurrentUserLogin().orElseGet(() -> "system"));
			BeanUtils.setProperty(object, "createDate", new Date());
			BeanUtils.setProperty(object, "recordStatus", RecordStatus.O.name());
			BeanUtils.setProperty(object, "recordStat", RecordStatus.O.name());
			BeanUtils.setProperty(object, "updateDate", new Date());
			BeanUtils.setProperty(object, "makerId", SecurityUtils.getCurrentUserLogin().orElseGet(() -> "system"));
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}

	}

	public static void forUpdate(Object object) {
		try {
			String username = SecurityUtils.getCurrentUserLogin().orElseGet(() -> "system");
			BeanUtils.setProperty(object, "checkerDate", new Date());
			BeanUtils.setProperty(object, "checkerId", username);
			BeanUtils.setProperty(object, "updateDate", new Date());
			BeanUtils.setProperty(object, "updateId", username);
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}

	public static void forUpdateWithUser(Object object, String username) {
		try {
			BeanUtils.setProperty(object, "checkerDate", new Date());
			BeanUtils.setProperty(object, "checkerId", username);
			BeanUtils.setProperty(object, "updateDate", new Date());
			BeanUtils.setProperty(object, "updateId", username);
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}
	
	public static String toHashKey(String key) {
		return DigestUtils.md5DigestAsHex(key.getBytes());
	}
}
