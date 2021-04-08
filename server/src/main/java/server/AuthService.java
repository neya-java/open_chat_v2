package server;

public interface AuthService {
    /**
     * метод получения nickname по логину и паролю
     * Если учетки с таким логином и паролем нет то вернет null
     * Если учетка с таким логином и паролем есть то вернет nickname
     * @return nickname если есть совпадеие по логину и паролюб null если нет совпадений
     * */
    String getNicknameByLoginAndPassword(String login, String password);

    boolean getNicknameYesNo(String name);

}
