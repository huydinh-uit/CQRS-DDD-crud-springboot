package vn.com.vng.mcrusprofile.util;

public final class NameCreatorUtils {
	public static final String SPLIT_EMAIL = "@";

	public static final String DOT = "\\.";

	public static final String ROLE = "ROLE";

	public static final String UNDERSCORE = "_";

	public static final String ROLE_NAME_FORMAT_OF_TENANT = "ROLE_{APP_CODE}_{TENANT_CODE}_{NAME}";

	public static final String ROLE_NAME_FORMAT_OF_APP = "ROLE_{APP_CODE}_{NAME}";

	public static final String USER_CODE_FORMAT = "{APP_CODE}.{TENANT_CODE}.{USER_CODE}";

//	public static String roleNameCreator(String roleName) {
//		return roleNameCreator(roleName, AppInfo.getAppCode(), AppInfo.getTenantCode());
//	}

	public static String roleNameCreator(String roleName, String appCode, String tenantCode) {
		roleName = roleName.toUpperCase();
		if(roleName.startsWith("ROLE_")) {
			return roleName;
		}
		return "ROLE_{APP_CODE}_{TENANT_CODE}_{NAME}".replace("{APP_CODE}", appCode)
				.replace("{TENANT_CODE}", tenantCode).replace("{NAME}", roleName);
	}

	public static String roleNameCreatorOfApp(String roleName, String appCode) {
		roleName = roleName.toUpperCase();
		if(roleName.startsWith("ROLE_")) {
			return roleName;
		}
		return "ROLE_{APP_CODE}_{NAME}".replace("{APP_CODE}", appCode).replace("{NAME}", roleName);
	}
	
	public static String getNameOfRole(String role,String appCode, String tenantCode) {
		String roleNamePrefixOfTenant = String.format("ROLE_%s_%s_", appCode, tenantCode);
		String roleNamePrefixOfApp = String.format("ROLE_%s_", appCode);
		return role.replace(roleNamePrefixOfTenant, "").replace(roleNamePrefixOfApp, "");
	}

	public static String userCodeCreator(String userName, String appCode, String tenantCode) {
		return "{APP_CODE}.{TENANT_CODE}.{USER_CODE}".replace("{APP_CODE}", appCode)
				.replace("{TENANT_CODE}", tenantCode).replace("{USER_CODE}", userName);
	}

//	public static String userCodeCreator(String userName) {
//		return userCodeCreator(userName, AppInfo.getAppCode(), AppInfo.getTenantCode());
//	}

	public static String getUserNameFromEmail(String email) {
		String[] strArr = email.split("@");
		return strArr[0];
	}

	public static String getTenantCodeFromEmail(String email) {
		String[] strArr = email.split("@");
		String item = strArr[1];
		String[] dots = item.split("\\.");
		return dots[0].toUpperCase();
	}
	
	public static String getDomain(String email) {
		if (hasEmail(email)) {
			return email.substring(email.indexOf("@") + 1, email.length());
		}
		return "a4b.com";
	}
	
	public static boolean hasEmail(String username) {
		return username.contains("@");
	}
}
