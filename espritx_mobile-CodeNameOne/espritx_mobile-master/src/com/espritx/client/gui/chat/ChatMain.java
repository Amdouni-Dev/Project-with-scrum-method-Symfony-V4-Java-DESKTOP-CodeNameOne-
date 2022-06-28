package com.espritx.client.gui.chat;

import com.codename1.rad.models.Entity;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.chatroom.ChatBubbleView;
import com.codename1.rad.ui.chatroom.ChatRoomView;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.plaf.UIManager;
import com.codename1.uikit.pheonixui.BaseForm;
import com.espritx.client.entities.Conversation;
import com.espritx.client.entities.Message;
import com.espritx.client.services.Conversation.ServiceConversation;
import com.espritx.client.services.Conversation.ServiceMessages;
import com.espritx.client.services.User.AuthenticationService;

import static com.codename1.rad.ui.UI.*;

import com.codename1.ui.util.Resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatMain extends BaseForm {
    private Entity CreateMessage(String text,
                                 Date datePosted,
                                 String participant,
                                 String iconUrl) {
        ChatBubbleView.ViewModel msg = new ChatBubbleView.ViewModel();
        msg.messageText(text)
                .date(datePosted)
                .iconUrl(iconUrl)
                .isOwn(participant == null);
        if (participant != null) {
            msg.postedBy(participant);
        }
        return msg;

    }

    public ChatMain(Resources resourceObjectInstance, Conversation c1) {
        /*installSidemenu(resourceObjectInstance);*/
        System.out.println(c1);
        ServiceConversation s2 = new ServiceConversation();
        List<Conversation> lu = new ArrayList<>();
        lu = s2.afficher_conver();
        lu = s2.Filter_conver(lu);
        ServiceMessages s1 = new ServiceMessages();
        List<Message> listeMessage = new ArrayList<>();
        listeMessage = s1.afficher_message();

        UIManager.initNamedTheme("/theme", "Theme");
        ChatRoomView.ViewModel room = new ChatRoomView.ViewModel();
        // Make up some dummy times for the chat messages.

        long SECOND = 1000l;
        long MINUTE = SECOND * 60;
        long HOUR = MINUTE * 60;
        long DAY = HOUR * 24;

        // Make first message 2 days ago.
        long t = System.currentTimeMillis() - 2 * DAY;

        // Some thumbnails for the avatars of the participants
        String georgeThumb = "https://weblite.ca/cn1tests/radchat/george.jpg";
        String kramerThumb = "https://weblite.ca/cn1tests/radchat/kramer.jpg";
        String ImageThumb = "https://cdn.pixabay.com/photo/2016/11/29/12/13/fence-1869401_640.jpg";
        t += HOUR;
        for (Message msg : listeMessage) {
            if (msg.Conversationid.getInt() == c1.id.getInt()) {
                if (msg.author.get().id.getInt() == (AuthenticationService.getAuthenticatedUser().id.getInt())) {
                    room.addMessages(CreateMessage(msg.content.get(),
                            new Date(t), null, georgeThumb));
                } else {
                    room.addMessages(CreateMessage(msg.content.get(),
                            new Date(t), msg.author.get().first_name.get(), georgeThumb));
                }

            }
        }

        ActionNode send = action(icon(FontImage.MATERIAL_SEND));
        ViewNode viewNode = new ViewNode(
                actions(ChatRoomView.SEND_ACTION, send)
        );
        send.addActionListener(evt -> {
            String textFieldContents = room.getInputBuffer();
            if (textFieldContents != null && !textFieldContents.isEmpty()) {
                ChatBubbleView.ViewModel message = new ChatBubbleView.ViewModel();
                message.messageText(textFieldContents);
                message.date(new Date());
                message.isOwn(true);


                // Now add the message
                room.addMessages(message);
                Message m = new Message();
                m.author.set(AuthenticationService.getAuthenticatedUser());
                m.content.set(textFieldContents);
                m.Conversationid.set(c1.id.getInt());
                s1.Add_Message(m);

                room.inputBuffer("");
            }

        });
        ChatRoomView view = new ChatRoomView(room, viewNode, this);
        addAll(view);
    }


}
