package main;

import ui.viewer.LoginPageViewer;
import connectivite.*;

import javax.swing.*;

public class  Lancer {

    public static void main(String[] args){

        ChatManager chatManager = new ChatManager();
        JFrame frame = new JFrame();

        LoginPageViewer loginDialog = new LoginPageViewer(frame, chatManager);
        loginDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginDialog.setVisible(true);

    }
}
