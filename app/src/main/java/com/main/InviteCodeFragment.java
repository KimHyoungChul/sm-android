package com.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.util.SingleInstance;

import java.util.UUID;

public class InviteCodeFragment extends Fragment {
    private static InviteCodeFragment inviteCodeFragment;
    private static Context mainContext;
    public View view;
    LinearLayout content;
    AppMainActivity appMainActivity;
    public static InviteCodeFragment newInstance(Context context) {
        try {
            if (inviteCodeFragment == null) {
                mainContext = context;
                inviteCodeFragment = new InviteCodeFragment();
            }

            return inviteCodeFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return inviteCodeFragment;
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            Button select = (Button) getActivity().findViewById(R.id.btn_brg);
            select.setVisibility(View.GONE);
            final RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
            mainHeader.setVisibility(View.VISIBLE);
            LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
            contact_layout.setVisibility(View.GONE);

            Button imVw = (Button) getActivity().findViewById(R.id.im_view);
            imVw.setVisibility(View.GONE);

            final Button search1 = (Button) getActivity().findViewById(R.id.btn_settings);
            search1.setVisibility(View.GONE);

            final Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
            plusBtn.setVisibility(View.GONE);
            plusBtn.setText("");
            plusBtn.setBackgroundResource(R.drawable.navigation_check);

            TextView title = (TextView) getActivity().findViewById(
                    R.id.activity_main_content_title);
            title.setVisibility(View.VISIBLE);
            title.setText("INVITE");
            appMainActivity = (AppMainActivity) SingleInstance.contextTable
                    .get("MAIN");
            Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appMainActivity.removeFragments(InviteCodeFragment.newInstance(SingleInstance.mainContext));
                    SettingsFragment settingsFragment = SettingsFragment.newInstance(mainContext);
                    FragmentManager fragmentManager = SingleInstance.mainContext
                            .getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(
                            R.id.activity_main_content_fragment, settingsFragment)
                            .commitAllowingStateLoss();
                }
            });
            view = null;
            if (view == null) {
                view = inflater.inflate(R.layout.main_endorse, null);
                try {
                    content = (LinearLayout)view. findViewById(R.id.content);
                    content.removeAllViews();
                    LayoutInflater layoutInflater = (LayoutInflater)mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v1 = layoutInflater.inflate(R.layout.invite, content);
                    Button generate=(Button)v1.findViewById(R.id.generate);
                    generate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            content.removeAllViews();
                            LayoutInflater layoutInflater = (LayoutInflater) mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View v1 = layoutInflater.inflate(R.layout.invite2, content);
                            final TextView invitecode = (TextView) v1.findViewById(R.id.invitecode);
                            Button regenerate = (Button) v1.findViewById(R.id.regenerate);
                            String code= UUID.randomUUID().toString().trim().substring(0,8).toUpperCase();
                            invitecode.setText(code);
                            regenerate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String code = UUID.randomUUID().toString().trim().substring(0, 8).toUpperCase();
                                    invitecode.setText(code);
                                }
                            });
                        }
                    });

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return view;
    }
}