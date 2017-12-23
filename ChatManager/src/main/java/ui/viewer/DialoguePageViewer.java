package ui.viewer;


import main.ChatManager;
import ui.presenter.DialoguePageController;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.sql.Time;
import java.util.Locale;

import static javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION;

public class DialoguePageViewer extends JFrame{
    private DialoguePageController presenter;
    private JTextArea textArea;
    private JTextField inputTextField;
    private JButton sendButton;
    private JTree listeUsersConnectes, listeSessions;
    private ChatManager chatManager;

    public DialoguePageViewer(ChatManager theCM)throws HeadlessException {
        this.chatManager = theCM;
        presenter = new DialoguePageController(theCM);
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


        DefaultMutableTreeNode root = presenter.actualiserMenuUsersCo();

        listeUsersConnectes = new JTree(root);
        listeUsersConnectes.setScrollsOnExpand(true);
        listeUsersConnectes.updateUI();
        listeUsersConnectes.addTreeSelectionListener(e -> presenter.onClickSelectionUserCo(listeUsersConnectes.getSelectionPath()));

        DefaultMutableTreeNode rootSession = presenter.actualiserMenuSessions();
        listeSessions = new JTree(rootSession);
        listeSessions.setScrollsOnExpand(true);

        listeSessions.updateUI();
        //listeSessions.addTreeSelectionListener(e -> presenter.onClickSelection(listeSessions.getName(),rootSession,listeSessions));


        JPanel listePane = new JPanel();
        listePane.setBackground(Color.WHITE);
        listePane.add(listeUsersConnectes);

        JButton ActualiseUsersCoButton = new JButton("Actualiser");
        ActualiseUsersCoButton.addActionListener(e -> presenter.actualiserMenuUsersCo());



        Box userCoBox = Box.createVerticalBox();
        userCoBox.add(listePane);
        userCoBox.add(ActualiseUsersCoButton);


        JPanel sessionsPane = new JPanel();
        sessionsPane.setBackground(Color.WHITE);
        sessionsPane.add(listeSessions);

        JButton stopSessionButton = new JButton("Déconnexion");
        stopSessionButton.addActionListener(e -> presenter.onClickStopButtonSession(listeSessions, listeSessions.getName()));
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

    public void ajoutTextAreaByUserLoc(String text) {
        textArea.append(chatManager.userLogin()+" : ");
        textArea.append(text);
        textArea.append("\n");
    }

    public void ajoutTextAreaByUserDistant(String text, String loginDistant) {
        textArea.append(loginDistant +" : ");
        textArea.append(text);
        textArea.append("\n");
    }

}