package midnight.filefix;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class BroadswordWorldSavedData extends WorldSavedData {
	public static final String DATA_NAME = "broadsword_world_data";
	public boolean broadswordFixed;

	public BroadswordWorldSavedData() { super(DATA_NAME); }

	public BroadswordWorldSavedData(String s) { super(s); }

	@Override
	public void readFromNBT(NBTTagCompound nbt) { this.broadswordFixed = nbt.getBoolean("broadswordFixed"); }

	@Override
	public void writeToNBT(NBTTagCompound nbt) { nbt.setBoolean("broadswordFixed", this.broadswordFixed); }

	public static BroadswordWorldSavedData get(World world) {
		final MapStorage storage = world.perWorldStorage;
		BroadswordWorldSavedData instance = (BroadswordWorldSavedData) storage.loadData(BroadswordWorldSavedData.class,
				BroadswordWorldSavedData.DATA_NAME);
		
		if (instance == null) {
			instance = new BroadswordWorldSavedData();
			storage.setData(BroadswordWorldSavedData.DATA_NAME, instance);
		}
		
		return instance;
	}
}
