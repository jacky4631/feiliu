package com.jiam365.modules.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

public class Encodes
{
    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    public static String encodeHex(byte[] input)
    {
        return Hex.encodeHexString(input);
    }

    public static byte[] decodeHex(String input)
    {
        try
        {
            return Hex.decodeHex(input.toCharArray());
        }
        catch (DecoderException e)
        {
            throw Exceptions.unchecked(e);
        }
    }

    public static String encodeHexAsLetter(byte[] bytes)
    {
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes)
        {
            buf.append((char)((b >> 4 & 0xF) + 97));
            buf.append((char)((b & 0xF) + 97));
        }
        return buf.toString();
    }

    public static byte[] decodeFromLetter(String str)
    {
        if ((str == null) || (str.length() == 0) || (str.length() % 2 == 1)) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length(); i += 2)
        {
            char hi = (char)(str.charAt(i) - 'a');
            char lo = (char)(str.charAt(i + 1) - 'a');
            bytes[(i / 2)] = ((byte)(0xFF & (hi << '\004' | lo)));
        }
        return bytes;
    }

    public static String encodeBase64(byte[] input)
    {
        return Base64.encodeBase64String(input);
    }

    public static String encodeUrlSafeBase64(byte[] input)
    {
        return Base64.encodeBase64URLSafeString(input);
    }

    public static byte[] decodeBase64(String input)
    {
        return Base64.decodeBase64(input);
    }

    public static String escapeJava(String input)
    {
        return StringEscapeUtils.escapeJava(input);
    }

    public static String unescapeJava(String input)
    {
        return StringEscapeUtils.unescapeJava(input);
    }

    public static String escapeHtml(String html)
    {
        return StringEscapeUtils.escapeHtml4(html);
    }

    public static String unescapeHtml(String htmlEscaped)
    {
        return StringEscapeUtils.unescapeHtml4(htmlEscaped);
    }

    public static String escapeXml(String xml)
    {
        return StringEscapeUtils.escapeXml(xml);
    }

    public static String unescapeXml(String xmlEscaped)
    {
        return StringEscapeUtils.unescapeXml(xmlEscaped);
    }

    public static String urlEncode(String part)
    {
        try
        {
            return URLEncoder.encode(part, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw Exceptions.unchecked(e);
        }
    }

    public static String urlDecode(String part)
    {
        try
        {
            return URLDecoder.decode(part, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw Exceptions.unchecked(e);
        }
    }
}
