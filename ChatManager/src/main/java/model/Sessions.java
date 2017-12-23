package model;

import historique.MessageHistorique;
import main.ChatManager;


import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.HashMap;
import java.util.Map;

public class Sessions {

    private final ChatManager chatManager;
    private UsersDistants sessionEnCoursDeChat;
    private HashMap<UsersDistants, MessageHistorique> listeSessions;

    public Sessions(ChatManager theChatManager){
        chatManager = theChatManager;
        listeSessions = new HashMap<>();
    }

    public void addUserDistantToSession(UsersDistants usersDistants) {
        MessageHistorique messageHistorique = new MessageHistorique(chatManager);
        if (!listeSessions.containsKey(usersDistants)){
            listeSessions.put(usersDistants, messageHistorique);
        }
    }

    public void afficheListeSessions() {
        for (Map.Entry<UsersDistants, MessageHistorique> courant : listeSessions.entrySet()){
            System.out.println(courant.getKey().toString());
            System.out.println(courant.getValue().toString());
        }
    }

    public HashMap<UsersDistants, MessageHistorique> retourneListeSessions(){
        return listeSessions;
    }

    public void removeUserDistantOfSession(UsersDistants userARemove) {
        listeSessions.remove(userARemove);
    }

    public void definieUserDistantCourant(UsersDistants utilisateurDistantEnChat) {
        System.out.println("Sessions : definieUserDistantCourant "+ utilisateurDistantEnChat.toString());
        sessionEnCoursDeChat = utilisateurDistantEnChat;
        System.out.println("Sessions : apres modif "+sessionEnCoursDeChat.toString());
    }

    public UsersDistants userDistantSessionCourante() {
        return sessionEnCoursDeChat;
    }

    public MessageHistorique retourneLHistoriqueDesMessagesDuUserRecherche(String leLoginDuUserDistant){

        UsersDistants userDistantRecherche = chatManager.accesALaListeDesUsagers().retourneUnUtilisateurDistantParSonLogin(leLoginDuUserDistant);

        MessageHistorique messageHistorique  = listeSessions.get(userDistantRecherche);

        return messageHistorique;


    }

    public MessageHistorique retourneHistoriqueEnFonctionDuUserDistantCourant(){
        UsersDistants userDistantCourant = chatManager.useSessionCourante();

        MessageHistorique historiqueCourant = listeSessions.get(userDistantCourant);

        return historiqueCourant;
    }


}