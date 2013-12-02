package ro.swl.engine.util;

import static java.util.Arrays.asList;
import static java.util.Collections.sort;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Ordering;


public class FileUtil {


	public static List<File> listFilesOrderedByTypeAndName(File folder) {
		List<File> files = asList(folder.listFiles());
		sortByTypeAndByName(files);
		return files;
	}



	private static void sortByTypeAndByName(List<File> files) {
		Ordering<File> compoundComparator = Ordering.from(new FileTypeComparator()).compound(new FileNameComparator());
		sort(files, compoundComparator);
	}

	static class FileTypeComparator implements Comparator<File> {

		@Override
		public int compare(File file1, File file2) {

			if (file1.isDirectory() && file2.isFile()) {
				return -1;
			}
			if (file1.isDirectory() && file2.isDirectory()) {
				return 0;
			}
			if (file1.isFile() && file2.isFile()) {
				return 0;
			}

			return 1;
		}
	}

	static class FileNameComparator implements Comparator<File> {

		@Override
		public int compare(File file1, File file2) {
			return String.CASE_INSENSITIVE_ORDER.compare(file1.getName(), file2.getName());
		}
	}
}
