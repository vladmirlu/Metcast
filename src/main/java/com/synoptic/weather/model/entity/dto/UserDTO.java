package com.synoptic.weather.model.entity.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * User data transfer object
 * */
@Data
@Builder
public class UserDTO {

    private long id;

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    /**
     * Customised to string method
     *
     * @return current object as string
     * */
    @Override
    public String toString(){
        return new StringBuilder().append("UserDTO: { id: ").append(id).append(", username")
                .append(username).append(", email: ").append(email).toString();
    }

}
