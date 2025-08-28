package com.example.userdataprocessingstorage.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInput {

    @NotBlank(message = "Name obrigatorio")
    @NonNull
    private String name;

    @NotBlank(message = "Email obrigatorio")
    @Email(message = "Email inválido")
    @NonNull
    private String email;
}
