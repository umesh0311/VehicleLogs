package net.inov8design.vehicles;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.net.Uri;

import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Delayed;

import layout.Home_fragment;
import layout.Vehicle_fragment;
import layout.profile_fragment;
import layout.vehicle_list_fragment;
import net.inov8design.vehicles.Vehiclelogs;






public class MainActivity extends Activity {
    int currentPage = 0;
    public static String pageNames[] = {"Car", "5T Truck", "10T Truck", "Tipper", "Articulated"};
    public static ArrayList<Vehiclelogs> entries = new ArrayList<Vehiclelogs>();
    ProgressDialog progress;
    String data = "";

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentmanager = getFragmentManager();
        FragmentTransaction ft = fragmentmanager.beginTransaction();
        Home_fragment hf = new Home_fragment();
        ft.replace(R.id.carPlace, hf);
        ft.commit();

        setContentView(R.layout.activity_main);

        DBAdapter db = new DBAdapter(this);
        try {

            String destPath = "/data/data/" + getPackageName() +
                    "/databases";

            File f = new File(destPath);
            if (!f.exists()) {
                f.mkdirs();
                f.createNewFile();

                //---copy the db from the assets folder into
                // the databases folder---
                CopyDB(getBaseContext().getAssets().open("vehiclelog"),
                        new FileOutputStream(destPath + "/Vehiclelog"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //---get all contacts---
        db.open();
        // int itemcount = db.getItemcount();

        Cursor c = db.getAlllogs();
        if (c.moveToFirst()) {
            do {
                Vehiclelogs newvehicle = new Vehiclelogs(c.getInt(c.getColumnIndex("vehicletype")), c.getString(c.getColumnIndex("driver")), c.getString(c.getColumnIndex("rego")), c.getString(c.getColumnIndex("starttime")), c.getString(c.getColumnIndex("firstbreak")), c.getString(c.getColumnIndex("secondbreak")), c.getString(c.getColumnIndex("endtime")));
                MainActivity.entries.add(newvehicle);
                //DisplayContact(c);
            } while (c.moveToNext());
        }
        db.close();
        progress = new ProgressDialog(MainActivity.this);
    }

    public void CopyDB(InputStream inputStream,
                       OutputStream outputStream) throws IOException {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onRestorInstanceState(Bundle savedInstanceSate) {
        super.onRestoreInstanceState(savedInstanceSate);
    }

    public void onClick(View view) {
        currentPage = Integer.valueOf((String) view.getTag());
        showCurrentPage();
    }

    public void showCurrentPage() {

        //Toast.makeText(MainActivity.this,"Current Page:"+currentPage ,Toast.LENGTH_SHORT).show();
        if (currentPage == 0) {
            FragmentManager fragmentmanager = getFragmentManager();
            FragmentTransaction ft = fragmentmanager.beginTransaction();
            Home_fragment hf = new Home_fragment();
            ft.replace(R.id.carPlace, hf);
            ft.commit();
        } else {
            FragmentManager fragmentmanager = getFragmentManager();
            Vehicle_fragment vf = new Vehicle_fragment();
            Bundle args = new Bundle();
            args.putInt("vehicle", currentPage);
            vf.setArguments(args);
            FragmentTransaction ft = fragmentmanager.beginTransaction();
            ft.replace(R.id.carPlace, vf);
            ft.commit();
        }

    }

    public void prev(View view) {
        currentPage = currentPage - 1;
        if (currentPage <= 0) {
            currentPage = 5;
        }

        showCurrentPage();
    }

    public void next(View view) {
        currentPage = currentPage + 1;
        if (currentPage > 5) {
            currentPage = 1;
        }

        showCurrentPage();
    }

    public void home(View view) {
        currentPage = 0;


        showCurrentPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.themenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return MenuChoice(item);
    }

    private Boolean MenuChoice(MenuItem item) {
        final DBAdapter db = new DBAdapter(this);
        switch (item.getItemId()) {
            case R.id.profile:
                FragmentManager fragmentmanager = getFragmentManager();
                FragmentTransaction ft = fragmentmanager.beginTransaction();
                profile_fragment pf = new profile_fragment();
                ft.replace(R.id.carPlace, pf);
                ft.commit();
                return true;
            case R.id.deletedb:
                db.open();
                db.deletealllogs();
                db.close();
                Toast.makeText(MainActivity.this, "Database has been deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.send:


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);


                alertDialog.setTitle("Email the Logs");

                alertDialog.setMessage("Do you wants to send it?");


                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        progress.setTitle("Logs Processing");
                        progress.setMessage("Getting the Logs Please wait....");
                        progress.show();


                        new EmailBackground().execute();

                    }
                });


                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();

                return true;


            case R.id.save:

                db.open();
                db.deletealllogs();
                Iterator itr = (Iterator) MainActivity.entries.iterator();
                //Iterator over array and save each entry to the database.
                while (itr.hasNext()) {
                    Vehiclelogs vehicleitem = (Vehiclelogs) itr.next();
                    db.insertlog(vehicleitem.getDriver(), vehicleitem.getRego(), vehicleitem.getStarttime(), vehicleitem.getFb(), vehicleitem.getSb(), vehicleitem.getEndtime(), vehicleitem.getVehicleid());
                }
                Toast.makeText(MainActivity.this, "Data Has Been Saved", Toast.LENGTH_SHORT).show();
                int itemcount = db.getItemcount();
                Toast.makeText(MainActivity.this, itemcount + " items in the database", Toast.LENGTH_SHORT).show();
                db.close();
                return true;
        }
        return true;
    }

    public static boolean isEmpty(TextView txtview) {

        return txtview.getText().toString().trim().length() == 0;
    }

    public void sendEmail( String message) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        String[] to = {"prajapatiumesh03@gmail.com"};
        String[] cc = {"prajapatibadal3@gmail.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Vehicles Logs");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email"));
        Toast.makeText(MainActivity.this, "Sent All Data", Toast.LENGTH_SHORT).show();
    }

    private class EmailBackground extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... urls) {

            DBAdapter db = new DBAdapter(MainActivity.this);
            db.open();
            final Iterator itrr = (Iterator) MainActivity.entries.iterator();
            data = "";
            while (itrr.hasNext()) {

                Vehiclelogs vehicleitem = (Vehiclelogs) itrr.next();
                data = data.concat(vehicleitem.toString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            db.close();
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            sendEmail(result);
        }
    }
}

/*Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent.setType("plain/text");
            emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"someone@gmail.com"});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Yo");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi");
            startActivity(emailIntent);*/
