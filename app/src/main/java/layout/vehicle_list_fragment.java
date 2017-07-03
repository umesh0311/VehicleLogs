package layout;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import net.inov8design.vehicles.MainActivity;
import net.inov8design.vehicles.R;
import net.inov8design.vehicles.Vehiclelogs;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class vehicle_list_fragment extends Fragment {


    public vehicle_list_fragment() {
        // Required empty public constructor
    }
    @Override
    public void onStart(){
        super.onStart();
        Bundle args = getArguments();
        final int vehicleid = args.getInt("vehicle",0);
        ListView lv = (ListView) getActivity().findViewById(R.id.vehiclelist);
        ArrayList<String> lv_arr = new ArrayList<String>();
        Iterator itr = MainActivity.entries.iterator();
        while (itr.hasNext()) {
            Vehiclelogs item = (Vehiclelogs) itr.next();
            if(vehicleid == item.getVehicleid()) {
                lv_arr.add( item.getDriver() +" "+ item.getRego()+" " +item.getStarttime() +" "+ item.getFb()+" "+ item.getSb()+" "+ item.getEndtime());
            }// lv_arr.add(itr.next());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lv_arr);
        lv.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.vehicle_list_fragment, container, false);
        Bundle args = getArguments();
        final int vehicleid = args.getInt("vehicle",0);
        Button btnback = (Button)view.findViewById(R.id.btnback);

        btnback.setText("Back to " + MainActivity.pageNames[vehicleid-1]);
        btnback.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Vehicle_fragment frag;
                Bundle args;
                frag = new Vehicle_fragment();
                args = new Bundle();
                args.putInt("vehicle",vehicleid);
                frag.setArguments(args);

                fragmentTransaction.replace(R.id.carPlace,frag).commit();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

}
