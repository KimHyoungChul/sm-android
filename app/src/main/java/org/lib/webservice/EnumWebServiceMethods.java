package org.lib.webservice;

/**
 * EnumWebServiceMethods of enum Type. This enum is used for set state of
 * webservice call.
 * 
 * SUBSCRIBE used for Registration. SIGNIN used for Login . FINDPEOPLE used to
 * find peoples. ADDPEOPLE used to Add people. DELETEPEOPLE used to Delete
 * People. ACCEPT used to Accept People. REJECT used to Reject People. HEARTBEAT
 * used for HeartBeat SIGNOUT used for Logout ERROR used for ErrorState.
 * SHARE_REMAINDER used for SHARE_REMAINDER(Files).
 * 
 * 
 */
public enum EnumWebServiceMethods {
	SUBSCRIBE, SIGNIN, FINDPEOPLE, ADDPEOPLE, DELETEPEOPLE, ACCEPT, REJECT, HEARTBEAT, SIGNOUT, GETPROFILETEMPLATE, SETSTANDARDPROFILEFIELDVALUES, GETPROFILEFIELDVALUES, DELETEPROFILE, SHAREBYPROFILE, SECRETANSWER, FORGOTPASSWORD, SHARE_REMAINDER, BLOCKUNBLOCKBUDDY, GETSETUTILITY, GETBLOCKBUDDYLIST, SYNCUTILITYITEMS, DELETEACCESSFORM, ADDFORMRECORDS, GETFORMSTEMPLATE, GETFORMATTRIBUTE, ACCESSFORM, ACCESSMUTIPLEFORM, DELETEFORMRECORD, DELETEFORM, CREATEFORM, UPDATEFORMRECORDS, GETFORMSETTINGS, GETFORMRECORDS, OFFLINECONFIGURATION, GETMYCONFIGURATIONDETAILS, OFFLINECALLRESPONSE, RESPONSEFORCALLCONFIGURATION, GETCONFIGURATIONRESPONSEDETAILS, DELETEMYRESPONSEDETAILS, DELETEALLSHARES, CHANGEPASSWORD, SETPERMISSION, GETALLPERMISSION, SAVEMYLOCATION, CREATEGROUP,DELETEGROUP, GETGROUPANDMEMBERS, GETPARTICIPATEGROUPS, GROUPMEMBERSDELETE, LEAVEGROUP, MODIFYGROUP, SETGROUPCHATSETTING, GETGROUPCHATSETTING, SETORDELETEFORMFIELDSETTINGS, SAVESURVEYAPPLICANTFORM, GETCENTREDETAILS, GETLOANDEMANDTRANSACTION, SETLOANDEMANDTRANSACTION, SUBSCRIBEUSER, USERSIGNIN,  SETANDGETUTILITYITEMS,GETPROFESSIONS,SETPROFESSIONPERMISSION, SETUTILITYSERVICES, EDITFORM, EDITFORMFORNEWFIELDADD,NEWVERIFICATION,SUBSCRIBENEW,FORGOTPASSWORDNEW,SETMYACCOUNT,GETMYACCOUNT,RESETPASSWORD,RESETPIN,UPDATESECRETANSWER,RESETACCOUNT,SEARCHPEOPLEBYACCOUNT,DOWNLOAD,GETMYPIN,GETALLPROFILE, GETSECURITYQUESTIONS,GETMYSECRETQUESTION,GETSTATES,GETHOSPITALDETAILS,GETMEDICALSOCIETY,UPLOAD,ACCEPTREJECTGROUP,GETGROUPDETAILS,CREATEORMODIFYROUNDING,GETROUNDINGGROUPS,SETMEMBERRIGHTS,SETPATIENTRECORD,CREATETASK,SETOREDITROLEACCESS,GETPATIENTRECORDS,GETTASKINFO,GETCHATTEMPLATE,SETPATIENTCOMMENTS,SETPATIENTDESCRIPTION,GETROLEACCESS,GETPATIENTDESCRIPTION,GETPATIENTCOMMENTS,PATIENTDISCHARGE,GETMEMBERRIGHTS,GETCITIES,DELETETASK,GETFILEDETAILS,GETSPECIALTY,MEDICALSCHOOLS,SETFEEDBACK,UPDATECHATTEMPLATE,SYNC
}
