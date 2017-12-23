package ui.viewer;


import historique.NotFileException;
import main.ChatManager;
import ui.presenter.DialoguePageController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DialoguePageViewer extends JFrame{
    private DialoguePageController presenter;
    private JTextArea textArea;
    private JTextField inputTextField;
    private JButton sendButton;
    private JTree listeUsersConnectes, listeSessions;
    private ChatManager chatManager;
    private JPanel sessionsPane;

    public DialoguePageViewer(ChatManager theCM)throws HeadlessException {
        this.chatManager = theCM;
        presenter = new DialoguePageController(theCM);
        chatManager.setPageViewer(this);
        build(chatManager.userPseudo());


    }
    private void build(String pseudo){
        setTitle("Chat System : Bonjour " + pseudo);
        setSize(300,150);
        setBackground(Color.WHITE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel centerPanel = new JPanel();
        getContentPane().setLayout(new BorderLayout(1, 1));
        centerPanel.setBackground(Color.WHITE);


        textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        textArea.setLineWrap(true);

        JScrollPane textPane = new JScrollPane(textArea);
        textPane.setBackground(Color.WHITE);
        textPane.createHorizontalScrollBar();
        textPane.createVerticalScrollBar();

        Box box = Box.createHorizontalBox();
        centerPanel.add(box, BorderLayout.SOUTH);
        inputTextField = new JTextField();
        inputTextField.setHorizontalAlignment(JTextField.LEFT);
        inputTextField.addActionListener(e -> presenter.onEnterTouch(inputTextField.getText(), this));
        sendButton = new JButton("Envoyer");
        sendButton.addActionListener(e -> presenter.onSendButton(inputTextField.getText(), this));
        box.add(inputTextField);
        box.add(sendButton);

        Box chatBox = Box.createVerticalBox();
        centerPanel.add(chatBox, BorderLayout.CENTER);
        chatBox.add(textPane);
        chatBox.add(box);


        listeUsersConnectes = presenter.creeMenuUsersCo(listeUsersConnectes, listeSessions, sessionsPane, this);
        listeUsersConnectes.setScrollsOnExpand(true);
        listeUsersConnectes.updateUI();
        listeUsersConnectes.addTreeSelectionListener(e -> {
            try {
                presenter.onClickSelectionUserCo(listeUsersConnectes.getSelectionPath(), listeSessions, sessionsPane,  this);
            } catch ( IOException e1 ) {
                e1.printStackTrace();
            } catch ( NotFileException e1 ) {
                e1.printStackTrace();
            }
        });

        listeSessions = presenter.creerMenuSessions();
        listeSessions.setScrollsOnExpand(true);

        listeSessions.updateUI();
        //listeSessions.addTreeSelectionListener(e -> presenter.onClickSelection(listeSessions.getName(),rootSession,listeSessions));


        JPanel listePane = new JPanel();
        listePane.setBackground(Color.WHITE);
        listePane.add(listeUsersConnectes);

        JButton actualiseUsersCoButton = new JButton("Actualiser");




        Box userCoBox = Box.createVerticalBox();
        userCoBox.add(listePane);
        userCoBox.add(actualiseUsersCoButton);
        actualiseUsersCoButton.addActionListener(e -> presenter.actualiserMenuUsersCo(listePane, listeUsersConnectes, this, sessionsPane, listeSessions));

        sessionsPane = new JPanel();
        sessionsPane.setBackground(Color.WHITE);
        sessionsPane.add(listeSessions);

        JButton stopSessionButton = new JButton("Déconnexion");
        stopSessionButton.addActionListener(e -> presenter.onClickStopButtonSession(listeSessions, listeSessions.getName(), sessionsPane, this));
        stopSessionButton.setEnabled(false);

        Box sessionsBox = Box.createVerticalBox();
        listePane.setBackground(Color.WHITE);
        sessionsBox.add(sessionsPane);
        sessionsBox.add(stopSessionButton);



        add(centerPanel, BorderLayout.EAST);
        add(sessionsBox, BorderLayout.CENTER);
        add(userCoBox, BorderLayout.WEST);



    }

    public void display() {
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }


    public void setJtreeUsersCo (JTree nouveau){
        listeUsersConnectes = nouveau;
    }

    public void setJtreeSessions (JTree nouveau){
        listeSessions = nouveau;
    }

    public void ajoutTextAreaByUserLoc(String text) {
        textArea.append(chatManager.userLogin()+" : ");
        textArea.append(text);
        textArea.append("\n");
    }

    public void ajoutTextAreaByUserDistant(String text, String loginDistant) {
        textArea.append(loginDistant +" : " + text + "\n");

    }

}