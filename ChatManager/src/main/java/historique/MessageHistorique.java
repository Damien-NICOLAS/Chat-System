package historique;


import main.ChatManager;
import ui.viewer.DialoguePageViewer;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class MessageHistorique {
    private static final int TAILLE_LISTE = 10;
    private final File repertoire;
    private static HashMap<String, File> listeFichiers;
    private static final String PATH = "src/main/HistoriqueDossier/";
    private final ChatManager chatManager;
    private File fichierSessionHistorique;


    public MessageHistorique(ChatManager theCM) {
        repertoire = new File(PATH);
        chatManager = theCM;
        listeFichiers = new HashMap<String, File>();


    }

    public void setFile(){
        fichierSessionHistorique = new File(toString());
        System.out.println(fichierSessionHistorique.getName());
    }

    public String toString() {
        File file = new File("ChatManager/src/main/HistoriqueDossier/path.txt");
        String path = file.getAbsolutePath();
        String pathMoinsText = path.substring(0, path.length()-8);
        System.out.println(pathMoinsText+chatManager.userLogin()+"_"+chatManager.useSessions().userDistantSessionCourante().getLogin()+".txt");
        return pathMoinsText+chatManager.userLogin()+"_"+chatManager.useSessions().userDistantSessionCourante().getLogin()+".txt";
    }

    public void creerFichier() throws IOException, NotFileException {
        fichierSessionHistorique = new File(toString());
        FileWriter fw = new FileWriter(toString());


    }


    public void ecriturefichier(MomentEcriture moment, String message) throws IOException {
        String message_prepare = null;
        FileWriter write = new FileWriter(toString(), true);
        switch (moment){

            case MESSAGE_ENVOYE:
                message_prepare =  chatManager.userPseudo() + "-"+ chatManager.useSessions().userDistantSessionCourante().getPseudoActuel() + ":" + LocalDateTime.now() + ":" + message ;
                break;

            case MESSAGE_RECU:
                message_prepare =  chatManager.useSessions().userDistantSessionCourante().getPseudoActuel() + "-"+ chatManager.userPseudo() + ":" + LocalDateTime.now() + ":" + message ;
                break;
            default:
                throw new IOException("Ecriture failed");


        }

        write.write(message_prepare);
        write.append("\n");
        write.close();
    }

    private boolean checkNomFichier(String nomfichier) {
        String filtre = "[a-zA-Z0-9]+_[a-zA-Z0-9]+\\.[a-zA-Z]{3}";

        Pattern p = Pattern.compile(filtre);

        Matcher matcher = p.matcher(nomfichier);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public void listeFichiersDossier() throws NotDirectoryException, CasseNomFichierException {

        File[] files = repertoire.listFiles();

        for (int i = 0; i < (files != null ? files.length : 0); i++) {
            if (!checkNomFichier(files[i].getName())) {
                throw new CasseNomFichierException("Modifier le nom du fichier!!!!");
            }
            System.out.println("Ajout fichier"+ files[i].toString());
            listeFichiers.put(files[i].getName(), files[i]);
        }
    }

    public void affichageListeFichierDuDossier(){
        for (Map.Entry<String, File> e : listeFichiers.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }

    public File findfichier(){

        String nomfichier = toString();
        return listeFichiers.get(nomfichier);
    }


    private Queue<String> historique10DerniersMessages(String nomfichier) throws IOException, NotFileException {
        File fichier = fichierSessionHistorique;
        Queue<String> fifo = new LinkedBlockingQueue<>(TAILLE_LISTE);
        if(fichier.exists()) {
            InputStream file = new FileInputStream(nomfichier);
            InputStreamReader fich = new InputStreamReader(file);
            BufferedReader reader = new BufferedReader(fich);
            String line;
            int i=0;
            while (( line = reader.readLine() ) != null) {
                System.out.println("fifo size = "+fifo.size());
                if (fifo.size() == TAILLE_LISTE) {
                    fifo.remove();
                    System.out.println("je supprime et je relance" + i);
                }
                fifo.add(line);
            }
            reader.close();
        }else{
            creerFichier();
        }
        return fifo;
    }


    public void lireFichier(String nomfichier, String loginUserDist) throws IOException, NotFileException {
        Queue<String> lignes = historique10DerniersMessages(nomfichier);
        for (String ligneCourante : lignes){
            System.out.println("lirefichier, messageHistorique : "+ligneCourante);
            DialoguePageViewer dialoguePageViewer = new DialoguePageViewer(chatManager);
            dialoguePageViewer.ajoutTextAreaByUserDistant(ligneCourante, loginUserDist);

        }

    }
}