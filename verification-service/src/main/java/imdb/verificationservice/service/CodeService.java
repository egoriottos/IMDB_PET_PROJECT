package imdb.verificationservice.service;

public interface CodeService {
    Long generateCode(String email);
    boolean validateCode(String email,String code);
}
