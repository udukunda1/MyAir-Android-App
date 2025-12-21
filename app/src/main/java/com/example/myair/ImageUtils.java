package com.example.myair;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ImageUtils {

    /**
     * Decode a Base64 string to a Bitmap
     * @param base64String The Base64 encoded image string
     * @return Bitmap or null if decoding fails
     */
    public static Bitmap decodeBase64(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }

        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Encode a Bitmap to a Base64 string
     * @param bitmap The bitmap to encode
     * @return Base64 encoded string or null if encoding fails
     */
    public static String encodeBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        try {
            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
