package ru.voronkov.server.chat.auth;

import java.util.Set;

public class AuthService {

    private static final Set<User> USERS = Set.of(
            new User("login1","password1","userName1"),
            new User("login2","password2","userName2"),
            new User("login3","password3","userName3")
    );

    public  String getUserNameByLoginAndPassword(String login,String password){
        User requiredUser = new User(login,password);
        for (User user : USERS) {
            if (requiredUser.equals(user)){
                return user.getUserName();
            }
        }
        return null;
    }
}
