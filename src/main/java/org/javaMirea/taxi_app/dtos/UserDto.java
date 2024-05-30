package org.javaMirea.taxi_app.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String username;
    private String email;
    private String rawPassword;
}
