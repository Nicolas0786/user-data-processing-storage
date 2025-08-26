package com.example.userdataprocessingstorage.enums;

public enum FileType {
    CSV, JSON, XML;

    public static FileType from(String s) {
        if (s == null) throw new IllegalArgumentException("Type ausente");
        return FileType.valueOf(s.trim().toLowerCase());
    }

}
