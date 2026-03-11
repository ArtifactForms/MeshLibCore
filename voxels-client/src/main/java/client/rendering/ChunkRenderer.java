package client.rendering;

import java.util.Collection;

import client.world.Chunk;
import engine.rendering.Graphics;

public interface ChunkRenderer {
	
	void renderChunks(Graphics g, Collection<Chunk> chunks);
	
}
