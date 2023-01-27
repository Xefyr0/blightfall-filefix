package midnight.filefix;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = FileFix.MODID, name = FileFix.MODNAME, version = FileFix.VERSION, dependencies = FileFix.DEPS)
public class FileFix {

	public static final String MODID = "blightfallfilefix";
	public static final String MODNAME = "BlightfallFileFixer";
	public static final String VERSION = "1.0.0";
	public static final String DEPS = "";

	@Mod.EventHandler
	// public void preInit(FMLPreInitializationEvent event) {
	public void postInit(FMLPostInitializationEvent event) {
		final File check = new File(".filefixer.done");
		final String oldVersion = this.getFileFixerVersion(check);
		System.out.println("Old FileFixer version: " + oldVersion);
		if (!VERSION.equals(this.getFileFixerVersion(check))) {
			System.out.println(
					"Previous version: " + oldVersion + ". Current version: " + VERSION + ". Running fixes...");
			try {
				this.fixFiles();
			}
			catch (final IOException ex) {
				ex.printStackTrace();
			}
			
			try {
				check.createNewFile();
				final BufferedWriter bw = new BufferedWriter(new FileWriter(check));
				bw.write(VERSION);
				bw.close();
			}
			catch (final IOException e) {
				System.out.println("Failed to create .filefixer.done");
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Old version matches current version, skipping.");
		}
	}
	
	private void fixFiles() throws IOException {
		this.purgeTerrainControlConfigs();
		this.fixDim112();
	}

	private void fixDim112() throws IOException {
		InputStream zipfile;

		zipfile = this.getClass().getResourceAsStream("/midnight/filefix/embeddedfiles/r.-2.-3.mca.zip");

		final ZipInputStream zipIn = new ZipInputStream(zipfile);
		final ZipEntry entry = zipIn.getNextEntry();
		final String[] pathnames = { "flans", "DIM-112", "region", entry.getName() };
		final String filePath = String.join(File.separator, pathnames);
		this.extractFile(zipIn, filePath);
		zipIn.closeEntry();
		zipIn.close();
	}

	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		System.out.println("Extracting file: " + filePath);
		final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		final byte[] bytesIn = new byte[4096];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
		System.out.println("Extracted file: " + filePath);
	}
	
	private void purgeTerrainControlConfigs() {
		final File saves = new File("saves");
		if (saves.exists() && saves.isDirectory()) {
			for (final File directory : saves.listFiles()) {
				if (directory.isDirectory()) {
					final File tc = new File(new File(directory, "TerrainControl"), "WorldBiomes");
					try {
						System.out.println("Deleting " + tc.getCanonicalPath());
						FileUtils.deleteDirectory(tc);
					}
					catch (final IOException e) {
						System.out.println("Failed to delete " + tc.getName());
					}
				}
			}
		}
		final File world = new File("world");
		if (world.exists() && world.isDirectory()) {
			final File tc = new File(new File(world, "TerrainControl"), "WorldBiomes");
			try {
				System.out.println("Deleting " + tc.getCanonicalPath());
				FileUtils.deleteDirectory(tc);
			}
			catch (final IOException e) {
				System.out.println("Failed to delete " + tc.getName());
			}
		}
	}

	private String getFileFixerVersion(File file) {
		try {
			if (file.exists()) {
				final BufferedReader br = new BufferedReader(new FileReader(file));
				return br.readLine();
			}
			return "0.0.0";
		}
		catch (final IOException ex) {
			return "0.0.0";
		}
	}
	
}
