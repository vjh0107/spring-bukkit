package kr.junhyung.springbukkit.serializer;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.springframework.core.serializer.Serializer;
import org.springframework.lang.NonNull;

import java.io.*;

public class BukkitSerializer implements Serializer<ConfigurationSerializable> {
    @Override
    public void serialize(@NonNull ConfigurationSerializable object, @NonNull OutputStream outputStream) throws IOException {
        try (BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(outputStream)) {
            bukkitObjectOutputStream.writeObject(object);
        }
    }

    @Override
    public @NonNull byte[] serializeToByteArray(@NonNull ConfigurationSerializable object) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            serialize(object, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
