package view;

import controller.Controller;
import model.Message;
import model.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ClientMainGUI extends JFrame
{
    //Komponenter
    private Controller controller;
    private JPanel leftChatPanel;
    private JPanel rightContactsPanel;
    private JPanel southPanel;
    private JFrame frame;
    private JList<Message> chatBox;
    private JList<User> contactList;
    private JList<User> onlineList;
    private JButton addReceiverFromContactsBtn;
    private JButton removeContactBtn;
    private JButton addReceiverFromOnlineUsersBtn;
    private JButton addContactBtn;
    private JLabel receiversNamesLabel;
    private JTextField messageBox; // where the text message is written
    private JLabel insertedImageLabel;
    private JButton openFileButton;
    private JButton sendButton;
    private JButton clearAllReceiversBtn;

    private JFileChooser fileChooser;
    private File selectedImage;
    private Font labelFont = new Font("", Font.PLAIN, 25);

    //Vald användare i kontaktlista
    private User selectedUser = null;

    //Test users;
    User Mads = new User("Mads", new ImageIcon("images/goat.jpg"));
    User Jagtej = new User("Jagtej", new ImageIcon("images/robot.png"));

    //Kontaktlista
    User[] contacts = {Mads,Jagtej};
    String[] online = {};

    Message testMessage = new Message(Mads, "hej på dig!", new ImageIcon("images/goat.jpg"), null);
    Message testMessage2 = new Message(Jagtej, "Tjo snygging!!");

    Message[] chatLogs = {testMessage, testMessage2};

    //Variabler som gör vi kan hämta satta värden av programmet.
    private String username;
    private ImageIcon imageIcon;
    private String message;


    public ClientMainGUI(Controller controller)
    {
        this.controller = controller;
        InitializePanels();
    }

    public void InitializePanels()
    {
        createMainFrame();
        createSouthPanel();
        createLeftPanel();
        createRightPanel();
    }

    public void createMainFrame()
    {
        frame = new JFrame("Logged in");
        frame.setBounds(0, 0, 900, 820);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); // window gets placed on the middle of the screen
    }

    public void createLeftPanel()
    {
        leftChatPanel = new JPanel();
        leftChatPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        leftChatPanel.setBounds(20,20,530,450);
        leftChatPanel.setLayout(null);

        //Skapar chatt label
        JLabel lblChatbox = new JLabel("Chat");
        lblChatbox.setBounds(30,10,100,30);
        lblChatbox.setFont(labelFont);

        //Skapar chattbox
        chatBox = new JList<>(chatLogs);
        chatBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chatBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));

        // scrollbar
        JScrollPane scrollPane = new JScrollPane(chatBox);
        scrollPane.setBounds(30,50,470,380);
        scrollPane.getSize(chatBox.getPreferredScrollableViewportSize());

        chatBox.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Message message = chatBox.getSelectedValue();
                ImageIcon imageIcon = message.getSentImage();
                if (e.getValueIsAdjusting()) // bara en handling vid mus klick
                {
                    return;
                }
                if (imageIcon != null)
                {
                   JFrame frame = new JFrame("Attached image");
                   JLabel label = new JLabel(imageIcon);
                   frame.add(label);
                   frame.pack();
                   frame.setVisible(true);
                }
            }
        });

        leftChatPanel.add(lblChatbox);
        leftChatPanel.add(scrollPane);
        frame.add(leftChatPanel);
    }

    public void createRightPanel()
    {
        rightContactsPanel = new JPanel();
        rightContactsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        rightContactsPanel.setBounds(570,20,295,740);
        rightContactsPanel.setLayout(null);

        JLabel lblContacts = new JLabel("Contacts");
        lblContacts.setBounds(30,10,100,30);
        JLabel lblOnline = new JLabel("Online users");
        lblOnline.setBounds(30,410,150,30);
        lblContacts.setFont(labelFont);
        lblOnline.setFont(labelFont);

        addReceiverFromContactsBtn = new JButton("Add reciever");
        addReceiverFromContactsBtn.setBounds(30, 280, 115, 50);
        removeContactBtn = new JButton("Remove contact");
        removeContactBtn.setMargin(new Insets(1,1,1,1)); // changes the padding of the button
        removeContactBtn.setBounds(150, 280, 115, 50);

        Border blackline = BorderFactory.createLineBorder(Color.black);
        contactList = new JList(contacts);
        contactList.setBounds(30, 50, 235, 220);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // makes sure that one list index is selected at a time
        contactList.setBorder(blackline);
        contactList.setFont(new Font("", Font.PLAIN,20));

        contactList.addListSelectionListener(new ListSelectionListener() { // FIXME : leder till 2 handlingar vid mus klick
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting())
                {
                    return;
                }
                User selected = contactList.getSelectedValue();
                username = selected.getUsername();
                imageIcon = selected.getImageIcon();
                selectedUser = new User(username,imageIcon);
            }
        });

        onlineList = new JList(online);
        onlineList.setBounds(30, 450, 235, 220);
        onlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // makes sure that one list index is selected at a time
        onlineList.setBorder(blackline);
        onlineList.setForeground(Color.green);
        onlineList.setFont(new Font("", Font.PLAIN,20));

        onlineList.addListSelectionListener(new ListSelectionListener() { // FIXME : leder till 2 handlingar vid mus klick
            @Override
            public void valueChanged(ListSelectionEvent e) {
                User selected = onlineList.getSelectedValue();
                username = selected.getUsername();
                imageIcon = selected.getImageIcon();
//                selectedUser = new User(username,imageIcon);
                System.out.println(username);
            }
        });

        addReceiverFromOnlineUsersBtn = new JButton("Add receiver");
        addReceiverFromOnlineUsersBtn.setBounds(30, 680, 115, 50);
        addContactBtn = new JButton("Add contact");
        addContactBtn.setBounds(150, 680, 115, 50);

        rightContactsPanel.add(lblContacts);
        rightContactsPanel.add(contactList);
        rightContactsPanel.add(addReceiverFromContactsBtn);
        rightContactsPanel.add(removeContactBtn);
        rightContactsPanel.add(lblOnline);
        rightContactsPanel.add(onlineList);
        rightContactsPanel.add(addReceiverFromOnlineUsersBtn);
        rightContactsPanel.add(addContactBtn);
        frame.add(rightContactsPanel);
    }


    public void createSouthPanel()
    {
        southPanel = new JPanel();
        southPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        southPanel.setBounds(20, 490, 530, 270);
        southPanel.setLayout(null);

        receiversNamesLabel = new JLabel("No receivers selected..");
        receiversNamesLabel.setBounds(30, 30, 470, 50);
        receiversNamesLabel.setBackground(Color.lightGray);
        receiversNamesLabel.setOpaque(true);

        messageBox = new JTextField();
        messageBox.setBounds(30, 90, 470, 50);

        sendButton = new JButton("Send Message");
        sendButton.setBounds(30, 210, 325, 50);
        clearAllReceiversBtn = new JButton("Clear receivers");
        clearAllReceiversBtn.setMargin(new Insets(1,1,1,1)); // changes the padding of the button
        clearAllReceiversBtn.setBounds(385, 210, 115, 50);

        insertedImageLabel = new JLabel("There is not an inserted image at the moment..");
        insertedImageLabel.setBounds(30, 150, 325, 50);
        insertedImageLabel.setBackground(Color.lightGray);
        insertedImageLabel.setOpaque(true);
        openFileButton = new JButton("Open file");
        openFileButton.setBounds(385, 150, 115, 50);
        fileChooser = new JFileChooser("images");

        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if(e.getSource() == openFileButton)
               {
                   int returnVal = fileChooser.showOpenDialog(ClientMainGUI.this);
                   if (returnVal == JFileChooser.APPROVE_OPTION)
                   {
                       selectedImage = fileChooser.getSelectedFile();
                       System.out.println("images/"+selectedImage.getName());
                       ImageIcon imageIcon = new ImageIcon(String.valueOf((selectedImage)));
                       JFrame frame = new JFrame();
                       JLabel label = new JLabel(imageIcon);
                       label.setVisible(true);
                       label.setPreferredSize(new Dimension(100,100));
                       frame.add(label);
                       frame.pack();
                       frame.setVisible(true);
                       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                   }
               }
            }
        });

        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == openFileButton)
                {
                    int returnVal = fileChooser.showOpenDialog(ClientMainGUI.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION)
                    {
                        File file = fileChooser.getSelectedFile();
                        ImageIcon imageIcon = new ImageIcon(("images/"+file));
                        JFrame frame = new JFrame();
                        JLabel label = new JLabel(imageIcon);
                        frame.add(label);
                        frame.setVisible(true);
                    }
                }
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==sendButton)
                {
                    message = messageBox.getText();
                    selectedImage = null;
                    controller.buttonPressed(ButtonType.Send);
                }
            }
        });

        southPanel.add(receiversNamesLabel);
        southPanel.add(messageBox);
        southPanel.add(insertedImageLabel);
        southPanel.add(openFileButton);
        southPanel.add(sendButton);
        southPanel.add(clearAllReceiversBtn);
        frame.add(southPanel);
    }

//    public void addContact(User user)
//    {
//        User[] tmpContacts = contacts;
//        int size = tmpContacts.length;
//        User[] newContacts = new User[size+1];
//        for (int i = 0;i<tmpContacts.length;i++)
//        {
//            newContacts[i] = tmpContacts[i];
//        }
//        newContacts[size+1] = user;
//    }
//
//    public User getSelectedContact()
//    {
//        return selectedUser;
//    }

    public String getMessage()
    {
        if (messageBox.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"The chat chatbox is empty, right something before u send a message!");
            return null;
        }
        return message;
    }

//    public ImageIcon getImageIcon()
//    {
//        if (selectedImage == null)
//        {
//            return null;
//        }
//        else return new ImageIcon("images/"+selectedImage.getName());
//    }

    public void updateChat(String newChat, User user){
        Message message = new Message(user,newChat);
        Message[] tmp = new Message[chatLogs.length+1];
        for (int i = 0;i<chatLogs.length;i++) //Kopierar över de existerande meddelanden till en temporär array.
        {
            tmp[i] = chatLogs[i];
        }

        tmp[chatLogs.length] = message; //Lägger in det nya meddelandet.
        chatLogs = new Message[tmp.length];
        for (int j = 0; j<tmp.length;j++) //Lägger tillbaka meddelanden tillsammans med det nya skapade.
        {
            chatLogs[j] = tmp[j];
        }

        chatBox.setListData(chatLogs); // update the gui componenet
    }

//    public void updateChat(String newChat, User user, ImageIcon image){
//        String chat = newChat;
//        Message message = new Message(user,chat,image);
//        Message[] tmp = new Message[chatLogs.length+1];
//        for (int i = 0;i<chatLogs.length;i++) //Kopierar över de existerande meddelanden till en temporär array.
//        {
//            tmp[i] = chatLogs[i];
//        }
//
//        tmp[chatLogs.length] = message; //Lägger in det nya meddelandet.
//        chatLogs = new Message[tmp.length];
//        for (int j = 0; j<tmp.length;j++) //Lägger tillbaka meddelanden tillsammans med det nya skapade.
//        {
//            chatLogs[j] = tmp[j];
//        }
//    }
//
//    public void updateChat(User user, ImageIcon image){
//        Message message = new Message(user,image);
//        Message[] tmp = new Message[chatLogs.length+1];
//        for (int i = 0;i<chatLogs.length;i++) //Kopierar över de existerande meddelanden till en temporär array.
//        {
//            tmp[i] = chatLogs[i];
//        }
//
//        tmp[chatLogs.length] = message; //Lägger in det nya meddelandet.
//        chatLogs = new Message[tmp.length];
//        for (int j = 0; j<tmp.length;j++) //Lägger tillbaka meddelanden tillsammans med det nya skapade.
//        {
//            chatLogs[j] = tmp[j];
//        }
//    }

    public void updateOnlineJList(User[] onlineUsers) {
        onlineList.removeAll();
        onlineList.setListData(onlineUsers);
    }
}

