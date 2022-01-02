/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chat.ejb;

 
import java.util.Date;
import javax.ejb.Local;
import chat.Message;
 
/**
 * Local interface for chat lagic EJB
 * @author Juan Diego
 */
@Local
public interface MessageManagerLocal {
 
    void sendMessage(Message msg);
 
    Message getFirstAfter(Date after);
 
}
