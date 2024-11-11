package com.library;

import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    // Đọc ảnh từ thư mục resources và trả về dưới dạng byte array
    public static byte[] getDefaultImageBytes(String resourcePath) throws IOException {
        try (InputStream inputStream = ImageUtils.class.getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                return inputStream.readAllBytes();
            } else {
                throw new IOException("Image not found in resources: " + resourcePath);
            }
        }
    }
}
