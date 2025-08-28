package com.example.userdataprocessingstorage.dto.response;

import com.example.userdataprocessingstorage.enums.FileType;
import com.example.userdataprocessingstorage.model.User;

public record UserOutput(Long id, String name, String email, FileType source) {

    public static UserOutput toOutput(User u) {
        return new UserOutput(u.getId(), u.getName(), u.getEmail(), u.getSource());
    }

}


