package imdb.webservice.dto;

import lombok.Data;

@Data
public class RegistrationDto {
    private String email;
    private String password;
    private String username;
    private String number;
    private Long age;
}
