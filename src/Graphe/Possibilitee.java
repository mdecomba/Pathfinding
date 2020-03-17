package Graphe;

public class Possibilitee {
    private Integer sommetOrigine;
    private Integer sommet;
    private Integer poidCheminLePlusCourt;

    public Possibilitee(Integer sommetOrigine, Integer distance, Integer sommet) {
        this.sommetOrigine = sommetOrigine;
        this.poidCheminLePlusCourt = distance;
        this.sommet = sommet;
    }

    public int getSommetOrigine() {
        return sommetOrigine;
    }

    public int getSommet() {
        return sommet;
    }

    public Integer getPoidCheminLePlusCourt() {
        return poidCheminLePlusCourt;
    }

    public void setPoidCheminLePlusCourt(Integer poidCheminLePlusCourt) {
        this.poidCheminLePlusCourt = poidCheminLePlusCourt;
    }

    public void setSommetOrigine(Integer sommetOrigine) {
        this.sommetOrigine = sommetOrigine;
    }

    @Override
    public String toString() {
        return "Possibilitee{" +
                "sommetOrigine=" + sommetOrigine +
                ", poidCheminLePlusCourt=" + poidCheminLePlusCourt +
                ", sommet=" + sommet +
                '}';
    }
}
