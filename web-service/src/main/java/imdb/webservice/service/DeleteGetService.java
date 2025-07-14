package imdb.webservice.service;

import imdb.webservice.dto.UserDto;
import imdb.webservice.dto.UserResponse;
import java.util.List;

public interface DeleteGetService {
    String deleteUser(Long id, String jwt);
    List<UserDto> getAll(String jwt);
}
