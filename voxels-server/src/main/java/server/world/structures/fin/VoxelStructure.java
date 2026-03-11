package server.world.structures.fin;

import java.util.Collection;

import math.Bounds;
import voxels.Voxel;

public interface VoxelStructure {

  Collection<Voxel> getVoxels(long seed);

  Bounds getLocalBounds(long seed);
}
