package utils

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import utils.*

import groovy.json.JsonSlurper
import java.text.SimpleDateFormat

public class EmployeeReader {
	static List<Employee> readEmployeesFromExcel(String filePath, String sheetName) {
		
		ExcelHelper excel = new ExcelHelper(filePath, sheetName)
	
		List<Employee> employees = []
	
		try {
	
			int lastRow = excel.getRowCount()
	
			// Skip header row (row 0)
			for (int row = 1; row <= lastRow; row++) {
	
				Employee employee = new Employee(
					excel.getCellValue(row, "Name") as String,
					excel.getCellValue(row, "Position") as String,
					excel.getCellValue(row, "Office") as String,
					(excel.getCellValue(row, "Age") as Double).intValue(),
					excel.getCellValue(row, "Start Date"),
					BigDecimal.valueOf(excel.getCellValue(row, "Salary") as Double)
				)
	
				employees.add(employee)
			}
		} finally {
			excel.close()
		}
	
		return employees
	}
	
	static List<Employee> readEmployeesFromJson(String filePath) {
		
		File file = new File(filePath)
	
		if (!file.exists()) {
			throw new Exception("JSON file not found: " + filePath)
		}
	
		def jsonSlurper = new JsonSlurper()
		def data = jsonSlurper.parse(file)
	
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy")
	
		List<Employee> employees = []
	
		data.each { item ->
	
			Employee employee = new Employee(
				item.name,
				item.position,
				item.office,
				(item.age as Number).intValue(),
				item.startDate ? sdf.parse(item.startDate.toString()) : null,
				new BigDecimal(item.salaryInDollar.toString())
			)
	
			employees.add(employee)
		}
	
		return employees
	}
	
	static List<Employee> readEmployeesFromCsv(String filePath) {
		
		File file = new File(filePath)
	
		if (!file.exists()) {
			throw new Exception("CSV file not found: " + filePath)
		}
	
		List<Employee> employees = []
	
		List<String> lines = file.readLines("UTF-8")
	
		// Skip header
		for (int i = 1; i < lines.size(); i++) {
	
			String line = lines[i]
	
			if (line.trim().isEmpty()) {
				continue
			}
	
			// Split CSV (simple version)
			String[] tokens = line.split(",")
	
			Employee employee = new Employee(
				tokens[0].replaceAll('"', '').trim(),              // name
				tokens[1].replaceAll('"', '').trim(),              // position
				tokens[2].replaceAll('"', '').trim(),              // office
				tokens[3].trim() as Integer,                       // age
				tokens[4] ? new SimpleDateFormat("dd/MM/yyyy")
							.parse(tokens[4].replaceAll('"','').trim()) : null,
				new BigDecimal(tokens[5].trim())                   // salary
			)
	
			employees.add(employee)
		}
	
		return employees
	}
}
