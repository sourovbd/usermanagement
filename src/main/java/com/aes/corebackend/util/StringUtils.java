package com.aes.corebackend.util;

public class StringUtils {
    public boolean isEmptyOrNull(String string){
        return (string.isEmpty() || string == null) ? true : false;
    }

}
