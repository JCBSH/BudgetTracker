package com.mycompany.btrack.models;

import java.util.ArrayList;

/**
 * Created by godfreytruong on 12/10/2015.
 */
public class DebtorDB {
    private String name;
    private ArrayList<DebtDB> debts;

    public DebtorDB() {
    }

    public DebtorDB(Debtor d) {
        this.name = d.getName();
        debts = new ArrayList<DebtDB>();
        if (d.getDebts() != null) {
            for (Debt debt : d.getDebts()) {
                debts.add(new DebtDB(debt));
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<DebtDB> getDebts() {
        return debts;
    }

    public void setDebts(ArrayList<DebtDB> debts) {
        this.debts = debts;
    }
}
