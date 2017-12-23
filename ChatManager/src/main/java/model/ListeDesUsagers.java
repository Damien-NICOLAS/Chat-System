package model;

import main.ChatManager;

import java.util.*;

public class ListeDesUsagers {

    private ChatManager clavardageManager;

    private static HashMap<String, UsersDistants> listeDesUsersParLeurLogin = new HashMap<String, UsersDistants>();

    public ListeDesUsagers(ChatManager theManager){
        this.clavardageManager = theManager;
        listeDesUsersParLeurLogin.put(theManager.userLogin(), theManager.returnUserLocal().retourneUserLocalAsDistant());


    }

    public void ajouteUnUtilisateurDistantALaListe(UsersDistants nouveauUserDistant){
        String leLoginUserDistant = nouveauUserDistant.getLogin();
        if(listeDesUsersParLeurLogin.containsKey(leLoginUserDistant)){
            listeDesUsersParLeurLogin.replace(leLoginUserDistant, nouveauUserDistant);
        }
        else{
            listeDesUsersParLeurLogin.put(leLoginUserDistant, nouveauUserDistant);
        }

        System.out.println("j'ai ajouté un usager : "+ leLoginUserDistant + " " + retourneUnUtilisateurDistantParSonLogin(leLoginUserDistant).getAdresseIP());
    }

    public UsersDistants retourneUnUtilisateurDistantParSonLogin(String login){
        UsersDistants leUser = listeDesUsersParLeurLogin.get(login);
        return leUser;
    }

    public Set<String> retourneToutLesUsagers() {
        Set<String> toutLesKeyDeLaTableDesUsers = listeDesUsersParLeurLogin.keySet();
        return toutLesKeyDeLaTableDesUsers;


    }

    public String retourneToutLesUsagersAsString(){
        String toutLesUsagersConnusAsString = "";
        Set<String> toutlesKeyDeLaTableDesUsers = retourneToutLesUsagers();
        for (String st: toutlesKeyDeLaTableDesUsers) {
            toutLesUsagersConnusAsString = toutLesUsagersConnusAsString.concat(retourneUnUtilisateurDistantParSonLogin(st).toString());
            toutLesUsagersConnusAsString = toutLesUsagersConnusAsString.concat("/");

        }
        toutLesUsagersConnusAsString = toutLesUsagersConnusAsString.substring(0, toutLesUsagersConnusAsString.length()-1);
        return  toutLesUsagersConnusAsString;
    }


    public void ajouteToutLesUsagers(String toutLesUsagersAsString) {

        String[] usersAsString = toutLesUsagersAsString.split("[/]");
        for (String users : usersAsString) {
            String[] elementsDuUsers = users.split("[,]");
            UsersDistants nouveauUser = new UsersDistants(elementsDuUsers[0], elementsDuUsers[1], elementsDuUsers[2], Integer.parseInt(elementsDuUsers[3]));

            this.clavardageManager.accesALaListeDesUsagers().ajouteUnUtilisateurDistantALaListe(nouveauUser);

        }
    }

    public UsersDistants retourneUtilisateurDistantsParSonPseudo(String lePseudo){
        for (Map.Entry<String, UsersDistants> courant : listeDesUsersParLeurLogin.entrySet()){
            System.out.println("types "+lePseudo.compareTo(courant.getValue().getPseudoActuel()));
            if(lePseudo.equals(courant.getValue().getPseudoActuel())){
                return courant.getValue();
            }
        }
        return null;
    }

    public void enleveUnUserDistant(String leLoginDuUserDistantQuiSeDeconnecte) {
        listeDesUsersParLeurLogin.remove(leLoginDuUserDistantQuiSeDeconnecte);
    }

    void run(){

    }
}
