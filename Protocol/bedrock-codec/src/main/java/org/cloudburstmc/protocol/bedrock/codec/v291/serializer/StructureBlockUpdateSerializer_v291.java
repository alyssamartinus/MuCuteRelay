package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.structure.*;
import org.cloudburstmc.protocol.bedrock.packet.StructureBlockUpdatePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StructureBlockUpdateSerializer_v291 implements BedrockPacketSerializer<StructureBlockUpdatePacket> {
    public static final StructureBlockUpdateSerializer_v291 INSTANCE = new StructureBlockUpdateSerializer_v291();

    @Override
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, StructureBlockUpdatePacket packet) {
        StructureEditorData editorData = packet.getEditorData();
        StructureSettings settings = editorData.getSettings();

        helper.writeBlockPosition(buffer, packet.getBlockPosition());
        VarInts.writeUnsignedInt(buffer, editorData.getType().ordinal());
        // Structure Editor Data start
        helper.writeString(buffer, editorData.getName());
        helper.writeString(buffer, editorData.getName());
        helper.writeBlockPosition(buffer, settings.getOffset());
        helper.writeBlockPosition(buffer, settings.getSize());
        buffer.writeBoolean(!settings.isIgnoringEntities());
        buffer.writeBoolean(settings.isIgnoringBlocks());
        buffer.writeBoolean(editorData.isIncludingPlayers());
        buffer.writeBoolean(false); // show air
        // Structure Settings start
        buffer.writeFloatLE(settings.getIntegrityValue());
        VarInts.writeUnsignedInt(buffer, settings.getIntegritySeed());
        VarInts.writeUnsignedInt(buffer, settings.getMirror().ordinal());
        VarInts.writeUnsignedInt(buffer, settings.getRotation().ordinal());
        buffer.writeBoolean(settings.isIgnoringEntities());
        buffer.writeBoolean(true); // ignore structure blocks
        Vector3i min = packet.getBlockPosition().add(settings.getOffset());
        helper.writeVector3i(buffer, min);
        Vector3i max = min.add(settings.getSize());
        helper.writeVector3i(buffer, max);
        // Structure Settings end
        // Structure Editor Data end
        buffer.writeBoolean(editorData.isBoundingBoxVisible());
        buffer.writeBoolean(packet.isPowered());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, StructureBlockUpdatePacket packet) {
        packet.setBlockPosition(helper.readBlockPosition(buffer));
        StructureBlockType structureType = StructureBlockType.values()[VarInts.readUnsignedInt(buffer)];
        // Structure Editor Data start
        String name = helper.readString(buffer);
        String dataField = helper.readString(buffer);
        Vector3i offset = helper.readBlockPosition(buffer);
        Vector3i size = helper.readBlockPosition(buffer);
        buffer.readBoolean(); // include entities
        boolean ignoreBlocks = !buffer.readBoolean();
        boolean includePlayers = buffer.readBoolean();
        buffer.readBoolean(); // show air
        // Structure Settings start
        float structureIntegrity = buffer.readFloatLE();
        int integritySeed = VarInts.readUnsignedInt(buffer);
        StructureMirror mirror = StructureMirror.from(VarInts.readUnsignedInt(buffer));
        StructureRotation rotation = StructureRotation.from(VarInts.readUnsignedInt(buffer));
        boolean ignoreEntities = buffer.readBoolean();
        buffer.readBoolean(); // ignore structure bocks
        helper.readVector3i(buffer); // bounding box min
        helper.readVector3i(buffer); // bounding box max
        // Structure Settings end
        // Structure Editor Data end
        boolean boundingBoxVisible = buffer.readBoolean();

        StructureSettings settings = new StructureSettings("", ignoreEntities, ignoreBlocks, true, size, offset,
                -1, rotation, mirror, StructureAnimationMode.NONE, 0f,
                structureIntegrity, integritySeed, Vector3f.ZERO);
        StructureEditorData editorData = new StructureEditorData(name, "", dataField, includePlayers, boundingBoxVisible,
                structureType, settings, StructureRedstoneSaveMode.SAVES_TO_DISK);

        packet.setEditorData(editorData);
        packet.setPowered(buffer.readBoolean());
    }
}
