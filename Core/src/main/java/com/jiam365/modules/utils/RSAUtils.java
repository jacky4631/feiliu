
package com.jiam365.modules.utils;

import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.Signature;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.crypto.Cipher;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.GeneralSecurityException;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyPair;
import java.util.HashMap;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.util.Map;

public class RSAUtils
{
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    public static Map<String, String> generateKeyPair(final int size) {
        try {
            final KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(size);
            final KeyPair keyPair = keyPairGen.generateKeyPair();
            final PublicKey publicKey = keyPair.getPublic();
            final PrivateKey privateKey = keyPair.getPrivate();
            final String publicKeyString = getKeyString(publicKey);
            final String privateKeyString = getKeyString(privateKey);
            final Map<String, String> map = new HashMap<String, String>();
            map.put("publicKey", publicKeyString);
            map.put("privateKey", privateKeyString);
            return map;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PublicKey getPublicKey(final String key) {
        final byte[] keyBytes = Encodes.decodeBase64(key);
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        try {
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static PrivateKey getPrivateKey(final String key) {
        final byte[] keyBytes = Encodes.decodeBase64(key);
        final PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(pkcs8KeySpec);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getKeyString(final Key key) throws Exception {
        final byte[] keyBytes = key.getEncoded();
        return Encodes.encodeBase64(keyBytes);
    }

    public static String encrypt(final PublicKey publicKey, final String plainText) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(1, publicKey);
            final byte[] result = cipher.doFinal(plainText.getBytes());
            return Encodes.encodeBase64(result);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(final String publicKeystore, final String plainText) {
        try (final BufferedReader br = new BufferedReader(new FileReader(publicKeystore))) {
            String publicKeyString = "";
            String str;
            while ((str = br.readLine()) != null) {
                publicKeyString += str;
            }
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(1, getPublicKey(publicKeyString));
            final byte[] enBytes = cipher.doFinal(plainText.getBytes());
            return Encodes.encodeBase64(enBytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(final PrivateKey privateKey, final String enStr) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, privateKey);
            final byte[] deBytes = cipher.doFinal(Encodes.decodeBase64(enStr));
            return new String(deBytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(final String privateKeystore, final String enStr) {
        try (final BufferedReader br = new BufferedReader(new FileReader(privateKeystore))) {
            String privateKeyString = "";
            String str;
            while ((str = br.readLine()) != null) {
                privateKeyString += str;
            }
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, getPrivateKey(privateKeyString));
            final byte[] deBytes = cipher.doFinal(Encodes.decodeBase64(enStr));
            return new String(deBytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sign(final byte[] data, final String privateKey) {
        try {
            final PrivateKey privateK = getPrivateKey(privateKey);
            final Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(privateK);
            signature.update(data);
            return Encodes.encodeBase64(signature.sign());
        }
        catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public static String sign(final byte[] data, final PrivateKey privateKey) {
        try {
            final Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(privateKey);
            signature.update(data);
            return Encodes.encodeBase64(signature.sign());
        }
        catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}