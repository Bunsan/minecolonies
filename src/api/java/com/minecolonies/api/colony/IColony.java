package com.minecolonies.api.colony;

import com.minecolonies.api.colony.permissions.IPermissions;
import com.minecolonies.api.colony.requestsystem.IRequestManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Interface of the Colony and ColonyView which will have to implement the
 * following methods.
 */
public interface IColony
{

    /**
     * Returns the position of the colony.
     *
     * @return pos of the colony.
     */
    BlockPos getCenter();

    /**
     * Returns the name of the colony.
     *
     * @return Name of the colony.
     */
    String getName();

    /**
     * Returns the permissions of the colony.
     *
     * @return {@link IPermissions} of the colony.
     */
    IPermissions getPermissions();

    /**
     * Determine if a given chunk coordinate is considered to be within the
     * colony's bounds.
     *
     * @param w   World to check.
     * @param pos Block Position.
     * @return True if inside colony, otherwise false.
     */
    boolean isCoordInColony(World w, BlockPos pos);

    /**
     * Returns the squared (x, z) distance to the center.
     *
     * @param pos Block Position.
     * @return Squared distance to the center in (x, z) direction.
     */
    long getDistanceSquared(BlockPos pos);

    /**
     * Returns whether or not the colony has a town hall.
     *
     * @return whether or not the colony has a town hall.
     */
    boolean hasTownHall();

    /**
     * returns this colonies unique id.
     *
     * @return an int representing the id.
     */
    int getID();

    /**
     * Check if the colony has a warehouse.
     * @return true if so.
     */
    boolean hasWarehouse();

    /**
     * Get the last contact of a player to the colony in hours.
     * @return an integer with a describing value.
     */
    int getLastContactInHours();

    /**
     * Method to get the World this colony is in.
     * @return the World the colony is in.
     */
    World getWorld();

    /**
     * Get the current {@link IRequestManager} for this Colony.
     * Returns null if the current Colony does not support the request system.
     * @return the {@link IRequestManager} for this colony, null if not supported.
     */
    @Nullable
    IRequestManager getRequestManager();

    /**
     * Get whether there will be a raid in this colony tonight, or not.
     * @return Boolean value true if raid, false if not
     */
    boolean hasWillRaidTonight();
}
