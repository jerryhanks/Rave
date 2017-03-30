package com.flutterwave.rave.utils;

import com.flutterwave.rave.models.request.BaseRequestData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.codec.binary.Base64;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Shittu on 22/12/2016.
 */

public class RaveUtil {

    private static final String ALGORITHM = "DESede";
    private static final String TRANSFORMATION = "DESede/ECB/PKCS5Padding";
    private static final String TARGET = "FLWSECK-";
    private static final String MD5 = "MD5";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String INVALID_ARGUMENT = "Invalid Argument";
    private static final String UTF_8 = "utf-8";

    //used for all serialization and deserialization
    private static Gson gson = new GsonBuilder()
            .create();

    public static String addPadding(String t, String s, int num) {
        StringBuilder retVal;

        if (null == s || 0 >= num) {
            throw new IllegalArgumentException(INVALID_ARGUMENT);
        }

        if (s.length() <= num) {
            return s.concat(t);
        }

        retVal = new StringBuilder(s);

        for (int i = retVal.length(); i > 0; i -= num) {
            retVal.insert(i, t);
        }
        return retVal.toString();
    }


    public static String cleanText(String text, String removeCharacter) {
        return text.trim().replace(removeCharacter, "");
    }

    public static String getJsonStringFromRequestData(BaseRequestData requestData) {

        return gson.toJson(requestData);

    }

    @Deprecated
    public static Map<String, Object> getMapFromJsonString(String jsonString) {
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();

        return new Gson().fromJson(jsonString, type);
    }

    public static String getEncryptedData(String unEncryptedString, String secret) {
        try {
            // hash the secret
            String md5Hash = getMd5(secret);
            String cleanSecret = secret.replace(TARGET, "");
            int hashLength = md5Hash.length();
            String encryptionKey = cleanSecret.substring(0, 12).concat(md5Hash.substring(hashLength - 12, hashLength));

            return harden(unEncryptedString, encryptionKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String harden(String data, String key) throws Exception {
        byte[] keyBytes = key.getBytes(UTF_8);
        SecretKeySpec sKey = new SecretKeySpec(keyBytes, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        cipher.init(Cipher.ENCRYPT_MODE, sKey);
        byte[] plainTextBytes = data.getBytes(UTF_8);
        byte[] buf = cipher.doFinal(plainTextBytes);
        byte[] base64Bytes = Base64.encodeBase64(buf);
        String base64EncryptedString = new String(base64Bytes);

        return base64EncryptedString;
    }

    private static String getMd5(String md5) throws Exception {
        MessageDigest md = java.security.MessageDigest.getInstance(MD5);
        byte[] array = md.digest(md5.getBytes(CHARSET_NAME));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

}
