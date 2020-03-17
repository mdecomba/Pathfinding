package Graphe;

// On doit chercher les chemins minimum pour tout les sommet à partir du départ

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Graphes {
    private int[][] matrice_adjacence;
    private int[][] matrice_valeur;
    private int      nombre_de_sommets;
    private List <String> allLines;
    
    public Graphes(String graphe) {
        try 
        {
            String[] splited;

            //Charge le fichier texte et le découpe en plusieurs lignes
            allLines = Files.readAllLines(Paths.get(graphe));

            //La première valeur est le nombre de sommet on lui la rentre dans l'objet
            nombre_de_sommets = Integer.parseInt(allLines.get(0));

            //On définit la taille des matrice d'adjascence et de valeur
            matrice_adjacence = new int[nombre_de_sommets][nombre_de_sommets];
            matrice_valeur = new int[nombre_de_sommets][nombre_de_sommets];
            allLines.remove(0);

            //On sépare chaque ligne, au niveau des espaces, en trois valeurs puis on indique l'adjascence entre le premier numero et le troisième
            for (String s : allLines)
            {
                splited = s.split(" ");
                for(int i=0; i<nombre_de_sommets; i++){
                    if (Integer.parseInt(splited[0]) == i){
                        matrice_adjacence[i][Integer.parseInt(splited[2])] = 1;
                        matrice_valeur[i][Integer.parseInt(splited[2])] = Integer.parseInt(splited[1]);
                    }
                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void bellman_ford(){

        //------   Variables   ------
        Scanner sc = new Scanner(System.in);
        int sommetDepart;
        //Sommet dont l'on veur le chemin le plus court
        int sommet_a_calculer;
        System.out.print("Entrez le sommet de départ =>");
        sommetDepart = sc.nextInt();

        //poid de l'actuel chemin le plus court vers J
        Integer distJ = 200;

        //Liste contenant tout nos sommet leur origine et le poid de leur chemin le plus court
        List<Possibilitee> listBellman = init_bellman(sommetDepart);




        //------   Boucle   ------
        for (int i=0; i<nombre_de_sommets-1; i++){
            //Sur chaque sommet de la liste on regarde...
            for (Possibilitee A : listBellman){
                //si son poids de chemin le plus court n'est pas ∞ ...
                if(A.getPoidCheminLePlusCourt() != null){
                    //et s'il y a adjascence avec un autre sommet
                    for (int j =0; j < nombre_de_sommets; j++){
                        if (matrice_adjacence[A.getSommet()][j] == 1){
                            //si oui on note le poid du chemin le plus court de ce sommet dans distJ
                            for (Possibilitee B : listBellman){
                                if (B.getSommet() == j) {
                                    if(B.getPoidCheminLePlusCourt() == null){
                                        distJ = null;
                                    }
                                    else{
                                        distJ = B.getPoidCheminLePlusCourt();
                                    }
                                }
                            }
                            //Si On peut améliorer le chemin le plus court on le fait et on change donc le sommet d'origine
                            if (distJ == null || distJ > A.getPoidCheminLePlusCourt() + matrice_valeur[A.getSommet()][j]) {
                                for (Possibilitee C : listBellman) {
                                    if (C.getSommet() == j) {
                                        C.setPoidCheminLePlusCourt(A.getPoidCheminLePlusCourt() + matrice_valeur[A.getSommet()][j]);
                                        C.setSommetOrigine(A.getSommet());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //------   Affichage du chemin le plus court   ------
        printCheminLePlusCourt(listBellman, sommetDepart);



    }

    private void printCheminLePlusCourt(List<Possibilitee> listAlgo, int sommetDepart){
        Scanner sc = new Scanner(System.in);
        int sommet_a_calculer;
        int distanceSommet_a_caluler = 0;

        System.out.println("Algorithme terminé");
        System.out.print("Pour quel sommet voulez vous le chemin le plus court =>");
        sommet_a_calculer = sc.nextInt();

        for(Possibilitee A : listAlgo){
            if (A.getSommet() == sommet_a_calculer) distanceSommet_a_caluler = A.getPoidCheminLePlusCourt();
        }
        List<Integer> cheminLePlusCourt = calculCheminPlusCourt(sommet_a_calculer, listAlgo, sommetDepart);

        rersePrintList(cheminLePlusCourt);

        System.out.println("\nIl a pour distance " + distanceSommet_a_caluler);

    }

    private void printList(List<Possibilitee> list){
        for(Possibilitee A : list){
            System.out.println(A);
        }
    }

    private List<Possibilitee> init_bellman(int sommetDepart){
        List<Possibilitee> listBellman = new ArrayList<Possibilitee>();
        for (int i=0; i<nombre_de_sommets; i++){
            if(i==sommetDepart) listBellman.add(new Possibilitee(null, 0, sommetDepart));
            else listBellman.add(new Possibilitee(null, null, i));
        }

        return listBellman;
    }

    public void dijkstra(){
        //Variables
        int sommetActuel;
        List <Possibilitee> possibilitees = new ArrayList<Possibilitee>();
        List <Possibilitee> possibiliteesSommet = new ArrayList<Possibilitee>();
        List <Possibilitee> choix = new ArrayList<Possibilitee>();
        List <Integer> interdit = new ArrayList<Integer>();

        int sommetDepart;
        int sommetChoixPlusCourt;


        //------   Initialisation   ------
        Scanner sc = new Scanner(System.in);
        System.out.print("Par quel sommet voulez vous commencer =>");
        sommetDepart = sc.nextInt();

        //L'algorithme commence au sommet choisis par l'utilisateur
        Possibilitee ChoixActuel = new Possibilitee(sommetDepart,0,sommetDepart);



        //------   Boucle   ------

        // La boucle s'arrête quand le chemin le plus court a été trouvé pour chauque sommet du graphe
        while(choix.size() != nombre_de_sommets){
            choix.add(ChoixActuel);
            //S'il est dans la liste des possibilitees on retire celle que l'on vient de choisir
            possibilitees.remove(ChoixActuel);

            //On ne repasse plus par le sommet par lequel on vient de passer
            interdit.add(ChoixActuel.getSommet());

            //On ajoute les sommet adjascent à celui sur lequel on se trouve en excluant ceux par lesquels on ai déjà passés
            possibiliteesSommet = buildPossibilitees(ChoixActuel, interdit);
            for(Possibilitee A : possibiliteesSommet){
                possibilitees.add(A);
            }

            //Celui qui sera choisis sera celui dans la liste des possibilitées ayant la distance la plus courte
            ChoixActuel = possibilitees.get(0);
            for(Possibilitee A : possibilitees){
                if (A.getPoidCheminLePlusCourt()<ChoixActuel.getPoidCheminLePlusCourt()) ChoixActuel = A;
            }

        }




        //------   Affichage du chemin le plus court   ------
        System.out.println("Chemins les plus court trouvés");
        System.out.print("Lequel voulez-vous afficher =>");

        sommetChoixPlusCourt = sc.nextInt();
        int distanceSommetChoixPlusCourt = 0;

        for(Possibilitee A : choix){
            if (A.getSommet() == sommetChoixPlusCourt) distanceSommetChoixPlusCourt = A.getPoidCheminLePlusCourt();
        }

        List<Integer> cheminPlusCourt = calculCheminPlusCourt(sommetChoixPlusCourt, choix, sommetDepart);

        System.out.println("Le chemin le plus court vers le sommet " + sommetChoixPlusCourt + " est :");
        rersePrintList(cheminPlusCourt);
        System.out.println("\nIl a pour distance " + distanceSommetChoixPlusCourt);

    }

    //Affiche une liste à l'envers Utile pour printCheminLePlusCourt
    private void rersePrintList(List<Integer> list){
        for (int i= list.size()-1; i>=0; i--){
            System.out.print(list.get(i) + "->");
        }
        System.out.println();
    }

    //Fonction permettant de retrouver le chemin parcourru en faisant le chemin depuis la fin
    private List<Integer> calculCheminPlusCourt(int sommet_a_calculer, List<Possibilitee> choix, int sommetDepart){
        List <Integer> cheminPlusCourt  = new ArrayList<Integer>();

        //Dabord on cherche le sommet demandé dans la liste des choix
        for (Possibilitee A : choix){
            if(A.getSommet() == sommet_a_calculer){

                //On l'ajoute à liste ui va être retournée
                cheminPlusCourt.add(A.getSommet());

                //Ensuite on remonte dans la liste jusqu'à ce qu'on arrive au début du chemin
                while(A.getSommetOrigine() != sommetDepart){
                    int origine = A.getSommetOrigine();
                    cheminPlusCourt.add(A.getSommetOrigine());

                    //En actualisant à chaque fois le sommet dont on cherche l'origine
                    for(Possibilitee B : choix){
                        if(B.getSommet() == origine) A = B;
                    }
                }
            }
        }
        cheminPlusCourt.add(sommetDepart);
        return cheminPlusCourt;
    }

    //Construit les possibilités en à partir d'un sommet en regardant les sommet adjascent et en excluant les sommets déjà choisis(interdit)
    private List<Possibilitee> buildPossibilitees(Possibilitee A, List<Integer> interdit){
        List <Possibilitee> possibiliteesSommet = new ArrayList<Possibilitee>();
        int sommetDestination = A.getSommet();
        int distance = A.getPoidCheminLePlusCourt();

        for (int i=0; i<nombre_de_sommets; i++){
            //s'il y a adjascence entre les deux sommets et que le prochain n'est pas dans la liste des sommets déjà calculé:
            //On ajoute la Possibilitee en actualisant la distance
            if ((matrice_adjacence[sommetDestination][i] == 1) && interdit.contains(i) == false ){
                possibiliteesSommet.add(new Possibilitee(sommetDestination, distance+matrice_valeur[sommetDestination][i], i));
            }
        }
        return possibiliteesSommet;
    }

    public boolean testArcNegatif(){
        boolean arcNegatif = false;
        for(int i=0; i<nombre_de_sommets; i++){
            for(int j=0; j< nombre_de_sommets; j++){
                if(matrice_valeur[i][j]<0) arcNegatif = true;
            }
        }
        return arcNegatif;

    }

    //Affiche le graphe
    public void printGraph(){
        for (String s : allLines)
        {
            String[] splited = s.split(" ");
            for (int i = 0 ; i<3; i++) System.out.print(splited[i] + "\t");
            System.out.println();
        }

    }

    //Affiche la matrice d'adjascence
    public void printMatrice_a(){

        System.out.print("  ");
        for (int i=0; i<nombre_de_sommets; i++){
            System.out.print("\t" + i);
        }
        System.out.println();
        System.out.println();

        for(int i=0; i<nombre_de_sommets; i++){
            System.out.print(i + "\t");
            for(int j=0; j< nombre_de_sommets; j++){
                System.out.print(this.matrice_adjacence[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    //Affiche la Matrice des valeurs
    public void printMatrice_v(){
        System.out.print("  ");
        for (int i=0; i<nombre_de_sommets; i++){
            System.out.print("\t" + i);
        }
        System.out.println();
        System.out.println();

        for(int i=0; i<nombre_de_sommets; i++){
            System.out.print(i + "\t");
            for(int j=0; j< nombre_de_sommets; j++){
                System.out.print(this.matrice_valeur[i][j] + "\t");
            }
            System.out.println("");
        }
    }
    
}