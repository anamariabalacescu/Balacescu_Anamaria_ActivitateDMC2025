package com.example.activitatedmc;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Banca implements Parcelable {

    private String numeBanca;
    private boolean estePrivata;
    private int numarFiliale;
    private TipBanca tipBanca;
    private double rating;
    private List<TipCredit> tipuriCredite;
    private boolean internetBanking;

    public enum TipBanca {
        COMERCIAL, INVESTITIONALA, ECONOMICA
    }

    public enum TipCredit {
        IPOTECAR, AUTO, PERSONAL
    }

    public Banca(String numeBanca, boolean estePrivata, int numarFiliale,
                 TipBanca tipBanca, double rating, List<TipCredit> tipuriCredite, boolean internetBanking) {
        this.numeBanca = numeBanca;
        this.estePrivata = estePrivata;
        this.numarFiliale = numarFiliale;
        this.tipBanca = tipBanca;
        this.rating = rating;
        this.tipuriCredite = tipuriCredite;
        this.internetBanking = internetBanking;
    }

    @Override
    public String toString() {
        return "Banca{" +
                "numeBanca='" + numeBanca + '\'' +
                ", estePrivata=" + estePrivata +
                ", numarFiliale=" + numarFiliale +
                ", tipBanca=" + tipBanca +
                ", rating=" + rating +
                ", tipuriCredite=" + tipuriCredite +
                ", internetBanking=" + internetBanking +
                '}';
    }

    // Implementare Parcelable
    protected Banca(Parcel in) {
        numeBanca = in.readString();
        estePrivata = in.readByte() != 0;
        numarFiliale = in.readInt();
        tipBanca = TipBanca.valueOf(in.readString());
        rating = in.readDouble();
        int size = in.readInt();
        tipuriCredite = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String enumName = in.readString();
            tipuriCredite.add(TipCredit.valueOf(enumName));
        }
        internetBanking = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(numeBanca);
        dest.writeByte((byte) (estePrivata ? 1 : 0));
        dest.writeInt(numarFiliale);
        dest.writeString(tipBanca.name());
        dest.writeDouble(rating);
        dest.writeInt(tipuriCredite.size());
        for (TipCredit tip : tipuriCredite) {
            dest.writeString(tip.name());
        }
        dest.writeByte((byte) (internetBanking ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Banca> CREATOR = new Creator<Banca>() {
        @Override
        public Banca createFromParcel(Parcel in) {
            return new Banca(in);
        }

        @Override
        public Banca[] newArray(int size) {
            return new Banca[size];
        }
    };
}
