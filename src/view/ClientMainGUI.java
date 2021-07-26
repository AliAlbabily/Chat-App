package view;

import controller.Controller;
import model.Message;
import model.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

public class ClientMainGUI extends JFrame
{

    //Komponeneter
    private Controller controller;
    private JPanel leftChatPanel;
    private JPanel rightContactsPanel;
    private JPanel southPanel;
    private JFrame frame;
    private JList<Message> chatBox;
    private JList<User> contactList;
    private JList<User> onlineList;
    private JButton sendButton;
    private JButton openFileButton;
    private JTextField messageBox;
    private JFileChooser fileChooser;
    private File selectedImage;
    private Font labelFont = new Font("", Font.PLAIN, 25);


    //Vald användare i kontaktlista
    private User selectedUser = null;

    //Test users;
    User Mads = new User("Madzic", new ImageIcon("images/goat.jpg"));
    User Jagtej = new User("DeGGi", new ImageIcon("images/robot.png"));
//    User Hanis = new User("5nis", new ImageIcon("images/tuff-guy.jpg"));
//    User Ali = new User("Ali", new ImageIcon("images/ma-girl.jpg"));

    //Kontaktlista
    User[] contacts = {Mads,Jagtej};
    String[] online = {};

    HashMap<String, ImageIcon> contactListHM = new HashMap<String, ImageIcon>();

    Message test = new Message(Mads, "hej på dig!", new ImageIcon("images/goat.jpg"));
    //Våra test chattlogs
    Message[] chatLogs = {test,test,test};


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
        frame.setPreferredSize(new Dimension(920, 820));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
    }

    public void createLeftPanel()
    {
        Border blackline = BorderFactory.createLineBorder(Color.black);

        leftChatPanel = new JPanel();
        leftChatPanel.setPreferredSize(new Dimension(600,700));
        leftChatPanel.setVisible(true);
        leftChatPanel.setLayout(new BoxLayout(leftChatPanel,BoxLayout.Y_AXIS));

        //Skapar chatt label
        JLabel lblChatbox = new JLabel("Chat");
        lblChatbox.setFont(labelFont);

        //Skapar chattbox
        chatBox = new JList<>(chatLogs);
        chatBox.setBorder(blackline);
        chatBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chatBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        chatBox.setMinimumSize(new Dimension (400,600));
        chatBox.setMaximumSize(new Dimension(400, 600));
        //chatBox.setPreferredSize(new Dimension(450,500));
        chatBox.setEnabled(true);
        chatBox.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Message message = chatBox.getSelectedValue();
               ImageIcon imageIcon = message.getSentImage();
                if (e.getValueIsAdjusting())
                {
                    //System.out.println("Adjusting. Ignore this");
                    return;
                }

               if (imageIcon != null)
               {
                   JFrame frame = new JFrame();
                   JLabel header = new JLabel("Attached image");
                   JLabel label = new JLabel(imageIcon);
                   frame.add(label);
                   frame.pack();
                   frame.setVisible(true);
               }
            }
        });

        JScrollPane scrollPane = new JScrollPane(chatBox);
        scrollPane.getSize(chatBox.getPreferredScrollableViewportSize());
        leftChatPanel.add(lblChatbox);
        leftChatPanel.add(scrollPane);
        frame.add(leftChatPanel,BorderLayout.WEST);
        frame.pack();
    }

    public void createRightPanel()
    {
        rightContactsPanel = new JPanel();
        rightContactsPanel.setPreferredSize(new Dimension(300, 1000));
        rightContactsPanel.setLayout(null);
        rightContactsPanel.setVisible(true);

        JLabel lblContacts = new JLabel("Contacts");
        lblContacts.setBounds(55,30,100,50);
        JLabel lblOnline = new JLabel("Online users");
        lblOnline.setBounds(55,410,250,50);
        lblContacts.setFont(labelFont);
        lblOnline.setFont(labelFont);

        Border blackline = BorderFactory.createLineBorder(Color.black);
        contactList = new JList(contacts);
        contactList.setBounds(55, 70, 200, 250);
        contactList.setMaximumSize(new Dimension(250, 300));  // this line does not do the job
        contactList.setMinimumSize (new Dimension (250,300));
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactList.setBorder(blackline);
        contactList.setFont(new Font("", Font.PLAIN,20));

        // FIXME : leder till 2 handlingar vid mus klick
        contactList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting())
                {
                    //System.out.println("Adjusting. Ignore this");
                    return;
                }
                User selected = (User) contactList.getSelectedValue();
                username = selected.getUsername();
                imageIcon = selected.getImageIcon();
                selectedUser = new User(username,imageIcon);
            }
        });
        //

        onlineList = new JList(online);
        onlineList.setBounds(55, 450, 200, 250);
        onlineList.setMaximumSize(new Dimension(250, 300));  // this line does not do the job
        onlineList.setMinimumSize (new Dimension (250,300));
        onlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        onlineList.setBorder(blackline);
        onlineList.setForeground(Color.green);
        onlineList.setFont(new Font("", Font.PLAIN,20));

        // FIXME : leder till 2 handlingar vid mus klick
        onlineList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                User selected = onlineList.getSelectedValue();
                username = selected.getUsername();
                imageIcon = selected.getImageIcon();
//                selectedUser = new User(username,imageIcon);
                System.out.println(username);
            }
        });
        //

        rightContactsPanel.add(lblContacts);
        rightContactsPanel.add(contactList);
        rightContactsPanel.add(lblOnline);
        rightContactsPanel.add(onlineList);
        frame.add(rightContactsPanel,BorderLayout.EAST);
    }


    public void createSouthPanel()
    {
        southPanel = new JPanel();
        southPanel.setVisible(true);

        messageBox = new JTextField();
        sendButton = new JButton("Send");
        openFileButton = new JButton("Open file");
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
        messageBox.setPreferredSize(new Dimension(300,40));
        sendButton.setPreferredSize(new Dimension(200,40));
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message = messageBox.getText();
            }
        });
        addListeners();
        southPanel.add(messageBox);
        southPanel.add(sendButton);
        southPanel.add(openFileButton);
        frame.add(southPanel,BorderLayout.SOUTH);
    }

    private void addListeners() {
        ActionListener listener = new ButtonActionListeners();
        sendButton.addActionListener(listener);
    }

    class ButtonActionListeners implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource()==sendButton)
            {
                controller.buttonPressed(ButtonType.Send);
                selectedImage = null;
            }
        }
        }

    public void addContact(User user)
    {
        User[] tmpContacts = contacts;
        int size = tmpContacts.length;
        User[] newContacts = new User[size+1];
        for (int i = 0;i<tmpContacts.length;i++)
        {
            newContacts[i] = tmpContacts[i];
        }
        newContacts[size+1] = user;
    }

    public User getSelectedContact()
    {
        return selectedUser;
    }

    public String getMessage()
    {
        if (messageBox.equals(""))
        {
            JOptionPane.showMessageDialog(null,"Du måste fylla i chatboxen för att skicka.");
            return null;
        }
        return message;
    }

    public ImageIcon getImageIcon()
    {
        if (selectedImage == null)
        {
            return null;
        }
        else return new ImageIcon("images/"+selectedImage.getName());
    }

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
    }

    public void updateChat(String newChat, User user, ImageIcon image){
        String chat = newChat;
        Message message = new Message(user,chat,image);
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
    }

    public void updateChat(User user, ImageIcon image){
        Message message = new Message(user,image);
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
    }

    public void updateOnlineJList(User[] onlineUsers) {
        onlineList.removeAll();
        onlineList.setListData(onlineUsers);
    }

//    public void addNewOnlineUser(User user) {
//
//    }
}

