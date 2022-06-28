package edu.esprit.tests;

import edu.esprit.DAO.Chat.ChatCrud;
import edu.esprit.entities.Channel;
import edu.esprit.entities.Message;

public class ChatTests {
    public static void main(String[] args) {
        ChatCrud c1 = new ChatCrud();
        Channel tempChannel=new Channel(55);
        Message testMessage=new Message();
        c1.ReadChannels();
        tempChannel= c1.ReadChannels().get(0);
        testMessage=c1.ReadMessagesChannel(c1.FindChannelbyId(32)).get(0);
  /*    c1.DeletMessage(testMessage);*/
       /* c1.CreateMessage(c);*/


      /*  c1.ReadMessagesChannel(tempChannel);*/
     /*   c1.DeleteChannel( tempChannel);*/
    }
}
