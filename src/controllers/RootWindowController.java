package controllers;

import data_managers.StubDataManager;
import data_managers.XMLDataManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import launchers.Graphique;
import utils.ClassesManager;
import model.DataManager;
import model.Fabrique;
import model.IRecette;

/**
 * FXML Controller class
 *
 * @author Clément
 */
public class RootWindowController implements Initializable {

    /**
     * Classe Graphique main, la classe qui lance l'application graphique
     */
    Graphique main;
    
    /**
     * Menu qui contiendra les modes de persistance de type DataManager
     */
    @FXML
    Menu dataManagerMenu;
    /**
     * Le groupe à "bascule" pour les modes de persistance
     */
    private ToggleGroup dataManagerGroup;
    /**
     * La liste des classes pour la persistance de type DataManager
     */
    private List<Class> dataManagerModes;
    
    /**
     * Menu qui contiendra le champs de texte pour l'url du fichier
     */
    @FXML
    Menu fileMenu;
    /**
     * TextField contenant le chemin du fichier
     */
    @FXML
    TextField fileTextField;
    /**
     * Label
     */
    /*@FXML
    Label rechercherLabel;*/
    
    /**
     * Url du fichier de sauvegarde
     */
    private /*SimpleObjectProperty<File>*/ File currentFile;
    
    /**
     * Initialise le contrôleur de la classe.
     * @param url l'emplacement pour les chemins de l'objet racine
     * @param rb les ressources utilisées pour localiser l'objet racine
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataManagerGroup = new ToggleGroup();
        dataManagerGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(dataManagerGroup.getSelectedToggle() != null){
                try {
                    Class clazz = Class.forName(dataManagerGroup.getSelectedToggle().getUserData().toString());
                    main.getLivre().setDataManager((DataManager)clazz.newInstance());
                    // Modifie le fichier courant du menu
                        if(currentFile != null)
                            changerCurrentFile(currentFile);
                        else
                            changerCurrentFile(main.getLivre().getDataManager().getFile());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(RootWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        // Mise à jour du nom de fichier depuis le text field
        fileTextField.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                currentFile = new File(fileTextField.getText());
            }
        });
    }
    
    /**
     * La méthode est appelé par l'application pour donné une référence vers lui-même
     * @param main la classe mère du GUI
     */
    public void setMain(Graphique main) {
        this.main = main;
        creerMenuModeDataManager();
    }    
    
    /**
     * Méthode qui permet de créer le menu contenant les différents modes de persistances disponible
     */
    public void creerMenuModeDataManager(){
        try {
            dataManagerModes = new ArrayList<>(Arrays.asList(StubDataManager.class,XMLDataManager.class));//ClassesManager.getClasses("data_managers");
            /*
             * Ma boucle for pour parcourir les classes.
            for(Class classe : dataManagerModes){
                RadioMenuItem item = new RadioMenuItem(classe.getSimpleName().replace("DataManager", ""));
                item.setUserData(classe.getName());
                item.setToggleGroup(dataManagerGroup);
                if(main.getLivre().getDataManager() != null && classe.equals(main.getLivre().getDataManager().getClass()))
                    item.setSelected(true);
                dataManagerMenu.getItems().add(item);
            }
             */
            // "functional operation" créée par Netbeans équivalent à ma boucle for
            dataManagerModes.stream().map((classe) -> { 
                RadioMenuItem item = new RadioMenuItem(classe.getSimpleName().replace("DataManager", ""));
                item.setUserData(classe.getName());
                item.setToggleGroup(dataManagerGroup);
                if(main.getLivre().getDataManager() != null && classe.equals(main.getLivre().getDataManager().getClass()))
                    item.setSelected(true);
                return item;
            }).forEachOrdered((item) -> {
                dataManagerMenu.getItems().add(item);
            }); 
            
        } catch (Exception ex){//ClassNotFoundException | IOException ex) {
            Logger.getLogger(RootWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Méthode action de sauvegarder
     * <br>Sauvegarde les recettes depuis le DataManager
     */
    @FXML
    public void sauvegarder(){
        try {
            if(main.dialogSauvegarde("Sauvegarde","La précédentes sauvegarde va être écraser !","Ëtes-vous sûr de vouloir valider la sauvegarde ?"))
                main.getLivre().sauvegarderRecettes();
        } catch (Exception ex) {
            main.popup("Erreur opération","Problème durant la sauvegarde",ex.getMessage()+"\nVeuillez vérifier le fichier choisi dans l'onglet 'Sauvegarde' puis 'Fichier'"+"\nVeuillez selectionner un mode de persistance dans l'onglet 'Sauvegarde' puis 'Changer mode'",true);
        }
    }
    
    /**
     * Méthode action de charger
     * <br>Charge les recettes depuis le DataManager
     */
    @FXML
    public void charger(){
        try{
            if(main.dialogSauvegarde("Chargement","Les modifications effectuées sur les recettes, non sauvegarder, vont être écrasées !","Ëtes-vous sûr de vouloir valider le chargement ?"))
                main.getLivre().chargerRecettes();
        } catch (Exception ex) {
            main.popup("Erreur opération","Problème durant le chargement",ex.getMessage()+"\nVeuillez vérifier le fichier choisi dans l'onglet 'Sauvegarde' puis 'Fichier'"+"\nVeuillez selectionner un mode de persistance valide dans l'onglet 'Sauvegarde' puis 'Changer mode'",true);
        }
    }
    
    /**
     * Méthode action ouvrir.
     * <br>Ouvre la fenêtre de sélection de fichier pour sélectionner un fichier dans l'arborescence.
     */
    @FXML
    public void ouvrir(){
        FileChooser fc = new FileChooser();
        fc.setTitle("Fichier de sauvegarde");
        if(currentFile != null && currentFile.getParentFile().isDirectory())
            fc.setInitialDirectory(currentFile.getParentFile()); //currentFile.getValue().getParentFile()
        changerCurrentFile(fc.showOpenDialog(main.getStage()));
        System.out.println(currentFile);
    }
    
    /**
     * Métohde permettant de changer le fichier de sauvegarde courant. 
     * @param file le nouveau fichier courant.
     */
    public void changerCurrentFile(File file){
        if(file != null){
            if(main.getLivre().getDataManager() != null)
                main.getLivre().getDataManager().setFile(file.getAbsolutePath());
            if(file.exists()){
                currentFile = file;
                fileTextField.setText(currentFile.getAbsolutePath());
            }
        }
    }
    
    /**
     * Méthode action rechercher.
     * <br>Permet de lancer la fenêtre de selection des recettes à rechercher.
     */
    @FXML
    public void rechercher(){
        IRecette recette = main.lancerRechercheFormulaire();
        if(recette != null)
            main.getLivre().rechercherRecettes(recette);
    }
    
    /**
     * Méthode action réinitialiser.
     * <br>Permet de reinitialiser la liste des recettes
     */
    @FXML
    public void reset(){
        main.getLivre().restaurerRecettes();
    }
}
