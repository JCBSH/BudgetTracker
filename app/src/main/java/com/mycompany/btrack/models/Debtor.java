package com.mycompany.btrack.models;

import com.mycompany.btrack.utils.DebtComparator;
import com.mycompany.btrack.utils.MoneyTextWatcher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by JCBSH on 30/09/2015.
 */
public class Debtor {
    private static final String TAG = Debtor.class.getSimpleName();

    private static final String DEFAULT_NAME = "person";
    private String mName;
    private static int count = 0;
    private ArrayList<Debt> mDebts;

    public Debtor() {
        mName = getNextDefaultName();
        mDebts =  new ArrayList<Debt>();
        count++;
    }

    public Debtor(DebtorDB d) {
        mName = d.getName();
        mDebts = new ArrayList<Debt>();
        if (d.getDebts() != null) {
            for (DebtDB debt : d.getDebts()) {
                mDebts.add(new Debt(debt));
            }
        }
        count++;
    }


    @Override
    public String toString() {
        return mName;
    }




    public double getTotalDebtsAmount () {
        Double total = 0.0;
        for (Debt d: mDebts) {
            total += d.getAmount();
        }
        return total;
    }

    public String getFormatTotalDebtsAmount () {
        double total = getTotalDebtsAmount();
        DecimalFormat df = new DecimalFormat("#.00");
        if (total == 0.00) {
            return "0.00";
        } else {
            String formatted = df.format(total);
            return formatted;
        }
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Debtor.count = count;
    }

    public static String getNextDefaultName() {
        return DEFAULT_NAME + String.valueOf(count);
    }

    public static String getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    public boolean equals(Object o) {
        Debtor o1 = (Debtor) o;
        return mName.equalsIgnoreCase(((Debtor) o).getName());
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ArrayList<Debt> getDebts() {
        return mDebts;
    }

    public void addDebt(Debt d) {
        mDebts.add(d);
        sortDebts();
    }

    public void sortDebts () {
        Collections.sort(mDebts, new DebtComparator());
    }

    public void deleteDebt(Debt d) {
        mDebts.remove(d);
    }

    public Debt getDebt(UUID uuid) {
        for (Debt debt:mDebts) {
            if (debt.getId().equals(uuid)) return debt;
        }
        return null;
    }

    public String getFormatBalance() {
        double balance = getBalance();


        DecimalFormat df = new DecimalFormat("#.00");
        String formatted = "";
        if (balance > MoneyTextWatcher.MAX_DISPLAY_AMOUNT_LIMIT) {
            formatted = df.format(MoneyTextWatcher.MAX_DISPLAY_AMOUNT_LIMIT);
            formatted = "> " + formatted;
        } else if (balance < MoneyTextWatcher.MIN__DISPLAY_AMOUNT_LIMIT) {
            formatted = df.format(MoneyTextWatcher.MIN__DISPLAY_AMOUNT_LIMIT);
            formatted = "< " + formatted;

        } else if (balance == 0.0){
            formatted = "0.00";
        } else {
            formatted = df.format(balance);
        }

        return formatted;
    }

    public Double getBalance() {
        double balance = 0.00;
        if (mDebts == null) {
            return balance;
        }
        for (Debt debt: mDebts) {
            balance += debt.getAmount();
        }

        return balance;
    }
}
