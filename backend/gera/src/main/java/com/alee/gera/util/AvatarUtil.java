package com.alee.gera.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class AvatarUtil {

    public String createAvatarName(){
        String fileName = UUID.randomUUID().toString();
        fileName+=".avatar";
        return fileName;
    }

    public boolean saveAvatar(MultipartFile file,String path){
        File dest = new File(path);
        try{
            file.transferTo(dest);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
