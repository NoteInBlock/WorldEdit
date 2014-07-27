/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.extent;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.OperationQueue;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.biome.BaseBiome;

import javax.annotation.Nullable;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A base class for {@link Extent}s that merely passes extents onto another.
 */
public abstract class AbstractDelegateExtent<T extends Extent> implements Extent {

    private final T extent;

    /**
     * Create a new instance.
     *
     * @param extent the extent
     */
    protected AbstractDelegateExtent(T extent) {
        checkNotNull(extent);
        this.extent = extent;
    }

    /**
     * Get the extent.
     *
     * @return the extent
     */
    public T getExtent() {
        return extent;
    }

    @Override
    public BaseBlock getBlock(Vector position) {
        return extent.getBlock(position);
    }

    @Override
    public BaseBlock getLazyBlock(Vector position) {
        return extent.getLazyBlock(position);
    }

    @Override
    public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
        return extent.setBlock(location, block);
    }

    @Override
    @Nullable
    public Entity createEntity(Location location, BaseEntity entity) {
        return extent.createEntity(location, entity);
    }

    @Override
    public List<? extends Entity> getEntities() {
        return extent.getEntities();
    }

    @Override
    public List<? extends Entity> getEntities(Region region) {
        return extent.getEntities(region);
    }

    @Override
    public BaseBiome getBiome(Vector2D position) {
        return extent.getBiome(position);
    }

    @Override
    public boolean setBiome(Vector2D position, BaseBiome biome) {
        return extent.setBiome(position, biome);
    }

    @Override
    public Vector getMinimumPoint() {
        return extent.getMinimumPoint();
    }

    @Override
    public Vector getMaximumPoint() {
        return extent.getMaximumPoint();
    }

    /**
     * Get the interleaved operation for this extent.
     *
     * <p>Implementations of this method do not need to worry about calling
     * the parent's methods.</p>
     *
     * @return an operation or {@code null} if there is no operation to execute
     */
    @Nullable
    protected Operation thisInterleaveOperation() {
        return null;
    }

    /**
     * Get the finalization operation for this extent.
     *
     * <p>Implementations of this method do not need to worry about calling
     * the parent's methods.</p>
     *
     * @return an operation or {@code null} if there is no operation to execute
     */
    @Nullable
    protected Operation thisFinalizeOperation() {
        return null;
    }

    @Nullable
    @Override
    public final Operation getInterleaveOperation() {
        Operation ours = thisInterleaveOperation();
        Operation other = extent.getInterleaveOperation();
        if (ours != null && other != null) {
            return new OperationQueue(ours, other);
        } else if (ours != null) {
            return ours;
        } else if (other != null) {
            return other;
        } else {
            return null;
        }
    }

    @Override
    public final @Nullable Operation getFinalizeOperation() {
        Operation ours = thisFinalizeOperation();
        Operation other = extent.getFinalizeOperation();
        if (ours != null && other != null) {
            return new OperationQueue(ours, other);
        } else if (ours != null) {
            return ours;
        } else if (other != null) {
            return other;
        } else {
            return null;
        }
    }

}
