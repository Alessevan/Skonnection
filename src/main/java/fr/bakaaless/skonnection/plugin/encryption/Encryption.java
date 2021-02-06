package fr.bakaaless.skonnection.plugin.encryption;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Encryption {

    private static List<Encryption> registered = new ArrayList<>();

    public static Optional<Encryption> getEncryption(final String key) {
        return registered.stream().filter(encrypt -> encrypt.rawKey.equals(key)).findFirst();
    }

    public static Optional<Encryption> getEncryption(final String key, final EncryptionType type) {
        return registered.stream().filter(encrypt ->
                encrypt.rawKey.equals(key) && encrypt.getType() == type
        ).findFirst();
    }

    private final String rawKey;

    public Encryption(final String key) {
        this.rawKey = key;
    }

    public abstract String encrypt(final String message);

    public abstract String decrypt(final String message);

    public abstract EncryptionType getType();

    public enum EncryptionType {
        AES;
    }
}
