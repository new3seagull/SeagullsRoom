package com.new3seagull.SeagullsRoom.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

public class ImageUtils {
    public static String encodeImageToBase64(MultipartFile imageFile) throws IOException {
        byte[] fileContent = imageFile.getBytes();
        return Base64.getEncoder().encodeToString(fileContent);
    }
}