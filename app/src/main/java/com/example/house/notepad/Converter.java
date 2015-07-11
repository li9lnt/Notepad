package com.example.house.notepad;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by house on 2015/07/05.
 */
public class Converter {

    public static Calendar convertStringToCalendar(String str) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            calendar.set(2000, 0, 1, 0, 0, 0); //フォーマットエラー時は2000/01/01 00:00:00にする
            date = calendar.getTime();
        }
        calendar.setTime(date);
        return calendar;
    }

    public static String convertCalendarToString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(calendar.getTime());
        return str;
    }

    public static String convertShortStatement(String str) {
        int length = 10; // 1文の文字数
        String res = null;

        // 先頭10文字切り出し
        if (str == null || str == "") {
            res = "";
        } else if (str.length() < length) {
            res = str.substring(0);
        } else  {
            res = str.substring(0, length);
        }

        // 改行が含まれていたら、改行以降は除外
        int index = res.indexOf("\n");
        if (index >= 0) {
            res = res.substring(0, index);
        }
        return res;
    }
}
