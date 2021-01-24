import java.io.*;
import java.util.ArrayList;

public class Main {

    String typeAlgo; //args[0].substring(1);
    String compOuDecomp; //args[1].substring(1);
    String fichierEntree; //args[2];
    String fichierSortie; //args[3];

    Algorithme algo;


    public Main(String typeAlgo, String compOuDecomp, String fichierEntree, String fichierSortie){
        this.typeAlgo = typeAlgo;
        this.compOuDecomp = compOuDecomp;
        this.fichierEntree = fichierEntree;
        this.fichierSortie = fichierSortie;
    }

    public static void main(String args[]){

        String e = System.getProperty("user.dir") + "\\exemple-1.txt";
        String s = System.getProperty("user.dir") + "\\fichierSortie.bin";
        Main m = new Main("lzw", "d", e, s);

        m.execute();

    }

    public void execute(){
        switch(typeAlgo) {
            case "lzw":
                algo = new LZW();
                break;
            case "huff":
                break;
            case "opt":
                break;
        }
        if(compOuDecomp.equals("c")){
            algo.compresser(fichierEntree, fichierSortie);
        }
        else if(compOuDecomp.equals("d")){
            String f = System.getProperty("user.dir") + "\\allo.txt";
            algo.decompresser(fichierSortie, f);
        }
    }

}
