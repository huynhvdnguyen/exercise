package utils

import java.time.format.DateTimeFormatter
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

public class EmployeeReader {
	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy"
	
	static List<Employee> readEmployeesFromExcel(String filePath, String sheetName) {
		
		ExcelHelper excel = new ExcelHper(filePath, sheetName)
	
		List<Employee> employees = []
	
		try {
	
			int lastRow = excel.getRowCount()
	
			// Skip header row (row 0)
			for (int row = 1; row <= lastRow; row++) {
				String name = excel.getCellValue(row, "Name")?.toString()?.trim()
	            String position = excel.getCellValue(row, "Position")?.toString()?.trim()
	            String office = excel.getCellValue(row, "Office")?.toString()?.trim()
	            String startDate = excel.getCellValue(row, "Start Date")?.toString()?.trim()
	
	            Integer age = null
	            def ageValue = excel.getCellValue(row, "Age")
	            if (ageValue != null) {
	                age = (ageValue as Number).intValue()
	            }
	
	            BigDecimal salary = null
	            def salaryValue = excel.getCellValue(row, "Salary")
	            if (salaryValue != null) {
	                salary = BigDecimal.valueOf((salaryValue as Number).doubleValue())
	            }
	
	            employees.add(new Employee(
	                name,
	                position,
	                office,
	                age,
	                startDate,
	                salary
	            ))
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
	
		DateTimeFormatter sdf = new DateTimeFormatter(DEFAULT_DATE_FORMAT)
	
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
			
			// Build header map
			String[] headers = lines[0].split(",")
			Map<String, Integer> headerMap = [:]
		
			headers.eachWithIndex { header, index ->
				headerMap.put(
					header.replaceAll('"', '').trim(),
					index
				)
			}
	
			Employee employee = new Employee(
	            tokens[headerMap["Name"]].replaceAll('"', '').trim(),
	            tokens[headerMap["Position"]].replaceAll('"', '').trim(),
	            tokens[headerMap["Office"]].replaceAll('"', '').trim(),
	            tokens[headerMap["Age"]].trim() as Integer,
	            tokens[headerMap["StartDate"]] ?
	                new DateTimeFormatter(DEFAULT_DATE_FORMAT).parse(
	                    tokens[headerMap["StartDate"]]
	                        .replaceAll('"', '')
	                        .trim()
	                ) : null,
	            new BigDecimal(
	                tokens[headerMap["Salary"]]
	                    .replaceAll('"', '')
	                    .trim()
	            )
	        )

	
			employees.add(employee)
		}
	
		return employees
	}
	
	/**
	 * Export Employee list with formatted date
	 */
	static void exportEmployeesInJson(List employees, String filePath) {

		DateTimeFormatter sdf = new DateTimeFormatter(DEFAULT_DATE_FORMAT)

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
	
	static void exportUsdVndInJson(String filePath, BigDecimal usd, BigDecimal vnd) {
		
		def data = [
			USD: usd,
			VND: vnd
		]
	
		String json = JsonOutput.prettyPrint(
			JsonOutput.toJson(data)
		)
	
		FileWriter.writeFile(filePath, json)
	}
	
	static void exportEmployeesInCsv(String filePath, List<Employee> employees) {
		
		StringBuilder sb = new StringBuilder()
	
		// Header
		sb.append("Name,Position,Office,Age,StartDate,Salary\n")
	
		DateTimeFormatter sdf = new DateTimeFormatter(DEFAULT_DATE_FORMAT)
	
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
	
		FileWriter.writeFile(filePath, sb.toString())
	}
	
	static void exportUsdVndInCsv(String filePath, BigDecimal usd, BigDecimal vnd) {
		
		StringBuilder sb = new StringBuilder()
	
		// Header
		sb.append("USD, VND\n")
			
		sb.append("${usd},${vnd}")
	
		FileWriter.writeFile(filePath, sb.toString())
	}
}
