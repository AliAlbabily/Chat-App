package server.view;

import server.controller.ServerController;
import sharedResources.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ServerLogsGUI {

    private JTextField timestamp1 = new JTextField("yyyy-MM-dd HH:mm");
    private JTextField timestamp2 =  new JTextField("yyyy-MM-dd HH:mm");
    private JList<String> log;
    private JButton checkBtn = new JButton("Check");;
    private JButton saveBtn = new JButton("Save");;
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel  timeStamplbl1 = new JLabel("From:");
    private JLabel timeStamplbl2 = new JLabel("To:");

    private ServerController serverController;

    public ServerLogsGUI(ServerController serverController) {
        this.serverController = serverController;
        initializeComponents();
    }

    public void initializeComponents() {
        createFrame();
        createMainPanel();
    }

    public void createFrame() {
        frame = new JFrame("Logs");
        frame.setBounds(0, 0, 600, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); // window gets placed on the middle of the screen
    }

    public void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        mainPanel.setBounds(18, 20,545, 570);
        mainPanel.setLayout(null);

        timeStamplbl1.setBounds(33,10, 100, 50);
        timeStamplbl1.setFont(new Font("", Font.PLAIN, 25));
        timeStamplbl1.setBackground(Color.black);
        timestamp1.setBounds(33, 60, 120, 25);

        timeStamplbl2.setBounds(213,10, 100, 50);
        timeStamplbl2.setFont(new Font("", Font.PLAIN, 25));
        timeStamplbl2.setBackground(Color.black);
        timestamp2.setBounds(213, 60, 120, 25);

        checkBtn.setBounds(392, 60, 120, 25);
        checkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterByTime();
            }
        });

        saveBtn.setBounds(225, 510, 100, 50);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChatlogsToTxt();
            }
        });

        log = new JList();
        log.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        log.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));

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

    private void filterByTime() {
        String dateStart = timestamp1.getText(); // gets the written text from the text-field
        String dateEnd = timestamp2.getText(); // gets the written text from the text-field
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTimeOne = null;
        LocalDateTime dateTimeTwo = null;

        try {
             dateTimeOne = LocalDateTime.parse(dateStart,formatter);
             dateTimeTwo = LocalDateTime.parse(dateEnd,formatter);

        } catch (Exception e){
            System.err.println(e);
        }

        ArrayList<String> tempArrayList = new ArrayList<>();
        Message[] tempLoggedMessages = serverController.getLoggedMessages().toArray(new Message[0]); // get and convert arraylist to array

        try {
            for ( Message message : tempLoggedMessages ) {
                if(message.getTimeReceivedByServer().isBefore(dateTimeTwo) && message.getTimeReceivedByServer().isAfter(dateTimeOne)) {
                    tempArrayList.add(message.printMessageInfoWithTime(message.getTimeReceivedByServer()));
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        String [] messageList = tempArrayList.toArray(new String[0]);
        updateLogsJList(messageList);
    }

    private void saveChatlogsToTxt() {
        ListModel listModel = log.getModel();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("files/chatlogs.txt"))) {
            for ( int i = 0; i < listModel.getSize(); i++ ) {
                String log = (String) listModel.getElementAt(i) + "\n";
                bw.write(log);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
