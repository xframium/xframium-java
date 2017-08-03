package org.xframium.integrations.jira.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/**
 * Util class used to create zip file
 * @author rravs
 *
 */
public class ZIPFileUtil {

	public static String createZipFile(File rootFolder){
		
		Path inPath = Paths.get(rootFolder.getAbsolutePath());
		String zipFilePath = inPath.getParent().toAbsolutePath().toString()+"\\"+inPath.getFileName()+".zip";
		
		Path zipPath = Paths.get(zipFilePath);
		
		try {
			pack(inPath, zipPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return zipPath.toAbsolutePath().toString();
	}
	
	
	private static void pack(final Path folder, final Path zipFilePath) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
				ZipOutputStream zos = new ZipOutputStream(fos)) {
			Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					zos.putNextEntry(new ZipEntry(folder.relativize(file).toString()));
					Files.copy(file, zos);
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}

				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					zos.putNextEntry(new ZipEntry(folder.relativize(dir).toString() + "/"));
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}
			});
		}
	}
}
