package TFC.Blocks;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import TFC.TerraFirmaCraft;
import TFC.Core.Helper;
import TFC.Core.TFC_Core;
import TFC.Core.TFC_Textures;
import TFC.Core.Player.PlayerInfo;
import TFC.Core.Player.PlayerManagerTFC;
import TFC.Items.ItemChisel;
import TFC.Items.ItemHammer;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class BlockIgInSmooth extends Block
{
	public BlockIgInSmooth(int i, int tex) 
	{
		super(i, Material.rock);
		this.blockIndexInTexture = tex;
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@SideOnly(Side.CLIENT)
    @Override
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List list)
	{
		for(int i = 0; i < 3; i++) {
			list.add(new ItemStack(this,1,i));
		}
	}

	/*
	 * Mapping from metadata value to damage value
	 */
	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return blockIndexInTexture+j;
	}

	@Override
	public String getTextureFile()
	{
		return TFC_Textures.RockSheet;
	}

	/**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
	   /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) 
    {
        boolean hasHammer = false;
        for(int i = 0; i < 9;i++)
        {
            if(entityplayer.inventory.mainInventory[i] != null && entityplayer.inventory.mainInventory[i].getItem() instanceof ItemHammer)
                hasHammer = true;
        }
        if(entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() instanceof ItemChisel && hasHammer && !world.isRemote)
        {
            MovingObjectPosition objectMouseOver = Helper.getMouseOverObject(entityplayer, world);
            if(objectMouseOver == null) {
                return false;
            }       
            int side = objectMouseOver.sideHit;

            int id = world.getBlockId(x, y, z);
            byte meta = (byte) world.getBlockMetadata(x, y, z);

            return ItemChisel.handleActivation(world, entityplayer, x, y, z, id, meta, side, par7, par8, par9);
        }
        return false;
    }
}
