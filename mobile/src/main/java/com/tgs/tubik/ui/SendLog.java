package com.tgs.tubik.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tgs.tubik.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SendLog extends BaseActivity implements View.OnClickListener
{
    private static final String TIMESTAMP_FORMAT = "dd-MM-yyyy HH:mm:ss";

    private static final String FILE_LOGCAT_NAME = ".logcat";
    private String mLogcatFile;
    private TextView tvLogContent;
    private Button btnSend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_log);

        tvLogContent = (TextView)findViewById(R.id.tvLogContent);
        btnSend = (Button)findViewById(R.id.btnSend);

        mLogcatFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FILE_LOGCAT_NAME;

        ExtractToLogFileTask task = new ExtractToLogFileTask();
        task.execute();
    }

    private String readLogcatFile() {
        try {
            return readFileToString(mLogcatFile);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String readFileToString(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }


    @Override
    public void onClick (View v) {
        sendLogFile();
    }

    private void sendLogFile () {
        Intent intent = new Intent (Intent.ACTION_SEND_MULTIPLE);
        intent.setType("plain/text");

        ArrayList<Uri> attachList = new ArrayList<>();
        if (new File(mLogcatFile).exists())
            attachList.add(Uri.parse ("file://" + mLogcatFile));

        intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"auumusic@gmail.com"});
        intent.putExtra (Intent.EXTRA_SUBJECT, "Disney Radio crash report " + getCurrentTimeStamp());
        intent.putParcelableArrayListExtra (Intent.EXTRA_STREAM, attachList);
        intent.putExtra (Intent.EXTRA_TEXT, "Log file attached."); // do this so some email clients don't complain about empty body.
        startActivity(intent);
    }

    public static String getCurrentTimeStamp()
    {
        String currentTimeStamp = null;

        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT,
                    java.util.Locale.getDefault());
            currentTimeStamp = dateFormat.format(new Date());
        }
        catch (Exception e)
        {
            Log.e("FileLog", Log.getStackTraceString(e));
        }

        return currentTimeStamp;
    }

    private boolean extractLogToFile() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo (this.getPackageName(), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;

        // Extract to file.
        File file = new File (mLogcatFile);
        BufferedReader reader = null;
        FileWriter writer = null;
        try
        {
            String cmd = "logcat -d -v time com.tgs.tubik:v dalvikvm:v System.err:v *:s";

            // getLinkBy input stream
            Process process = Runtime.getRuntime().exec(cmd);

            // write output stream
            writer = new FileWriter (file);
            writer.write ("Android version: " +  Build.VERSION.SDK_INT + "\n");
            writer.write ("Device: " + model + "\n");
            writer.write ("App version: " + (info == null ? "(null)" : info.versionCode) + "\n");

            StringBuilder text = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader (process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
                writer.append(line).append("\n");
            }

            reader.close();
            writer.close();
        }
        catch (IOException e)
        {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e1) {
                }
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e1) {
                }

            // You might want to write a failure message to the log here.
            return false;
        }

        return new File(mLogcatFile).exists();
    }

    private class ExtractToLogFileTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return (extractLogToFile());
        }

        @Override
        protected void onPostExecute(Boolean value) {
            super.onPostExecute(value);
            if (value) {
                tvLogContent.setText(readLogcatFile());
            } else {
                tvLogContent.setText("Cannot create logcat");
            }
        }
    }
}
