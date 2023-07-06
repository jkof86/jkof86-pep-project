package Service;

import java.util.List;

import DAO.MessageDao;
import Model.Message;

public class MessageService {
    
    public MessageDao messageDao;

    public MessageService() {
        this.messageDao = new MessageDao();
    }

    public List<Message> getAllMessages() {
        return this.messageDao.getAllMessages();
    }
    public Message getMessageById(int id) {
        return this.messageDao.getMessageById(id);
    }
    public List<Message> getAllMessagesByAccountId(int id) {
        return this.messageDao.getAllMessagesByAccountId(id);
    }
    public Message deleteMessageById(int id) {
        return this.messageDao.deleteMessageById(id);
    }
    public Message updateMessageById(Message m) {
        return this.messageDao.updateMessageById(m);
    }
    public Message createNewMessage(Message m) {
        return this.messageDao.createNewMessage(m);
    }
}