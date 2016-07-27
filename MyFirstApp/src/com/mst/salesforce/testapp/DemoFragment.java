package com.mst.salesforce.testapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceR;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 */
public class DemoFragment extends Fragment {

    private FrameLayout fragmentContainer;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    //private RestClient client;
    JSONArray records;
    //Toolbar toolBar;

    /**
     * Create a new instance of the fragment
     */
    public static DemoFragment newInstance(int index) {
        DemoFragment fragment = new DemoFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //toolBar= MainActivity.toolbar;
        if (getArguments().getInt("index", 0) == 0) {

            View view = inflater.inflate(R.layout.fragment_demo_list, container, false);
            MainActivity.toolbar.setTitle(R.string.tab_1);
            //getActivity().getActionBar().setTitle(R.string.tab_1);
            try {

                sendRequest("SELECT Id,Name FROM Contact", view);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return view;
        } else if (getArguments().getInt("index", 0) == 1) {

            View view = inflater.inflate(R.layout.fragment_demo_list, container, false);
            MainActivity.toolbar.setTitle(R.string.tab_2);
            //getActivity().getActionBar().setTitle(R.string.tab_2);
            try {
                sendRequest("SELECT Id,Name FROM Account", view);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return view;
        } else if (getArguments().getInt("index", 0) == 2) {
            View view = inflater.inflate(R.layout.fragment_demo_list, container, false);
            MainActivity.toolbar.setTitle(R.string.tab_3);
            try {
                sendRequest("SELECT Id,Name FROM Course__c", view);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return view;
        } else if (getArguments().getInt("index", 0) == 3) {
            View view = inflater.inflate(R.layout.fragment_demo_list, container, false);
            MainActivity.toolbar.setTitle(R.string.tab_4);
            try {
                sendRequest("SELECT Id,Name FROM Course__c", view);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return view;
        } else if (getArguments().getInt("index", 0) == 4) {
            View view = inflater.inflate(R.layout.fragment_demo_settings, container, false);
            MainActivity.toolbar.setTitle(R.string.tab_5);
            initDemoSettings(view);
            return view;
        }
        return null;
    }

    @Override
    public void onResume() {

        if (getArguments().getInt("index", 0) == 0) {


            MainActivity.toolbar.setTitle(R.string.tab_1);

        } else if (getArguments().getInt("index", 0) == 1) {


            MainActivity.toolbar.setTitle(R.string.tab_2);

        } else if (getArguments().getInt("index", 0) == 2) {

            MainActivity.toolbar.setTitle(R.string.tab_3);

        } else if (getArguments().getInt("index", 0) == 3) {

            MainActivity.toolbar.setTitle(R.string.tab_4);

        } else if (getArguments().getInt("index", 0) == 4) {

            MainActivity.toolbar.setTitle(R.string.tab_5);

        }
        super.onResume();
    }

    /**
     * Init demo settings
     */
    private void initDemoSettings(View view) {

        final MainActivity demoActivity = (MainActivity) getActivity();
        final SwitchCompat switchColored = (SwitchCompat) view.findViewById(R.id.fragment_demo_switch_colored);
        final SwitchCompat switchFiveItems = (SwitchCompat) view.findViewById(R.id.fragment_demo_switch_five_items);
        final SwitchCompat showHideBottomNavigation = (SwitchCompat) view.findViewById(R.id.fragment_demo_show_hide);

        final Button btn_logout = (Button) view.findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalesforceSDKManager.getInstance().logout(demoActivity);
            }
        });

        switchColored.setChecked(demoActivity.isBottomNavigationColored());
        switchFiveItems.setChecked(demoActivity.getBottomNavigationNbItems() == 5);

        switchColored.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                demoActivity.updateBottomNavigationColor(isChecked);
            }
        });
        switchFiveItems.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                demoActivity.updateBottomNavigationItems(isChecked);
            }
        });
        showHideBottomNavigation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                demoActivity.showOrHideBottomNavigation(isChecked);
            }
        });
    }

    /**
     * Init the fragment
     */
    private void initDemoList(View view, JSONArray recordss) {

        fragmentContainer = (FrameLayout) view.findViewById(R.id.fragment_container);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_demo_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> itemsData = new ArrayList<>();
    /*	for (int i = 0; i < 50; i++) {
            itemsData.add("Fragment " + getArguments().getInt("index", -1) + " / Item " + i);
		}
*/
        try {
            for (int i = 0; i < recordss.length(); i++) {
                itemsData.add(recordss.getJSONObject(i).getString("Name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DemoAdapter adapter = new DemoAdapter(itemsData);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Refresh
     */
    public void refresh() {
        if (getArguments().getInt("index", 0) > 0 && recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * Called when a fragment will be displayed
     */
    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            fragmentContainer.startAnimation(fadeIn);
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    public void willBeHidden() {
        if (fragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            fragmentContainer.startAnimation(fadeOut);
        }
    }

    private void sendRequest(String soql, final View view) throws UnsupportedEncodingException {
        RestRequest restRequest = RestRequest.getRequestForQuery(getString(R.string.api_version), soql);

        if(MainActivity.client!=null) {
            MainActivity.client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
                @Override
                public void onSuccess(RestRequest request, RestResponse result) {
                    try {
                        //recyclerView.clear();
                        records = null;
                        records = result.asJSONObject().getJSONArray("records");

                        initDemoList(view, records);
				/*	for (int i = 0; i < records.length(); i++) {
						listAdapter.add(records.getJSONObject(i).getString("Name"));
					}*/
                    } catch (Exception e) {
                        onError(e);
                    }
                }

                @Override
                public void onError(Exception exception) {
                    Toast.makeText(getActivity(),
                            getActivity().getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
