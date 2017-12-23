package ui.presenter;

import historique.MessageHistorique;
import historique.MomentEcriture;
import historique.NotFileException;
import main.ChatManager;
import model.ListeDesUsagers;
import model.Sessions;
import model.UsersDistants;
import ui.viewer.DialoguePageViewer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DialoguePageController {

    private final ChatManager chatManager;


    public DialoguePageController(ChatManager theCM){
        chatManager = theCM;
    }
    public void onSendButton(String text, DialoguePageViewer view) {
        view.ajoutTextAreaByUserLoc(text);
        chatManager.getProtocoleDeCommunication().envoieMessage(chatManager.useSessionCourante().getLogin(), text );
        MessageHistorique messageHistorique = chatManager.useSessions().retourneHistoriqueEnFonctionDuUserDistantCourant();
        try {
            messageHistorique.ecriturefichier(MomentEcriture.MESSAGE_ENVOYE, text);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void onEnterTouch(String text, DialoguePageViewer view) {
        view.ajoutTextAreaByUserLoc(text);
        chatManager.getProtocoleDeCommunication().envoieMessage(chatManager.useSessionCourante().getLogin(), text );
        MessageHistorique messageHistorique = chatManager.useSessions().retourneHistoriqueEnFonctionDuUserDistantCourant();
        try {
            messageHistorique.ecriturefichier(MomentEcriture.MESSAGE_ENVOYE, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void onClickSelectionUserCo(TreePath selection, JTree listeSessions, JPanel panel, DialoguePageViewer view) throws IOException, NotFileException {
        String chemin = selection.toString();
        String pseudo = null;
        System.out.println(chemin);
        if(chemin.contains(",")) {
            String[] result = chemin.split(",");
            System.out.println(result[1]);
            String[] result1 = result[1].split("]");
            System.out.println(result1[0]);
            String[] result2 = result1[0].split(" ");
            System.out.println(result2[1]);
            pseudo = result2[1];
        }else{
            String[] result = chemin.split("]");
            System.out.println(result[0]);
            String[] result1 = result[0].split("\\[");
            System.out.println(result1[0] + result1[1]);
            pseudo = result1[1];

        }
        System.out.println("onClickSelectionUserCo :"+pseudo);
        if (!(pseudo == null) && !(pseudo.isEmpty()) && !(pseudo.equals("Utilisateurs Connectés")) && !(pseudo.equals(chatManager.userPseudo()))) {
            System.out.println("onClickSelectionUserCo :"+pseudo);
            System.out.println(chatManager.accesALaListeDesUsagers().retourneUtilisateurDistantsParSonPseudo(pseudo));
            UsersDistants newUserDistant = chatManager.accesALaListeDesUsagers().retourneUtilisateurDistantsParSonPseudo(pseudo);
            if(newUserDistant != null) {
                chatManager.useSessions().addUserDistantToSession(newUserDistant);
                chatManager.defenieSessionCourante(newUserDistant);
                System.out.println("Après ajout : ");
                chatManager.useSessions().afficheListeSessions();
                actualiserMenuSession(panel,listeSessions, view);
                MessageHistorique messageHistorique = new MessageHistorique(chatManager);
                messageHistorique.setFile();
                messageHistorique.creerFichier();
                messageHistorique.lireFichier(chatManager.useSessions().retourneFileUserCourant(),chatManager.useSessionCourante().getLogin());
                chatManager.getProtocoleDeCommunication().envoieDeDemandeDeSession(newUserDistant.getLogin());
                System.out.println("onClickSectionUserCo fin :" + chatManager.useSessionCourante().toString());
            }
        }
    }

    public void onClickSelectionSession(String pseudoUtilisateurDistantEnChat, JPanel panel, JTree listeSession, DialoguePageViewer view){
        UsersDistants newUserDistant = chatManager.accesALaListeDesUsagers().retourneUtilisateurDistantsParSonPseudo(pseudoUtilisateurDistantEnChat);
        view.setModifierEtatButtonDeconnexion();
        view.setModifierTitle(newUserDistant.getLogin());
        if(newUserDistant !=null) {
            chatManager.defenieSessionCourante(newUserDistant);
            actualiserMenuSession(panel,listeSession, view);
        }



    }

    public JTree creeMenuUsersCo(JTree listeUsersConnectes,JTree listeSessions, JPanel sessionsPane, DialoguePageViewer view) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Utilisateurs Connectés");

        if (!(this.chatManager.accesALaListeDesUsagers().retourneToutLesUsagers().isEmpty())) {
            for (String usersCo : this.chatManager.accesALaListeDesUsagers().retourneToutLesUsagers()) {

                if(!(usersCo == null) && !usersCo.equals(chatManager.userLogin())) {
                    String pseudoUsersCo = this.chatManager.accesALaListeDesUsagers().retourneUnUtilisateurDistantParSonLogin(usersCo).getPseudoActuel();

                    System.out.println("Pseudo co : " + pseudoUsersCo);

                    if(pseudoUsersCo != null) {
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(pseudoUsersCo);
                        root.add(child);
                    }
                }
            }
        }
        JTree nouveauJtree= new JTree(root);
        nouveauJtree.addTreeSelectionListener(e -> {
            try {
                onClickSelectionUserCo(nouveauJtree.getSelectionPath(), listeSessions, sessionsPane,  view);
            } catch ( IOException e1 ) {
                e1.printStackTrace();
            } catch ( NotFileException e1 ) {
                e1.printStackTrace();
            }
        });
        nouveauJtree.setScrollsOnExpand(true);
        return nouveauJtree;
    }

    public JTree creerMenuSessions(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Sessions Actives");
        if(!(chatManager.useSessions().retourneListeSessions().isEmpty())){
            for(Map.Entry<UsersDistants, MessageHistorique> entry : chatManager.useSessions().retourneListeSessions().entrySet()) {
                String pseudoUsersSession = entry.getKey().getPseudoActuel();
                System.out.println("Session Activée : " + pseudoUsersSession);
                if (!(pseudoUsersSession.equals(chatManager.userPseudo()))) {
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(pseudoUsersSession);
                    root.add(child);
                }
            }
        }
        return new JTree(root);
    }

    public void onClickStopButtonSession(JTree listeSessions, String pseudo, JPanel panel, DialoguePageViewer view) {
        UsersDistants userSelectionne = chatManager.accesALaListeDesUsagers().retourneUtilisateurDistantsParSonPseudo(pseudo);
        chatManager.getProtocoleDeCommunication().envoieDeFinDeSession(userSelectionne.getLogin());
        chatManager.useSessions().removeUserDistantOfSession(userSelectionne);
        actualiserMenuSession(panel, listeSessions, view);
        listeSessions.updateUI();

    }

    public void actualiserMenuUsersCo(JPanel panel, JTree listeUsersCo, DialoguePageViewer view, JPanel sessionsPane, JTree listeSessions) {
        JTree nouveauJtree = creeMenuUsersCo(listeUsersCo,listeSessions, sessionsPane, view);
        panel.remove(listeUsersCo);

        view.setJtreeUsersCo(nouveauJtree);

        panel.add(nouveauJtree);
        panel.updateUI();
    }

    private void actualiserMenuSession(JPanel panel, JTree listeSession, DialoguePageViewer view) {
        JTree nouveauJtree = creerMenuSessions();
        panel.remove(listeSession);

        view.setJtreeSessions(nouveauJtree);
        panel.add(nouveauJtree);
        panel.updateUI();
    }
}




