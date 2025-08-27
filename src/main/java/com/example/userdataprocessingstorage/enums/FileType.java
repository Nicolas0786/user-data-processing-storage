package com.example.userdataprocessingstorage.enums;

public enum FileType {
    CSV, JSON, XML;

    public static FileType exists(String type) {
        if (type == null) throw new IllegalArgumentException("Type ausente");
        return FileType.valueOf(type.trim().toUpperCase());
    }

}
