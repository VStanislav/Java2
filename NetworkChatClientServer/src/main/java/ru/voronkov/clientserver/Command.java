package ru.voronkov.clientserver;

import ru.voronkov.clientserver.commands.*;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {

    private Object data;
    private CommandType type;

    public static Command authCommand(String login,String password){
        Command command = new Command();
        command.type = CommandType.AUTH;
        command.data = new AuthCommandData(login,password);
        return command;
    }

    public static Command authOkCommand(String userName){
        Command command = new Command();
        command.type = CommandType.AUTH_OK;
        command.data = new AuthOkCommandData(userName);
        return command;
    }

    public static Command errorCommand(String errorMessage){
        Command command = new Command();
        command.type = CommandType.ERROR;
        command.data = new ErrorCammandData(errorMessage);
        return command;
    }

    public static Command privateMessageCommand(String receiver,String message){
        Command command = new Command();
        command.type = CommandType.PRIVATE_MESSAGE;
        command.data = new PrivateMessageCommandData(receiver,message);
        return command;
    }

    public static Command publicMessageCommand(String message){
        Command command = new Command();
        command.type = CommandType.PUBLIC_MESSAGE;
        command.data = new PublicMessageCommandData(message);
        return command;
    }

    public static Command clientMessageCommand(String sender,String message) {
        Command command = new Command();
        command.type = CommandType.CLIENT_MESSAGE;
        command.data = new ClientMessageCommandData(sender, message);
        return command;
    }

    public static Command updateUserListCommand(List<String> userListOnline) {
        Command command = new Command();
        command.type = CommandType.UPDATE_USER_LIST;
        command.data = new UpdateUserListCommandData(userListOnline);
        return command;
    }

    public Object getData() {
        return data;
    }

    public CommandType getType() {
        return type;
    }
}
