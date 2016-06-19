package mx.com.cubozsoft.sunshineapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfWeather.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListOfWeather#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOfWeather extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListOfWeather() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListOfWeather.
     */
    // TODO: Rename and change types and number of parameters
    public static ListOfWeather newInstance(String param1, String param2) {
        ListOfWeather fragment = new ListOfWeather();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.fragment_list_of_weather, container, false);


        List<ForecastItem> dataList = new ArrayList<>();
        dataList.add(new ForecastItem("Today - Sunny - 25/35",R.drawable.sunny));
        dataList.add(new ForecastItem("Tomorrow - Sunny - 25/35",R.drawable.sunny));
        dataList.add(new ForecastItem("Tuesday - Windy - 20/25",R.drawable.windy));
        dataList.add(new ForecastItem("Wednesday - Rainy - 15/25",R.drawable.rainy));
        dataList.add(new ForecastItem("Thursday - Rainy - 15/25",R.drawable.rainy));
        dataList.add(new ForecastItem("friday - Clowdy - 25/35",R.drawable.cludy));
        dataList.add(new ForecastItem("Today - Sunny - 40/45",R.drawable.sunny));

        mManager = new LinearLayoutManager(getContext());
        mAdapter = new ForecastAdapter(dataList);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_forecast);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
