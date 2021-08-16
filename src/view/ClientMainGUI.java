package view;

import controller.Controller;
import model.Message;
import model.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientMainGUI extends JFrame
{
    private final Controller controller;

    //Komponenter
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
    private Font labelFont = new Font("", Font.PLAIN, 25);
    private ImageIcon uploadedImage = null;
    private String message;

    public ClientMainGUI(Controller controller)
    {
        this.controller = controller;
        InitializePanels();
        addListeners();
        controller.addSelectedUserToReceivers(controller.getUser()); // user (the owner of the gui) is always selected as a receiver by default
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
        Message[] chatLogs = {};
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
                ImageIcon imageIcon = null;

                if (e.getValueIsAdjusting()) // bara en handling vid mus klick
                {
                    imageIcon = message.getSentImage();
                }
                if (imageIcon != null)
                {
                    JFrame frame = new JFrame("Attached image");
                    JLabel label = new JLabel(imageIcon);
                    frame.add(label);
                    frame.pack();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null); // window gets placed on the middle of the screen
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

        User[] contactsArr = {};
        contactList = new JList(contactsArr);
        contactList.setBounds(30, 50, 235, 220);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // makes sure that one list index is selected at a time
        contactList.setBorder(blackline);
        contactList.setFont(new Font("", Font.PLAIN,20));

        User[] onlineUsersArr = {};
        onlineList = new JList(onlineUsersArr);
        onlineList.setBounds(30, 450, 235, 220);
        onlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // makes sure that one list index is selected at a time
        onlineList.setBorder(blackline);
        onlineList.setForeground(Color.green);
        onlineList.setFont(new Font("", Font.PLAIN,20));

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
        messageBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    message = messageBox.getText();
                    controller.buttonPressed(ButtonType.Send);
                    uploadedImage = null;
                    insertedImageLabel.setText("");
                    messageBox.setText("");
                }
            }
        });

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
                       File selectedImage = fileChooser.getSelectedFile();
                       insertedImageLabel.setText(selectedImage.getName());
                       uploadedImage = new ImageIcon(String.valueOf((selectedImage)));
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
                    controller.buttonPressed(ButtonType.Send);
                    uploadedImage = null;
                    insertedImageLabel.setText("");
                    messageBox.setText("");
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

    public String getMessage()
    {
        return message;
    }

    public ImageIcon getUploadedImage() {
        return uploadedImage;
    }

    private void updateChatBoxJList(Message message) {
        ListModel chatBoxListModel = chatBox.getModel(); // gets back what is inside chatbox-componenet (array)
        Message[] tmpMessages = new Message[chatBoxListModel.getSize()];

        for ( int i = 0; i < chatBoxListModel.getSize(); i++ ) {
            tmpMessages[i] = (Message) chatBoxListModel.getElementAt(i); //Kopierar över de existerande meddelanden till en temporär array.
        }

        ArrayList<Message> tempMessagesArrayList = new ArrayList<>(Arrays.asList(tmpMessages)); // convert array to arraylist
        tempMessagesArrayList.add(message); // add the new message

        tmpMessages = tempMessagesArrayList.toArray(new Message[0]);
        chatBox.setListData(tmpMessages); // update the gui component
    }

    public void updateChat(User user, String newChat){
        Message message = new Message(user,newChat);
        updateChatBoxJList(message);
    }

    public void updateChat(User user, String newChat, ImageIcon image){
        Message message = new Message(user, newChat, image);
        updateChatBoxJList(message);
    }

    public void updateChat(User user, ImageIcon image){
        Message message = new Message(user, image);
        updateChatBoxJList(message);
    }

    public void updateOnlineJList(User[] onlineUsers) {
        onlineList.removeAll();
        onlineList.setListData(onlineUsers);
    }

    private void updateReceiversLabel() {
        User[] selectedUsersArr = controller.getSelectedUsers().toArray(new User[0]);
        String selectedUsersStr = "Receivers: ";

        if(!controller.getSelectedUsers().isEmpty()) { // when the list is not empty
            for (User user : selectedUsersArr) {
                selectedUsersStr = selectedUsersStr + user.toString() + " / ";
                receiversNamesLabel.setText(selectedUsersStr);
            }
        } else { receiversNamesLabel.setText("Receivers: "); }
    }

    public void updateContactsJList(ArrayList<User> contacts) {
        User[] contactsArr = contacts.toArray(new User[0]); // convert arrayList to array
        contactList.setListData(contactsArr);
    }

    private void addListeners() {
        addReceiverFromOnlineUsersBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User selectedUser = onlineList.getSelectedValue();

                if(!controller.getSelectedUsers().contains(selectedUser)) { // if the selected user hasn't been added yet as a receiver
                    controller.addSelectedUserToReceivers(selectedUser);
                    updateReceiversLabel();
                } else { JOptionPane.showMessageDialog(null, "Already added as a receiver!"); }
            }
        });

        addReceiverFromContactsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User selectedUser = contactList.getSelectedValue();

                if(!controller.getSelectedUsers().contains(selectedUser)) { // if the selected user hasn't been added yet as a receiver
                    controller.addSelectedUserToReceivers(selectedUser);
                    updateReceiversLabel();
                } else { JOptionPane.showMessageDialog(null, "Already added as a receiver!"); }
            }
        });

        clearAllReceiversBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clearAllSelectedReceivers();
                controller.addSelectedUserToReceivers(controller.getUser()); // user (the owner of the gui) is always selected as a receiver by default
                updateReceiversLabel();
            }
        });

        addContactBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User selectedUser = onlineList.getSelectedValue();
                ArrayList<User> tempContacts = controller.getContacts().fetchContactsFromFile();

                if(selectedUser.getUsername().equals(controller.getUser().getUsername())) {
                    JOptionPane.showMessageDialog(null, "Cannot add yourself to contact list!");
                }
                else if(tempContacts.contains(selectedUser)) {
                    JOptionPane.showMessageDialog(null, "The selected user has already been added!");
                }
                else {
                    tempContacts.add(selectedUser);
                    User[] contactsArr = tempContacts.toArray(new User[0]); // convert arrayList to array
                    contactList.setListData(contactsArr);
                    controller.saveNewContact(selectedUser); // updates contacts in the file
                }

                System.out.println(tempContacts.size());
            }
        });

        removeContactBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<User> tempContacts = controller.getContacts().fetchContactsFromFile();

                if (tempContacts.size() > 0)
                {
                    User selectedContact = contactList.getSelectedValue(); //get selected user from contactlist
                    tempContacts.remove(selectedContact);
                    updateContactsJList(tempContacts);
                    controller.removeContact(selectedContact); // updates contacts in the file
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "You have no contacts to delete.");
                }

                System.out.println(tempContacts.size());
            }
        });
    }
}