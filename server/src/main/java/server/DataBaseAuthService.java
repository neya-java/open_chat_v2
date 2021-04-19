package server;

public class DataBaseAuthService implements AuthService {
    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        return DataBaseHandler.getNicknameByLoginAndPassword(login, password);
    }

//    @Override
//    public boolean getNicknameYesNo(String name) {
//        return DataBaseHandler.changeNick(name);
//    }

    @Override
    public boolean registration(String login, String pass, String nick) {
        return DataBaseHandler.registration(login, pass, nick);
    }
}
