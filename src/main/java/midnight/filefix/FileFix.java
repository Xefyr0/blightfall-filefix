package midnight.filefix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = FileFix.MODID, name = FileFix.MODNAME, version = FileFix.VERSION, dependencies = FileFix.DEPS)
public class FileFix {

	public static final String MODID = "blightfallfilefix";
	public static final String MODNAME = "BlightfallFileFixer";
	public static final String VERSION = "1.0.2";
	public static final String DEPS = "";
	public EnterChunkEventHandler worldLoadEventHandler;

	@Mod.EventHandler
	// public void preInit(FMLPreInitializationEvent event) {
	public void postInit(FMLPostInitializationEvent event) {
		
		this.worldLoadEventHandler = new EnterChunkEventHandler();
		
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
	
	private void fixFiles() throws IOException { this.purgeTerrainControlConfigs(); }
	
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
