package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private String nickname;

    public ClientHandler(Server server, Socket socket) {
        try {

            this.server = server;
            this.socket = socket;

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    //цикл authentication
                    while (true) {
                        String str = in.readUTF(); // получили сообщение

                        if (str.equals("/end")) { // проверяем, что не end
                            out.writeUTF("/end");
                            break;
                        }
                        if (str.startsWith("/auth")) { // проверяем, что авторизационное
                            String[] token = str.split("\\s+");
                            String newNick = server
                                    .getAuthService()
                                    .getNicknameByLoginAndPassword(token[1], token[2]);
                            if (newNick != null) {
                                nickname = newNick;
                                sendMsg("/auth_ok " + nickname);
                                server.subscribe(this);
                                System.out.println("Client authenticated. nick:" + nickname +
                                        " Address: " + socket.getRemoteSocketAddress());
                                break;
                            } else {
                                sendMsg("error login password");
                            }

                        }
                    }

                    //цикл работы
                    while (true) { // авторизовались, идем в чат
                        String str = in.readUTF();

                        if (str.startsWith("/")) {
                            if (str.equals("/end")) {
                                out.writeUTF("/end");
                                break;
                            }

                            String[] token = str.split("\\s+", 2);
                            String user = token[0].substring(1);
                            if (server.getAuthService().getNicknameYesNo(user)) {

                                server.userMsg(nickname, user, token[1]);

                            } else {
                                out.writeUTF("/there is no such user");
                            }


                        } else {
                            server.broadcastMsg(this, str); // поэтому отрабатывает broadcastMsg
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    server.unsubscribe(this);
                    System.out.println("Client disconnect " + socket.getRemoteSocketAddress());
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }
}
