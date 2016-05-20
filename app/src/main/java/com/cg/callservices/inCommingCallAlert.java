package com.cg.callservices;

import java.util.ArrayList;
import java.util.HashMap;

import org.audio.AudioProperties;
import org.lib.model.SignalingBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class inCommingCallAlert extends Activity {

	private ImageView accept = null;

	private ImageView reject = null;

	private TextView tv_title = null;

	private TextView call_type = null;

	private String strPrevScreen = null;

	private CallDispatcher callDisp = null;

	private ImageView profilePicture = null;

	private String from = null;

	private String to = null;

	private SignalingBean sbaen = null;

	private Context context = null;

	private KeyguardManager keyguardManager;

	private KeyguardLock lock;

	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.callalertscreen);
		keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
		lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
		lock.disableKeyguard();
		context = this;

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int noScrHeight = displaymetrics.heightPixels;
		int noScrWidth = displaymetrics.widthPixels;
		this.setFinishOnTouchOutside(false);

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
		callDisp = new CallDispatcher(context);

		callDisp.setNoScrHeight(noScrHeight);
		callDisp.setNoScrWidth(noScrWidth);
		displaymetrics = null;

		accept = (ImageView) findViewById(R.id.tv_accept);
		reject = (ImageView) findViewById(R.id.tv_decline);
		tv_title = (TextView) findViewById(R.id.caller_name);
		call_type = (TextView) findViewById(R.id.call_type);
		profilePicture = (ImageView) findViewById(R.id.profile_pic);
		sbaen = (SignalingBean) getIntent().getSerializableExtra("bean");
		CallDispatcher.sb = sbaen;
		CallDispatcher.notify_sb = sbaen;
		changeTextalert();
		Log.i("thread", ">>>>>>>>>>>> incoming call on create");
		Bitmap bitmap = null;
		String profilePic = callDisp.getdbHeler(context).getProfilePic(
				sbaen.getFrom());
		bitmap = callDisp.setProfilePicture(profilePic,
				R.drawable.icon_buddy_aoffline);
		profilePicture.setImageBitmap(bitmap);
		if (WebServiceReferences.contextTable
				.containsKey("multimediautils"))
			((MultimediaUtils) WebServiceReferences.contextTable
					.get("multimediautils")).StopAudioPlay();
		accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (WebServiceReferences.missedcallCount.containsKey(sbaen
						.getFrom()))
					WebServiceReferences.missedcallCount.remove(sbaen.getFrom());

				if (CallDispatcher.LoginUser != null) {
					acceptCall(sbaen.getFrom());
				} else {
					callDisp.hangUpCall();
				}
			}
		});

		reject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (WebServiceReferences.missedcallCount.containsKey(sbaen
						.getFrom()))
					WebServiceReferences.missedcallCount.remove(sbaen.getFrom());

				rejectCall();
			}
		});
		SharedPreferences sPreferences = PreferenceManager
				.getDefaultSharedPreferences(SingleInstance.mainContext
						.getApplicationContext());
		boolean isAutoAccept = sPreferences.getBoolean("autoaccept", false);
		if (isAutoAccept
				&& SingleInstance.mainContext.isAutoAcceptEnabled(
						CallDispatcher.LoginUser,
						CallDispatcher.getUser(sbaen.getFrom(), sbaen.getTo()))) {
			acceptCall(sbaen.getFrom());
			finish();
		}

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

	public void changeTextalert() {
		CallDispatcher.isCallAcceptRejectOpened = true;
		CallDispatcher.isIncomingAlert = true;
		WebServiceReferences.contextTable.put("alertscreen", this);

		Log.i("ACal", "showIncomingAlert will be viewed");
		from = sbaen.getFrom();
		to = sbaen.getTo();

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int noScrHeight = displaymetrics.heightPixels;
		int noScrWidth = displaymetrics.widthPixels;

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);

		callDisp.setNoScrHeight(noScrHeight);
		callDisp.setNoScrWidth(noScrWidth);
		displaymetrics = null;
		ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(sbaen.getFrom());
		String CallerName=pBean.getFirstname()+" "+pBean.getLastname();

		if (sbaen.getCallType().equals("AC")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.audio_call));
		} else if (sbaen.getCallType().equals("VC")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.video_call));
		} else if (sbaen.getCallType().equals("ABC")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.audio_broadcast_bc));
		} else if (sbaen.getCallType().equals("VBC")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.video_broadcast_bc));
		} else if (sbaen.getCallType().equals("AP")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.audio_unicast));
		} else if (sbaen.getCallType().equals("VP")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.video_unicast));
		} else if (sbaen.getCallType().equals("SS")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.screen_sharing));
		}

	}

	public void rejectCall() {
		try {

			CallDispatcher.isCallInitiate = false;
			CallDispatcher.isCallAcceptRejectOpened = false;

			CallDispatcher.isIncomingCall = false;
			CallDispatcher.isIncomingAlert = false;
			if (CallDispatcher.LoginUser != null) {

				callDisp.rejectInComingCall(sbaen);
			}
			CallDispatcher.currentSessionid = null;
			callDisp.isHangUpReceived = false;

			//

			CallDispatcher.conferenceMembers.clear();
			CallDispatcher.buddySignall.clear();
			//

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			callDisp.stopRingTone();
			finishactivity();
		}
	}

	public void acceptCall(String strBuddyName) {
		try {
			CallDispatcher.isCallInitiate = false;
			callDisp.isHangUpReceived = false;
			callDisp.stopRingTone();
			if (CallDispatcher.LoginUser != null) {

				if (sbaen.getCallType().equals("AC")) {

					Log.d("call", "start alert");
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");

					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine
									.acceptCall(CallDispatcher.sb);
						}
					}).start();

					CallDispatcher.conferenceMembers = new ArrayList<String>();
					CallDispatcher.conferenceMembers.add(strBuddyName);
					Log.d("test", "open Adding "
							+ CallDispatcher.conferenceMembers.size());
					Log.i("ACal", "Audio Screen will be displayedd");
					callDisp.stopRingTone();
					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = true;
					SignalingBean sb = (SignalingBean) CallDispatcher.sb
							.clone();
					ShowConnectionScreen(sb);
					finishactivity();

				} else if (sbaen.getCallType().equals("VC")) {
					Log.d("call", "start alert");
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine
									.acceptCall(CallDispatcher.sb);
						}
					}).start();
					Log.i("ACal", "Video Screen will be displayed");

					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = true;
					CallDispatcher.conferenceMembers = new ArrayList<String>();

					CallDispatcher.conferenceMembers.add(strBuddyName);

					Log.d("test", "on video call received ^^^^^^^^^"
							+ CallDispatcher.conferenceMembers.size());
					CallDispatcher.buddySignall.put(strBuddyName,
							CallDispatcher.sb);
					// openVideoCallScreen();
					SignalingBean sb = (SignalingBean) CallDispatcher.sb
							.clone();
					ShowConnectionScreen(sb);
					finishactivity();

				}

				else if (sbaen.getCallType().equals("ABC")) {
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine.acceptCall(sbaen);
						}
					}).start();

					CallDispatcher.pagingMembers = new ArrayList<String>();

					CallDispatcher.conferenceMembers = new ArrayList<String>();
					CallDispatcher.conferenceMembers.add(strBuddyName);

					CallDispatcher.pagingMembers.add(sbaen.getTo());
					Log.d("PM", "s " + sbaen.getTo());

					CallDispatcher.buddySignall.put(sbaen.getTo(), sbaen);
					callDisp.stopRingTone();
					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = false;

					SignalingBean sb = (SignalingBean) sbaen.clone();
					ShowConnectionScreen(sb);
					finishactivity();
				} else if (sbaen.getCallType().equals("VBC")) {
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine.acceptCall(sbaen);
						}
					}).start();

					CallDispatcher.pagingMembers = new ArrayList<String>();

					CallDispatcher.pagingMembers.add(sbaen.getTo());
					Log.d("PM", "s " + sbaen.getTo());
					CallDispatcher.buddySignall.put(sbaen.getTo(), sbaen);

					CallDispatcher.conferenceMembers = new ArrayList<String>();
					CallDispatcher.conferenceMembers.add(strBuddyName);
					callDisp.stopRingTone();

					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = false;

					CallDispatcher.isCallInProgress = true;

					CallDispatcher.hsAddedBuddyNameFromConferenceCall = new HashMap<String, SignalingBean>();
					if (CallDispatcher.callType == null) {
						CallDispatcher.callType = "VBC";
					}
					if (CallDispatcher.audioProperties == null) {
						CallDispatcher.audioProperties = new AudioProperties(
								this);
					}
					SignalingBean sb = (SignalingBean) sbaen.clone();
					ShowConnectionScreen(sb);
					finishactivity();
				} else if (sbaen.getCallType().equalsIgnoreCase("AP")) {

					//
					CallDispatcher.sb = sbaen;
					CallDispatcher.isCallInitiate = false;

					Log.d("call", "start alert");
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");

					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine
									.acceptCall(CallDispatcher.sb);
						}
					}).start();

					CallDispatcher.conferenceMembers = new ArrayList<String>();
					CallDispatcher.conferenceMembers.add(from);

					callDisp.stopRingTone();
					callDisp.isHangUpReceived = false;
					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = true;
					SignalingBean sb = (SignalingBean) CallDispatcher.sb
							.clone();
					ShowConnectionScreen(sb);
					finishactivity();
					//

				} else if (sbaen.getCallType().equalsIgnoreCase("VP")) {

					Log.i("thread", "came to listall notification.....");

					CallDispatcher.sb = sbaen;
					CallDispatcher.isCallInitiate = false;
					callDisp.isHangUpReceived = false;

					Log.d("call", "start alert");
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");

					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine
									.acceptCall(CallDispatcher.sb);
						}
					}).start();

					CallDispatcher.conferenceMembers = new ArrayList<String>();
					CallDispatcher.conferenceMembers.add(from);

					callDisp.stopRingTone();
					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = true;

					SignalingBean sb = (SignalingBean) CallDispatcher.sb
							.clone();
					ShowConnectionScreen(sb);
					finishactivity();
				} else if (sbaen.getCallType().equals("SS")) {
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine.acceptCall(sbaen);
						}
					}).start();

					CallDispatcher.pagingMembers = new ArrayList<String>();

					CallDispatcher.pagingMembers.add(sbaen.getTo());
					Log.d("PM", "s " + sbaen.getTo());
					CallDispatcher.buddySignall.put(sbaen.getTo(), sbaen);

					CallDispatcher.conferenceMembers = new ArrayList<String>();
					CallDispatcher.conferenceMembers.add(strBuddyName);
					callDisp.stopRingTone();

					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = false;

					CallDispatcher.isCallInProgress = true;

					CallDispatcher.hsAddedBuddyNameFromConferenceCall = new HashMap<String, SignalingBean>();
					if (CallDispatcher.callType == null) {
						CallDispatcher.callType = "SS";
					}
					if (CallDispatcher.audioProperties == null) {
						CallDispatcher.audioProperties = new AudioProperties(
								this);
					}
					SignalingBean sb = (SignalingBean) sbaen.clone();
					ShowConnectionScreen(sb);
					finishactivity();
				}

			} else {
				finishactivity();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			callDisp.stopRingTone();
		}
	}

	public void finishactivity() {
		this.finish();
	}

	public void openAudiocallScreen() {
		Intent aintent = new Intent(this, AudioCallScreen.class);
		aintent.putExtra("buddy", to);
		aintent.putExtra("buddyname", from);
		aintent.putExtra("isreceiver", true);
		startActivity(aintent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (WebServiceReferences.contextTable.containsKey("alertscreen")) {
			WebServiceReferences.contextTable.remove("alertscreen");
		}
		/* lock.reenableKeyguard(); */
		if (callDisp.wl != null) {
			Log.v("a", "Releasing wakelock");
			try {

				callDisp.wl.release();
			} catch (Throwable th) {
				th.printStackTrace();
			}
		} else {
			// should never happen during normal workflow
			Log.e("aa", "Wakelock reference is null");
		}

		super.onDestroy();
	}

	private void ShowConnectionScreen(final SignalingBean sbean) {
		final String from = sbean.getTo();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method
				// stub
				callDisp.stopRingTone();
			}
		}).start();

		try {
			Intent intent = new Intent(this, CallConnectingScreen.class);
			Bundle bundle = new Bundle();
			bundle.putString("name", from);
			bundle.putString("type", sbean.getCallType());
			bundle.putBoolean("status", true);
			bundle.putSerializable("bean", sbean);
			intent.putExtras(bundle);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			AlertDialog alert = null;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			String ask = SingleInstance.mainContext.getResources().getString(
					R.string.need_call_hangup);

			builder.setMessage(ask)
					.setCancelable(false)
					.setPositiveButton(
							SingleInstance.mainContext.getResources()
									.getString(R.string.yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									rejectCall();
								}
							})
					.setNegativeButton(
							SingleInstance.mainContext.getResources()
									.getString(R.string.no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			alert = builder.create();
			alert.show();

		}
		return super.onKeyDown(keyCode, event);
	}

	public void putxmlobj(String key, Object obj) {
		if (xmlmap.containsKey(key)) {
			xmlmap.remove(key);
			xmlmap.put(key, obj);
		} else {
			xmlmap.put(key, obj);
		}
	}

}