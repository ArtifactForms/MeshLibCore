package print;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class PackageStructurePrinter {

	public static void main(String[] args) throws IOException {
		Path sourceRoot = Paths.get("src/main/java");

		try (Stream<Path> paths = Files.walk(sourceRoot)) {
			paths.filter(p -> p.toString().endsWith(".java")).forEach(p -> {
				Path relative = sourceRoot.relativize(p);
				String className = relative.toString().replace(".java", "")
						.replace(FileSystems.getDefault().getSeparator(), ".");
				System.out.println(className);
			});
		}
	}
}