package TFC.Items.Tools;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import TFC.TFCBlocks;
import TFC.API.ISize;
import TFC.API.Enums.EnumSize;
import TFC.API.Enums.EnumWeight;
import TFC.Core.TFCTabs;
import TFC.Core.TFC_Core;
import TFC.TileEntities.TileEntityPottery;

public class ItemFlintSteel extends ItemFlintAndSteel implements ISize
{
	public ItemFlintSteel()
	{
		super();
		setCreativeTab(TFCTabs.TFCTools);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
		{
			boolean surroundSolids = TFC_Core.isNorthFaceSolid(world, x, y, z-1) && TFC_Core.isSouthFaceSolid(world, x, y, z+1) &&
					TFC_Core.isEastFaceSolid(world, x-1, y, z) && TFC_Core.isWestFaceSolid(world, x+1, y, z);

			if(side == 1 && world.getBlock(x, y, z).isNormalCube() && world.getBlock(x, y, z).isOpaqueCube() && 
					world.getBlock(x, y, z).getMaterial() != Material.wood && world.getBlock(x, y, z).getMaterial() != Material.cloth &&
					world.isAirBlock(x, y+1, z) && world.getBlock(x, y, z) != TFCBlocks.Charcoal)
			{

				List list = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x, y+1, z, x+1, y+2, z+1));
				int numsticks = 0;
				int hasPaper = 0;

				if (list != null && !list.isEmpty())
				{
					for (Iterator iterator = list.iterator(); iterator.hasNext();)
					{
						EntityItem entity = (EntityItem)iterator.next();
						if(entity.getEntityItem().getItem() == Items.paper)
							hasPaper = 20;
						else if(entity.getEntityItem().getItem() == Items.stick)
							numsticks+=entity.getEntityItem().stackSize;
					}
				}

				itemstack.setItemDamage(itemstack.getItemDamage()+1);
				if(itemstack.getItemDamage() >= itemstack.getMaxDamage())
					itemstack.stackSize = 0;

				if(numsticks >= 3)
				{
					for (Iterator iterator = list.iterator(); iterator.hasNext();)
					{
						EntityItem entity = (EntityItem)iterator.next();
						if(entity.getEntityItem().getItem() == Items.stick)
							entity.setDead();
						if(entity.getEntityItem().getItem() == Items.paper)
							entity.setDead();
					}
					itemstack.damageItem(1, entityplayer);
					world.setBlock(x, y+1, z, TFCBlocks.Firepit, 1, 0x2);
					if(world.isRemote) {
						world.markBlockForUpdate(x, y+1, z);
					}
					return true;
				}
				return true;
			}
			else if(world.getBlock(x, y, z) == TFCBlocks.Charcoal && world.getBlockMetadata(x, y, z) > 6)
			{
				if(world.getBlock(x, y-1, z).getMaterial() == Material.rock && 
						world.getBlock(x+1, y, z).getMaterial() == Material.rock && world.getBlock(x-1, y, z).getMaterial() == Material.rock && 
						world.getBlock(x, y, z+1).getMaterial() == Material.rock && world.getBlock(x, y, z-1).getMaterial() == Material.rock &&
						surroundSolids)
				{
					itemstack.damageItem(1, entityplayer);
					world.setBlock(x, y, z, TFCBlocks.Forge, 1, 0x2);
					return true;
				}
			}
			else if(world.getBlock(x, y, z) == TFCBlocks.Pottery && surroundSolids)
			{
				TileEntityPottery te = (TileEntityPottery) world.getTileEntity(x, y, z);
				te.StartPitFire();
				itemstack.damageItem(1, entityplayer);
				return true;
			}
			else
			{
				super.onItemUse(itemstack, entityplayer, world, x, y, z, side, hitX, hitY, hitZ);
			}
			return false;
		}
		return false;
	}

	@Override
	public EnumSize getSize(ItemStack is)
	{
		return EnumSize.VERYSMALL;
	}

	@Override
	public EnumWeight getWeight(ItemStack is)
	{
		return EnumWeight.LIGHT;
	}

	@Override
	public boolean canStack()
	{
		return false;
	}
}
