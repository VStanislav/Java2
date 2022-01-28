package ru.voronkov.server.chat.auth;

import java.util.Set;

public class AuthService {

    private static final Set<User> USERS = Set.of(
            new User("login1","pass1","userName1"),
            new User("login2","pass2","userName2"),
            new User("login3","pass3","userName3")
    );

    public  String getUserNameByLoginAndPassword(String login,String password){
//        System.out.println("in getUserNameByLoginAndPassword");
        User requiredUser = new User(login,password);
        for (User user : USERS) {
            if (requiredUser.equals(user)){
                return user.getUserName();
            }
        }
//        System.out.println("out getUserNameByLoginAndPassword");
        return null;
    }
}
