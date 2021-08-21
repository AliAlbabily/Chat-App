package client.model;

import sharedResources.User;

import java.io.*;
import java.util.ArrayList;

public class Contacts {

    private ArrayList<User> contacts = new ArrayList<>();
    private final static String contactsFile = "files/contacts.dat";

    public void saveNewContactInFile(User user) {
        ArrayList<User> contactsTemp = null;

        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(contactsFile)))) {
            contactsTemp = (ArrayList<User>) in.readObject();
        } catch (Exception e) {}

        if (contactsTemp == null) { // create a new array if there is no data in the file
            contactsTemp = new ArrayList<>();
        }

        contactsTemp.add(user); // add the new value to the list before it gets added to the file

        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(contactsFile)))) {
            out.writeObject(contactsTemp);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> fetchContactsFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(contactsFile)))) {
            contacts = (ArrayList<User>) in.readObject();
        }
        catch (FileNotFoundException e) {
//            e.printStackTrace();
            System.err.println(e);
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println(e);
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
            System.err.println(e);
        }

        return contacts;
    }

    public void removeContactInFile(User user) {
        ArrayList<User> contactsTemp = fetchContactsFromFile();

        contactsTemp.remove(user); // removes the value from the list

        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(contactsFile)))) {
            out.writeObject(contactsTemp);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
