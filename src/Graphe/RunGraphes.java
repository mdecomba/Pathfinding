package Graphe;

import  java.io.BufferedReader;
import  java.io.FileReader;
import  java.io.IOException;
import  java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RunGraphes {
    public static void main(String[] args) {


        Scanner sc = new Scanner(System.in);
        String choixGraph;
        boolean menu;
        int choixMenu;
        int choixContinuer;


        System.out.print("Que voulez-vous faire?\n1.Entrer un graphe\n2.Quitter\n\nchoix =>");
        choixMenu = sc.nextInt();

        if (choixMenu == 1) menu = true;
        else if (choixMenu == 2) menu = false;
        else{
            System.out.println("Choix inconnu");
            menu = true;
        }

        while(menu == true){
            if (choixMenu == 1){
                Scanner bc = new Scanner(System.in);
                System.out.print("Quel est le nom du graph? =>");
                choixGraph = bc.nextLine();
                Graphes A = new Graphes(choixGraph);
                boolean arcNegatif = A.testArcNegatif();
                if(arcNegatif == true) {
                    System.out.println("Arc négatif détecté");
                    System.out.println("Nous allons utilisé l'algorithme de Bellman-Ford");
                    A.printMatrice_a();
                    System.out.println();
                    A.printMatrice_v();
                    A.bellman_ford();
                }
                else{
                    int choix;
                    System.out.println("Aucun arc négatif détecté");
                    System.out.print("Quel algorithme voulez vous utilisé:\n1:Bellman-Ford\n2:Dijkstra\n\nchoix =>");
                    choix = sc.nextInt();
                    if(choix == 1) A.bellman_ford();
                    else if(choix == 2) A.dijkstra();
                    else System.out.println("Choix non reconnu");
                }
            }
        }

    }
}
