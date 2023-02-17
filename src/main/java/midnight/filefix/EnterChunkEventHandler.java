package midnight.filefix;

import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.fybertech.modelcitizens.TileEntityCorpse;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;

public class EnterChunkEventHandler {
	
	public EnterChunkEventHandler() {
		MinecraftForge.EVENT_BUS.register(this);
		System.out.println("I am being created");
	}

	@SubscribeEvent
	public void onEnterChunk(EntityEvent.EnteringChunk event) {
		if (event.entity.worldObj.isRemote) { return; }
		final World world = event.entity.worldObj;
		final FileFixWorldSavedData data = FileFixWorldSavedData.get(world);
		if (!data.broadswordFixed) {
			this.doSwordFix(event, world, data);
		}
		if (!data.caysykaBookFixed) {
			this.doCaysykaBookFix(event, world, data);
		}
	}
	
	private void doCaysykaBookFix(EnteringChunk event, World world, FileFixWorldSavedData data) {

		if (event.newChunkX == -67 && event.newChunkZ == -64) {
			System.out.println("Fixing caysyka book");
			if (world.provider.dimensionId != 0) { return; }
			final String nbtString = "{id:387,Count:1,Damage:0,tag:{pages:[\"Caysyka is a great critic. While she makes sure people know she's not a designer, she speaks enough design language to intelligently and insightfully critique design decisions, making her an incredible tester. Also, she has a SA account, and posted the \",\"notification in SA that Blightfall was initially published. Given that CanVox learned about Blightfall from the SA thread, it's Caysyka's fault that Blightfall was discovered by Technic at all.\"],author:\"Talonos\",title:\"About Caysyka\"}}";
			final TileEntityCorpse model = (TileEntityCorpse) world.getTileEntity(-1060, 142, -1021);
			final ItemStack itemstack = new ItemStack(Items.written_book);
			NBTTagCompound tag = null;
			if (model == null) {
				System.out.println("Unable to find model");
			}
			try {
				tag = (NBTTagCompound) JsonToNBT.func_150315_a(nbtString);
			}
			catch (final NBTException e) {
				e.printStackTrace();
				System.out.println("Error converting string to NBT");
				return;
			}
			if (tag == null) { return; }
			itemstack.readFromNBT(tag);
			model.setInventorySlotContents(1, itemstack);
			data.caysykaBookFixed = true;
			data.markDirty();
		}
	}
	
	public void doSwordFix(EntityEvent.EnteringChunk event, World world, FileFixWorldSavedData data) {
		if (event.newChunkX == 102 && event.newChunkZ == 7) {
			System.out.println("Fixing broadsword");
			if (world.provider.dimensionId != 0) { return; }
			final AxisAlignedBB box = AxisAlignedBB.getBoundingBox(1633, 42, 124, 1635, 44, 126);
			@SuppressWarnings("unchecked")
			final List<EntityMinecartChest> chests = world.getEntitiesWithinAABB(EntityMinecartChest.class, box);
			for (final EntityMinecartChest chest : chests) {
				System.out.println(chest.toString());
			}

			for (final EntityMinecartChest chest : chests) {
				if (chest.getStackInSlot(13) == null) {
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
						return;
					}
					if (tag == null) { return; }
					itemstack.readFromNBT(tag);
					chest.setInventorySlotContents(13, itemstack);
					data.broadswordFixed = true; // set broadswordFixed to true
					data.markDirty();
				}
			}
		}
		
	}

}
