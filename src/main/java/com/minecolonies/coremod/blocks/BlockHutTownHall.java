package com.minecolonies.coremod.blocks;

import com.minecolonies.coremod.configuration.Configurations;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * Hut for the town hall.
 * Sets the working range for the town hall in the constructor
 */
public class BlockHutTownHall extends AbstractBlockHut
{
    protected BlockHutTownHall()
    {
        super();
        //Sets the working range to whatever the config is set to
        this.workingRange = Configurations.workingRangeTownHall;
    }

    @NotNull
    @Override
    public String getName()
    {
        return "blockHutTownHall";
    }
}
