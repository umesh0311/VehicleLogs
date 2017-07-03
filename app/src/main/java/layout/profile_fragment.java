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
import android.widget.Toast;

import net.inov8design.vehicles.MainActivity;
import net.inov8design.vehicles.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class profile_fragment extends Fragment {


    public profile_fragment() {
        // Required empty public constructor
    }

@Override
    public void onStart(){
        super.onStart();
    Button btnsave = (Button) getActivity().findViewById(R.id.saveprofile);
    btnsave.setOnClickListener(new View.OnClickListener(){
        public void onClick(View v){

            EditText username = (EditText) getActivity().findViewById(R.id.username);
            EditText pwd1 = (EditText) getActivity().findViewById(R.id.pwd1);
            EditText pwd2 = (EditText) getActivity().findViewById(R.id.pwd2);
            if (MainActivity.isEmpty(username)){
                Toast.makeText(getActivity(),"Username is empty",Toast.LENGTH_SHORT).show();
            }else{
                if (pwd1.getText().toString().equals(pwd2.getText().toString())) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Home_fragment hf;
                    hf = new Home_fragment();
                    fragmentTransaction.replace(R.id.carPlace, hf).commit();
                }else {
                    Toast.makeText(getActivity(),"Passwords do not match",Toast.LENGTH_SHORT).show();
                }
            }


        }
    });
    Button btncancel = (Button) getActivity().findViewById(R.id.cancelprofile);
    btncancel.setOnClickListener(new View.OnClickListener(){
        public void onClick(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Home_fragment hf;
            hf = new Home_fragment();
            fragmentTransaction.replace(R.id.carPlace, hf).commit();
        }
        });
}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

}
