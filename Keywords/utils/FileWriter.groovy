package utils

import java.nio.file.Files
import java.nio.file.Paths

public class FileWriter {
	static void writeFile(String filePath, String content) {
		
		def path = Paths.get(filePath)

		Files.createDirectories(path.parent)

		Files.write(path, content.getBytes("UTF-8"))

		println "JSON exported to: ${filePath}"
	}
}
