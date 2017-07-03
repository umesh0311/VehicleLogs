package layout;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.inov8design.vehicles.MainActivity;
import net.inov8design.vehicles.R;
import net.inov8design.vehicles.TrackGPS;
import net.inov8design.vehicles.Vehiclelogs;

import java.lang.reflect.Array;
import java.util.Calendar;
// import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Vehicle_fragment extends Fragment {

    private Button b_get;
    private TrackGPS gps;
    double longitude;
    double latitude;

    public Vehicle_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();
        final Button btnSavelog = (Button) getActivity().findViewById(R.id.savelog);
        final Button btnShowlog = (Button) getActivity().findViewById(R.id.showlog);
        final Button btnStarttime = (Button)getActivity().findViewById(R.id.btnstarttime);
        final Button btnFbtime = (Button)getActivity().findViewById(R.id.btnfbtime);
        final Button btnSbtime = (Button)getActivity().findViewById(R.id.btnsbtime);
        final Button btnEndtime = (Button)getActivity().findViewById(R.id.btnendtime);
        final TextView txtStarttime = (TextView)getActivity().findViewById(R.id.starttext);
        final TextView txtFbtime = (TextView)getActivity().findViewById(R.id.fbtext);
        final TextView txtSbtime = (TextView)getActivity().findViewById(R.id.sbtext);
        final TextView txtEndtime = (TextView)getActivity().findViewById(R.id.ettext);
        final EditText driver = (EditText)getActivity().findViewById(R.id.driver);
        final EditText rego = (EditText)getActivity().findViewById(R.id.rego);


  //Save Log Button
        btnSavelog.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {

                if (!MainActivity.isEmpty(txtStarttime) || !MainActivity.isEmpty(txtFbtime) || !MainActivity.isEmpty(txtSbtime) || !MainActivity.isEmpty(txtEndtime)){
                    int vehicleid = getArguments().getInt("vehicle");
                    //Toast.makeText(getActivity(),driver.getText().toString(),Toast.LENGTH_SHORT).show();
                    Vehiclelogs vehicleobj = new Vehiclelogs(vehicleid,driver.getText().toString(),rego.getText().toString(),txtStarttime.getText().toString(),txtFbtime.getText().toString(),txtSbtime.getText().toString(),txtEndtime.getText().toString());
                    MainActivity.entries.add(vehicleobj);
                    btnStarttime.setVisibility(View.VISIBLE);
                    btnFbtime.setVisibility(View.INVISIBLE);
                    btnSbtime.setVisibility(View.INVISIBLE);
                    btnEndtime.setVisibility(View.INVISIBLE);
                    btnStarttime.setEnabled(true);
                    btnFbtime.setEnabled(true);
                    btnSbtime.setEnabled(true);
                    btnEndtime.setEnabled(true);
                    txtStarttime.setText("");
                    txtFbtime.setText("");
                    txtSbtime.setText("");
                    txtEndtime.setText("");
                    driver.setText("");
                    rego.setText("");


                }else{
                    Toast.makeText(getActivity(),"One of the times has not been set",Toast.LENGTH_SHORT).show();
                }

            }

        });
//Show Log Button
        btnShowlog.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                FragmentManager fragmentmanager = getFragmentManager();
                FragmentTransaction ft = fragmentmanager.beginTransaction();
                vehicle_list_fragment vlf = new vehicle_list_fragment();
                Bundle args = new Bundle();
                args.putInt("vehicle",getArguments().getInt("vehicle"));
                vlf.setArguments(args);
                ft.replace(R.id.carPlace, vlf);
                ft.commit();
            }

        });

//Start Time Button
        btnStarttime.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                gps = new TrackGPS(getActivity());
                if(gps.canGetLocation()){


                    longitude = gps.getLongitude();
                    latitude = gps .getLatitude();

                    txtStarttime.setText(btnStarttime.getText().toString() + ":"+currentdate()+ " Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude));
                    btnFbtime.setVisibility(View.VISIBLE);
                    btnStarttime.setVisibility(View.INVISIBLE);
                }
                else
                {

                    gps.showSettingsAlert();
                }

            }

        });

//First Break Button
        btnFbtime.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {

                gps = new TrackGPS(getActivity());
                if(gps.canGetLocation()){


                    longitude = gps.getLongitude();
                    latitude = gps .getLatitude();

                    txtFbtime.setText(btnFbtime.getText().toString() + ":"+currentdate() + " Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude));
                    btnSbtime.setVisibility(View.VISIBLE);
                    btnFbtime.setVisibility(View.INVISIBLE);
                }
                else
                {

                    gps.showSettingsAlert();
                }
            }

        });

//Second Break Button
        btnSbtime.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {



                gps = new TrackGPS(getActivity());
                if(gps.canGetLocation()){


                    longitude = gps.getLongitude();
                    latitude = gps .getLatitude();

                    txtSbtime.setText(btnSbtime.getText().toString() + ":"+currentdate()+" Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude));
                    btnEndtime.setVisibility(View.VISIBLE);
                    btnSbtime.setVisibility(View.INVISIBLE);
                }
                else
                {

                    gps.showSettingsAlert();
                }
            }

        });

//End Time Button
        btnEndtime.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {


                gps = new TrackGPS(getActivity());
                if(gps.canGetLocation()){


                    longitude = gps.getLongitude();
                    latitude = gps .getLatitude();

                    txtEndtime.setText(btnEndtime.getText().toString() + ":"+currentdate()+"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude));
                    btnEndtime.setVisibility(View.INVISIBLE);
                }
                else
                {

                    gps.showSettingsAlert();
                }
            }

        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.vehicle_fragment, container, false);
        Bundle args = getArguments();
        int vehicle = args.getInt("vehicle",0);
        TextView tv1 = (TextView)view.findViewById(R.id.txttitle);
        tv1.setText(MainActivity.pageNames[vehicle-1]);
        return view;
    }

    private String currentdate (){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH) + 1;
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);

        String currenttime = day + ":" +month + ":" + year + " " + hour + ":" + minute + ":"+ seconds;
        return currenttime;
    }



}
