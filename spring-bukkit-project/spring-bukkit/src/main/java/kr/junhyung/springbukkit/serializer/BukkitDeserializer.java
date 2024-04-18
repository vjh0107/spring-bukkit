package kr.junhyung.springbukkit.serializer;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.springframework.core.serializer.Deserializer;
import org.springframework.lang.NonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BukkitDeserializer implements Deserializer<ConfigurationSerializable> {
    @Override
    public @NonNull ConfigurationSerializable deserialize(@NonNull InputStream inputStream) throws IOException {
        try (BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(inputStream)) {
            return (ConfigurationSerializable) bukkitObjectInputStream.readObject();
        } catch (ClassNotFoundException exception) {
            throw new IOException(exception);
        }
    }

    @Override
    public @NonNull ConfigurationSerializable deserializeFromByteArray(@NonNull byte[] serialized) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serialized)) {
            return deserialize(byteArrayInputStream);
        }
    }
}