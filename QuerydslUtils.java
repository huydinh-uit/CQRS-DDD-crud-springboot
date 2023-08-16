package vn.com.vng.mcrusprofile.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;

import vn.com.vng.core.ag.ag_grid.utils.AccentRemover;

/**
 * @author MINH
 * 
 */
public final class QuerydslUtils {

	private static Logger log = LoggerFactory.getLogger(QuerydslUtils.class);

	private QuerydslUtils() {

	}

	private static final String APP_CONFIG_DATABASE = "app_config.database";

	private static String LIKE_TEMPLATE_ORACLE = "{0}  like  {1}  escape  '\\'";
	private static String LIKE_TEMPLATE_POSTGRES = "{0} similar to {1} ";
	private static String FTS_POSTGRES = "fts({0}, {1})";

	private static final String ESCAPE_ORACLE = "\\";

	private static final String ORACLE = "Oracle";
	private static final String POSTGRES = "Postgres";

	private static String escapeWithOracle(String str) {
		return str.replace("_", ESCAPE_ORACLE + "_").replace("%", ESCAPE_ORACLE + "%");

	}

	private static String escapeWithPostgres(String str) {
		return str.replace("\\", "[\\]").replace("_", "[_]").replace("%", "[%]");
	}

	public static BooleanExpression fulltextSearchWithPostgres(StringExpression exp, String str) {
		return Expressions.booleanTemplate(FTS_POSTGRES, exp,
				ConstantImpl.create(buildTsVectorValue(escapeWithPostgres(AccentRemover.removeAccentAndSpecialChars(str)))));
	}

	public static final String buildTsVectorValue(String str) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		
		str = str.trim();

		StringBuilder bd = new StringBuilder();
		String[] parts = str.split(" ");
		for (int count = 0; count < parts.length; count++) {
			String part = parts[count];
			if (!StringUtils.isEmpty(part)) {
				if (count == 0) {
					bd.append(part).append(":*");
				} else {
					bd.append(" & ").append(part).append(":*");
				}
			}
		}

		return bd.toString();
	}
	
	private static String DATE_EFFECTIVE = "(({0} is null and {1} is null) "
			+ "or ({0} is not null and {1} is not null and {0} <= {2} and {1} >= {2}) "
			+ "or ({0} is not null and {1} is null and {0} <= {2}) "
			+ "or ({0} is null and {1} is not null and {1} >= {2}))";
	
	public static BooleanExpression dateEffective(DateTimePath startDate, DateTimePath endDate, Date date) {
		return Expressions.booleanTemplate(DATE_EFFECTIVE, startDate, endDate,
				ConstantImpl.create(date));
	}
}
