package me.duncanruns.reversecrafingtable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.*;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class ReverseCraftingTableBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory, Tickable {
    private static final Random RANDOM = new Random();
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public ReverseCraftingTableBlockEntity() {
        super(ReverseCraftingTable.BLOCK_ENTITY);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ReverseCraftingTableScreenHandler(syncId, inv, this);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, this.inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, this.inventory);
        return tag;
    }

    @Override
    public void tick() {
        ItemStack itemStack = inventory.get(0);
        if (itemStack.isEmpty()) return;
        if (itemStack.isDamageable() && itemStack.getDamage() > 0) {
            dropInventory();
            return;
        }

        List<CraftingRecipe> matchingRecipes = world.getServer().getRecipeManager().method_30027(RecipeType.CRAFTING);
        matchingRecipes.removeIf(craftingRecipe -> !(craftingRecipe.getOutput().getItem().equals(itemStack.getItem())));
        matchingRecipes.removeIf(craftingRecipe -> !(craftingRecipe instanceof ShapelessRecipe || craftingRecipe instanceof ShapedRecipe));
        matchingRecipes.removeIf(craftingRecipe -> craftingRecipe.getOutput().getCount() > itemStack.getCount());

        while ((!itemStack.isEmpty()) && !matchingRecipes.isEmpty()) {
            CraftingRecipe randomRecipe = matchingRecipes.get(RANDOM.nextInt(matchingRecipes.size()));
            List<Ingredient> ingredients = randomRecipe.getPreviewInputs();
            for (Ingredient ingredient : ingredients) {
                ingredient.cacheMatchingStacks();
                ItemStack[] possibleStacks = ingredient.matchingStacks;
                if (possibleStacks == null || possibleStacks.length == 0) continue;
                // For convenience just use the first, this prevents things like 12 types of planks from some beds etc
                ItemStack toDrop = possibleStacks[0].copy();
                returnItem(toDrop);
            }
            itemStack.decrement(randomRecipe.getOutput().getCount());

            matchingRecipes.removeIf(craftingRecipe -> craftingRecipe.getOutput().getCount() > itemStack.getCount());
        }

        dropInventory();
    }

    private void returnItem(ItemStack itemStack) {
        PlayerEntity player = world.getClosestPlayer(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1000000, false);
        if (player == null) {
            dropItem(itemStack);
            return;
        }
        ItemEntity entity = player.dropItem(itemStack, false);
        if (entity == null) return;
        entity.setPickupDelay(0);
    }

    private void dropInventory() {
        ItemStack itemStack = inventory.get(0);
        if (!itemStack.isEmpty()) {
            returnItem(itemStack.copy());
            inventory.set(0, ItemStack.EMPTY);
        }
    }

    private void dropItem(ItemStack itemStack) {
        ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5, itemStack);
        world.spawnEntity(itemEntity);
    }
}
