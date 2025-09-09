package com.cloudvalley.nebula.monolith.business.user.model.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private String username;

    private String passwordHash;

}
