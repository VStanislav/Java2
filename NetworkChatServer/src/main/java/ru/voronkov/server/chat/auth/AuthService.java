package ru.voronkov.server.chat.auth;

import java.util.Set;

public class AuthService implements IAuthService {

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

    @Override
    public void updateUsername(String currentUsername, String newUsername) {
        User user = getUserByUsername(currentUsername);
        if (user != null) {
            user.setUsername(newUsername);
        }
    }

    private User getUserByUsername(String username) {
        for (User user : USERS) {
            if (user.getUserName().equals(username)) {
                return user;
            }
        }

        return null;
    }
}
