package view;

import controller.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ClientGUI extends JFrame implements ActionListener {

    private Controller controller;
    private JPanel panel = new JPanel();
    private JFrame frame = new JFrame();

    String[] images = {"Levi", "Mikasa", "Bakugo", "Robot"};
    JComboBox imageBox = new JComboBox();

    private JLabel profilePicLbl = new JLabel();
    private BufferedImage currImagePath;

    private JButton connectBtn;
    JTextField userText;

    public ClientGUI(Controller controller) {
        this.controller = controller;

        loginBox();
    }

    public void loginBox() {
        frame = new JFrame("Login");
        frame.setSize(230, 230);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setResizable(false);


        JPanel panelTop = new JPanel();
        JPanel panelBottom = new JPanel();
        GridLayout gridLayoutTop = new GridLayout(2, 2);
        GridLayout gridLayoutBottom = new GridLayout(2, 1);
        panelTop.setLayout(gridLayoutTop);
        panelBottom.setLayout(gridLayoutBottom);
        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(panelBottom, BorderLayout.SOUTH);

        JLabel usernameLabel = new JLabel("Username");
        userText = new JTextField();
        userText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
               if (e.getKeyCode() == KeyEvent.VK_ENTER)
               {
                   controller.buttonPressed(ButtonType.Connect);
               }
            }
        });
        JLabel imageText = new JLabel("Profile Picture");
        imageBox = new JComboBox(images);

        connectBtn = new JButton("Connect");
        Border margin = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        connectBtn.setBorder(new CompoundBorder(null, margin));

        imageBox.setSelectedIndex(1);
        imageBox.addActionListener(this);

        connectBtn.addActionListener(this);

        panelTop.add(usernameLabel);
        panelTop.add(userText);
        panelTop.add(imageText);
        panelTop.add(imageBox);


        try {
            currImagePath = ImageIO.read(new File("images/ma-girl.jpg"));
            profilePicLbl = new JLabel(new ImageIcon(currImagePath));
            panelBottom.add(profilePicLbl);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        panelBottom.add(connectBtn);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == imageBox) {
            JComboBox cb = (JComboBox) e.getSource();
            String pic = String.valueOf(cb.getSelectedItem());
            updateImage(pic);
        }
        if (e.getSource() == connectBtn) {
            controller.buttonPressed(ButtonType.Connect);
        }
    }

    public String getUsername() {
        return userText.getText();
    }

    public ImageIcon getImageIcon() {
        return (ImageIcon) profilePicLbl.getIcon();
    }

    private void updateImage(String pic) {
        switch (pic) {
            case "Levi":
                displayImageByFilePath("images/goat.jpg");
                break;
            case "Mikasa":
                displayImageByFilePath("images/ma-girl.jpg");
                break;
            case "Bakugo":
                displayImageByFilePath("images/tuff-guy.png");
                break;
            case "Robot":
                displayImageByFilePath("images/robot.png");
                break;
        }
    }

    private void displayImageByFilePath(String filePath) {
        try {
            currImagePath = ImageIO.read(new File(filePath));
            if (profilePicLbl != null) {
                profilePicLbl.setIcon(new ImageIcon(currImagePath));
                profilePicLbl.repaint();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void closeClientConnectionWindow() {
        frame.setVisible(false); // hide window
        frame.dispose(); // Destroy the JFrame object, (close window)
    }
}




