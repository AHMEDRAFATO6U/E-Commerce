package com.boot_demo1.ecommerce_springboot.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadimage(String path, MultipartFile file) throws IOException;
}
