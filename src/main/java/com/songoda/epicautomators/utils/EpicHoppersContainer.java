package com.songoda.epicautomators.utils;

import com.craftaro.epichoppers.EpicHoppersApi;
import com.craftaro.epichoppers.containers.CustomContainer;
import com.craftaro.epichoppers.containers.IContainer;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import com.songoda.epicautomators.EpicAutomators;
import com.songoda.epicautomators.automator.Automator;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class EpicHoppersContainer implements IContainer {

    public EpicHoppersContainer() {
        EpicHoppersApi.getApi().getContainerManager().registerCustomContainerImplementation("EpicAutomators", this);
    }

    @Override
    public CustomContainer getCustomContainer(Block block) {
        return new Container(block);
    }

    static class Container extends CustomContainer {
        private final Automator automator;

        public Container(Block block) {
            this.automator = EpicAutomators.getInstance().getAutomatorManager().getAutomator(block.getLocation());
        }

        @Override
        public boolean addToContainer(ItemStack itemToMove) {
            if (!this.automator.hasRoomForItem(itemToMove)) {
                return false;
            }
            this.automator.addItem(itemToMove);
            return true;
        }

        @Override
        public ItemStack[] getItems() {
            return Arrays.stream(this.automator.getInventory())
                    .filter(item -> item != null && !ItemUtils.isFuel(XMaterial.matchXMaterial(item)))
                    .toArray(ItemStack[]::new);
        }

        @Override
        public void removeFromContainer(ItemStack itemToMove, int amountToMove) {
            automator.removeItem(itemToMove, amountToMove);
        }

        @Override
        public boolean isContainer() {
            return this.automator != null;
        }
    }
}
