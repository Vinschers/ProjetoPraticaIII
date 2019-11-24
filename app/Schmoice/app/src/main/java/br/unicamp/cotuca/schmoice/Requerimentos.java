package br.unicamp.cotuca.schmoice;

import java.io.Serializable;
import java.util.ArrayList;

public class Requerimentos implements Serializable {
    public ArrayList<int[]> getReqs() {
        return reqs;
    }
    public void setReqs(ArrayList<int[]> reqs) {
        this.reqs = reqs;
    }

    private ArrayList<int[]> reqs;
    public Requerimentos() {
        reqs = new ArrayList<int[]>();
    }
    public void adicionarRequerimento(int[] i){
        reqs.add(i);
    }
    public boolean serve(ArrayList<Integer> vet)
    {
        for(int i = 0; i < reqs.size(); i++)
            if (vet.get(reqs.get(i)[0]) != reqs.get(i)[1])
                return false;
        return true;
    }
}