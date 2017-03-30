package com.algonquin.cst2335final;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by marvi on 3/29/2017.
 */

public class ListDetailHouseFragment extends Fragment {
    protected long id;
    protected Context context;
    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        Bundle bundle = getArguments();
        id = bundle.getLong("ID");
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container,Bundle savedInstanceState){
        View gui = inflater.inflate(R.layout.house_fragment_layout,null);
        TextView textView = (TextView)gui.findViewById(R.id.houseFragmentTextView);
        textView.setText("You clicked on ID: " + id);
        return gui;
    }
}
