package top.tim;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Blocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AirdropMod implements ModInitializer {
    public static final String MOD_ID = "tim_random_things";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private int timer = 0;
    private int nextTriggerTime = 300;


    @Override
    public void onInitialize() {
        LOGGER.info("Tim's Random Things Mod load!");

        // 注册服务器每 Tick 事件
        ServerTickEvents.START_SERVER_TICK.register(this::onServerTick);
        // 注册服务器启动完成事件
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
    }

    private void onServerStarted(MinecraftServer server) {
        // 在此处执行你的指令
        executeStartupCommands(server);
    }

    private void executeStartupCommands(MinecraftServer server) {
        try {
            // 示例：设置游戏时间为白天
            server.getCommandManager().executeWithPrefix(
                    server.getCommandSource().withLevel(4),
                    "/time set day"
            );

            // 广播
            server.getCommandManager().executeWithPrefix(
                    server.getCommandSource(),
                    "/tellraw @a {\"text\":\"服务器已启动！\",\"color\":\"green\"}"
            );

            LOGGER.info("服务器启动指令已执行！");
        } catch (Exception e) {
            LOGGER.error("执行启动指令失败: " + e.getMessage());
        }
    }

    private void onServerTick(MinecraftServer server) {
        if (server.getPlayerManager().getCurrentPlayerCount() > 0) {
            timer++;
            if (timer >= nextTriggerTime * 20) {
                spawnAirdrop(server);
                resetTimer();
            }
        }
    }

    private void spawnAirdrop(MinecraftServer server) {
        ServerWorld world = server.getOverworld();
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        if (players.isEmpty()) {
            return;
        }

        // 随机选择玩家并生成位置
        ServerPlayerEntity randomPlayer = players.get(world.random.nextInt(players.size()));
        BlockPos pos = randomPlayer.getBlockPos().add(
                world.random.nextInt(200) - 100,
                30,
                world.random.nextInt(200) - 100
        );

        // 放置箱子方块
        world.setBlockState(pos, Blocks.CHEST.getDefaultState());

        // 设置战利品表
        ChestBlockEntity chest = (ChestBlockEntity) world.getBlockEntity(pos);
        if (chest != null) {
            Identifier lootTableId = Identifier.of(MOD_ID, "chests/airdrop");
            RegistryKey<LootTable> lootTableKey = RegistryKey.of(RegistryKeys.LOOT_TABLE, lootTableId);
            chest.setLootTable(lootTableKey, world.random.nextLong());
        }

        // 添加特效
        world.spawnParticles(
                ParticleTypes.END_ROD,
                pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                50,
                0.5, 0.5, 0.5,
                0.1
        );

        server.getPlayerManager().broadcast(Text.literal("§e空投将在 30 秒后到达！"), false);
    }


    private void resetTimer() {
        timer = 0;
        nextTriggerTime = 300 + (int) (Math.random() * 600);
    }
}