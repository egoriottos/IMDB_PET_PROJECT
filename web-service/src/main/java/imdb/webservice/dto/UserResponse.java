package imdb.webservice.dto;

import lombok.Data;

@Data
public class UserResponse {
    private String username;

    private String password;

    private String email;

    private String number;

    private Long age;
}
