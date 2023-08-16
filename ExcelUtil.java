package vn.com.vng.mcrusprofile.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import vn.com.vng.mcrusprofile.constant.DataType;
import vn.com.vng.mcrusprofile.dto.ColumnDto;
import vn.com.vng.mcrusprofile.dto.ReportColumnDto;
import vn.com.vng.mcrusprofile.dto.ReportDecorateDto;

/**
 * @author sonnd
 *
 */
public class ExcelUtil {

	public static final List<ReportColumnDto> buildColumns(String[] headers, String[] nameMapping) {
		List<ReportColumnDto> columns = new ArrayList<>();
		for (int i = 0; i < nameMapping.length; i++) {
			ReportColumnDto col = new ReportColumnDto(nameMapping[i], headers[i], null, false, null);
			columns.add(col);
		}

		return columns;
	}
	
	public static final String buildExcelFileStreaming(List data, List<ReportColumnDto> columns)
			throws IOException {
		return buildExcelFileStreaming(data, columns, null, false);
	}

	public static final String buildExcelFileStreaming(List data, List<ReportColumnDto> columns,
			ReportDecorateDto reportDecorate, boolean addIdxColumn) throws IOException {

		// Write the output to a file
		File outputFile = File.createTempFile(UUID.randomUUID().toString(), ".xlsx");
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(outputFile);
			// Create a Sheet
			String sheetName = reportDecorate != null && StringUtils.isNotEmpty(reportDecorate.getReportTitle())
					? reportDecorate.getReportTitle()
					: "Data";
			// Create a Workbook
			Workbook workbook = new Workbook(fileOut, "iCollect", "1.0");
			Worksheet sheet = workbook.newWorksheet(sheetName);
			int dataStartRowNum = doGenerateExcelFileStructure(workbook, data, columns, reportDecorate, sheet, addIdxColumn);
			doGenerateExcelData(workbook, data, columns, reportDecorate, sheet, dataStartRowNum, addIdxColumn);
			workbook.finish();
			return outputFile.getAbsolutePath();
		} finally {
			if (fileOut != null) {
				fileOut.close();
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final int doGenerateExcelFileStructure(Workbook workbook, List<Map<String, Object>> data,
			List columns, ReportDecorateDto reportDecorate, Worksheet ws, boolean addIdxColumn) throws IOException {
		if (columns != null && addIdxColumn) {
			ReportColumnDto idxColumn = new ReportColumnDto("_idx_", "IDX", null, false, null);
			columns.add(0, idxColumn);
		}

		int rowNum = 0;

		if (reportDecorate != null && StringUtils.isNotEmpty(reportDecorate.getReportTitle())) {
			int maxColumnSize = columns.size();
			if (reportDecorate.getReportFields() != null && maxColumnSize < 5) {
				maxColumnSize = 5;
			}
			ws.value(rowNum, 0, reportDecorate.getReportTitle());
			ws.range(rowNum, 0, rowNum + 1, maxColumnSize - 1).style().fontSize(14).bold().horizontalAlignment("center")
					.set();
			ws.range(rowNum, 0, rowNum + 1, maxColumnSize - 1).merge();

			rowNum += 2;

			// empty row
			ws.value(rowNum, 0, "");
			rowNum++;
		}

		// Filter form fields
//        if(reportDecorate != null && CollectionUtils.isNotEmpty(reportDecorate.getReportFields())) {
//	        Font fieldLabelFont = workbook.createFont();
//	        fieldLabelFont.setBold(true);
//	        fieldLabelFont.setFontHeightInPoints((short) 11);
//	        
//	        Font fieldValueFont = workbook.createFont();
//	        fieldValueFont.setBold(false);
//	        fieldValueFont.setFontHeightInPoints((short) 11);
//	        
//	        CellStyle fieldLabelCellStyle = workbook.createCellStyle();
//	        fieldLabelCellStyle.setFont(fieldLabelFont);
//	        
//	        CellStyle fieldValueCellStyle = workbook.createCellStyle();
//	        fieldValueCellStyle.setFont(fieldValueFont);
//	        
//	        Row filterFormRow = sheet.createRow(rowNum);
//	        for(int i = 0; i < reportDecorate.getReportFields().size(); i ++) {
//	        	int colIdx = (i % 2) * 2 + (i % 2);
//	        	
//	        	ReportFieldDto reportField = reportDecorate.getReportFields().get(i);
//	        	// label
//	            Cell fieldLabelCell = filterFormRow.createCell(colIdx);
//	            fieldLabelCell.setCellValue(reportField.getTitle());
//	            fieldLabelCell.setCellStyle(fieldLabelCellStyle);
//	            
//	            // value
//	            Cell fieldValueCell = filterFormRow.createCell(colIdx + 1);
//	            fieldValueCell.setCellValue(reportField.getValue());
//	            fieldValueCell.setCellStyle(fieldValueCellStyle);
//	        	
//	        	if(i % 2 == 1) {
//	        		rowNum ++;
//	        		filterFormRow = sheet.createRow(rowNum);
//	        	}
//	        }
//	        
//	        rowNum ++;
//	        Row spaceRow = sheet.createRow(rowNum);
//	        Cell emptyCell = spaceRow.createCell(0);
//	        emptyCell.setCellValue("");
//	        rowNum ++;
//        }
		//

		for (int i = 0; i < columns.size(); i++) {
			ColumnDto column = (ColumnDto) columns.get(i);
			ws.style(rowNum, i).bold().fontSize(12).set();
			String val = !StringUtils.isEmpty(column.getTitle()) ? column.getTitle() : column.getName();
			ws.value(rowNum, i, val);
		}

		rowNum++;
		return rowNum;
	}

	@SuppressWarnings("rawtypes")
	public static final Workbook doGenerateExcelData(Workbook workbook, List data, List columns,
			ReportDecorateDto reportDecorate, Worksheet sheet, int dataStartRowNum, boolean addIdxColumn)
			throws IOException {
		for (int dataIdx = 0; dataIdx < data.size(); dataIdx++) {
			int rowNum = dataStartRowNum++;

			for (int i = 0; i < columns.size(); i++) {
				ColumnDto column = (ColumnDto) columns.get(i);
				Object objVal = null;
				if (addIdxColumn && "_idx_".equals(column.getName())) {
					objVal = (dataIdx + 1);
				} else {
					objVal = getItemValue(data.get(dataIdx), column.getName());
				}

				if (objVal instanceof Number) {
					if ("currency".equals(column.getTemplate())) {
						sheet.style(rowNum, i).format("#.###############").set();
					} else {
						sheet.style(rowNum, i).format("#.###############").set();
					}
					sheet.value(rowNum, i, (Number) objVal);
				} else if (objVal instanceof Date) {
					sheet.value(rowNum, i, (Date) objVal);
					sheet.style(rowNum, i).format("dd/MM/yyyy HH:mm:ss").set();
				} else {
					String val = getStringValue(objVal, column);
					sheet.value(rowNum, i, val);
				}
			}
		}

		return workbook;
	}
	
	private static Object getItemValue(Object item, String name) {
		if(item instanceof Map) {
			return ((Map) item).get(name);
		}
		else {
			try {
				Object val = PropertyUtils.getProperty(item, name);
				return val;
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public static String getStringValue(Object value, ColumnDto column) {
		if (value == null) {
			return null;
		}

		String stringValue = null;

		if (StringUtils.isNotEmpty(column.getTemplate())) {
			try {
				stringValue = applyDefaultTemplate(value, column);
			} catch (IOException e) {
				try {
					stringValue = applyDefaultTemplate(value, column);
				} catch (IOException e1) {
					stringValue = getDefaultValue(value);
				}
			}
		} else {
			try {
				stringValue = applyDefaultTemplate(value, column);
			} catch (IOException e) {
				stringValue = getDefaultValue(value);
			}
		}

		// Apply i18n for cell value?
		if (column instanceof ReportColumnDto && Boolean.TRUE.equals(((ReportColumnDto) column).getUseI18n())
				&& ((ReportColumnDto) column).getI18nSource() != null) {
			stringValue = ((ReportColumnDto) column).getI18nSource().getOrDefault(stringValue, stringValue);
		}

		return stringValue;
	}

	private static String getDefaultValue(Object value) {
		if (value instanceof Date) {
			SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
			return fmt.format(value);
		} else if (value instanceof Number) {
			DecimalFormat formatter = new DecimalFormat("00000");
			return formatter.format(value);
		} else {
			return String.valueOf(value);
		}
	}

	private static String applyDefaultTemplate(Object value, ColumnDto column) throws IOException {
		String dataType = FieldDataUtil.detectDataType(value);
		String val = null;
		switch (dataType) {
		case DataType.BOOLEAN:
			val = Boolean.TRUE.equals(value) ? "true" : "false";
			break;
		case DataType.STRING:
			val = (String) value;
			break;
		case DataType.DATE:
			val = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss").print(new DateTime((Date) value));
			break;
		case DataType.NUMERIC:
			val = String.valueOf(value);
			break;
		case DataType.JSON_ARRAY:
			val = "<array>";
			break;
		case DataType.JSON_OBJECT:
			val = "<object>";
			break;
		default:
			break;
		}

		if (val == null) {
			return String.valueOf(value);
		}

		return val;
	}
}
