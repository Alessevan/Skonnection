package fr.bakaaless.skonnection.plugin.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AESEncryption extends Encryption {

    public static AESEncryption getOrCreate(final String key) {
        return (AESEncryption) Encryption.getEncryption(key, EncryptionType.AES).orElse(new AESEncryption(key));
    }

    private SecretKeySpec secretKey;
    private byte[] byteKey;

    private boolean ready;

    public AESEncryption(final String key) {
        super(key);
        this.ready = false;
        try  {
            this.byteKey = key.getBytes(StandardCharsets.UTF_8);
            this.byteKey = MessageDigest.getInstance("SHA-1").digest(this.byteKey);
            this.byteKey = Arrays.copyOf(this.byteKey, 16);
            this.secretKey = new SecretKeySpec(this.byteKey, "AES");
            this.ready = true;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String encrypt(final String message) {
        if (!this.ready)
            return message;
        try {
            final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public String decrypt(final String message) {
        if (!this.ready)
            return message;
        try {
            final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(message)));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public EncryptionType getType() {
        return EncryptionType.AES;
    }
}
