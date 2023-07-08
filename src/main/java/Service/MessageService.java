package Service;

import java.util.List;

import DAO.MessageDao;
import Model.Account;
import Model.Message;

public class MessageService {
    
    public MessageDao messageDao;
    public AccountService accountService;

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
        if (m.getMessage_text() == "" || m.getMessage_text().length() > 254) {
            return null;
        }
        return this.messageDao.updateMessageById(m);
    }
    public Message createNewMessage(Message m) {
        // initial message validation
        if (m.getMessage_text() == "" || m.getMessage_text().length() > 254) {
            return null;
        }
        
        return this.messageDao.createNewMessage(m);
    }
}