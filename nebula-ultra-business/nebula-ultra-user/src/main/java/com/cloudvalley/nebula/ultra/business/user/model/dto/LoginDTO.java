package com.cloudvalley.nebula.ultra.business.user.model.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private String username;

    private String passwordHash;

}
