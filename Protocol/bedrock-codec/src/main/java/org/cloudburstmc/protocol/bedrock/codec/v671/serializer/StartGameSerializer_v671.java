package org.cloudburstmc.protocol.bedrock.codec.v671.serializer;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v589.serializer.StartGameSerializer_v589;
import org.cloudburstmc.protocol.bedrock.data.*;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;
import org.cloudburstmc.protocol.common.util.VarInts;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StartGameSerializer_v671 extends StartGameSerializer_v589 {
    public static final StartGameSerializer_v671 INSTANCE = new StartGameSerializer_v671();

    @Override
    protected void writeLevelSettings(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        writeSeed(buffer, packet.getSeed());
        buffer.writeShortLE(packet.getSpawnBiomeType().ordinal());
        helper.writeString(buffer, packet.getCustomBiomeName());
        VarInts.writeInt(buffer, packet.getDimensionId());
        VarInts.writeInt(buffer, packet.getGeneratorId());
        VarInts.writeInt(buffer, packet.getLevelGameType().ordinal());
        buffer.writeBoolean(packet.isHardcore());
        VarInts.writeInt(buffer, packet.getDifficulty());
        helper.writeBlockPosition(buffer, packet.getDefaultSpawn());
        buffer.writeBoolean(packet.isAchievementsDisabled());
        buffer.writeBoolean(packet.isWorldEditor());
        buffer.writeBoolean(packet.isCreatedInEditor());
        buffer.writeBoolean(packet.isExportedFromEditor());
        VarInts.writeInt(buffer, packet.getDayCycleStopTime());
        VarInts.writeInt(buffer, packet.getEduEditionOffers());
        buffer.writeBoolean(packet.isEduFeaturesEnabled());
        helper.writeString(buffer, packet.getEducationProductionId());
        buffer.writeFloatLE(packet.getRainLevel());
        buffer.writeFloatLE(packet.getLightningLevel());
        buffer.writeBoolean(packet.isPlatformLockedContentConfirmed());
        buffer.writeBoolean(packet.isMultiplayerGame());
        buffer.writeBoolean(packet.isBroadcastingToLan());
        VarInts.writeInt(buffer, packet.getXblBroadcastMode().ordinal());
        VarInts.writeInt(buffer, packet.getPlatformBroadcastMode().ordinal());
        buffer.writeBoolean(packet.isCommandsEnabled());
        buffer.writeBoolean(packet.isTexturePacksRequired());
        helper.writeArray(buffer, packet.getGamerules(), helper::writeGameRule);
        helper.writeExperiments(buffer, packet.getExperiments());
        buffer.writeBoolean(packet.isExperimentsPreviouslyToggled());
        buffer.writeBoolean(packet.isBonusChestEnabled());
        buffer.writeBoolean(packet.isStartingWithMap());
        VarInts.writeInt(buffer, packet.getDefaultPlayerPermission().ordinal());
        buffer.writeIntLE(packet.getServerChunkTickRange());
        buffer.writeBoolean(packet.isBehaviorPackLocked());
        buffer.writeBoolean(packet.isResourcePackLocked());
        buffer.writeBoolean(packet.isFromLockedWorldTemplate());
        buffer.writeBoolean(packet.isUsingMsaGamertagsOnly());
        buffer.writeBoolean(packet.isFromWorldTemplate());
        buffer.writeBoolean(packet.isWorldTemplateOptionLocked());
        buffer.writeBoolean(packet.isOnlySpawningV1Villagers());
        buffer.writeBoolean(packet.isDisablingPersonas());
        buffer.writeBoolean(packet.isDisablingCustomSkins());
        buffer.writeBoolean(packet.isEmoteChatMuted());
        helper.writeString(buffer, packet.getVanillaVersion());
        buffer.writeIntLE(packet.getLimitedWorldWidth());
        buffer.writeIntLE(packet.getLimitedWorldHeight());
        buffer.writeBoolean(packet.isNetherType());
        helper.writeString(buffer, packet.getEduSharedUriResource().getButtonName());
        helper.writeString(buffer, packet.getEduSharedUriResource().getLinkUri());
        helper.writeOptional(buffer, OptionalBoolean::isPresent, packet.getForceExperimentalGameplay(),
                (buf, optional) -> buf.writeBoolean(optional.getAsBoolean()));
        buffer.writeByte(packet.getChatRestrictionLevel().ordinal());
        buffer.writeBoolean(packet.isDisablingPlayerInteractions());
    }

    @Override
    protected void readLevelSettings(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        packet.setSeed(readSeed(buffer));
        packet.setSpawnBiomeType(SpawnBiomeType.byId(buffer.readShortLE()));
        packet.setCustomBiomeName(helper.readString(buffer));
        packet.setDimensionId(VarInts.readInt(buffer));
        packet.setGeneratorId(VarInts.readInt(buffer));
        packet.setLevelGameType(GameType.from(VarInts.readInt(buffer)));
        packet.setHardcore(buffer.readBoolean());
        packet.setDifficulty(VarInts.readInt(buffer));
        packet.setDefaultSpawn(helper.readBlockPosition(buffer));
        packet.setAchievementsDisabled(buffer.readBoolean());
        packet.setWorldEditor(buffer.readBoolean());
        packet.setCreatedInEditor(buffer.readBoolean());
        packet.setExportedFromEditor(buffer.readBoolean());
        packet.setDayCycleStopTime(VarInts.readInt(buffer));
        packet.setEduEditionOffers(VarInts.readInt(buffer));
        packet.setEduFeaturesEnabled(buffer.readBoolean());
        packet.setEducationProductionId(helper.readString(buffer));
        packet.setRainLevel(buffer.readFloatLE());
        packet.setLightningLevel(buffer.readFloatLE());
        packet.setPlatformLockedContentConfirmed(buffer.readBoolean());
        packet.setMultiplayerGame(buffer.readBoolean());
        packet.setBroadcastingToLan(buffer.readBoolean());
        packet.setXblBroadcastMode(GamePublishSetting.byId(VarInts.readInt(buffer)));
        packet.setPlatformBroadcastMode(GamePublishSetting.byId(VarInts.readInt(buffer)));
        packet.setCommandsEnabled(buffer.readBoolean());
        packet.setTexturePacksRequired(buffer.readBoolean());
        helper.readArray(buffer, packet.getGamerules(), helper::readGameRule);
        helper.readExperiments(buffer, packet.getExperiments());
        packet.setExperimentsPreviouslyToggled(buffer.readBoolean());
        packet.setBonusChestEnabled(buffer.readBoolean());
        packet.setStartingWithMap(buffer.readBoolean());
        packet.setDefaultPlayerPermission(PLAYER_PERMISSIONS[VarInts.readInt(buffer)]);
        packet.setServerChunkTickRange(buffer.readIntLE());
        packet.setBehaviorPackLocked(buffer.readBoolean());
        packet.setResourcePackLocked(buffer.readBoolean());
        packet.setFromLockedWorldTemplate(buffer.readBoolean());
        packet.setUsingMsaGamertagsOnly(buffer.readBoolean());
        packet.setFromWorldTemplate(buffer.readBoolean());
        packet.setWorldTemplateOptionLocked(buffer.readBoolean());
        packet.setOnlySpawningV1Villagers(buffer.readBoolean());
        packet.setDisablingPersonas(buffer.readBoolean());
        packet.setDisablingCustomSkins(buffer.readBoolean());
        packet.setEmoteChatMuted(buffer.readBoolean());
        packet.setVanillaVersion(helper.readString(buffer));
        packet.setLimitedWorldWidth(buffer.readIntLE());
        packet.setLimitedWorldHeight(buffer.readIntLE());
        packet.setNetherType(buffer.readBoolean());
        packet.setEduSharedUriResource(new EduSharedUriResource(helper.readString(buffer), helper.readString(buffer)));
        packet.setForceExperimentalGameplay(helper.readOptional(buffer, OptionalBoolean.empty(), buf -> OptionalBoolean.of(buf.readBoolean())));
        packet.setChatRestrictionLevel(ChatRestrictionLevel.values()[buffer.readByte()]);
        packet.setDisablingPlayerInteractions(buffer.readBoolean());
    }
}
