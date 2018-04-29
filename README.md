Livre de Recettes
=================

Explication du projet :
-----------------------

### Objectif du projet :

Ce projet a pour objectif de réaliser une application Java en utilisant les fonctionnalités de la POO (Programmation Orientée Objet) pour cela j'ai choisi de réaliser un livre de recettes.  
En effet un livre de recette permet d'appliquer la programmation objet.

Un livre de recettes est en fait un conteneur d'une liste non-exhaustive de recettes.  
Une recette est un élément composé d'un nom, des instructions pour la réaliser, d'une liste d'ingrédients, et d'autres informations.  
Un ingrédient quant à lui est un élément composé d'un nom, d'une quantité et d'une unité associée à la quantité.

### Architecture utilisée :

Pour réaliser ce projet j'ai construit mon programme en utilisant une architecture MVC pour Modèle-vue-contrôleur.

Projet réalisé en JavaFX, c'est-à-dire que la partie ***modèle*** est réalisée en Java et la partie ***vue*** ou graphique est réalisée en FXML et enfin la partie ***contrôleur*** en Java.

Explication des packages :
--------------------------

Mon code est donc réparti suivant plusieurs répertoires :
- **model :** le package représentant le modèle de l'application, les classes de base livre, recette et ingrédient par exemple.
- **data_managers :** dans ce package sont les classes pour la gestion de la persistance.
- **launchers :** le package permettant d'exécuter le code, il contient les différentes classes ayant une méthode `main`, une pour lancer l'application en mode console et l'autre lançant l'application en mode graphique.
- **views :** contient les vues écrites en FXML.
- **controllers :** le package des contrôleurs associés à chacune des vues.
- **utils :** est le package avec des classes "outils" récupérées sur le net qui sont nécessaires au bon fonctionnement de l'application

Conception du modèle :
----------------------

La partie modèle de l'application est la partie qui gère les données et la logique en rapport avec les données. Sont, dans le modèle, les classes de base telles que la classe `Recette` ou la classe `Ingredient` par exemple, ainsi que toutes les classes nécessaires à la construction de ces classes ou leurs gestions telle que la gestion de la persistance des instances de ces classes.

### Diagramme de classes :

Voici donc le diagramme de classes de la partie Modèle de l'application, ce diagramme représente donc les classes de base pour que l'application fonctionne, mais ne montre pas les classes gérant la partie graphique de l'application.  
En effet sur ce diagramme sont réprésentées les classes du package `model` ainsi que les classes du package `data_managers` (les classes héritant de l'interface `DataManager`).

![Diagramme de classe du Modèle](https://raw.githubusercontent.com/BOISSARD/LivreRecettes/master/UMLDiagram/exports/Class-Diagram-Model-002.png)

### Conception :

Cette application a demandé une certaine quantité de réflexions et de recherches rien qu'au niveau de sa conception afin que l'application soit la plus "propre" possible au niveau code et notamment que celle-ci soit évolutive sans que tout le code ne se casse.  
Déjà la séparation avec la partie graphique doit être nette et logique.

#### Le Livre :

La classe `Livre` est la classe qui doit contenir la liste des recettes, c'est donc une classe importante dans la conception, c'est pourquoi c'est elle qui gère la partie modèle et qui en est l'entrée. Cette classe est la mise en place du patron de conception "_Façade_".  
En effet, c'est par cette classe que toute la partie modèle est gérée et par laquelle il faut passer pour la gestion des recettes, c'est à dire l'ajout/modification/suppression d'une recette mais aussi la persistance (sauvegarde/chargement) des recettes.

#### Recettes et Ingrédients :

Ensuite viennent les classes les plus intéressantes, la classe `Recette` qui est l'élement important de l'application et la classe `Ingredient`, ces deux classes sont protégées afin que les classes extérieures au sous-système ne puissent pas les manipuler directement mais doivent passer par la façade `Livre`.  
Le problème est qu'il est malgré tout nécessaire d'accéder à certains éléments des classes notamment les "_getter_". Pour cela, chacune de ces deux classes implémente respectivement l'interface `IRecette` et `IIngredient`, ce sont ces deux interfaces qui seront manipulées par les objets extérieurs au sous-système afin de manipuler des recettes et des ingrédients protégés.  

#### Les énumérations :

Chacune des classes peut avoir un ou plusieurs attribut(s) correspondant à l'une des différentes Enumérations. J'ai utilisé des énumérations afin de prédéfinir les différentes possibilités pour certaines notions, la plus évidente étant l'énumération `Unite`.  
L'utilisation d'énumération est plus pratique après dans la partie graphique dans un formulaire, car il suffit de générer une _ComboBox_ avec les différentes valeurs de l'énumération en question.

#### La fabrique :

La classe `Fabrique` permet d'instancier un objet d'une classe dite "package" appartenant au sous-système (ici le package _model_), ainsi cette classe _static_ permet de contrôler l'instanciation d'une classe protégée hors du sous-système. Cela correspond au patron de conception "_Fabrique Simple_". Cette fabrique permet donc d'instancier des recettes et des ingrédients.  
Si cette classe n'était pas mise en place, il serait impossible d'instancier les objets recettes ou ingrédients sans mettre ces dernières en visibilité publique, ce qui serait gênant.

#### La persistance (DataManager) :

L'interface `DataManager` permet de définir les classes gérant la persistance afin qu'elles puissent être utilisées pour la persistance des recettes.  
Cette conception correspond au patron de conception "_Stratégie_".  
Les classes permettant réellement de réaliser la persistance se trouvent dans le package `data_managers` telle que la classe `XMLDataManager` permettant la persistance au format XML. Ainsi avec cette conception il est possible d'avoir autant de classes permettant la persistance que l'on souhaite, par exemple si l'on veut maintenant effectuer la persistance au format JSon il suffit de créer la classe avec le bon code et d'instancier la classe JSonDataManager plutôt que XMLDataManager ou autre. 

On peut voir qu'il y a une autre classe, nommée `StubDataManager`, cette classe correspond à une "persistance" en "dur" dans le code et permettait de voir si le code était fonctionnel avant de m'attaquer à une vraie classe de persistance, car dans ce cas si ça ne marche pas, ça ne peut venir que d'un endroit : la classe en rédaction.

Le développement :
------------------

Le développement à évidemment été la partie la plus longue et aussi (et surtout) la plus intéressante.  
Le développement s'est déroulé en plusieurs étapes :
 1. La première a été la partie **modèle** et **persistance**.
 2. La deuxième a été l'**application en console** me permettant de tester si tout fonctionne correctement.
 3. La troisième a été l'**application graphique**.
 4. La dernière partie est l'**amélioration**, le rajout de fonctionnalités, ce qui peut impliquer la modification des 3 parties précédentes.
 
Au fur et à mesure du développement de l'application j'ai essayé de réaliser les commentaires afin d'avoir une documentation réalisée avec _JavaDoc_.

### Développement du Modèle :

L'ensemble du modèle se situe dans le package `model`.

Tout d'abord j'ai commencé par écrire la classe `Recette` puis la classe `Ingredient` ainsi que les énumérations nécessaires pour des objets plus complets.  
Une fois cela fait, j'ai écrit la classe `Livre` qui est le centre de la partie modèle et donc de l'application.  

J'ai ensuite mis en place la persistance en commençant par l'interface `DataManager` puis la classe fille `StubDataManager`, dans le package `data_managers`, permettant de générer quelques recettes de tests. Pour mettre en place cette classe j'ai été obligé d'écrire la classe `Fabrique` pour pouvoir instancier des recettes et des ingrédients à l'extérieur du package `model`.  
Une fois toute la partie persistance fonctionnelle, j'ai mis en place un vrai mode de persistance en _XML_ avec la classe `XMLDataManager`.

### Développement de(s) l'application(s) console(s) :

La classe qui lance l'application console est la class `Console` qui se situe dans le package `launchers`. Cette application est fonctionnelle est permet de tester l'ensemble des fonctionnalités importantes au projet : la gestion d'un livre et de ses recettes ainsi que la persistance. 
Cette classe a été développée en parallèle aux fonctionnalités pour pouvoir tester au fur et à mesure et de vérifier si le modèle de l'application est facile à utiliser.

J'ai aussi créé une classe permettant d'exécuter une application console mais qui ne demande pas à l'utilisateur d'effectuer des actions car les actions à effectuer sont déjà écrites afin de tester les mêmes "requêtes" utilisateur sans devoir les taper à chaque redémarrage de l'application pour tester une fonctionnalité qui ne marche pas jusqu'à ce qu'elle marche.

### Développement de l'application graphqiue :

La classe qui permet de lancer l'application en mode graphique est la classe du package `launchers` nommé `Graphique` qui hérite de la classe _JavaFx_ `Application` permettant de lancer une application **_JavaFX_**.
Le développement de la partie graphique est composé de 2 parties : les vues dans le package `views` et leur contrôleurs respectifs dans le package `controllers`.  
J'ai commencé par la fenêtre permettant d'afficher le livre (la vue `LivreWindow.fxml`) sous forme de _Master-Details_, c'est-à-dire que je voulais une table sur la gauche contenant les différentes recettes (la partie Master) et sur le reste de la fenêtre les informations de la recette (la partie Details), ensuite j'ai écrit le contrôleur qui permet de gérer les événements lors d'un clic sur les boutons ajouter, modifier ou supprimer.  

Une fois l'affichage des recettes et de leurs informations fonctionnel, j'ai écrit le code de la fenêtre pour ajouter ou modifier une recette. J'ai écrit le code FXML et son contrôleur `RecetteFormulaire` qui est un formulaire. Le plus compliqué a été la gestion des ingrédients dans le formulaire car le nombre d'ingrédients est variable, les ingrédients sont dans une liste ayant chacun son mini formulaire, j'ai donc dû définir une cellule de `ListView` pour qu'elle soit constituée de quelques éléments de formulaire, et qu'il soit possible de faire varier ce nombre de cellules pour rajouter ou enlever un (ou plusieurs) ingrédient(s).  
Ça a été assez compliqué à mettre en place mais c'est très efficace.

J'ai mis en place la gestion de la persistance dans l'application graphique avec la possibilité pour l'utilisateur de sélectionner le fichier dans lequel effectuer la persistance. Cette partie est gérée par l'utilisateur dans la barre de menu.

### Amélioration :

Il était intéressant de pouvoir rechercher des recettes dans la liste pour cela j'ai écrit une nouvelle fenêtre, un formulaire de recherche (`RechercheFormulaire`) afin d'y renseigner les informations de la recherche. Pour récupérer les recettes correspondant à ma recherche j'ai dû écrire une classe `RecetteComparateur` qui permet de tester si une recette correspond à la recherche notamment grâce à l'utilisation de `stream` permettant de filtrer des collections d'objets suivant certains critères de sélection.

Conclusion :
------------

Ce projet m'a demandé beaucoup de travail mais m'a permis d'apprendre beaucoup quant à la programmation en **Java** et notamment l'utilisation de **JavaFX**. J'ai passé beaucoup d'heures sur ce projet et m'y suis donné à fond pour que ce soit le plus parfait possible dans la limite de mes compétences.
