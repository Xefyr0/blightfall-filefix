package midnight.filefix;

import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;

public class WorldLoadEventHandler {

	public WorldLoadEventHandler() {
		MinecraftForge.EVENT_BUS.register(this);
		System.out.println("I am being created");
	}
	
	@SubscribeEvent
	// public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
	public void onEnterChunk(EntityEvent.EnteringChunk event) {
		if (event.newChunkX == 102 && event.newChunkZ == 7) {
			final World world = event.entity.worldObj;
			final BroadswordWorldSavedData data = BroadswordWorldSavedData.get(world);
			if (world.provider.dimensionId != 0 || data.broadswordFixed) { return; }
			final AxisAlignedBB box = AxisAlignedBB.getBoundingBox(1633, 42, 124, 1635, 44, 126);
			final List<EntityMinecartChest> chests = world.getEntitiesWithinAABB(EntityMinecartChest.class, box);
			System.out.println("Chests: ");
			for (final EntityMinecartChest chest : chests) {
				System.out.println(chest.toString());
			}
			
			for (final EntityMinecartChest chest : chests) {
				if (chest.getStackInSlot(13) == null) {
					this.doSwordFix(chest);

					data.broadswordFixed = true; // set broadswordFixed to true
					data.markDirty();
				}
			}
		}
	}

	public void doSwordFix(EntityMinecartChest chest) {
		final String nbtString = "{id:4502,Count:1,Damage:0,tag:{InfiTool:{BaseDurability:405,Head:101,Tooltip1:\"§dBeheading\",ToolEXP:0l,Effect1:13,HarvestLevel:4,ModifierTip1:\"§dBeheading\",Attack:9,RenderHead:101,ModDurability:0.0f,Handle:14,Broken:0b,Shoddy:0.0f,RenderHandle:14,Accessory:123,MiningSpeed:500,RenderAccessory:123,ToolLevel:1,Unbreaking:2,Damage:0,Beheading:1,BonusDurability:0,Modifiers:0,TotalDurability:405},display:{Name:\"§fArtaxerxes\"}}}";
		
		final Item item = GameRegistry.findItem("TConstruct", "broadsword");
		if (item == null) {
			System.out.println("Failed to create item!");
		}
		final ItemStack itemstack = new ItemStack(item, 1, 0);
		NBTTagCompound tag = null;
		
		try {
			tag = (NBTTagCompound) JsonToNBT.func_150315_a(nbtString);
		}
		catch (final NBTException e) {
			System.out.println("Error converting string to NBT");
		}
		if (tag == null) { return; }
		itemstack.readFromNBT(tag);
		chest.setInventorySlotContents(13, itemstack);
	}
	
}
