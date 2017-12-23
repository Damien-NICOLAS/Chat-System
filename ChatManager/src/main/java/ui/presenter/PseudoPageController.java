package ui.presenter;

import connectivite.Entete;
import connectivite.MessageSurLeReseau;
import connectivite.ProtocoleDeCommunication;
import main.ChatManager;
import model.UsersDistants;
import ui.viewer.DialoguePageViewer;
import ui.viewer.ErrorViewer;

import javax.swing.*;


public class PseudoPageController {

    private ErrorViewer errorViewer = new ErrorViewer();
    private ChatManager chatManager;

    public PseudoPageController( ChatManager theCM){
        this.chatManager = theCM;
    }

    public void onEnterCheckPseudo(String newPseudo, JButton btn) {
        if (newPseudo.isEmpty()) {
            this.errorViewer.WarningViewer("Les champs pseudo est obligatoire!!!!!");
        } else if (!chatManager.verificationCasse(newPseudo)) {
            this.errorViewer.WarningViewer("Attention : les caractères suivants sont interdits: (!,;:?[]()éàè\"')");
        } else if (!chatManager.verificationUnicitePseudo(newPseudo)) {
            this.errorViewer.WarningViewer("Pseudo non Disponible, Vous pouvez utiliser le meme pseudo en ajoutant des nombres");
        } else {
            btn.setEnabled(true);
        }
    }

    public void onChatOuvertureButtonClicked(String pseudo, JFrame parent) {
        chatManager.addPseudoToUser(pseudo);
        chatManager.accesALaListeDesUsagers().ajouteUnUtilisateurDistantALaListe(chatManager.returnUserLocal().retourneUserLocalAsDistant());


        this.chatManager.getProtocoleDeCommunication().diffusionDuUserLocal();

        DialoguePageViewer dialoguePageViewer = new DialoguePageViewer(this.chatManager);
        parent.dispose();
        dialoguePageViewer.display();
    }
}
