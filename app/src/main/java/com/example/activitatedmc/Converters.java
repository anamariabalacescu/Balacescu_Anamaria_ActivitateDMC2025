package com.example.activitatedmc;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Converters {

    // Conversie pentru Date
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    // Conversie pentru lista de TipCredit (de exemplu, "IPOTECAR,AUTO,PERSONAL")
    @TypeConverter
    public static String fromTipCreditList(List<Banca.TipCredit> list) {
        if (list == null || list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Banca.TipCredit t : list) {
            sb.append(t.name()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @TypeConverter
    public static List<Banca.TipCredit> toTipCreditList(String data) {
        List<Banca.TipCredit> list = new ArrayList<>();
        if (data == null || data.isEmpty()) return list;
        String[] tokens = data.split(",");
        for (String token : tokens) {
            list.add(Banca.TipCredit.valueOf(token));
        }
        return list;
    }

    // Conversie pentru TipBanca (opțional, deoarece poți salva și cu .name())
    @TypeConverter
    public static String fromTipBanca(Banca.TipBanca tipBanca) {
        return tipBanca == null ? null : tipBanca.name();
    }

    @TypeConverter
    public static Banca.TipBanca toTipBanca(String name) {
        return name == null ? null : Banca.TipBanca.valueOf(name);
    }
}