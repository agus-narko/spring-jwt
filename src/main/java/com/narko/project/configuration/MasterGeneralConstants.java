package com.narko.project.configuration;

import java.text.SimpleDateFormat;

public class MasterGeneralConstants {

    //status
    public static final String YES  = "Y";
    public static final String NO  = "N";
    public static final String NOT_AUTHORIZED  = "NA";
    public static final String IN_PROGRESS  = "IN PROGRESS";
    public static final String APPROVED  = "APPROVED";
    public static final String DRAFT  = "DRAFT";
    public static final String REJECTED  = "REJECTED";
    public static final String REQUEST  = "REQUEST";
    public static final String APPROVAL = "APPROVAL";
    public static final String USER = "USER";



    public static final String STATUS_SUCCESS = "S";
    public static final String STATUS_ERROR = "E";
    public static final String STATUS_FAILED = "F";

    public static final String ACTIVATE = "ACTIVE";
    public static final String DEACTIVATE = "DEACTIVE";
    public static final String RUNNING = "RUNNING";
    public static final String STOP = "STOP";
    public static final String EXPIRED = "EXPIRED";
    public static final String CONST = "CONST";
    public static final String EDITABLE = "EDITABLE";
//    public static final String ACTIVATE = "ACTIVE";
//    public static final String DEACTIVATE = "DEACTIVE";
    public static final String DONE = "D";
    public static final String LOGIN = "L";

    public static final String SECRET_KEY  = "iniRahasia";

    //activity
    public static final String CREATE = "CREATE";
    public static final String READ = "READ";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";
    public static final String ADD = "ADD";
    public static final String REPLACE = "REPLACE";
    public static final String COPY = "COPY";

    //BO DATA
    public static final String DATAPROCESS = "DataProcess";
    public static final String DATATRANSACTION = "DataTransaction";
    public static final String DATAFUNCTION = "DataFunction";


    public static final String EMPTY_VALUE = "";
    public static final String SPACE_VALUE = " ";
    public static final String STRIP_VALUE = "-";

    public static final Long NULL_REF_VALUE_LONG = -99L;

    public static final String NO_PARAM = "NO PARAM";
    public static final String ID_PARAM = "ID PARAM";
    public static final String ACTIVE_PARAM = "ACTIVE PARAM";
    public static final String OTHER_PARAM = "OTHER PARAM";
    public static final String INVALID_PARAM = "INVALID PARAM";
    
    
    public static final String DATE_FORMAT_STR = "yyyy-MM-dd";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STR);
    
    
    public static final String CHANNEL = "CHANNEL";
    public static final String PRODUCT = "PRODUCT";
    public static final String FINANCIAL = "FINANCIAL";
    public static final String NASABAH = "NASABAH";
    public static final String AKUMULASI = "AKUMULASI";
    public static final String MATURITY_DATE = "MD";
    public static final String MONTHLY = "M";
    public static final String CASHBACK = "CASHBACK";
    public static final String PROCESS_LOGIN = "LOGIN";
    public static final String PROCESS_LOGOUT = "LOGOUT";
    
    public static final String ADD_APPROVAL_RESOURCES = "addApprovalResources";
    
    
    public static final String CONST_COL = "COL_";
    public static final String CONST_TABLE = "TABLE_NAME";
    public static final String CONST_GET_DATA = "GET_DATA";
    public static final String CONST_ID = "id";

    
    public static final String PACK_MODEL = "com.narko.project.model.";
    public static final String PACK_MODEL_REQ = "com.narko.project.payload.request.";
    public static final String PACK_MODEL_RESP = "com.narko.project.payload.response.";


    




    



}
