package com.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bean.NotifyListBean;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.hostedconf.AppReference;
import com.main.DashBoardFragment;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

/**
 * Created by Rajalakshmi on 30-11-2015.
 */
public class NotifyListAdapter extends ArrayAdapter<NotifyListBean> {
    private Context context;
    private Vector<NotifyListBean> fileList;
    private LayoutInflater inflater = null;
    private String filetype=null;
    DashBoardFragment dashBoardFragment = null;
    private Boolean isEntered=false;
    private Boolean isEnter=false;
    private ArrayList<String> datelist= new ArrayList<String>();

    public NotifyListAdapter(Context context, Vector<NotifyListBean> notifyList) {

        super(context, R.layout.notify_list_row, notifyList);
        this.context = context;
        this.fileList = notifyList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dashBoardFragment = DashBoardFragment.newInstance(context);
    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;
        try {
            if (view == null) {
                view = inflater.inflate(R.layout.notify_list_row, null,
                        false);
                holder = new ViewHolder();
                holder.fileType = (TextView) view
                        .findViewById(R.id.file_type);
                holder.fileName = (TextView) view
                        .findViewById(R.id.file_txt);
                holder.fileIcon = (ImageView) view
                        .findViewById(R.id.file_icon);
                holder.header = (TextView) view
                        .findViewById(R.id.file_header);
                holder.time = (TextView) view
                        .findViewById(R.id.time);
                holder.header_container = (RelativeLayout) view
                        .findViewById(R.id.header_container);
                holder.list_container = (LinearLayout)view.findViewById(R.id.linear);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            NotifyListBean notifyBean = fileList.get(position);
            if(notifyBean!=null) {
                Log.i("dateformat", "Format" + CallDispatcher.dateFormat);
                SimpleDateFormat df = new SimpleDateFormat(CallDispatcher.dateFormat + " hh:mm aa");
                SimpleDateFormat df2 = new SimpleDateFormat(
                        CallDispatcher.dateFormat);
                String[] receivedTimes = notifyBean.getSortdate().split(" ");
                Date receivedDate = null;

                if (receivedTimes[0].contains("/")
                        && CallDispatcher.dateFormat.contains("-")) {
                    SimpleDateFormat userDateFormat = new SimpleDateFormat(
                            "MM/dd/yyyy");
                    SimpleDateFormat dateFormatNeeded = new SimpleDateFormat(
                            CallDispatcher.dateFormat);
                    Date date = userDateFormat.parse(receivedTimes[0]);
                    String convertedDate = dateFormatNeeded.format(date);
                    receivedDate = dateFormatNeeded.parse(convertedDate);
                } else if (receivedTimes[0].contains("-")

                        && CallDispatcher.dateFormat.contains("/")) {
                    SimpleDateFormat userDateFormat = new SimpleDateFormat(
                            "dd-MM-yyyy");
                    SimpleDateFormat dateFormatNeeded = new SimpleDateFormat(
                            CallDispatcher.dateFormat);
                    Date date = userDateFormat.parse(receivedTimes[0]);
                    String convertedDate = dateFormatNeeded.format(date);
                    receivedDate = df2.parse(convertedDate);
                } else {
                    receivedDate = df2.parse(receivedTimes[0]);
                }
                Calendar cal = Calendar.getInstance();
                String[] todayDate = df.format(cal.getTime()).split(" ");
                for (int i = 0; i < todayDate.length; i++) {
                    Log.i("t1", "----" + todayDate[i]);
                }
                Date today = df2.parse(todayDate[0]);
                String[] yesterdayDate = getYesterdayDateString(df).split(" ");
                Date yesterday = df2.parse(yesterdayDate[0]);
                Log.i("dateformat", "receivedDate :: " + receivedDate);
                Log.i("dateformat", "today :: " + today);
                Log.i("dateformat", "yesterday :: " + yesterday);

                if (receivedDate.compareTo(today) == 0) {
                    if(!isEntered) {
                        holder.header_container.setVisibility(View.VISIBLE);
                        holder.header.setText("TODAY");
                        isEntered=true;
                    } else
                        holder.header_container.setVisibility(View.GONE);
                } else if (receivedDate.compareTo(yesterday) == 0) {
                    if(!isEnter) {
                        holder.header_container.setVisibility(View.VISIBLE);
                        holder.header.setText("YESTERDAY");
                        isEnter=true;
                    } else
                        holder.header_container.setVisibility(View.GONE);
                } else {
                    holder.header_container.setVisibility(View.VISIBLE);
                    Log.i("abcd", "=========notifyBean.getSortdate()" + notifyBean.getSortdate());
                    String formattedDate = null;
                    //Input date in String format
                    String input = notifyBean.getSortdate();
                    //Date/time pattern of input date
                    DateFormat df1 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    //Date/time pattern of desired output date
                    DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
                    Date convertdate = null;
                    String output = null;
                    try{
                        //Conversion of input String to date
                        convertdate= df1.parse(input);
                        //old date format to new date format
                        output = outputformat.format(convertdate);
                        Log.i("abcd","24 hours to 12hours"+output);
                        formattedDate=output;
                    }catch(ParseException pe){
                        pe.printStackTrace();
                        Log.i("abcd", "24 hours to 12hours exceptioon" + formattedDate);

                        formattedDate=notifyBean.getSortdate();
                    }





                    SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    SimpleDateFormat printFormat = new SimpleDateFormat("EEEE MMMM dd");
                    Date date = new Date();
                    try {
                        date = parseFormat.parse(formattedDate);
                     } catch (Exception e) {
                          e.printStackTrace();

                    }
                    Log.i("AAA","XML PARSER DATE:"+printFormat.format(date));
                    String senttime=printFormat.format(date);
                    if(datelist!=null) {
                        for(String dates:datelist) {
                            if(dates.equalsIgnoreCase(senttime))
                                holder.header_container.setVisibility(View.GONE);
                            else
                                holder.header_container.setVisibility(View.VISIBLE);
                        }
                    }
                    datelist.add(senttime);
                    holder.header.setText(senttime);
                }
                String time = notifyBean.getSortdate().split(" ")[1];
                String[] times=time.split(":");
                holder.time.setText(times[0]+":"+times[1]);
                Log.i("AAAA","NOTIFYLIST ADAPTER Values "+times);

                if (notifyBean.getNotifttype() != null) {
                    Log.i("AAAA","NOTIFYLIST ADAPTER type "+notifyBean.getNotifttype());
                    if (notifyBean.getNotifttype().trim().equalsIgnoreCase("F")) {
                        holder.fileIcon.setBackgroundResource(R.drawable.recent_files);
                        if (notifyBean.getType().trim().equalsIgnoreCase("audio"))
                            holder.fileName.setText("Audio file received");
                        else if (notifyBean.getType().trim().equalsIgnoreCase("video"))
                            holder.fileName.setText("Video file received");
                        else if (notifyBean.getType().trim().equalsIgnoreCase("photo"))
                            holder.fileName.setText("Photo file received");
                        else if (notifyBean.getType().trim().equalsIgnoreCase("note"))
                            holder.fileName.setText(" Note file received");
                        else if (notifyBean.getType().trim().equalsIgnoreCase("sketch"))
                            holder.fileName.setText("Sketch file received");
                        else if (notifyBean.getType().trim().equalsIgnoreCase("document"))
                            holder.fileName.setText("Document file received");
                        if(notifyBean.getContent()!=null)
                            holder.fileType.setText(notifyBean.getUsername()+" shared new file");
                    }
                    else if (notifyBean.getNotifttype().trim().equalsIgnoreCase("C")) {
                        holder.fileIcon.setBackgroundResource(R.drawable.recent_calls);
                        if (notifyBean.getType().trim().equalsIgnoreCase("AC"))
                            holder.fileType.setText(notifyBean.getUsername() +": AudioCall received");
                        else if (notifyBean.getType().trim().equalsIgnoreCase("VC"))
                            holder.fileType.setText(notifyBean.getUsername() +": VideoCall received");
                        else if (notifyBean.getType().trim().equalsIgnoreCase("ABC"))
                            holder.fileType.setText(notifyBean.getUsername() +": AudioBroadcastCall received");
                        else if (notifyBean.getType().trim().equalsIgnoreCase("VBC"))
                            holder.fileType.setText(notifyBean.getUsername() +": VideoBroadcastCall received");
                        else if (notifyBean.getType().trim().equalsIgnoreCase("AP"))
                            holder.fileType.setText(notifyBean.getUsername() +": AudioUnicastCall received");
                        else if (notifyBean.getType().trim().equalsIgnoreCase("VP"))
                            holder.fileType.setText(notifyBean.getUsername() +": VideoUnicastCall received");
                        if(notifyBean.getContent()!=null)
                            holder.fileName.setText(notifyBean.getContent());
                    }
                    else if (notifyBean.getNotifttype().trim().equalsIgnoreCase("I")) {
                        holder.fileIcon.setBackgroundResource(R.drawable.recent_message);
                        if(notifyBean.getType().trim().equalsIgnoreCase("image"))
                            holder.fileType.setText(notifyBean.getUsername() +": Image msg received");
                        else if(notifyBean.getType().trim().equalsIgnoreCase("text"))
                            holder.fileType.setText(notifyBean.getUsername() +": Text msg received");
                        else if (notifyBean.getType().trim().equalsIgnoreCase("audio"))
                            holder.fileType.setText(notifyBean.getUsername() +": Audio msg received");
                        else if (notifyBean.getType().trim().equalsIgnoreCase("video"))
                            holder.fileType.setText(notifyBean.getUsername() +": Video msg received");
                        else if (notifyBean.getType().trim().equalsIgnoreCase("sketch"))
                            holder.fileType.setText(notifyBean.getUsername() +": Sketch msg received");
                        if(notifyBean.getType().trim().equalsIgnoreCase("text")) {
                            if (notifyBean.getContent() != null)
                                holder.fileName.setText(notifyBean.getContent());
                        }else {
                            if (notifyBean.getMedia() != null) {
                                File newFile = new File(notifyBean.getMedia());
                                if(newFile.exists())
                                    holder.fileName.setText(notifyBean.getType() +" received");
                            }else
                                holder.fileName.setText("File Not Found");
                        }
                    }
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return view;
    }
    public static class ViewHolder {
        TextView fileName;
        TextView fileType;
        ImageView fileIcon;
        TextView header,time;
        LinearLayout list_container;
        RelativeLayout header_container;

    }
    public void filter(String charText) {
        // TODO Auto-generated method stub
        charText = charText.toLowerCase(Locale.getDefault());
        Log.i("AAAA", "DASHBORAD FILTER IF PART " + dashBoardFragment.notifyList.size());
        dashBoardFragment.tempnotifylist.clear();
        if (charText.length() == 0) {
            fileList.addAll(dashBoardFragment.seacrhnotifylist);
        } else {
            for (NotifyListBean storedData : dashBoardFragment.notifyList) {
                if (storedData.getFrom()
                        .toLowerCase(Locale.getDefault()).contains(charText) && storedData.getViewed()==0) {
                    fileList.add(storedData);
                }

            }
        }
        notifyDataSetChanged();
    }
    private String getYesterdayDateString(SimpleDateFormat dateFormat) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            Log.i("dateformat",
                    "yesterday format change :: "
                            + dateFormat.format(cal.getTime()));
            return dateFormat.format(cal.getTime());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return null;
        }
    }
    private String time(SimpleDateFormat dateFormat) {
        try {
            Calendar cal = Calendar.getInstance();
            String[] date = dateFormat.format(cal.getTime()).split(" ");
            return date[0];
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return null;
        }
    }
    private void dateConversion()
    {

    }

    }