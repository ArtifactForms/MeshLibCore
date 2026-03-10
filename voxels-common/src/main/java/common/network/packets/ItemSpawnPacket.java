package common.network.packets;

import java.io.IOException;

import common.entity.ItemEntity;
import common.game.block.BlockRegistry;
import common.game.block.BlockType;
import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

/**
 * Sent by the server to tell clients to spawn a new item entity in the world.
 */
public class ItemSpawnPacket implements Packet {

    private long entityId;
    private short blockTypeId;
    private float x, y, z;
    private float velX, velY, velZ;

    public ItemSpawnPacket() {}

    /**
     * Creates a spawn packet based on an existing ItemEntity.
     */
    public ItemSpawnPacket(ItemEntity entity) {
        this.entityId = entity.getEntityId();
        this.blockTypeId = entity.getBlockType().getId();
        this.x = entity.getPosition().x;
        this.y = entity.getPosition().y;
        this.z = entity.getPosition().z;
        this.velX = entity.getVelocity().x;
        this.velY = entity.getVelocity().y;
        this.velZ = entity.getVelocity().z;
    }

    @Override
    public int getId() {
        return PacketIds.ITEM_SPAWN;
    }

    @Override
    public void write(PacketBuffer out) throws IOException {
        out.writeLong(entityId);
        out.writeShort(blockTypeId);
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(z);
        out.writeFloat(velX);
        out.writeFloat(velY);
        out.writeFloat(velZ);
    }

    @Override
    public void read(PacketBuffer in) throws IOException {
        this.entityId = in.readLong();
        this.blockTypeId = in.readShort();
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.z = in.readFloat();
        this.velX = in.readFloat();
        this.velY = in.readFloat();
        this.velZ = in.readFloat();
    }

    // Getters for the Client Dispatcher
    public long getEntityId() { return entityId; }
    public BlockType getBlockType() { return BlockRegistry.get(blockTypeId); }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
    public float getVelX() { return velX; }
    public float getVelY() { return velY; }
    public float getVelZ() { return velZ; }
}