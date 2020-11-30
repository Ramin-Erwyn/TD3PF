package td1.commandes;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.ArrayList;

import static td1.commandes.Categorie.*;
import td1.paires.Paire;

public class DAO {
    private List<Commande> commandes;

    private DAO(Commande c1, Commande ...cs) {
        commandes = new ArrayList<>();
        commandes.add(c1);
        commandes.addAll(List.of(cs));
    }

    public static DAO instance = null;

    public static final DAO instance() {
        if (instance == null) {
            Produit camembert = new Produit("Camembert", 4.0, NORMAL);
            Produit yaourts = new Produit("Yaourts", 2.5, INTERMEDIAIRE);
            Produit masques = new Produit("Masques", 25.0, REDUIT);
            Produit gel = new Produit("Gel", 5.0, REDUIT);
            Produit tournevis = new Produit("Tournevis", 4.5, NORMAL);
            //
            Commande c1 = new Commande()
                .ajouter(camembert, 1)
                .ajouter(yaourts, 6);
            Commande c2 = new Commande()
                .ajouter(masques, 2)
                .ajouter(gel, 10)
                .ajouter(camembert, 2)
                .ajouter(masques, 3);
            //
            instance = new DAO(c1,c2);
        }
        return instance;
    }

    /**
     * liste de toutes les commandes
     */
    public List<Commande> commandes() { return commandes; }

    /**
     * ensemble des différents produits commandés
     */
    /*
    public Set<Produit> produits() {
        return commandes.stream()
                .flatMap(c -> c.lignes().stream())
                .map(Paire::fst)
                .collect(Collectors.toSet());
    }
    */
    public Set<Produit> produits() {
        Set<Produit> rtr = new HashSet<>();
        for (Commande commande : this.commandes) {
            for (Paire<Produit, Integer> ligne : commande.lignes()) {
                rtr.add(ligne.fst());
            }
        }
        return rtr;
    }

    /**
     * liste des commandes vérifiant un prédicat
     */
    /* public List<Commande> selectionCommande(Predicate<Commande> p) {
       return commandes.stream()
            .filter(p)
            .collect(Collectors.toList());


    }*/

    public List<Commande> selectionCommande (Predicate<Commande> p){
        List<Commande> selecCo = new ArrayList<>();
        for(Commande c : commandes) {
            if (p.test(c)) {
                selecCo.add(c);
            }
        }
        return selecCo;
    }

    /**
     * liste des commandes dont au moins une ligne vérifie un prédicat
     */
    public List<Commande> selectionCommandeSurExistanceLigne(Predicate<Paire<Produit,Integer>> p) {
        return commandes.stream()
            .filter(c -> c.lignes().stream().anyMatch(p))
            .collect(Collectors.toList());
    }
    public Set<Produit> selectionCommandeSurExistanceLigne(Predicate<Paire<Produit,Integer>> p) {
        List<Commande> selecCo = new ArrayList<>();
        for(Commande p : Commande()) {
            if(condition.test(p)) {
                selecCo.add(p);
            }
        }
        return selecCo;
    }


    /**
     * ensemble des différents produits commandés vérifiant un prédicat
     */
    public Set<Produit> selectionProduits(Predicate<Produit> p) {
        return produits()
            .stream()
            .filter(p)
            .collect(Collectors.toSet());
    }

//  TD1 EXO4
// [Masques, Gel]

public static void q1(){
    DAO db = DAO.instance();
    Set<Produit> v1 = db.selectionProduits(produitaTVAReduite);
    System.out.println(v1);
}
// [Masques]
private static void q2(){
    DAO db = DAO.instance();
    Set<Produit> v2 = db.selectionProduits(produitaTVAReduite.and(sup5Euros));
    System.out.println(v2);
}
// [Commande
//  Masques x2
//  Gel x10
//  Camembert x2
//  Masques x3
// ]
private static void q3(){
    DAO db = DAO.instance();
    List<Commande> v3  = db.selectionCommande(doublon);
    System.out.println(v3);

}
//non normalisé [Commande
//  Masques x2
//  Gel x10
//  Camembert x2
//  Masques x3
//]
private static void q4(){
    DAO db = DAO.instance();
     List<Commande> v4 = db.selectionCommandeSurExistanceLigne(
            paire -> paireTVAReduite.
                    and(genPredicate(2))
                    .test(paire)
    );
    System.out.println(v4);

}
// Commande
//+------------+------------+-----+------------+--------+------------+
//+ nom + prix + qté + prix ht + tva + prix ttc +
//+------------+------------+-----+------------+--------+------------+
//+ Yaourts + 2,50 + 6 + 15,00 + 10,00% + 16,50 +
//+ Camembert + 4,00 + 1 + 4,00 + 20,00% + 4,80 +
//+------------+------------+-----+------------+--------+------------+
//Total : 21,30
//Commande
//+------------+------------+-----+------------+--------+------------+
//+ nom + prix + qté + prix ht + tva + prix ttc +
//+------------+------------+-----+------------+--------+------------+
//+ Camembert + 4,00 + 2 + 8,00 + 20,00% + 9,60 +
//+ Masques + 25,00 + 5 + 125,00 + 5,50% + 131,88 +
//+ Gel + 5,00 + 10 + 50,00 + 5,50% + 52,75 +
//+------------+------------+-----+------------+--------+------------+
//Total : 194,23
private static void q5(){
    DAO db = DAO.instance();
     for (Commande cde : db.commandes()) {
        cde.affiche(calcul1);
    }
}
// Commande
//+------------+------------+-----+------------+--------+------------+
//+ nom + prix + qté + prix ht + tva + prix ttc +
//+------------+------------+-----+------------+--------+------------+
//+ Yaourts + 2,50 + 6 + 15,00 + 10,00% + 14,00 +
//+ Camembert + 4,00 + 1 + 4,00 + 20,00% + 4,80 +
//+------------+------------+-----+------------+--------+------------+
//Total : 18,80
//Commande
//+------------+------------+-----+------------+--------+------------+
//+ nom + prix + qté + prix ht + tva + prix ttc +
//+------------+------------+-----+------------+--------+------------+
//+ Camembert + 4,00 + 2 + 8,00 + 20,00% + 9,60 +
//+ Masques + 25,00 + 5 + 125,00 + 5,50% + 106,88 +
//+ Gel + 5,00 + 10 + 50,00 + 5,50% + 47,75 +
//+------------+------------+-----+------------+--------+------------+
//Total : 164,23
private static void q6(){
    DAO db = DAO.instance();
     for (Commande cde : db.commandes()) {
        cde.affiche(calcul2);
    }
 */


}
