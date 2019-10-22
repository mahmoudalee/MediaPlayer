package com.dell.mediaplayer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
   public static String  getCurrentDateTime(){
       Date d = new Date();
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
       return simpleDateFormat.format(d);
   }

}
