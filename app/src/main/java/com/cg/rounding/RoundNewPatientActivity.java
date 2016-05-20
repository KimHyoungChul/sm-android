package com.cg.rounding;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.group.AddGroupMembers;
import com.group.BuddyAdapter;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.PatientDetailsBean;
import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class RoundNewPatientActivity extends Activity {

    private Context context;
    public Vector<UserBean> membersList = new Vector<UserBean>();
    private String groupid;
    TextView membercount;
    private String addedMembers[];
    final Calendar myCalendar = Calendar.getInstance();
    private LinearLayout lv_buddylist;
    MembersAdapter adapter;
    RadioGroup gender_patient;
    RadioButton genderSelected;
    PatientDetailsBean pBean=new PatientDetailsBean();

    Handler handler = new Handler();
    private ProgressDialog progress = null;
    Vector<PatientDetailsBean> PatientList;
    PatientDetailsBean choosepBean=new PatientDetailsBean();
    Boolean isClicked=false;
    RoundingPatientAdapter patientadapter;


    private TextView ed_dob=null;
    private TextView ed_Admitdate=null;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.createorexistingpatient);
        context = this;
        Button back=(Button)findViewById(R.id.cancel);
        Button save=(Button)findViewById(R.id.save_new_patient);
        Button assibnBtn=(Button)findViewById(R.id.assibnBtn);
        final AutoCompleteTextView hospital=(AutoCompleteTextView)findViewById(R.id.hospital);
        LinearLayout members=(LinearLayout)findViewById(R.id.membersList);
        final TextView firstname=(TextView)findViewById(R.id.firstname);
        final TextView middlename=(TextView)findViewById(R.id.middlename);
        final TextView lastname=(TextView)findViewById(R.id.lastname);
        final TextView dob=(TextView)findViewById(R.id.dob);
        final TextView mrn=(TextView)findViewById(R.id.mrn);
        final TextView floor=(TextView)findViewById(R.id.floor);
        final TextView ward=(TextView)findViewById(R.id.ward);
        final TextView room=(TextView)findViewById(R.id.room);
        final TextView bed=(TextView)findViewById(R.id.bed);
        final TextView date=(TextView)findViewById(R.id.Admit_date);
        membercount=(TextView)findViewById(R.id.members_count);
        lv_buddylist = (LinearLayout) findViewById(R.id.membersList);
        gender_patient = (RadioGroup) findViewById(R.id.gender_patient);
        groupid=getIntent().getStringExtra("groupid");

        final EditText ed_firstname=(EditText)findViewById(R.id.ed_firstname);
        final EditText ed_middlename=(EditText)findViewById(R.id.ed_middlename);
        final EditText ed_lastname=(EditText)findViewById(R.id.ed_lastname);
        ed_dob=(TextView)findViewById(R.id.ed_dob);
        final EditText ed_mrn=(EditText)findViewById(R.id.ed_mrn);
        final EditText ed_floor=(EditText)findViewById(R.id.ed_floor);
        final EditText ed_ward=(EditText)findViewById(R.id.ed_ward);
        final EditText ed_room=(EditText)findViewById(R.id.ed_room);
        final EditText ed_bed=(EditText)findViewById(R.id.ed_bed);
        ed_Admitdate=(TextView)findViewById(R.id.ed_Admitdate);
        final RadioButton radioMale = (RadioButton) findViewById(R.id.radioMale);
        final RadioButton radioFemale = (RadioButton) findViewById(R.id.radioFemale);
         LinearLayout choose_existing=(LinearLayout) findViewById(R.id.choose_existing_patient);
        choose_existing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = li.inflate(R.layout.existing_patient, null, false);
                dialog.setContentView(v);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.horizontalMargin = 15;
                Window window = dialog.getWindow();
                window.setBackgroundDrawableResource(R.color.trans_black2);
//                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setAttributes(lp);
                window.setGravity(Gravity.BOTTOM);
                dialog.show();
                TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                ListView patient_list=(ListView)dialog.findViewById(R.id.patient_list);
                Button create=(Button)dialog.findViewById(R.id.create_patient);
                PatientList=DBAccess.getdbHeler().getAllPatientDetails(groupid);
                final ChoosePatientAdapter adapter=new ChoosePatientAdapter(context,R.layout.choose_patient_row,PatientList);
                patient_list.setAdapter(adapter);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                patient_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        choosepBean=(PatientDetailsBean)adapter.getItem(i);
                        Log.i("patientdetails","list onclick"+choosepBean.getFirstname());
                        dialog.dismiss();
                        if(choosepBean!=null){
                            Log.i("patientdetails","list onclick if"+choosepBean.getFirstname());
                            if(choosepBean.getFirstname()!=null)
                                ed_firstname.setText(choosepBean.getFirstname());
                            if(choosepBean.getMiddlename()!=null)
                                ed_middlename.setText(choosepBean.getMiddlename());
                            if(choosepBean.getLastname()!=null)
                                ed_lastname.setText(choosepBean.getLastname());
                            if(choosepBean.getDob()!=null)
                                ed_dob.setText(choosepBean.getDob());
                            if(choosepBean.getMrn()!=null)
                                ed_mrn.setText(choosepBean.getMrn());
                            if(choosepBean.getFloor()!=null)
                                ed_floor.setText(choosepBean.getFloor());
                            if(choosepBean.getRoom()!=null)
                                ed_room.setText(choosepBean.getRoom());
                            if(choosepBean.getBed()!=null)
                                ed_bed.setText(choosepBean.getBed());
                            if(choosepBean.getWard()!=null)
                                ed_ward.setText(choosepBean.getWard());
                            if(choosepBean.getAdmissiondate()!=null)
                                ed_Admitdate.setText(choosepBean.getAdmissiondate());
                            if(choosepBean.getSex().equalsIgnoreCase("male")){
                                radioFemale.setChecked(false);
                                radioMale.setChecked(true);
                            } else if(choosepBean.getSex().equalsIgnoreCase("female")){
                                radioFemale.setChecked(true);
                                radioMale.setChecked(false);
                            }

                        }
                    }
                });

            }
        });

        radioMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioMale.isChecked()) {
                    radioMale.setTextColor(Color.parseColor("#458EDB"));
                    radioFemale.setTextColor(Color.parseColor("#ffffff"));
                }


            }
        });
        radioFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioFemale.setTextColor(Color.parseColor("#458EDB"));
                radioMale.setTextColor(Color.parseColor("#ffffff"));

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ed_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DatePickerDialog(RoundNewPatientActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        myCalendar.set(Calendar.YEAR, i);
                        myCalendar.set(Calendar.MONTH, i1);
                        myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                        dob.setVisibility(View.VISIBLE);
                        updateLabel();
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        ed_Admitdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DatePickerDialog(RoundNewPatientActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        myCalendar.set(Calendar.YEAR, i);
                        myCalendar.set(Calendar.MONTH, i1);
                        myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                        date.setVisibility(View.VISIBLE);
                        updateadmitLabel();
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        UserBean bean=new UserBean();
        ProfileBean pbean=DBAccess.getdbHeler().getProfileDetails(CallDispatcher.LoginUser);
        bean.setBuddyName(CallDispatcher.LoginUser);
        bean.setFirstname(pbean.getFirstname()+" "+pbean.getLastname());
        membersList.add(bean);
       Log.i("sss", "memberss" + membersList.toString());
        adapter = new MembersAdapter(RoundNewPatientActivity.this,R.layout.rounding_member_row, membersList);
        final int adapterCount = adapter.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = adapter.getView(i, null, null);
            lv_buddylist.addView(item);
        }
        adapter.notifyDataSetChanged();


        ed_firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0)
                    firstname.setVisibility(View.VISIBLE);
                else
                    firstname.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        ed_middlename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0)
                    middlename.setVisibility(View.VISIBLE);
                else
                    middlename.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        ed_lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0)
                    lastname.setVisibility(View.VISIBLE);
                else
                    lastname.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        ed_mrn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0)
                    mrn.setVisibility(View.VISIBLE);
                else
                    mrn.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        ed_floor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    floor.setVisibility(View.VISIBLE);
                else
                    floor.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        ed_ward.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    ward.setVisibility(View.VISIBLE);
                else
                    ward.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        ed_room.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    room.setVisibility(View.VISIBLE);
                else
                    room.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        ed_bed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    bed.setVisibility(View.VISIBLE);
                else
                    bed.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        List<String> list = new ArrayList<String>();
        list=DBAccess.getdbHeler().getHospitalDetails();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown_list, list);

        dataAdapter.setDropDownViewResource
                (R.layout.spinner_dropdown_list);

        hospital.setAdapter(dataAdapter);
        hospital.setThreshold(1);
        hospital.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hospital.showDropDown();
            }
        });
        GroupBean gBean = DBAccess.getdbHeler()
                .getGroupAndMembers("select * from groupdetails where groupid="
                        + groupid);
        if (gBean != null) {
            if (gBean.getActiveGroupMembers() != null
                    && gBean.getActiveGroupMembers().length() > 0) {
                String[] list1 = (gBean.getActiveGroupMembers())
                        .split(",");
                for (String tmp : list1) {
                    UserBean userBean = new UserBean();
                    userBean.setBuddyName(tmp);
                    userBean.setInvite(true);
                    userBean.setGroupid(groupid);
                    userBean.setSelected(true);
                    membersList.add(userBean);
                }
            }
            if (gBean.getInActiveGroupMembers() != null
                    && gBean.getInActiveGroupMembers().length() > 0) {
                String[] list1 = (gBean.getInActiveGroupMembers())
                        .split(",");
                for (String tmp : list1) {
                    UserBean userBean = new UserBean();
                    userBean.setBuddyName(tmp);
                    userBean.setSelected(true);
                    membersList.add(userBean);
                }
            }
        }
        Log.i("AAAA","members list "+membersList.size());
        assibnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setEnabled(false);
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        view.setEnabled(true);

                    }
                }, 1000);
                Intent intent = new Intent(getApplicationContext(),
                        AddGroupMembers.class);
                ArrayList<String> buddylist = new ArrayList<String>();
                for (UserBean userBean : membersList) {
                    buddylist.add(userBean.getBuddyName());
                }
                intent.putStringArrayListExtra("buddylist", buddylist);
                Log.i("AAAA", "members list " + buddylist.size());
                startActivityForResult(intent, 3);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_firstname.getText().toString().trim().length() > 0
                    && ed_lastname.getText().toString().trim().length() > 0
                        && ed_dob.getText().toString().trim().length() > 0
                    && hospital.getText().toString().length()> 0
                        && ed_mrn.getText().toString().trim().length() > 0
                        && ed_floor.getText().toString().trim().length() > 0
                        && ed_ward.getText().toString().trim().length() > 0
                        && ed_room.getText().toString().trim().length() > 0
                        && ed_bed.getText().toString().trim().length() > 0
                        && ed_Admitdate.getText().toString().trim().length() > 0) {

                int selectedId = gender_patient.getCheckedRadioButtonId();
                    if(selectedId>0) {
                        genderSelected = (RadioButton) findViewById(selectedId);
                        pBean.setSex(genderSelected.getText().toString());
                    }else
                        pBean.setSex("");
                  int i=0;
                    addedMembers=new String[membersList.size()];
                    for (UserBean userBean : membersList) {
                        addedMembers[i]= userBean.getBuddyName();
                        Log.i("sss", "members list " + addedMembers[i]);
                        i++;
                    }
                    pBean.setGroupid(groupid);
                    pBean.setPatientid("");
                    pBean.setCreatorname(CallDispatcher.LoginUser);
                    pBean.setFirstname(ed_firstname.getText().toString());
                    pBean.setMiddlename(ed_middlename.getText().toString());
                    pBean.setLastname(ed_lastname.getText().toString());
                    pBean.setDob(ed_dob.getText().toString());
                    pBean.setHospital(hospital.getText().toString());
                    pBean.setMrn(ed_mrn.getText().toString());
                    pBean.setLocation("");

                    pBean.setFloor(ed_floor.getText().toString());
                    pBean.setWard(ed_ward.getText().toString());
                    pBean.setRoom(ed_room.getText().toString());
                    pBean.setBed(ed_bed.getText().toString());
                    pBean.setAdmissiondate(ed_Admitdate.getText().toString());
                    pBean.setAssignedmembers(CallDispatcher.LoginUser);
                    showprogress();
                WebServiceReferences.webServiceClient.SetPatientRecord(pBean, context);
            } else {
                Toast.makeText(context,
                        "Please enter all mandatory fields",
                        Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            // check if the request code is same as what is passed here it is 2
            membercount.setVisibility(View.VISIBLE);
            if (requestCode == 3) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    ArrayList<UserBean> list = (ArrayList<UserBean>) bundle
                            .get("list");
                    HashMap<String, UserBean> membersMap = new HashMap<String, UserBean>();
                    for (UserBean userBean : membersList) {
                        membersMap.put(userBean.getBuddyName(), userBean);
                    }
                    UserBean bean=new UserBean();
                    bean.setBuddyName(CallDispatcher.LoginUser);
                    membersList.add(bean);
                    membersList.clear();
                    for (UserBean userBean : list) {
                        if (!membersMap.containsKey(userBean.getBuddyName())) {
                            membersList.add(userBean);
                        }
                    }
                    refreshMembersList();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        ed_dob.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateadmitLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        ed_Admitdate.setText(sdf.format(myCalendar.getTime()));
    }
    private void refreshMembersList() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                membercount.setText("("
                        + String.valueOf(membersList.size()) + ")");
                lv_buddylist.removeAllViews();
                adapter = new MembersAdapter(RoundNewPatientActivity.this,R.layout.rounding_member_row, membersList);
                final int adapterCount = adapter.getCount();

                for (int i = 0; i < adapterCount; i++) {
                    View item = adapter.getView(i, null, null);
                    lv_buddylist.addView(item);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }
    public class MembersAdapter extends ArrayAdapter<UserBean> {

        private LayoutInflater inflater = null;
        private ViewHolder holder;
        private ImageLoader imageLoader;
        private Vector<UserBean> result;

        public MembersAdapter(Context context, int resource, Vector<UserBean> objects) {
            super(context, resource, objects);
            imageLoader=new ImageLoader(context);
            result = new Vector<UserBean>();
            result.addAll(objects);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            try {
                holder = new ViewHolder();
                if(convertView == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.rounding_member_row, null);
                    holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                    holder.edit = (ImageView) convertView.findViewById(R.id.edit);
                    holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                    holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                    holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                    holder.position = (TextView) convertView.findViewById(R.id.position);
                    holder.rights = (TextView) convertView.findViewById(R.id.rights);
                    holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
                    convertView.setTag(holder);
                }else
                    holder = (ViewHolder) convertView.getTag();
                final UserBean bib = result.get(i);
                if(bib!=null) {
                    if (bib.getProfilePic() != null) {
                        String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/COMMedia/" + bib.getProfilePic();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
                    holder.header_title.setVisibility(View.GONE);
                    holder.edit.setVisibility(View.GONE);
                    holder.buddyName.setText(bib.getFirstname());
                    if(bib.getBuddyName().equalsIgnoreCase(CallDispatcher.LoginUser)) {

                        holder.rights.setVisibility(View.VISIBLE);
                        holder.rights.setText("Creator");
                        holder.rights.setTextColor(getResources().getColor(R.color.green));
                    }else{

                        holder.rights.setVisibility(View.GONE);
                        holder.position.setTextColor(getResources().getColor(R.color.snazash));
                    }

                    holder.statusIcon.setVisibility(View.GONE);

                }
            }catch(Exception e){
                e.printStackTrace();
                Log.d("RRRR", "Error FindpeopleAdapter.java => " + e.toString());
            }
            return convertView;
        }
    }
    public static class ViewHolder {
        ImageView buddyicon;
        ImageView statusIcon,edit;
        TextView buddyName;
        TextView occupation,position,rights;
        TextView header_title;
    }
    public void showprogress() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    progress = new ProgressDialog(RoundNewPatientActivity.this);
                    if (progress != null) {
                        progress.setCancelable(false);
                        progress.setMessage("Progress ...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setProgress(0);
                        progress.setMax(100);
                        progress.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }

    public void cancelDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progress.dismiss();
                progress = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public void notifySetPatientRecord(Object obj)
    {
        cancelDialog();
        if(obj instanceof String[]){
            String[] result=(String[])obj;
            pBean.setPatientid(result[0]);
            DBAccess.getdbHeler().insertorUpdatePatientDetails(pBean);
            Log.i("patientdetails", "notifySetPatientRecord ");
            showToast("Successfully details updated");
            GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
                    .get("groupchat");
            gChat.refreshPatient();

            finish();

        }
    }

    private void showToast(final String message) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(context, message, 1).show();
            }
        });

    }
    public class ChoosePatientAdapter extends ArrayAdapter<PatientDetailsBean> {

        private Context context;
        ImageLoader imageLoader;
        Vector<PatientDetailsBean> patientlist;

        public ChoosePatientAdapter(Context context, int textViewResourceId,
                                   Vector<PatientDetailsBean> patientList) {

            super(context, R.layout.choose_patient_row, patientList);
            this.context = context;
            imageLoader=new ImageLoader(SingleInstance.mainContext);
            patientlist=new Vector<PatientDetailsBean>();
            patientlist.addAll(patientList);

        }
        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            View row = view;

            try {
                final ViewHolder holder;
                if (row == null) {
                    holder = new ViewHolder();
                    LayoutInflater inflater = (LayoutInflater) this.context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(R.layout.choose_patient_row, null, false);
                    holder.patientname = (TextView) row.findViewById(R.id.patient_name);
                    holder.dob = (TextView) row.findViewById(R.id.dob);
                    holder.hospital = (TextView) row.findViewById(R.id.hospital);
                    holder.sex = (TextView) row.findViewById(R.id.sex);
                    holder.mrn = (TextView) row.findViewById(R.id.mrn);
                    holder.floor = (TextView) row.findViewById(R.id.floor);
                    holder.ward = (TextView) row.findViewById(R.id.ward);
                    holder.room = (TextView) row.findViewById(R.id.room);
                    holder.los = (TextView) row.findViewById(R.id.los);
                    holder.bed = (TextView) row.findViewById(R.id.bed);
                    holder.details_lay = (LinearLayout) row.findViewById(R.id.details_lay);
                    row.setTag(holder);
                } else {
                    holder = (ViewHolder) row.getTag();
                }

                PatientDetailsBean pBean=PatientList.get(position);
                if(pBean!=null){
                    if(pBean.getFirstname()!=null && pBean.getLastname()!=null)
                        holder.patientname.setText(pBean.getFirstname()+" "+pBean.getLastname());
                    if(pBean.getDob()!=null)
                        holder.dob.setText("DOB: "+pBean.getDob());
                    if(pBean.getSex()!=null)
                        holder.sex.setText("Sex : "+pBean.getSex());
                    if(pBean.getMrn()!=null)
                        holder.mrn.setText("Mrn : "+pBean.getMrn());
                    if(pBean.getFloor()!=null)
                        holder.floor.setText("Floor : "+pBean.getFloor());
                    if(pBean.getRoom()!=null)
                        holder.room.setText("Room : "+pBean.getRoom());
                    if(pBean.getWard()!=null)
                        holder.ward.setText("Ward : "+pBean.getWard());
                    if(pBean.getBed()!=null)
                        holder.bed.setText("Bed : "+pBean.getBed());
                    if(pBean.getAdmissiondate()!=null)
                        holder.los.setText("Los : "+pBean.getAdmissiondate());
                    if(pBean.getHospital()!=null)
                        holder.hospital.setText("Hospital : "+pBean.getHospital());
                }
                holder.hospital.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!isClicked) {
                            holder.details_lay.setVisibility(View.VISIBLE);
                            isClicked=true;
                        }else {
                            holder.details_lay.setVisibility(View.GONE);
                            isClicked=false;
                        }
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return row;
        }
        class ViewHolder {
            TextView patientname;
            TextView dob,mrn;
            TextView hospital,los;
            TextView floor,room,bed;
            TextView sex,ward;
            LinearLayout details_lay;

        }
    }
}