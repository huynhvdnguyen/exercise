package utils

import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat

import groovy.json.JsonOutput

public class JsonHelper {
	/**
	 * Export any list of objects (e.g., Employee list) to JSON file
	 */
	static void exportToJson(List data, String filePath) {

		String json = JsonOutput.prettyPrint(
			JsonOutput.toJson(data)
		)

		FileWriter.writeFile(filePath, json)
	}
	
	/**
	 * Export Employee list with formatted date
	 */
	static void exportEmployees(List employees, String filePath) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy")

		def exportData = employees.collect { emp ->
			return [
				name          : emp.name,
				position      : emp.position,
				office        : emp.office,
				age           : emp.age,
				startDate     : emp.startDate ? sdf.format(emp.startDate) : null,
				salaryInDollar: emp.salaryInDollar
			]
		}

		String json = JsonOutput.prettyPrint(
			JsonOutput.toJson(exportData)
		)

		FileWriter.writeFile(filePath, json)
	}
	
	static void exportUsdVnd(String filePath, BigDecimal usd, BigDecimal vnd) {
		
		def data = [
			USD: usd,
			VND: vnd
		]
	
		String json = JsonOutput.prettyPrint(
			JsonOutput.toJson(data)
		)
	
		FileWriter.writeFile(filePath, json)
	}
}
