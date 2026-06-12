package utils

import java.io.FileInputStream

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.CellValue
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

public class ExcelHelper {
	private Workbook workbook
	private Sheet sheet
	private Map<String, Integer> headerMap = [:]
	private FormulaEvaluator formulaEvaluator
	
	ExcelHelper(String filePath, String sheetName) {
		
		FileInputStream fis = new FileInputStream(filePath)

		this.workbook = new XSSFWorkbook(fis)

		this.sheet = this.workbook.getSheet(sheetName)

		if (this.sheet == null) {
			throw new Exception("Sheet '${sheetName}' not found.")
		}
		
		this.formulaEvaluator = this.workbook.getCreationHelper().createFormulaEvaluator()
		
		initializeHeaderMap()
		
		fis.close()
	}
	
	private void initializeHeaderMap() {
		
		Row headerRow = sheet.getRow(0)
	
		if (headerRow == null) {
			return
		}
	
		for (int i = 0; i < headerRow.getLastCellNum(); i++) {
	
			Cell cell = headerRow.getCell(i)
	
			if (cell != null) {
				headerMap.put(
					cell.getStringCellValue().trim().toLowerCase(),
					i
				)
			}
		}
	}
	
	Object getCellValue(int rowNum, String columnName) {
		
		Integer columnIndex =
			headerMap.get(columnName.trim().toLowerCase())
	
		if (columnIndex == null) {
			throw new Exception(
				"Column '${columnName}' not found."
			)
		}
	
		return getCellValue(rowNum, columnIndex)
	}
	
	Object getCellValue(int rowNum, int colNum) {
		
		Row row = this.sheet.getRow(rowNum)

		if (row == null) {
			return ""
		}

		Cell cell = row.getCell(colNum)

		if (cell == null) {
			return ""
		}

		switch (cell.getCellTypeEnum()) {

	        case CellType.STRING:
	            return cell.getStringCellValue()
	
	        case CellType.NUMERIC:
	
	            if (DateUtil.isCellDateFormatted(cell)) {
	                return cell.getDateCellValue()      // java.util.Date
	            }
	
	            return cell.getNumericCellValue()       // Double
	
	        case CellType.BOOLEAN:
	            return cell.getBooleanCellValue()       // Boolean
	
	        case CellType.FORMULA:
	
	            CellValue evaluatedValue = formulaEvaluator.evaluate(cell)
	
	            switch (evaluatedValue.getCellTypeEnum()) {
	
	                case CellType.STRING:
	                    return evaluatedValue.getStringValue()
	
	                case CellType.NUMERIC:
	                    return evaluatedValue.getNumberValue()
	
	                case CellType.BOOLEAN:
	                    return evaluatedValue.getBooleanValue()
	
	                default:
	                    throw new IllegalArgumentException(
					        "Unsupported formula result type: ${evaluatedValue.getCellTypeEnum()}"
					    )
	            }
	
	        case CellType.BLANK:
	            return null
	
	        case CellType.ERROR:
	            return cell.getErrorCellValue()         // Byte
	
	        default:
	            throw new IllegalArgumentException(
		            "Unsupported cell type: ${cell.getCellTypeEnum()}"
		        )
	    }
	}
	
	int getRowCount() {
		int lastRow = sheet.getLastRowNum()

	    while (lastRow >= 0) {
	
	        Row row = sheet.getRow(lastRow)
	
	        if (row != null && !isRowEmpty(row)) {
	            return lastRow
	        }
	
	        lastRow--
	    }
	
	    return 0
	}
	
	private boolean isRowEmpty(Row row) {
		for (int c = row.getFirstCellNum();
			 c < row.getLastCellNum();
			 c++) {
	
			Cell cell = row.getCell(c)
	
			if (cell != null &&
				cell.getCellType() != CellType.BLANK) {
	
				return false
			}
		}
	
		return true
	}

	int getColumnCount() {

		Row headerRow = this.sheet.getRow(0)

		return headerRow != null ?
			   headerRow.getLastCellNum() : 0
	}

	void close() {
		this.workbook.close()
	}
}
