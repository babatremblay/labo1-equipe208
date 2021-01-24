import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LZW implements Algorithme{

    private ArrayList<String> dict = new ArrayList();
    private ArrayList<Integer> code = new ArrayList<>();

    private final int BYTE_SIZE = 16;

    public LZW(){
        initialiserDictionnaire();
    }

    @Override
    public void compresser(String fichierEntree, String fichierSortie) {
        try {
            // lire le fichier d'entree
            FileInputStream f = new FileInputStream(fichierEntree);

            // executer l'algorithme
            int i;
            String s = "";
            String c;
            while ((i = f.read()) != -1) {
                c = String.valueOf((char) i);
                if(dict.contains(s + c)){
                    s = s + c;
                } else{
                    code.add(dict.indexOf(s));
                    dict.add(s+c);
                    s = c;
                }
            }
            code.add(dict.indexOf(s));

            // creer et ecrire le dictionnaire dans le fichier de sortie
            File fs = new File(fichierSortie);
            fs.createNewFile();
            ByteOutputStream b = new ByteOutputStream(fs);
            String nBits = "";

            // écrit le code bit par bit dans le fichier de sortie selon BYTE_SIZE
            for(int n : code){
                nBits = toNbits(n, BYTE_SIZE);
                for (int j = 0; j < nBits.length(); j++){
                    byte k = (byte) Character.getNumericValue(nBits.charAt(j));
                    b.write(k);
                }
            }
            b.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String toNbits(int i, int n) {
        String str = Integer.toBinaryString(i);
        while (str.length() < n) {
            str = "0" + str;
        }
        return str;
    }

    @Override
    public void decompresser(String fichierEntree, String fichierSortie) {
        try {

            // execute l'algorithme
            String sortie = "";
            String s = "";
            String seq = "";
            code = rebuildCode(fichierEntree);
            int k;
            for(int i = 0; i < code.size(); i++){
                k = code.get(i);
                if(k < dict.size())
                    seq = dict.get(k);
                else seq = "";
                if(seq.equals("")){
                    seq = s + s.substring(0, 1);
                }
                sortie=sortie+seq;
                if (!s.equals("")){
                    dict.add(s + seq.substring(0, 1));
                }
                s = seq;
            }

            // creer et ecrire le dictionnaire dans le fichier de sortie
            File fs = new File(fichierSortie);
            fs.createNewFile();
            FileWriter w = new FileWriter(fichierSortie);
            w.write(sortie);
            w.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // reconstruit le code selon le BYTE_SIZE dans lequel le fichier a été encodé
    public ArrayList<Integer> rebuildCode(String fichier){
        String sBit = "";
        String s = "";
        int i;
        try {
            FileInputStream f = new FileInputStream(fichier);
            while ((i = f.read()) != -1) {
                sBit = sBit + toNbits(i, 8);
            }
            for(int j = 1; j <= sBit.length(); j++){
                s = s + sBit.charAt(j-1);
                if((j % BYTE_SIZE == 0 && j != 0)){
                    int nombre = Integer.parseInt(s, 2);
                    code.add(nombre);
                    s = "";
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return code;
    }

    public void initialiserDictionnaire(){
        for(int i = 0; i < 256; i++){
            dict.add(Character.toString((char) i));
        }
    }
}
