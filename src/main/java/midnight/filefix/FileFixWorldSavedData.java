package midnight.filefix;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class FileFixWorldSavedData extends WorldSavedData {
	public static final String DATA_NAME = "broadsword_world_data";
	public boolean broadswordFixed;
	public boolean caysykaBookFixed;

	public FileFixWorldSavedData() { super(DATA_NAME); }

	public FileFixWorldSavedData(String s) { super(s); }

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.broadswordFixed = nbt.getBoolean("broadswordFixed");
		this.caysykaBookFixed = nbt.getBoolean("caysykaBookFixed");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setBoolean("broadswordFixed", this.broadswordFixed);
		nbt.setBoolean("caysykaBookFixed", this.caysykaBookFixed);
	}

	public static FileFixWorldSavedData get(World world) {
		final MapStorage storage = world.perWorldStorage;
		FileFixWorldSavedData instance = (FileFixWorldSavedData) storage.loadData(FileFixWorldSavedData.class,
				FileFixWorldSavedData.DATA_NAME);
		
		if (instance == null) {
			instance = new FileFixWorldSavedData();
			storage.setData(FileFixWorldSavedData.DATA_NAME, instance);
		}
		
		return instance;
	}
}
