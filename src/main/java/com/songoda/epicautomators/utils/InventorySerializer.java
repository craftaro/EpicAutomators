package com.songoda.epicautomators.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

    public class InventorySerializer {

        /**
         * Serializes an inventory (ItemStack array) to a byte array
         *
         * @param inventory the inventory to serialize
         * @return the serialized byte array
         */
        public static byte[] serializeInventory(ItemStack[] inventory) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                 BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
                dataOutput.writeInt(inventory.length);
                for (ItemStack item : inventory) {
                    dataOutput.writeObject(item);
                }
                return outputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Deserializes a byte array to an inventory (ItemStack array)
         *
         * @param data the byte array to deserialize
         * @return the deserialized inventory
         */
        public static ItemStack[] deserializeInventory(byte[] data) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
                 BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
                int size = dataInput.readInt();
                ItemStack[] inventory = new ItemStack[size];
                for (int i = 0; i < size; i++) {
                    inventory[i] = (ItemStack) dataInput.readObject();
                }
                return inventory;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }