package com.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.lib.model.BuddyInformationBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;

public class AddGroupMembers extends Activity {
	public Button back, done,search;

	private TextView countofselection;
	private BuddyAdapter adapter = null;

	private ListView lv_addcontact = null;

	private Context context = null;

	public CheckBox selectAll = null;

	Handler handler = new Handler();
   private  boolean isUserSelected = true;

	private  int presentbuddiescount=0;
	private TextView text_memeberscount;

    Vector<UserBean> contactList = new Vector<UserBean>();
	Boolean invite;
	String groupid,calltype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.groupaddcontact);
			context = this;
			if(SingleInstance.mainContext.getResources()
					.getString(R.string.screenshot).equalsIgnoreCase(SingleInstance.mainContext.getResources()
							.getString(R.string.yes))){
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
			}

			WebServiceReferences.contextTable.put("groupcontact", context);
			invite=getIntent().getBooleanExtra("fromcall", false);
			groupid=getIntent().getStringExtra("groupid");
			back = (Button) findViewById(R.id.btn_backaddcontact);
			search = (Button) findViewById(R.id.search);
			done = (Button) findViewById(R.id.btn_done);
			lv_addcontact = (ListView) findViewById(R.id.lv_buddylist);
			lv_addcontact.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			countofselection=(TextView) findViewById(R.id.selected);
			selectAll = (CheckBox) findViewById(R.id.btn_selectall);
			final TextView txtView01 = (TextView) findViewById(R.id.tx_headingaddcontact);
			final TextView ed_search = (TextView) findViewById(R.id.searchet);
			text_memeberscount = (TextView)findViewById(R.id.text_memeberscount);
			calltype=getIntent().getStringExtra("calltype");

			if(invite){
				txtView01.setText("ADD MEMBERS");
				done.setText("ADD");
				text_memeberscount.setVisibility(View.VISIBLE);

			}
			search.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if(txtView01.getVisibility()==View.VISIBLE){
						txtView01.setVisibility(View.GONE);
						ed_search.setVisibility(View.VISIBLE);
						search.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_close));
					}else {
						txtView01.setVisibility(View.VISIBLE);
						ed_search.setVisibility(View.GONE);
						search.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_search));
					}
				}
			});
			search.addTextChangedListener(new TextWatcher() {

				public void afterTextChanged(Editable s) {
				}
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (s != null && s != "")
						adapter.filter(s.toString());
				}
			});

			ArrayList<String> buddylist = getIntent().getStringArrayListExtra(
					"buddylist");
			Log.i("AAAA", "buddy list addgroup.java " + buddylist.size());

			ArrayList<String> buddies = new ArrayList<String>();
			Vector<BuddyInformationBean> cList = ContactsFragment
					.getBuddyList();
			
			HashMap<String, BuddyInformationBean> bMap = new HashMap<String, BuddyInformationBean>();
			if(!invite) {
				if (cList != null) {
					for (BuddyInformationBean bib : cList) {
						if (!bib.isTitle()) {
							if (!bib.getName().equalsIgnoreCase(
									CallDispatcher.LoginUser)) {
								if (!bib.getStatus().equalsIgnoreCase("pending")
										&& !bib.getStatus().equalsIgnoreCase("new")) {
									bMap.put(bib.getName(), bib);
								}
							}
						}
					}
				}
			}else {
				Log.d("Onlinemembers","buddyslist");
					for (BuddyInformationBean bib : cList) {
						Log.d("Onlinemembers","buddyslist clist");
						for(String temp:buddylist){
							Log.d("Onlinemembers","buddyslist templist");
							if(temp.equalsIgnoreCase(bib.getName())) {
								Log.d("Onlinemembers","buddyslist if");
								bMap.put(bib.getName(), bib);
							}

						}
					}
			}
		

			Iterator it = bMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				System.out.println(pair.getKey() + " = " + pair.getValue());
				buddies.add((String) pair.getKey());

			}
			presentbuddiescount=0;
			Log.i("AAAA","loop before"+buddies.size());

				if(!invite) {
					for (String tmp : buddies) {

						if (!buddylist.contains(tmp)) {
							UserBean userBean = new UserBean();
							presentbuddiescount++;
							Log.i("asdf", "presentbuddiescount" + presentbuddiescount);
							for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
								if (bib.getName().equalsIgnoreCase(tmp)) {
									userBean.setStatus(bib.getStatus());
									ProfileBean pbean = DBAccess.getdbHeler().getProfileDetails(tmp);
									userBean.setProfilePic(pbean.getPhoto());
									userBean.setFirstname(pbean.getFirstname() + " " + pbean.getLastname());
									break;
								}
							}
							userBean.setBuddyName(tmp);
							contactList.add(userBean);
							Log.i("AAAA", "loop" + contactList.size());

						}
					}
				}else {
					for (String tmp : buddies) {
						UserBean userBean = new UserBean();
					presentbuddiescount++;
					Log.i("asdf", "presentbuddiescount" + presentbuddiescount);
					for(BuddyInformationBean bib:ContactsFragment.getBuddyList()){
						if(bib.getName().equalsIgnoreCase(tmp)) {
							userBean.setStatus(bib.getStatus());
							ProfileBean pbean= DBAccess.getdbHeler().getProfileDetails(tmp);
							userBean.setProfilePic(pbean.getPhoto());
							userBean.setFirstname(pbean.getFirstname()+" "+pbean.getLastname());
							break;
						}
					}
					userBean.setBuddyName(tmp);
					contactList.add(userBean);
				}
			}
			Collections.sort(contactList, new UserNameComparator());
			adapter = new BuddyAdapter(this, contactList);
			lv_addcontact.setAdapter(adapter);
			done.setOnClickListener(new OnClickListener() {
                boolean isSelectContact = false;

                public void onClick(View arg0) {
                    if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {
                        ArrayList<UserBean> users = new ArrayList<UserBean>();
                        for (UserBean userBean : contactList) {
                            userBean.setFlag("1");
                            if (userBean.isSelected()) {
                                isSelectContact = true;
                                users.add(userBean);
                            }
                        }
                        if (!isSelectContact) {
                            showToast("Buddy not selected");

                        } else {
							Intent intent=getIntent();
							intent.putExtra("calltype", calltype);
							intent.putExtra("list", users);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } else {
                        showAlert1("Info", "Check internet connection Unable to connect server");
                        finish();

                    }
                }
            });

			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			selectAll.setTag(true);
			selectAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					Log.i("AAAA","select all");
					try {
                        //if (isUserSelected) {
                            if ((Boolean) selectAll.getTag()) {
                                for (UserBean userBean : contactList) {
                                    userBean.setSelected(true);
                                }
                                selectAll.setTag(false);
								countofcheckbox(presentbuddiescount);
                            } else {
                                for (UserBean userBean : contactList) {
                                    userBean.setSelected(false);
                                }
                                selectAll.setTag(true);
								countofcheckbox(0);
                            }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						else
							e.printStackTrace();
					}
					adapter.notifyDataSetChanged();

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

	public void countofcheckbox(int count)
	{
		Log.i("asdf","count"+count);
		countofselection.setText(Integer.toString(count) + " selected");
		text_memeberscount.setText( "add " + Integer.toString(count) + "members to call");

	}
    public void showAlert1(String title,String message) {

		AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
		alertCall.setMessage(message).setTitle(title).setCancelable(false)
				.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		alertCall.show();
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


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("groupcontact");
		super.onDestroy();
	}
	public class UserNameComparator implements Comparator<UserBean> {

		@Override
		public int compare(UserBean oldBean,
						   UserBean newBean) {
			// TODO Auto-generated method stub
			return (oldBean.getFirstname().compareTo(newBean.getFirstname()));
		}
	}

}
