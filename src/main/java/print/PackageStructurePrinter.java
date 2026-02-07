package print;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class PackageStructurePrinter {

	private static class Node {
		String name;
		boolean isClass;
		Map<String, Node> children = new TreeMap<>();

		Node(String name, boolean isClass) {
			this.name = name;
			this.isClass = isClass;
		}
	}

	public static void main(String[] args) throws IOException {
		Path sourceRoot = Paths.get("src/main/java");
		Node root = new Node(sourceRoot.getFileName().toString(), false);

		try (var paths = Files.walk(sourceRoot)) {
			paths.filter(p -> p.toString().endsWith(".java")).forEach(p -> insert(root, sourceRoot.relativize(p)));
		}

		print(root, "", true);
	}

	private static void insert(Node root, Path relativePath) {
		Node current = root;

		for (int i = 0; i < relativePath.getNameCount(); i++) {
			String part = relativePath.getName(i).toString();

			boolean isClass = part.endsWith(".java");
			String name = isClass ? part.replace(".java", "") : part;

			current = current.children.computeIfAbsent(name, n -> new Node(n, isClass));
		}
	}

	private static void print(Node node, String prefix, boolean isLast) {
		if (!node.name.isEmpty()) {
			System.out.println(prefix + (isLast ? "└── " : "├── ") + node.name);
			prefix += isLast ? "    " : "│   ";
		}

		int size = node.children.size();
		int index = 0;

		for (Node child : node.children.values()) {
			print(child, prefix, ++index == size);
		}
	}
}