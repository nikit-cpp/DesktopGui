package utils;

public class IOHelper {
	public static String toFileSystemSafeName(String name) {
		String pattern = "[^a-zA-Zа-яА-Я0-9. -]";
		return name.replaceAll(pattern, "");
	}
}
