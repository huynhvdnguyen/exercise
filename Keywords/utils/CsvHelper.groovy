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

import utils.JsonHelper

import java.text.SimpleDateFormat

public class CsvHelper {
		
	static void exportEmployees(String filePath, List<Employee> employees) {
		
		StringBuilder sb = new StringBuilder()
	
		// Header
		sb.append("Name,Position,Office,Age,StartDate,Salary\n")
	
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy")
	
		employees.each { emp ->
	
			String startDate =
				emp.startDate ? sdf.format(emp.startDate) : ""
	
			sb.append("${emp.name},")
			  .append("${emp.position},")
			  .append("${emp.office},")
			  .append("${emp.age},")
			  .append("${startDate},")
			  .append("${emp.salaryInDollar}")
			  .append("\n")
		}
	
		JsonHelper.writeFile(filePath, sb.toString())
	}
	
	static void exportUsdVnd(String filePath, BigDecimal usd, BigDecimal vnd) {
		
		StringBuilder sb = new StringBuilder()
	
		// Header
		sb.append("USD, VND\n")
		
		sb.append("${usd},${vnd}")
	
		JsonHelper.writeFile(filePath, sb.toString())
	}
}
