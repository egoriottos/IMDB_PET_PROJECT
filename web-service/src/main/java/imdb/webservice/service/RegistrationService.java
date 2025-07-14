package imdb.webservice.service;

public interface RegistrationService {
    String registration(String username, String password, String email, Long age, String number);
    String registrationAdmin(String username, String password, String email, Long age, String number);

}
