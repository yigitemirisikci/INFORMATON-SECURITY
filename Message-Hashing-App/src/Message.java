public class Message {
    String messageID;
    String content;
    String password;
    User receiver;

    public Message(String messageID, String content, String password, User receiver) {
        this.messageID = messageID;
        this.content = content;
        this.password = password;
        this.receiver = receiver;
    }
}
