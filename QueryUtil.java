/**
* File Name  : QueryUtil.java
* Package    : vn.com.vng.mcrusprofile.util
* Author     : sonnd
* Created    : Aug 12, 2018 8:45:25 PM

* This source code is part of the mcrusprofile, and is copyrighted by A4B. 
* All rights reserved.  No part of this work may be reproduced, stored in a retrieval system,
* adopted or transmitted in any form or by any means,electronic, mechanical, photographic,
* graphic, optic recording or otherwise,translated in any language or computer language,
* without the prior written permission of A4B 

Copyright © 2017 - 2018 by A4B
-------------------------------------------------------------------
* Version Control:
*       $LastChangedRevision$
*       $LastChangedBy$
*       $LastChangedDate$
* 
-------------------------------------------------------------------
*/
package vn.com.vng.mcrusprofile.util;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.util.Pair;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;

import vn.com.vng.core.ag.ag_grid.builder.AgGridConstants;
import vn.com.vng.core.ag.ag_grid.builder.AgGridQueryBuilder;
import vn.com.vng.core.ag.ag_grid.filter.ColumnFilter;
import vn.com.vng.core.ag.ag_grid.filter.NumberColumnFilter;
import vn.com.vng.core.ag.ag_grid.filter.TextColumnFilter;
import vn.com.vng.core.ag.ag_grid.request.AgGridGetRowsRequest;
import vn.com.vng.core.ag.ag_grid.utils.AccentRemover;
import vn.com.vng.core.ag.ag_grid.utils.QueryUtils;
import vn.com.vng.core.server.data.FilterCfg;
import vn.com.vng.core.server.data.LoadCfg;
import vn.com.vng.core.server.data.LoadCfg.SortDir;
import vn.com.vng.mcrusprofile.dto.ExcelExportingRequestDto;
import vn.com.vng.mcrusprofile.security.SecurityUtils;

/**
 * @author sonnd
 *
 */
public class QueryUtil {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static JPAQuery addOrder(LoadCfg.SortDir sortDir, JPAQuery query, ComparableExpressionBase path) {
		if (SortDir.ASC.equals(sortDir)) {
			query = (JPAQuery) query.orderBy(path.asc());
		} else {
			query = (JPAQuery) query.orderBy(path.desc());
		}
		return query;
	}


	public static Date getDateValue(FilterCfg filter) {
		if (filter.getValue() == null) {
			return null;
		}

		Date dateVal = null;
		if (filter.getValue() instanceof Number) {
			Double numVal = Double.parseDouble(String.valueOf(filter.getValue()));
			dateVal = new Date(numVal.longValue());
		} else if (filter.getValue() instanceof Date) {
			dateVal = (Date) filter.getValue();
		}
		return dateVal;
	}

	public static Pair<Number, Number> getNumericRange(FilterCfg filter) {
		if (filter.getValue() == null) {
			return null;
		}

		String[] range = String.valueOf(filter.getValue()).split("#");
		if (range.length != 2) {
			return null;
		}
		Number fromVal = Double.parseDouble(range[0]);
		Number toVal = Double.parseDouble(range[1]);
		return Pair.of(fromVal, toVal);
	}
	
	public static Pair<Double, Double> getDoubleRange(FilterCfg filter) {
		if (filter.getValue() == null) {
			return null;
		}

		String[] range = String.valueOf(filter.getValue()).split("#");
		if (range.length != 2) {
			return null;
		}
		Double fromVal = Double.parseDouble(range[0]);
		Double toVal = Double.parseDouble(range[1]);
		return Pair.of(fromVal, toVal);
	}

	public static Pair<Date, Date> getDateRange(FilterCfg filter) {
		if (filter.getValue() == null) {
			return null;
		}

		String[] range = String.valueOf(filter.getValue()).split("#");
		if (range.length != 2) {
			return null;
		}
		Double fromVal = Double.parseDouble(range[0]);
		Double toVal = Double.parseDouble(range[1]);
		return Pair.of(new Date(fromVal.longValue()), new Date(toVal.longValue()));
	}

	public static void addStringFilter(BooleanBuilder whereExp, StringPath valuePath, FilterCfg filter) {
		if (filter.getValue() == null) {
			whereExp.and(valuePath.isNull());
			return;
		}

		String stringVal = String.valueOf(filter.getValue());

		if (FilterCfg.COMPARISION_LIKE.equals(filter.getComparison())) {
			whereExp.and(valuePath.like("%" + stringVal + "%"));
		} else if (FilterCfg.COMPARISION_NE.equals(filter.getComparison())) {
			whereExp.and(valuePath.ne(stringVal));
		} else if (FilterCfg.COMPARISION_EQ.equals(filter.getComparison())) {
			whereExp.and(valuePath.eq(String.valueOf(filter.getValue())));
		}else if (FilterCfg.COMPARISION_NE_OR_NULL.equals(filter.getComparison())) {
			whereExp.andAnyOf(valuePath.ne(stringVal), valuePath.isNull());
		} else if (FilterCfg.COMPARISION_EQ_OR_NULL.equals(filter.getComparison())) {
			whereExp.andAnyOf(valuePath.eq(stringVal), valuePath.isNull());
		} else {
			whereExp.and(valuePath.like("%" + stringVal + "%"));
		}
	}

	public static void addDoubleFilter(BooleanBuilder whereExp, NumberPath<Double> valuePath, FilterCfg filter) {
		if (filter.getValue() == null) {
			whereExp.and(valuePath.isNull());
			return;
		}

		if (FilterCfg.COMPARISION_ON.equals(filter.getComparison())) {
			Pair<Double, Double> range = getDoubleRange(filter);
			if (range == null) {
				return;
			}
			Double fromVal = range.getFirst();
			Double toVal = range.getSecond();
			whereExp.and(valuePath.gt(fromVal).and(valuePath.lt(toVal)));
			return;
		}

		Double numericVal = Double.parseDouble(String.valueOf(filter.getValue()));
		if (FilterCfg.COMPARISION_GT.equals(filter.getComparison())) {
			whereExp.and(valuePath.gt(numericVal));
		} else if (FilterCfg.COMPARISION_GE.equals(filter.getComparison())) {
			whereExp.and(valuePath.goe(numericVal));
		} else if (FilterCfg.COMPARISION_LT.equals(filter.getComparison())) {
			whereExp.and(valuePath.lt(numericVal));
		} else if (FilterCfg.COMPARISION_LE.equals(filter.getComparison())) {
			whereExp.and(valuePath.loe(numericVal));
		} else if (FilterCfg.COMPARISION_NE.equals(filter.getComparison())) {
			whereExp.and(valuePath.ne(numericVal));
		} else if (FilterCfg.COMPARISION_NE_OR_NULL.equals(filter.getComparison())) {
			whereExp.andAnyOf(valuePath.ne(numericVal), valuePath.isNull());
		} else if (FilterCfg.COMPARISION_EQ_OR_NULL.equals(filter.getComparison())) {
			whereExp.andAnyOf(valuePath.eq(numericVal), valuePath.isNull());
		} else {
			whereExp.and(valuePath.eq(numericVal));
		}
	}
	
	 public static void addNumberFilter(BooleanBuilder whereExp, NumberPath valuePath, FilterCfg filter) {
		 if (filter.getValue() == null) {
				whereExp.and(valuePath.isNull());
				return;
			}

			if (FilterCfg.COMPARISION_ON.equals(filter.getComparison())) {
				Pair<Number, Number> range = getNumericRange(filter);
				if (range == null) {
					return;
				}
				Number fromVal = range.getFirst();
				Number toVal = range.getSecond();
				whereExp.and(valuePath.gt(fromVal).and(valuePath.lt(toVal)));
				return;
			}

			Number numericVal = Double.parseDouble(String.valueOf(filter.getValue()));
			if (FilterCfg.COMPARISION_GT.equals(filter.getComparison())) {
				whereExp.and(valuePath.gt(numericVal));
			} else if (FilterCfg.COMPARISION_GE.equals(filter.getComparison())) {
				whereExp.and(valuePath.goe(numericVal));
			} else if (FilterCfg.COMPARISION_LT.equals(filter.getComparison())) {
				whereExp.and(valuePath.lt(numericVal));
			} else if (FilterCfg.COMPARISION_LE.equals(filter.getComparison())) {
				whereExp.and(valuePath.loe(numericVal));
			} else if (FilterCfg.COMPARISION_NE.equals(filter.getComparison())) {
				whereExp.and(valuePath.ne(numericVal));
			} else if (FilterCfg.COMPARISION_NE_OR_NULL.equals(filter.getComparison())) {
				whereExp.andAnyOf(valuePath.ne(numericVal), valuePath.isNull());
			} else if (FilterCfg.COMPARISION_EQ_OR_NULL.equals(filter.getComparison())) {
				whereExp.andAnyOf(valuePath.eq(numericVal), valuePath.isNull());
			} else {
				whereExp.and(valuePath.eq(numericVal));
			}
	    }

	public static void addBooleanFilter(BooleanBuilder whereExp, BooleanPath valuePath, FilterCfg filter) {
		if (filter.getValue() == null) {
			whereExp.and(valuePath.isNull());
			return;
		}
		Boolean booleanVal = Boolean.TRUE.equals(filter.getValue());
		if (FilterCfg.COMPARISION_NE.equals(filter.getComparison())) {
			whereExp.and(valuePath.ne(booleanVal));
		} else if (FilterCfg.COMPARISION_NE_OR_NULL.equals(filter.getComparison())) {
			whereExp.andAnyOf(valuePath.ne(booleanVal), valuePath.isNull());
		} else if (FilterCfg.COMPARISION_EQ_OR_NULL.equals(filter.getComparison())) {
			whereExp.andAnyOf(valuePath.eq(booleanVal), valuePath.isNull());
		} else {
			whereExp.and(valuePath.eq(booleanVal));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addDateFilter(BooleanBuilder whereExp, DateTimePath valuePath, FilterCfg filter) {
		if (filter.getValue() == null) {
			whereExp.and(valuePath.isNull());
			return;
		}

		if (FilterCfg.COMPARISION_ON.equals(filter.getComparison())) {
			Pair<Date, Date> range = getDateRange(filter);
			if (range == null) {
				return;
			}
			Date fromVal = range.getFirst();
			Date toVal = range.getSecond();
			whereExp.and(valuePath.gt(fromVal).and(valuePath.lt(toVal)));
			return;
		}

		Date dateVal = getDateValue(filter);
		if (dateVal == null) {
			return;
		}

		if (FilterCfg.COMPARISION_AFTER.equals(filter.getComparison())
				|| FilterCfg.COMPARISION_GT.equals(filter.getComparison())) {
			whereExp.and(valuePath.gt(dateVal));
		} else if (FilterCfg.COMPARISION_AFTER_OR_EQUAL.equals(filter.getComparison())
				|| FilterCfg.COMPARISION_GE.equals(filter.getComparison())) {
			whereExp.and(valuePath.goe(dateVal));
		} else if (FilterCfg.COMPARISION_BEFORE.equals(filter.getComparison())
				|| FilterCfg.COMPARISION_LT.equals(filter.getComparison())) {
			whereExp.and(valuePath.lt(dateVal));
		} else if (FilterCfg.COMPARISION_BEFORE_OR_EQUAL.equals(filter.getComparison())
				|| FilterCfg.COMPARISION_LE.equals(filter.getComparison())) {
			whereExp.and(valuePath.loe(dateVal));
		} else if (FilterCfg.COMPARISION_NE.equals(filter.getComparison())) {
			whereExp.and(valuePath.ne(dateVal));
		} else if (FilterCfg.COMPARISION_NE_OR_NULL.equals(filter.getComparison())) {
			whereExp.andAnyOf(valuePath.ne(dateVal), valuePath.isNull());
		} else if (FilterCfg.COMPARISION_EQ_OR_NULL.equals(filter.getComparison())) {
			whereExp.andAnyOf(valuePath.eq(dateVal), valuePath.isNull());
		} else {
			whereExp.and(valuePath.eq(dateVal));
		}
	}
	
	public static final AgGridGetRowsRequest addStringPivotRequestStringFilter(AgGridGetRowsRequest request, String filterName, String value, String comparision) {
		if(request.getFilterModel() == null) {
			request.setFilterModel(new HashMap<>());
		}
		
		ColumnFilter tenantCodeFilter = new TextColumnFilter(comparision, value);
		request.getFilterModel().put(filterName, tenantCodeFilter);
		return request;
	}

	public static final ExcelExportingRequestDto addStringPivotRequestStringFilter(ExcelExportingRequestDto request, String filterName, String value, String comparision) {
		if(request.getFilterModel() == null) {
			request.setFilterModel(new HashMap<>());
		}
		
		ColumnFilter tenantCodeFilter = new TextColumnFilter(comparision, value);
		request.getFilterModel().put(filterName, tenantCodeFilter);
		return request;
	}
	
	public static final AgGridGetRowsRequest addPivotRequestNumberFilter(AgGridGetRowsRequest request, String filterName, Long value, String comparision) {
		if(request.getFilterModel() == null) {
			request.setFilterModel(new HashMap<>());
		}
		
		ColumnFilter tenantCodeFilter = new NumberColumnFilter(comparision, value.intValue(), null);
		request.getFilterModel().put(filterName, tenantCodeFilter);
		return request;
	}
	
	public static final String escapePostgresLiteral(String str) {
		try {
			return org.postgresql.core.Utils.escapeLiteral(null, str, true).toString();
		} catch (SQLException e) {
			return str;
		}
	}
	public static final AgGridGetRowsRequest addTenantCodeFilter(AgGridGetRowsRequest request, String tenantCode) {
		if(request.getFilterModel() == null) {
			request.setFilterModel(new HashMap<>());
		}
		
		ColumnFilter tenantCodeFilter = new TextColumnFilter(AgGridConstants.OP_EQUALS, tenantCode);
		request.getFilterModel().put("tenantCode", tenantCodeFilter);
		return request;
	}
	
	public static final AgGridGetRowsRequest rebuildMakerFilter(AgGridGetRowsRequest request) {
		if(request.getFilterModel() == null) {
			request.setFilterModel(new HashMap<>());
		}
		
		Map<String, ColumnFilter> filter = request.getFilterModel();
		TextColumnFilter makerFilter = (TextColumnFilter)filter.get("makerId");
		if(makerFilter != null) {
			String filterValue = makerFilter.getFilter();
			String newfilterValue = TextUtils.buildClientId(filterValue, SecurityUtils.getCurrentUserTenantCode());
			
			ColumnFilter makerIdFilter = new TextColumnFilter(AgGridConstants.OP_EQUALS, newfilterValue);
			request.getFilterModel().put("makerId", makerIdFilter);
		}
		
		return request;
	}
	
	public static final String ftsValueToMe(AgGridGetRowsRequest request) {
		if (request.getFilterModel() == null) {
			request.setFilterModel(new HashMap<>());
		}

		Map<String, ColumnFilter> filter = request.getFilterModel();
		TextColumnFilter ftsValueToMe = (TextColumnFilter) filter.get("ftsValueToMe");
		if (ftsValueToMe != null) {

			String filterValue = ftsValueToMe.getFilter();
			if (AgGridQueryBuilder.isPhrase(filterValue)) {
				return "t.fts_value_to_me" + " @@ phraseto_tsquery('"
						+ AccentRemover.removeAccentAndSpecialChars(removeDoubleQuote(filterValue)) + "')";
			}
			return "t.fts_value_to_me" + " @@ to_tsquery('"
					+ QueryUtils.buildTsVectorValue(AccentRemover.removeAccentAndSpecialChars(filterValue)) + "')";
		}

		return null;
	}
	
	public static final AgGridGetRowsRequest removeSearchKey(AgGridGetRowsRequest request,String key) {
		if(request.getFilterModel() == null) {
			request.setFilterModel(new HashMap<>());
		}
		
		Map<String, ColumnFilter> filter = request.getFilterModel();
		TextColumnFilter keyFilter = (TextColumnFilter)filter.get(key);
		if(keyFilter != null) {
			request.getFilterModel().remove(key);
		}
		
		return request;
	}
	
	
	private static final AgGridGetRowsRequest addPivotRequestNumberFilter(AgGridGetRowsRequest request, String filterName,
			Number value, String comparision) {
		if (request.getFilterModel() == null) {
			request.setFilterModel(new HashMap<>());
		}

		ColumnFilter tenantCodeFilter = new NumberColumnFilter(comparision, value.intValue(), null);
		request.getFilterModel().put(filterName, tenantCodeFilter);
		return request;
	}
	
	public static String removeDoubleQuote(String filterValue) {
    	try {
    		filterValue = filterValue.trim();
    		filterValue = filterValue.substring(1, filterValue.length() - 1);
    		return filterValue;
    	}
    	catch(Exception ex) {
    		return filterValue;
    	}
    }
}
