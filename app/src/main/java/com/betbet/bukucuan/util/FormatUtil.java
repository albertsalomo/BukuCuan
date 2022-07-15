package com.betbet.bukucuan.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtil {
    public static String number(Integer number){
        NumberFormat numberFormat = new DecimalFormat("#,###");
        return numberFormat.format(number);
    }

    public static String date(String string){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault());
            Date date = dateFormat.parse(string);
            SimpleDateFormat finalFormat = new SimpleDateFormat("MM, dd yyyy",
                    Locale.getDefault());
            return finalFormat.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String dateProfile(String string){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault());
            Date date = dateFormat.parse(string);
            SimpleDateFormat finalFormat = new SimpleDateFormat("dd MMM yyyy",
                    Locale.getDefault());
            return finalFormat.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String dateAdapter(String string){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault());
            Date date = dateFormat.parse(string);
            SimpleDateFormat finalFormat = new SimpleDateFormat("dd-MMM-yyyy",
                    Locale.getDefault());
            return finalFormat.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

}
