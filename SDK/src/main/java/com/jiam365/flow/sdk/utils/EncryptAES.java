package com.jiam365.flow.sdk.utils;

import com.jiam365.modules.utils.Cryptos;
import com.jiam365.modules.utils.Encodes;

public class EncryptAES
{
    public static String encryptTelecom(String str, String skey, String siv)
    {
        byte[] key = skey.getBytes();
        byte[] iv = siv.getBytes();
        byte[] encryptResult = Cryptos.aesEncrypt(str.getBytes(), key, iv);
        return Encodes.encodeHexAsLetter(encryptResult);
    }

    public static String decryptTelecom(String str, String skey, String siv)
    {
        byte[] key = skey.getBytes();
        byte[] iv = siv.getBytes();

        byte[] bs = Encodes.decodeFromLetter(str);
        return Cryptos.aesDecrypt(bs, key, iv);
    }
}