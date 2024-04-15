package kr.junhyung.springbukkit.serializer;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.lang.NonNull;

import java.io.*;

public class BukkitSerializer implements Serializer<ConfigurationSerializable>, Deserializer<ConfigurationSerializable> {
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
