/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 * Classe permettant d'instancier des objects inacessible en dehors du paquet model
 * @author Clément
 */
public class Fabrique {
    
    /**
     * Appelle le constructeur par défaut d'une recette
     * @return une recette depuis son interface IRecette
     */
    public static IRecette creerRecette(){
        return new Recette();
    }
    
    /**
     * Appelle le constructeur prenant 2 attributs
     * @param nom le nom de la recette
     * @param recette la recette à suivre pour la réaliser
     * @return une recette
     */
    public static IRecette creerRecette(String nom, String recette){
        return new Recette(nom, recette);
    }
    
    /**
     * Appelle le constructeur prenant toutes les valeurs nécessaire permettant de créer une recette
     * @param nom son nom
     * @param recette la recette
     * @param minutes la durée de réalisation
     * @param difficulte la difficulté de réalisation
     * @param prix le prix
     * @return une recette
     */
    public static IRecette creerRecette(String nom, String recette, int minutes, Difficulte difficulte, Budget prix){
        return new Recette(nom,recette, minutes, difficulte, prix);
    }
    
    /**
     * Appelle le constructeur d'un ingrédient
     * @param nom le nom
     * @param quantite la quantite
     * @param unite l'unite de la quantite
     * @return un ingrédient depuis son interface IIngredient
     */
    public static IIngredient creerIngredient(String nom, int quantite, Unite unite){
        return new Ingredient(nom, quantite, unite);
    }
    
    /**
     * Permet de construire un nouvelle ingrédient
     * @param ingredient depuis un ingrédient
     * @return un clone de l'ingrédient
     */
    public static IIngredient creerIIngredient(IIngredient ingredient){
        return (IIngredient)ingredient.clone();
    }
    
    /**
     * Clone une liste d'ingrédient
     * @param ingredients la liste à cloner
     * @return la nouvelle liste avec les clones des ingredients
     */
    public static List<IIngredient> cloneListeIngredients(List<IIngredient> ingredients){
        List<IIngredient> copy = new ArrayList<>();
        for(IIngredient i : ingredients)
            copy.add(Fabrique.creerIIngredient(i));
        return copy;
    }
}
