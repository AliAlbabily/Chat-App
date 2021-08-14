package view;

import model.Message;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerLogsGUI
{
    private JTextField timestamp1;
    private JTextField timestamp2;
    private JList<String> log;

    private JButton checkBtn;
    private JButton saveBtn;

    private JFrame frame;
    private JPanel mainPanel;

    private JLabel timeStamplbl1;
    private JLabel timeStamplbl2;
    private Font fontlbl = new Font("Serif", Font.PLAIN, 25);

    private Message[] loggedMessages; // FIXME : for future use

    public ServerLogsGUI()
    {
        initializeComponents();
    }

    public void initializeComponents()
    {
        createFrame();
        createMainPanel();
    }

    public void createFrame()
    {
        frame = new JFrame("Logs");
        frame.setBounds(0, 0, 600, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); // window gets placed on the middle of the screen
    }

    public void createMainPanel()
    {
        Border blackline = BorderFactory.createLineBorder(Color.black);

        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        mainPanel.setBounds(18, 20,545, 570);
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.lightGray);

        //Skapar timestamp lbls
        timeStamplbl1 = new JLabel("From:");
        timeStamplbl1.setBounds(33,10, 100, 50);
        timeStamplbl1.setFont(fontlbl);
        timeStamplbl1.setBackground(Color.black);

        timestamp1 = new JTextField(" ~ Date ~");
        timestamp1.setBounds(33, 60, 120, 25);

        timeStamplbl2 = new JLabel("To:");
        timeStamplbl2.setBounds(213,10, 100, 50);
        timeStamplbl2.setFont(fontlbl);
        timeStamplbl2.setBackground(Color.black);

        timestamp2 = new JTextField(" ~ Date ~");
        timestamp2.setBounds(213, 60, 120, 25);

        checkBtn = new JButton("Check");
        checkBtn.setBounds(392, 60, 120, 25);
        checkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO implementera fuktionalitet här
            }
        });

        saveBtn = new JButton("Save");
        saveBtn.setBounds(225, 510, 100, 50);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO implementera fuktionalitet här
            }
        });

        log = new JList();
        log.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        log.setFont(fontlbl);

        JScrollPane scrollPane = new JScrollPane(log);
        scrollPane.setBounds(33, 100, 480, 400);
        scrollPane.getSize(log.getPreferredScrollableViewportSize());


        mainPanel.add(timeStamplbl1);
        mainPanel.add(timeStamplbl2);
        mainPanel.add(timestamp1);
        mainPanel.add(timestamp2);
        mainPanel.add(checkBtn);
        mainPanel.add(saveBtn);
        mainPanel.add(scrollPane);
        frame.add(mainPanel);
    }

    public void updateLogs(Message[] messagesToBeLogged) {
        loggedMessages = messagesToBeLogged; // save current logged messages in view

        String[] tempArr = new String[messagesToBeLogged.length];

        for ( int i = 0; i < messagesToBeLogged.length; i++ ) {
            // covert array of messages to array of Strings
            tempArr[i] = messagesToBeLogged[i].printMessageInfoWithTime( messagesToBeLogged[i].getTimeReceivedByServer() );
        }

        updateLogsJList(tempArr);
    }

    private void updateLogsJList(String[] messages) {
        log.setListData(messages);
    }

//    public static void main(String[] args) {
//        ServerLogsGUI logs = new ServerLogsGUI();
//    }
}
