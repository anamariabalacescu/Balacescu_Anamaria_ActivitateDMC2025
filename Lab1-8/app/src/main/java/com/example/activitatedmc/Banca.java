package com.example.activitatedmc;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "banca_table")
@TypeConverters({Converters.class})
public class Banca implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String numeBanca;
    private int numarFiliale;
    private TipBanca tipBanca;
    private double rating;
    private List<TipCredit> tipuriCredite;
    private boolean internetBanking;
    private Date dataInfiintare;

    public Banca() {
        this.tipuriCredite = new ArrayList<>();
    }

    public enum TipBanca {
        COMERCIAL, INVESTITIONALA, ECONOMICA
    }

    public enum TipCredit {
        IPOTECAR, AUTO, PERSONAL
    }

    public Banca(String numeBanca, int numarFiliale, TipBanca tipBanca,
                 double rating, List<TipCredit> tipuriCredite, boolean internetBanking, Date dataInfiintare) {
        this.numeBanca = numeBanca;
        this.numarFiliale = numarFiliale;
        this.tipBanca = tipBanca;
        this.rating = rating;
        this.tipuriCredite = tipuriCredite;
        this.internetBanking = internetBanking;
        this.dataInfiintare = dataInfiintare;
    }

    // Getter și Setter pentru id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // Getteri și setteri pentru celelalte câmpuri
    public String getNumeBanca() {
        return numeBanca;
    }

    public void setNumeBanca(String numeBanca) {
        this.numeBanca = numeBanca;
    }

    public int getNumarFiliale() {
        return numarFiliale;
    }

    public void setNumarFiliale(int numarFiliale) {
        this.numarFiliale = numarFiliale;
    }

    public TipBanca getTipBanca() {
        return tipBanca;
    }

    public void setTipBanca(TipBanca tipBanca) {
        this.tipBanca = tipBanca;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<TipCredit> getTipuriCredite() {
        return tipuriCredite;
    }

    public void setTipuriCredite(List<TipCredit> tipuriCredite) {
        this.tipuriCredite = tipuriCredite;
    }

    public boolean hasInternetBanking() {
        return internetBanking;
    }

    public void setInternetBanking(boolean internetBanking) {
        this.internetBanking = internetBanking;
    }

    public Date getDataInfiintare() {
        return dataInfiintare;
    }

    public void setDataInfiintare(Date dataInfiintare) {
        this.dataInfiintare = dataInfiintare;
    }

    @Override
    public String toString() {
        return "Banca{" +
                "id=" + id +
                ", numeBanca='" + numeBanca + '\'' +
                ", numarFiliale=" + numarFiliale +
                ", tipBanca=" + tipBanca +
                ", rating=" + rating +
                ", tipuriCredite=" + tipuriCredite +
                ", internetBanking=" + internetBanking +
                ", dataInfiintare=" + dataInfiintare +
                '}';
    }

    // Implementare Parcelable
    protected Banca(Parcel in) {
        id = in.readInt();
        numeBanca = in.readString();
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
        dataInfiintare = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(numeBanca);
        dest.writeInt(numarFiliale);
        dest.writeString(tipBanca.name());
        dest.writeDouble(rating);
        dest.writeInt(tipuriCredite.size());
        for (TipCredit tip : tipuriCredite) {
            dest.writeString(tip.name());
        }
        dest.writeByte((byte) (internetBanking ? 1 : 0));
        dest.writeLong(dataInfiintare.getTime());
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
