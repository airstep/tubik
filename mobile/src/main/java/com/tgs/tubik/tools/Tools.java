package com.tgs.tubik.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.tgs.tubik.tools.logger.Log;

import java.util.Locale;

public class Tools {
    private final static String TAG = "Tools";
    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     *
     * @param context Context reference to getLinkBy the TelephonyManager instance from
     * @return country code or null
     */
    public static String getCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toUpperCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getLinkBy country", e);
        }
        return context.getResources().getConfiguration().locale.getCountry();
    }

    public static String getEnglishCountryName(String code) {
        return new Locale(Locale.ENGLISH.getLanguage(), code).getDisplayCountry(Locale.ENGLISH).toLowerCase();
    }

    public static boolean isOnline(Context ctx) {
        if (ctx == null) return false;
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}