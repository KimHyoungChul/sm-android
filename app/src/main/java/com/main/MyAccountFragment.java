package com.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.cg.account.MyAccountActivity;
import com.cg.account.TermsAndAgreement;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.util.SingleInstance;

import org.lib.model.FileDetailsBean;
import org.lib.model.KeepAliveBean;
import org.lib.model.WebServiceBean;
import org.lib.webservice.Servicebean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MyAccountFragment extends Fragment {
    private static MyAccountFragment myAccountFragment;
    private static Context mainContext;
    public View view;
    ImageView profile_pic,status_icon;
    RelativeLayout statuslay2;
    TextView statusTxt1;
    TextView nicName,profession,offcAddr,hospital,citations,association;
    TextView title,fName, lName,sex,usertype,state,prof, speciality,medical,residency,fellowship;
    CheckBox tos;
    CheckBox baa;
    String strIPath;
    private Context context;

    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    private AlertDialog alert = null;
    private static CallDispatcher calldisp = null;
    private AppMainActivity appMainActivity;
    private ImageLoader imageLoader;
    private ProfileBean pBean=null;
    String status;

    public static MyAccountFragment newInstance(Context context) {
        try {
            if (myAccountFragment == null) {
                mainContext = context;
                myAccountFragment = new MyAccountFragment();
                myAccountFragment.setContext(context);

            }

            return myAccountFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return myAccountFragment;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            Button select = (Button) getActivity().findViewById(R.id.btn_brg);
            select.setVisibility(View.GONE);
            TextView title = (TextView) getActivity().findViewById(
                    R.id.activity_main_content_title);
            title.setText("MY ACCOUNT");
            title.setVisibility(View.VISIBLE);

            Button imVw = (Button) getActivity().findViewById(R.id.im_view);
            imVw.setVisibility(View.GONE);

            if (CallDispatcher.LoginUser == null) {
                statusTxt1.setText("");
            }
            else
            {
                loadCurrentStatus();
            }
            Button edit = (Button) getActivity().findViewById(
                    R.id.btn_settings);
            edit.setVisibility(View.VISIBLE);
            edit.setBackgroundResource(R.drawable.navigation_edit);
            edit.setText("");
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity()
                            .getApplicationContext(), MyAccountActivity.class);
                    intent.putExtra("status",status);
                    getActivity().startActivity(intent);

                }
            });
            RelativeLayout mainHeader = (RelativeLayout) getActivity().findViewById(R.id.mainheader);
            mainHeader.setVisibility(View.VISIBLE);
            LinearLayout contact_layout = (LinearLayout) getActivity()
                    .findViewById(R.id.contact_layout);
            contact_layout.setVisibility(View.GONE);
            Button plusBtn = (Button) getActivity()
                    .findViewById(R.id.add_group);
            plusBtn.setVisibility(View.VISIBLE);
            plusBtn.setText("");
            plusBtn.setBackgroundResource(R.drawable.dot);
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        final Dialog dialog = new Dialog(mainContext);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_myacc_menu);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.horizontalMargin = 15;
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        window.setAttributes(lp);
                        window.setGravity(Gravity.BOTTOM);
                        dialog.show();
                        TextView delete_acc = (TextView) dialog.findViewById(R.id.delete_acc);
                        TextView log_out = (TextView) dialog.findViewById(R.id.log_out);
                        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {

                                try {
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        delete_acc.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {

                                try {
                                    dialog.dismiss();
                                    WebServiceReferences.webServiceClient.ResetAccount(CallDispatcher.LoginUser, myAccountFragment);
                                    showDialog();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        log_out.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {

                                try {
                                    dialog.dismiss();
                                    appMainActivity.logout(true);
                                    appMainActivity.showprogress();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
            backBtn.setVisibility(View.GONE);
            imageLoader = new ImageLoader(mainContext);
            view = null;
            if (view == null) {
                view = inflater.inflate(R.layout.my_account, null);
                try {
                    TextView tos=(TextView)view.findViewById(R.id.tos);
                    TextView baa=(TextView)view.findViewById(R.id.baa);
                    appMainActivity= SingleInstance.mainContext;
                    imageLoader = new ImageLoader(context);
                    tos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(mainContext, TermsAndAgreement.class);
                            intent.putExtra("myaccount", true);
                            startActivity(intent);
                        }
                    });
                    baa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(mainContext, TermsAndAgreement.class);
                            intent.putExtra("myaccount", true);
                            startActivity(intent);
                        }
                    });

                    init();
                    if (CallDispatcher.LoginUser == null) {
                        statusTxt1.setText("");
                    }
                    else
                    {
                        loadCurrentStatus();
                    }
//                    statuslay2.setOnTouchListener(new View.OnTouchListener() {
//
//                        @Override
//                        public boolean onTouch(View v, MotionEvent event) {
//                            // TODO Auto-generated method stub
//                            if (CallDispatcher.LoginUser != null) {
//                                ShowView(v);
//                            }
//
//                            return false;
//                        }
//                    });

                    if(SingleInstance.myAccountBean.getUsername()==null)
                    showDialog();
                    loadFields();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    public void init(){
        profile_pic = (ImageView)view. findViewById(R.id.riv1);
        status_icon = (ImageView)view. findViewById(R.id.dot);
        statuslay2 = (RelativeLayout)view. findViewById(R.id.statuslay2);
        statusTxt1 = (TextView) view.findViewById(R.id.statusTxt1);
        nicName = (TextView) view.findViewById(R.id.nickname);
        title = (TextView) view.findViewById(R.id.title);
        fName = (TextView) view.findViewById(R.id.fName);
        lName = (TextView)view. findViewById(R.id.lName);
        profession = (TextView)view. findViewById(R.id.profession);
        sex = (TextView)view. findViewById(R.id.sex);
        usertype = (TextView)view. findViewById(R.id.usertype);
        state = (TextView)view. findViewById(R.id.state);
        prof = (TextView)view. findViewById(R.id.prof_desgn);
        speciality = (TextView)view. findViewById(R.id.speciality);
        medical = (TextView)view. findViewById(R.id.medical);
        residency = (TextView)view. findViewById(R.id.residency);
        fellowship = (TextView)view. findViewById(R.id.fellowship);
        offcAddr = (TextView)view. findViewById(R.id.offcAddress);
        hospital = (TextView)view. findViewById(R.id.hospital);
        association = (TextView)view. findViewById(R.id.association);
        citations = (TextView)view. findViewById(R.id.citations);
    }
    private String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }
    private void showDialog() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    progressDialog = new ProgressDialog(mainContext);
                    if (progressDialog != null) {
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Progress ...");
                        progressDialog
                                .setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setProgress(0);
                        progressDialog.setMax(100);
                        progressDialog.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }

    public String getFileName() {
        String strFilename = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = new Date();
            strFilename = dateFormat.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strFilename;
    }

    public void loadFields()
    {
        try {
            ProfileBean bean=SingleInstance.myAccountBean;
            if(bean.getUsername()!=null) {
                if(bean.getTitle()!=null && bean.getFirstname()!=null)
                     nicName.setText(bean.getTitle() + "" + bean.getFirstname());
                else
                    nicName.setText( bean.getFirstname());
                cancelDialog();
            }
            if(bean.getFirstname()!=null)
                fName.setText(bean.getFirstname());
            if(bean.getLastname()!=null)
                lName.setText(bean.getLastname());
            if(bean.getOfficeaddress()!=null)
                offcAddr.setText(bean.getOfficeaddress());
            if(bean.getPhoto()!=null){
                String profilePic=bean.getPhoto();
                Log.i("AAAA", "MYACCOUNT "+profilePic);
                if (profilePic != null && profilePic.length() > 0) {
                    if (!profilePic.contains("COMMedia")) {
                        profilePic = Environment
                                .getExternalStorageDirectory()
                                + "/COMMedia/" + profilePic;
                        strIPath = profilePic;
                    }
                    Log.i("AAAA","MYACCOUNT "+profilePic);
                    imageLoader.DisplayImage(profilePic, profile_pic,
                            R.drawable.user_photo);
                }
            }
            if(bean.getTitle()!=null)
                title.setText(bean.getTitle());

            if(bean.getSex() != null)
                sex.setText(String.valueOf(bean.getSex().charAt(0)));

            if(bean.getUsertype()!=null){
                usertype.setText(bean.getUsertype());
            }
            if(bean.getState()!=null){
                state.setText(bean.getState());
            }
            if(bean.getProfession()!=null){
                prof.setText(bean.getProfession());
            }
            if(bean.getSpeciality()!=null){
                speciality.setText(bean.getSpeciality());
            }
            if(bean.getMedicalschool()!=null){
                medical.setText(bean.getMedicalschool());
            }
            if(bean.getResidencyprogram()!=null){
                residency.setText(bean.getResidencyprogram());
            }
            if (bean.getFellowshipprogram()!=null){
                fellowship.setText(bean.getFellowshipprogram());
            }
            if(bean.getHospitalaffiliation()!=null){
                hospital.setText(bean.getHospitalaffiliation());
            }
            if(bean.getOrganizationmembership()!=null){
                association.setText(bean.getOrganizationmembership());
            }
            if(bean.getCitationpublications()!=null)
                citations.setText(bean.getCitationpublications());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            File new_file = new File(strIPath);
            if(new_file.exists()) {
                ImageLoader imageLoader;
                imageLoader = new ImageLoader(mainContext);
                imageLoader.DisplayImage(strIPath, profile_pic, R.drawable.userphoto);
                String[] param=new String[7];
                param[0]=CallDispatcher.LoginUser;
                param[1]=CallDispatcher.Password;
                param[2]="image";
                File file=new File(strIPath);
                param[3]=file.getName();
                long length = (int) file.length();
                length = length/1024;
                param[5]="other";
                param[6]= String.valueOf(length);
                if(file.exists()) {
                    param[4] = encodeTobase64(BitmapFactory.decodeFile(file.getPath()));
                    WebServiceReferences.webServiceClient.FileUpload(param,mainContext);
                    FileDetailsBean fBean=new FileDetailsBean();
                    fBean.setFilename(param[3]);
                    fBean.setFiletype("image");
                    fBean.setFilecontent(param[4]);
                    fBean.setServicetype("Upload");
                    SingleInstance.fileDetailsBean=fBean;
                }
            }
        }
    }
    protected void ShowView(final View v) {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
        builder.create();

        builder.setTitle(SingleInstance.mainContext.getResources().getString(
                R.string.change_status));
        final CharSequence[] choiceList = {
                SingleInstance.mainContext.getResources().getString(
                        R.string.online),
                SingleInstance.mainContext.getResources().getString(
                        R.string.busy),
                SingleInstance.mainContext.getResources().getString(
                        R.string.away),
                SingleInstance.mainContext.getResources().getString(
                        R.string.stealth) };

        builder.setItems(choiceList, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                changeFieldType(choiceList[which].toString(), v);
                alert.cancel();
            }
        });
        alert = builder.create();
        alert.show();
    }

    private void changeFieldType(String type, View v) {

        statusTxt1.setText(type);

        if (CallDispatcher.isConnected) {

            if (type.equals(SingleInstance.mainContext.getResources()
                    .getString(R.string.online))) {

                statusTxt1.setText(type);
                CallDispatcher.myStatus = "1";
                Toast.makeText(
                        mainContext,
                        SingleInstance.mainContext
                                .getString(R.string.receive_all_services), Toast.LENGTH_SHORT)
                        .show();

            } else if (type.equals(SingleInstance.mainContext.getResources()
                    .getString(R.string.away))) {
                statusTxt1.setText(type);
                CallDispatcher.myStatus = "3";
                Toast.makeText(
                        mainContext,
                        SingleInstance.mainContext
                                .getString(R.string.not_receive_call_services),
                        Toast.LENGTH_SHORT).show();

            } else if (type.equals(SingleInstance.mainContext.getResources()
                    .getString(R.string.stealth))) {
                CallDispatcher.myStatus = "4";
                Toast.makeText(
                        mainContext,
                        SingleInstance.mainContext
                                .getString(R.string.not_receive_call_msg_services),
                        Toast.LENGTH_SHORT).show();

            } else if (type.equals(SingleInstance.mainContext.getResources()
                    .getString(R.string.offline))) {

                statusTxt1.setText(type);
                CallDispatcher.myStatus = "0";
                Toast.makeText(
                        mainContext,
                        SingleInstance.mainContext
                                .getString(R.string.not_receive_call_msg_services),
                        Toast.LENGTH_SHORT).show();
            } else if (type.equals(SingleInstance.mainContext.getResources()
                    .getString(R.string.busy))) {
                CallDispatcher.myStatus = "2";
                Toast.makeText(
                        mainContext,
                        SingleInstance.mainContext
                                .getString(R.string.receive_all_services), Toast.LENGTH_SHORT)
                        .show();
            }
            if (calldisp == null) {
                calldisp = new CallDispatcher(mainContext);
            }
            KeepAliveBean aliveBean = calldisp.getKeepAliveBean();
            aliveBean.setKey("0");

            if (!WebServiceReferences.running) {
                calldisp.startWebService(
                        getResources().getString(R.string.service_url), "80");
            }
            WebServiceReferences.webServiceClient.heartBeat(aliveBean);
        } else {

            statusTxt1.setText(SingleInstance.mainContext.getResources()
                    .getString(R.string.offline));
            CallDispatcher.myStatus = "0";

            Toast.makeText(
                    context,
                    SingleInstance.mainContext
                            .getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }

        appMainActivity.chageMyStatus();

    }
    public void loadCurrentStatus() {
        if (statusTxt1 != null) {
            Log.d("ABCD", "Inside LoadCurrentChanges");
            if (CallDispatcher.myStatus.equals("1")) {
                Log.d("ABCD", "myStatus online");
                statusTxt1.setText(SingleInstance.mainContext.getResources()
                        .getString(R.string.online));
                status="Online";
                status_icon.setBackgroundResource(R.drawable.online_icon);
            } else if (CallDispatcher.myStatus.equals("3")) {
                Log.d("ABCD", "myStatus away");
                status="Invisible";
                statusTxt1.setText("Invisible");
                status_icon.setBackgroundResource(R.drawable.invisibleicon);
            } else if (CallDispatcher.myStatus.equals("4")) {
                Log.d("ABCD", "myStatus stealth");
                status="Offline";
                statusTxt1.setText(SingleInstance.mainContext.getResources()
                        .getString(R.string.offline));
                status_icon.setBackgroundResource(R.drawable.offline_icon);
            } else if (CallDispatcher.myStatus.equals("0")) {
                Log.d("ABCD", "myStatus offline");
                status="Offline";
                statusTxt1.setText(SingleInstance.mainContext.getResources()
                        .getString(R.string.offline));
                status_icon.setBackgroundResource(R.drawable.offline_icon);
            } else if (CallDispatcher.myStatus.equals("2")) {
                Log.d("ABCD", "myStatus busy");
                status="Busy";
                statusTxt1.setText(SingleInstance.mainContext.getResources()
                        .getString(R.string.busy));
                status_icon.setBackgroundResource(R.drawable.busy_icon);
            }


        }
    }
    public void notifyDeleteMyaccount(Servicebean servicebean)
    {
        Log.i("AAAA","MY ACCOUNT RESET ACCOUNT");
        cancelDialog();
        final WebServiceBean sbean = (WebServiceBean) servicebean.getObj();
        handler.post(new Runnable() {

            @Override
            public void run() {
                String Response = sbean.getText();
                Log.i("AAAA","MY ACCOUNT DELETE"+Response);
                try {
                    if (Response.contains("successfully")) {
                        Log.i("AAAA","MY ACCOUNT DELETE");
                        SingleInstance.myAccountBean=null;
                        strIPath = null;
                        ImageLoader imageLoader;
                        imageLoader = new ImageLoader(mainContext);
                        imageLoader.DisplayImage(strIPath, profile_pic, R.drawable.userphoto);
                        title.setText("");
                        fName.setText("");
                        lName.setText("");
                        sex.setText("");
                        usertype.setText("");
                        state.setText("");
                        prof.setText("");
                        speciality.setText("");
                        medical.setText("");
                        residency.setText("");
                        fellowship.setText("");
                        offcAddr.setText("");
                        hospital.setText("");
                        citations.setText("");
                        tos.setChecked(false);
                        baa.setChecked(false);
                        Toast.makeText(context, Response, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, Response, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
    }
    public void cancelDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public View getParentView() {
        return view;
    }
    public void setContext(Context cxt) {
        this.mainContext = cxt;
    }
}