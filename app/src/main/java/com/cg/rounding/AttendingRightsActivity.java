package com.cg.rounding;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.cg.DB.DBAccess;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.util.SingleInstance;

import org.lib.model.RoleAccessBean;
import org.lib.model.RoleCommentsViewBean;
import org.lib.model.RoleEditRndFormBean;
import org.lib.model.RolePatientManagementBean;
import org.lib.model.RoleTaskMgtBean;

import java.util.List;
import java.util.Objects;

public class AttendingRightsActivity extends Activity {
    private Context mainContext;
    String Role;
    String groupId;
    String groupOwner;
    String groupMember;
    RolePatientManagementBean rolePatientManagementBean=new RolePatientManagementBean();
    RoleEditRndFormBean roleEditRndFormBean=new RoleEditRndFormBean();
    RoleTaskMgtBean roleTaskMgtBean=new RoleTaskMgtBean();
    RoleCommentsViewBean roleCommentsViewBean=new RoleCommentsViewBean();
    Handler handler = new Handler();
    private ProgressDialog progress = null;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.attending_rights);
        mainContext = this;
        Button cancel=(Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button save_rights=(Button) findViewById(R.id.save_rights);
        TextView title=(TextView)findViewById(R.id.txtView01);
        final CheckBox ch_pm=(CheckBox)findViewById(R.id.ch_pm);
        final CheckBox ch_add=(CheckBox)findViewById(R.id.ch_add);
        final CheckBox ch_modify=(CheckBox)findViewById(R.id.ch_modify);
        final CheckBox ch_delete=(CheckBox)findViewById(R.id.ch_delete);
        final CheckBox ch_discharge=(CheckBox)findViewById(R.id.ch_discharge);


        final CheckBox ch_er=(CheckBox)findViewById(R.id.ch_er);
        final CheckBox ch_status=(CheckBox)findViewById(R.id.ch_status);
        final CheckBox ch_diagnosis=(CheckBox)findViewById(R.id.ch_diagnosis);
        final CheckBox ch_test=(CheckBox)findViewById(R.id.ch_test);
        final CheckBox ch_hospital=(CheckBox)findViewById(R.id.ch_hospital);
        final CheckBox ch_notes=(CheckBox)findViewById(R.id.ch_notes);
        final CheckBox ch_consults=(CheckBox)findViewById(R.id.ch_consults);

        final CheckBox ch_tm=(CheckBox)findViewById(R.id.ch_tm);
        final CheckBox ch_tmattending=(CheckBox)findViewById(R.id.ch_tmattending);
        final CheckBox ch_tmfellow=(CheckBox)findViewById(R.id.ch_tmfellow);
        final CheckBox ch_tmchief=(CheckBox)findViewById(R.id.ch_tmchief);
        final CheckBox ch_tmresident=(CheckBox)findViewById(R.id.ch_tmresident);
        final CheckBox ch_tmmedstudent=(CheckBox)findViewById(R.id.ch_tmmedstudent);

        final CheckBox ch_cv=(CheckBox)findViewById(R.id.ch_cv);
        final CheckBox ch_attending=(CheckBox)findViewById(R.id.ch_attending);
        final CheckBox ch_fellow=(CheckBox)findViewById(R.id.ch_fellow);
        final CheckBox ch_chief=(CheckBox)findViewById(R.id.ch_chief);
        final CheckBox ch_resident=(CheckBox)findViewById(R.id.ch_resident);
        final CheckBox ch_medstudent=(CheckBox)findViewById(R.id.ch_medstudent);

        EditText ed_roll=(EditText) findViewById(R.id.ed_rollname);
        groupId = getIntent().getStringExtra("groupid");
        Role = getIntent().getStringExtra("role");
        groupOwner = getIntent().getStringExtra("groupowner");
        groupMember = getIntent().getStringExtra("groupmembers");
        ed_roll.setText(Role);
        if(Role.equalsIgnoreCase("attending"))
            title.setText("ATTENDING RIGHTS");
        else if(Role.equalsIgnoreCase("fellow"))
            title.setText("FELLOW RIGHTS");
        else if(Role.equalsIgnoreCase("chief resident"))
            title.setText("CHIEF RESIDENT RIGHTS");
        else if(Role.equalsIgnoreCase("resident"))
            title.setText("RESIDENT RIGHTS");
        else if(Role.equalsIgnoreCase("medical student"))
            title.setText("MEDICAL STUDENT RIGHTS");
        RoleAccessBean rBean=DBAccess.getdbHeler().getRoleAccessDetails(groupId,Role);
        if(rBean!=null){
            if(rBean.getPatientmanagement()!=null )
                if(rBean.getPatientmanagement().equalsIgnoreCase("0"))
                    ch_pm.setChecked(false);
                else
                    ch_pm.setChecked(true);
            else
                ch_pm.setChecked(false);
            if(rBean.getTaskmanagement()!=null )
                if(rBean.getTaskmanagement().equalsIgnoreCase("0"))
                ch_tm.setChecked(false);
                else
                    ch_tm.setChecked(true);
            else
                ch_tm.setChecked(false);
            if(rBean.getCommentsview()!=null )
                if(rBean.getCommentsview().equalsIgnoreCase("0"))
                ch_cv.setChecked(false);
                else
                    ch_cv.setChecked(true);
            else
                ch_cv.setChecked(false);
            if(rBean.getEditroundingform()!=null)
                if(rBean.getEditroundingform().equalsIgnoreCase("0"))
                ch_er.setChecked(false);
                else
                    ch_er.setChecked(true);
            else
                ch_er.setChecked(false);
        }else {
            ch_pm.setChecked(false);
            ch_er.setChecked(false);
            ch_cv.setChecked(false);
            ch_tm.setChecked(false);
        }
        final RolePatientManagementBean pmBean=DBAccess.getdbHeler().getRolePatientManagement(groupId, Role);
        if(pmBean!=null){
            if(pmBean.getAdd()!=null)
                if(pmBean.getAdd().equalsIgnoreCase("0"))
                ch_add.setChecked(false);
                else
                    ch_add.setChecked(true);
            else {
                ch_add.setChecked(false);
                ch_add.setEnabled(false);
            }
            if(pmBean.getModify()!=null )
                if(pmBean.getModify().equalsIgnoreCase("0"))
                ch_modify.setChecked(false);
                else
                    ch_modify.setChecked(true);
            else {
                ch_modify.setChecked(false);
                ch_modify.setEnabled(false);
            }
            if(pmBean.getDelete()!=null )
                if(pmBean.getDelete().equalsIgnoreCase("0"))
                ch_delete.setChecked(false);
                else
                    ch_delete.setChecked(true);
            else {
                ch_delete.setChecked(false);
                ch_delete.setEnabled(false);
            }
            if(pmBean.getDischarge()!=null)
                if(pmBean.getDischarge().equalsIgnoreCase("0"))
                ch_discharge.setChecked(false);
                else
                    ch_discharge.setChecked(true);
            else {
                ch_discharge.setChecked(false);
                ch_discharge.setEnabled(false);
            }
        }

        final RoleEditRndFormBean rdBean=DBAccess.getdbHeler().getRoleEditRoundingDetails(groupId, Role);
        if(rdBean!=null){
            if(rdBean.getStatus()!=null)
                if(rdBean.getStatus().equalsIgnoreCase("0"))
                ch_status.setChecked(false);
                else
                    ch_status.setChecked(true);
            else {
                ch_status.setChecked(false);
                ch_status.setEnabled(false);
            }
            if(rdBean.getDiagnosis()!=null )
                if(rdBean.getDiagnosis().equalsIgnoreCase("0"))
                ch_diagnosis.setChecked(false);
                else
                    ch_diagnosis.setChecked(true);
            else {
                ch_diagnosis.setChecked(false);
                ch_diagnosis.setEnabled(false);
            }
            if(rdBean.getTestandvitals()!=null )
                if(rdBean.getTestandvitals().equalsIgnoreCase("0"))
                ch_test.setChecked(false);
                else
                    ch_test.setChecked(true);
            else {
                ch_test.setChecked(false);
                ch_test.setEnabled(false);
            }
            if(rdBean.getHospitalcourse()!=null )
                if(rdBean.getHospitalcourse().equalsIgnoreCase("0"))
                ch_hospital.setChecked(false);
                else
                    ch_hospital.setChecked(true);
            else {
                ch_hospital.setChecked(false);
                ch_hospital.setEnabled(false);
            }
            if(rdBean.getNotes()!=null )
                if(rdBean.getNotes().equalsIgnoreCase("0"))
                ch_notes.setChecked(false);
                else
                    ch_notes.setChecked(true);
            else {
                ch_notes.setChecked(false);
                ch_notes.setEnabled(false);
            }
            if(rdBean.getConsults()!=null)
                if(rdBean.getConsults().equalsIgnoreCase("0"))
                ch_consults.setChecked(false);
                else
                    ch_consults.setChecked(true);
            else {
                ch_consults.setChecked(false);
                ch_consults.setEnabled(false);
            }
        }

        final RoleTaskMgtBean rtBean=DBAccess.getdbHeler().getRoleTaskManagement(groupId, Role);
        if(rtBean!=null){
            if(rtBean.getTattending()!=null )
                if(rtBean.getTattending().equalsIgnoreCase("0"))
                ch_attending.setChecked(false);
                else
                    ch_attending.setChecked(true);
            else {
                ch_attending.setChecked(false);
                ch_attending.setEnabled(false);
            }
            if(rtBean.getTfellow()!=null )
                if(rtBean.getTfellow().equalsIgnoreCase("0"))
                ch_fellow.setChecked(false);
                else
                    ch_fellow.setChecked(true);
            else {
                ch_fellow.setChecked(false);
                ch_fellow.setEnabled(false);
            }
            if(rtBean.getTchiefresident()!=null )
                if(rtBean.getTchiefresident().equalsIgnoreCase("0"))
                ch_chief.setChecked(false);
                else
                    ch_chief.setChecked(true);
            else {
                ch_chief.setChecked(false);
                ch_chief.setEnabled(false);
            }
            if(rtBean.getTresident()!=null)
                if(rtBean.getTresident().equalsIgnoreCase("0"))
                ch_resident.setChecked(false);
                else
                    ch_resident.setChecked(true);
            else {
                ch_resident.setChecked(false);
                ch_resident.setEnabled(false);
            }
            if(rtBean.getTmedstudent()!=null )
                if(rtBean.getTmedstudent().equalsIgnoreCase("0"))
                ch_medstudent.setChecked(false);
                else
                    ch_medstudent.setChecked(true);
            else {
                ch_medstudent.setChecked(false);
                ch_medstudent.setEnabled(false);
            }
        }

        final RoleCommentsViewBean cvBean=DBAccess.getdbHeler().getRoleCommentsView(groupId,Role);
        if(cvBean!=null){
            if(cvBean.getCattending()!=null )
                if(cvBean.getCattending().equalsIgnoreCase("0"))
                ch_tmattending.setChecked(false);
                else
                    ch_tmattending.setChecked(true);
            else {
                ch_tmattending.setChecked(false);
                ch_tmattending.setEnabled(false);
            }
            if(cvBean.getCfellow()!=null )
                if(cvBean.getCfellow().equalsIgnoreCase("0"))
                ch_tmfellow.setChecked(false);
                else
                    ch_tmfellow.setChecked(true);
            else {
                ch_tmfellow.setChecked(false);
                ch_tmfellow.setEnabled(false);
            }
            if(cvBean.getCchiefresident()!=null)
                if(cvBean.getCchiefresident().equalsIgnoreCase("0"))
                ch_tmchief.setChecked(false);
                else
                    ch_tmchief.setChecked(true);
            else {
                ch_tmchief.setChecked(false);
                ch_tmchief.setEnabled(false);
            }
            if(cvBean.getCresident()!=null )
                if(cvBean.getCresident().equalsIgnoreCase("0"))
                ch_tmresident.setChecked(false);
                else
                    ch_tmresident.setChecked(true);
            else {
                ch_tmresident.setChecked(false);
                ch_tmresident.setEnabled(false);
            }
            if(cvBean.getCmedstudent()!=null)
                if(cvBean.getCmedstudent().equalsIgnoreCase("0"))
                ch_tmmedstudent.setChecked(false);
                else
                    ch_tmmedstudent.setChecked(true);
            else {
                ch_tmmedstudent.setChecked(false);
                ch_tmmedstudent.setEnabled(false);
            }
        }


        ch_pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch_pm.isChecked()) {
                    ch_add.setEnabled(true);
                    ch_modify.setEnabled(true);
                    ch_delete.setEnabled(true);
                    ch_discharge.setEnabled(true);
                } else {
                    ch_add.setEnabled(false);
                    ch_modify.setEnabled(false);
                    ch_delete.setEnabled(false);
                    ch_discharge.setEnabled(false);
                    ch_add.setChecked(false);
                    ch_modify.setChecked(false);
                    ch_delete.setChecked(false);
                    ch_discharge.setChecked(false);
                }
            }
        });
        ch_er.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch_er.isChecked()) {
                    ch_status.setEnabled(true);
                    ch_diagnosis.setEnabled(true);
                    ch_test.setEnabled(true);
                    ch_hospital.setEnabled(true);
                    ch_notes.setEnabled(true);
                    ch_consults.setEnabled(true);
                } else {
                    ch_status.setEnabled(false);
                    ch_status.setChecked(false);
                    ch_diagnosis.setEnabled(false);
                    ch_diagnosis.setChecked(false);
                    ch_test.setEnabled(false);
                    ch_test.setChecked(false);
                    ch_hospital.setEnabled(false);
                    ch_hospital.setChecked(false);
                    ch_notes.setEnabled(false);
                    ch_notes.setChecked(false);
                    ch_consults.setEnabled(false);
                    ch_consults.setChecked(false);
                }

            }
        });
        ch_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch_cv.isChecked()) {
                    ch_attending.setEnabled(true);
                    ch_fellow.setEnabled(true);
                    ch_chief.setEnabled(true);
                    ch_resident.setEnabled(true);
                    ch_medstudent.setEnabled(true);
                } else {
                    ch_attending.setEnabled(false);
                    ch_fellow.setEnabled(false);
                    ch_chief.setEnabled(false);
                    ch_resident.setEnabled(false);
                    ch_medstudent.setEnabled(false);
                    ch_attending.setChecked(false);
                    ch_fellow.setChecked(false);
                    ch_chief.setChecked(false);
                    ch_resident.setChecked(false);
                    ch_medstudent.setChecked(false);
                }
            }
        });
        ch_tm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch_tm.isChecked()) {
                    ch_tmattending.setEnabled(true);
                    ch_tmfellow.setEnabled(true);
                    ch_tmchief.setEnabled(true);
                    ch_tmresident.setEnabled(true);
                    ch_tmmedstudent.setEnabled(true);
                } else {
                    ch_tmattending.setEnabled(false);
                    ch_tmfellow.setEnabled(false);
                    ch_tmchief.setEnabled(false);
                    ch_tmresident.setEnabled(false);
                    ch_tmmedstudent.setEnabled(false);
                    ch_tmattending.setChecked(false);
                    ch_tmfellow.setChecked(false);
                    ch_tmchief.setChecked(false);
                    ch_tmresident.setChecked(false);
                    ch_tmmedstudent.setChecked(false);
                }
            }
        });
        save_rights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch_pm.isChecked() || ch_er.isChecked() || ch_cv.isChecked()){
                    rolePatientManagementBean.setGroupid(groupId);
                    roleEditRndFormBean.setGroupid(groupId);
                    roleTaskMgtBean.setGroupid(groupId);
                    roleCommentsViewBean.setGroupid(groupId);

                    rolePatientManagementBean.setGroupowner(groupOwner);
                    roleEditRndFormBean.setGroupowner(groupOwner);
                    roleTaskMgtBean.setGroupowner(groupOwner);
                    roleCommentsViewBean.setGroupowner(groupOwner);

                    rolePatientManagementBean.setGroupmember(groupMember);
                    roleEditRndFormBean.setGroupmember(groupMember);
                    roleTaskMgtBean.setGroupmember(groupMember);
                    roleCommentsViewBean.setGroupmember(groupMember);

                    if(pmBean!=null && pmBean.getRoleid()!=null)
                        rolePatientManagementBean.setRoleid(pmBean.getRoleid());
                    else
                        rolePatientManagementBean.setRoleid("");
                    if(rdBean!=null && rdBean.getRoleid()!=null)
                        roleEditRndFormBean.setRoleid(rdBean.getRoleid());
                    else
                        roleEditRndFormBean.setRoleid("");
                    if(rtBean!=null && rtBean.getRoleid()!=null)
                        roleTaskMgtBean.setRoleid(rtBean.getRoleid());
                    else
                        roleTaskMgtBean.setRoleid("");
                    if(cvBean!=null && cvBean.getRoleid()!=null)
                        roleCommentsViewBean.setRoleid(cvBean.getRoleid());
                    else
                        roleCommentsViewBean.setRoleid("");

                    rolePatientManagementBean.setRole(Role);
                    roleEditRndFormBean.setRole(Role);
                    roleTaskMgtBean.setRole(Role);
                    roleCommentsViewBean.setRole(Role);

                    if(ch_add.isChecked())
                    rolePatientManagementBean.setAdd("1");
                    else
                        rolePatientManagementBean.setAdd("0");
                    if(ch_modify.isChecked())
                        rolePatientManagementBean.setModify("1");
                    else
                        rolePatientManagementBean.setModify("0");
                    if(ch_delete.isChecked())
                        rolePatientManagementBean.setDelete("1");
                    else
                        rolePatientManagementBean.setDelete("0");
                    if(ch_discharge.isChecked())
                        rolePatientManagementBean.setDischarge("1");
                    else
                        rolePatientManagementBean.setDischarge("0");


                    if(ch_status.isChecked())
                        roleEditRndFormBean.setStatus("1");
                    else
                        roleEditRndFormBean.setStatus("0");
                    if(ch_diagnosis.isChecked())
                        roleEditRndFormBean.setDiagnosis("1");
                    else
                        roleEditRndFormBean.setDiagnosis("0");
                    if(ch_test.isChecked())
                        roleEditRndFormBean.setTestandvitals("1");
                    else
                        roleEditRndFormBean.setTestandvitals("0");
                    if(ch_hospital.isChecked())
                        roleEditRndFormBean.setHospitalcourse("1");
                    else
                        roleEditRndFormBean.setHospitalcourse("0");
                    if(ch_notes.isChecked())
                        roleEditRndFormBean.setNotes("1");
                    else
                        roleEditRndFormBean.setNotes("0");
                    if(ch_consults.isChecked())
                        roleEditRndFormBean.setConsults("1");
                    else
                        roleEditRndFormBean.setConsults("0");


                    if(ch_tmattending.isChecked())
                        roleTaskMgtBean.setTattending("1");
                    else
                        roleTaskMgtBean.setTattending("0");
                    if(ch_tmfellow.isChecked())
                        roleTaskMgtBean.setTfellow("1");
                    else
                        roleTaskMgtBean.setTfellow("0");
                    if(ch_tmchief.isChecked())
                        roleTaskMgtBean.setTchiefresident("1");
                    else
                        roleTaskMgtBean.setTchiefresident("0");
                    if(ch_tmresident.isChecked())
                        roleTaskMgtBean.setTresident("1");
                    else
                        roleTaskMgtBean.setTresident("0");
                    if(ch_tmmedstudent.isChecked())
                        roleTaskMgtBean.setTmedstudent("1");
                    else
                        roleTaskMgtBean.setTmedstudent("0");


                    if(ch_attending.isChecked())
                        roleCommentsViewBean.setCattending("1");
                    else
                        roleCommentsViewBean.setCattending("0");
                    if(ch_fellow.isChecked())
                        roleCommentsViewBean.setCfellow("1");
                    else
                        roleCommentsViewBean.setCfellow("0");
                    if(ch_chief.isChecked())
                        roleCommentsViewBean.setCchiefresident("1");
                    else
                        roleCommentsViewBean.setCchiefresident("0");
                    if(ch_resident.isChecked())
                        roleCommentsViewBean.setCresident("1");
                    else
                        roleCommentsViewBean.setCresident("0");
                    if(ch_medstudent.isChecked())
                        roleCommentsViewBean.setCmedstudent("1");
                    else
                        roleCommentsViewBean.setCmedstudent("0");

                }
                showprogress();
                WebServiceReferences.webServiceClient.SetOrEditRoleAccess(rolePatientManagementBean,roleEditRndFormBean,roleTaskMgtBean,roleCommentsViewBean, mainContext);
            }
        });

    }

    public void notifyRoleAccess(Object obj)
    {
        cancelDialog();
        Log.i("sss", "notifyRightsToUI of rights=============== ");
        if(obj instanceof String[]) {
            Log.i("sss", "notifyRightsToUI of rights");
            String[] result=(String[])obj;
            rolePatientManagementBean.setRoleid(result[0]);
            roleEditRndFormBean.setRoleid(result[0]);
            roleTaskMgtBean.setRoleid(result[0]);
            roleCommentsViewBean.setRoleid(result[0]);


            DBAccess.getdbHeler().insertorupdateRolepatientAccess(rolePatientManagementBean);
            DBAccess.getdbHeler().insertorupdateRoleEditRoundingFormAccess(roleEditRndFormBean);
            DBAccess.getdbHeler().insertorupdateRoleTransactionAccess(roleTaskMgtBean);
            DBAccess.getdbHeler().insertorupdateRoleCommentsViewAccess(roleCommentsViewBean);
            finish();
        }
    }

    public void showprogress() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    progress = new ProgressDialog(AttendingRightsActivity.this);
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
}
