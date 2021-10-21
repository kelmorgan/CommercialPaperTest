package com.cp.utils;

public class Query {
    public String getSolQuery(String userName) {
        return "select sole_id from usr_0_fbn_usr_branch_mapping where upper(user_id) = upper('" + userName + "')";
    }
    public static String getUserDetailsQuery(String userName) {
        return "select sole_id, branch_name from usr_0_fbn_usr_branch_mapping where upper(user_id) = upper('" + userName + "')";
    }
    public String getUsersInGroup(String groupName) {
        return "select username from pdbuser where userindex in (select userindex from pdbgroupmember where groupindex = (select groupindex from PDBGroup where GroupName='" + groupName + "'))";
    }
    public static String getMailQuery(String wiName, String sendMail, String copyMail, String mailSubject, String mailMessage) {
        return "insert into wfmailqueuetable (" +
                "mailfrom," +
                "mailto," +
                "mailcc," +
                "mailsubject," +
                "mailmessage," +
                "mailcontenttype," +
                "mailpriority," +
                "insertedby," +
                "mailactiontype," +
                "insertedtime," +
                "processdefid," +
                "processinstanceid," +
                "workitemid," +
                "activityid," +
                "mailstatus) " +
                "values (" +
                "'" + LoadProp.mailFrom + "'," +
                "'" + sendMail + "'," +
                "'" + copyMail + "'," +
                "'" + mailSubject + "'," +
                "'" + mailMessage + "'," +
                "'text/html;charset=UTF-8'," +
                "1," +
                "'System'," +
                "'TRIGGER'," +
                "SYSDATE," +
                "" + LoadProp.processDefId + "," +
                "'" + wiName + "'," +
                "1," +
                "1," +
                "'N')";
    }
    public String getSetupMarketWindowQuery(String refId, String wiName, String process, String marketType, String landingMessage,String minimumPrincipal, String openDate, String closeDate) {
        return "insert into mm_setup_tbl (REFID,WINAME,PROCESS,MARKETTYPE,LANDINGMESSAGE,MINPRINCIPALAMOUNT,OPENDATE,CLOSEDATE)" +
                " values ('" + refId + "','" + wiName + "','" + process + "','" + marketType + "','" + landingMessage + "','"+minimumPrincipal+"','" + openDate + "','" + closeDate + "')";
    }

    public String getSetupMarketWindowQuery(String refId, String wiName, String process, String marketType, String landingMessage,String minimumPrincipal,String issuerName, String openDate, String closeDate) {
        return "insert into mm_setup_tbl (REFID,WINAME,PROCESS,MARKETTYPE,LANDINGMESSAGE,MINPRINCIPALAMOUNT,CPISSUERNAME,OPENDATE,CLOSEDATE)" +
                "values ('" + refId + "','" + wiName + "','" + process + "','" + marketType + "','" + landingMessage + "','"+minimumPrincipal+"','"+issuerName+"','" + openDate + "','" + closeDate + "')";
    }
    public static String getCheckActiveWindowQuery(String process, String marketType) {
        return "select count(*) from mm_setup_tbl where process = '" + process + "' and markettype ='" + marketType + "' and  closeflag = 'N'";
    }

    public String getActiveWindowDetailsQuery(String process, String markType) {
        return "select refid, landingmessage, minprincipalamount,CPISSUERNAME from mm_setup_tbl where process = '" + process + "' and markettype ='" + markType + "' and  closeflag = 'N'";
    }

    public static String getCheckActiveWindowByIdQuery(String winRefId) {
        return "select COUNT(closeflag) from mm_setup_tbl where refid = '" + winRefId + "' and closeflag = 'N'";
    }
    public static String getSetupCpPmBidQuery(String reqDate, String custRefId, String winRefId, String wiName, String process, String marketType, String custAcctNo, String custName, String custEmail, String custPrincipal, String tenor, String rateType, String rate, String investmentType,String issuerName,String custodyFee) {
        return "insert into mm_bid_tbl (reqDate,custRefId,winRefId,bidwiname,process,marketType,custAcctNo,custName,custEmail,custPrincipal,tenor,rateType,rate,investmenttype,CPISSUERNAME,CUSTODYFEE) values ('" + reqDate + "','" + custRefId + "','" + winRefId + "','" + wiName + "', '" + process + "', '" + marketType + "', '" + custAcctNo + "','" + custName + "','" + custEmail + "','" + custPrincipal + "','" + tenor + "','" + rateType + "','" + rate + "','"+investmentType+"','"+issuerName+"','"+custodyFee+"')";
    }
    public static String getSetupCpPmBidQuery(String reqDate, String custRefId, String winRefId, String wiName, String process, String marketType, String custAcctNo, String custName, String custEmail, String custPrincipal, String tenor, String rateType, String rate, String investmentType,String issuerName,String custodyFee,String pbBeneName, String pbBeneAcctNo) {
        return "insert into mm_bid_tbl (reqDate,custRefId,winRefId,bidwiname,process,marketType,custAcctNo,custName,custEmail,custPrincipal,tenor,rateType,rate,investmenttype,CPISSUERNAME,CUSTODYFEE,PBBENENAME,PBBENEACCTNO,PBFLAG) values ('" + reqDate + "','" + custRefId + "','" + winRefId + "','" + wiName + "', '" + process + "', '" + marketType + "', '" + custAcctNo + "','" + custName + "','" + custEmail + "','" + custPrincipal + "','" + tenor + "','" + rateType + "','" + rate + "','"+investmentType+"','"+issuerName+"','"+custodyFee+"','"+pbBeneName+"','"+pbBeneAcctNo+"','Y')";
    }

    public String getSetupCpSmBidQuery(String reqDate, String custRefId, String winRefId, String wiName, String process, String marketType, String custAcctNo, String custName, String custEmail, String custPrincipal, String tenor, String rate,String maturityDate, String investmentType, String interest, String principalAtMaturity,String investmentId) {
        return "insert into mm_bid_tbl (reqDate,custRefId,winRefId,bidwiname,process,marketType,custAcctNo,custName,custEmail,custPrincipal,tenor,rate,maturitydate,investmentType,interest,principalatmaturity,SMINVESTMENTID) values ('" + reqDate + "','" + custRefId + "','" + winRefId + "','" + wiName + "', '" + process + "', '" + marketType + "', '" + custAcctNo + "','" + custName + "','" + custEmail + "','" + custPrincipal + "','" + tenor + "','" + rate + "', '"+maturityDate+"','"+investmentType+"','"+interest+"','"+principalAtMaturity+"','"+investmentId+"')";
    }


    public static String getCpPmBidsToProcessQuery () {
        return "select custrefid, tenor, rate, ratetype from mm_bid_tbl where process = 'Commercial Paper' and markettype= 'primary' and processflag ='N' and groupindexflag = 'N'";
    }
    public static String getCpPmUpdateBidsQuery (String id, String utilityWiName, String groupIndex){
        return "update mm_bid_tbl set utilitywiname = '" + utilityWiName + "', groupindex = '" + groupIndex + "', groupindexflag = 'Y' , processflag = 'Y' where custrefid = '" + id + "'";
    }
    public String getCpPmSummaryBidsQuery(String utilityWiName){
        return "select tenor , rate , sum(custprincipal) as TotalAmount ,ratetype, count(*) as TransactionCount, groupindex from mm_bid_tbl where utilitywiname = '" + utilityWiName + "' group by tenor, rate ,ratetype,groupindex";
    }
    public String getCpPmGroupBidsQuery(String groupIndex){
        return "select custrefid,custacctno, custname, tenor, rate, custprincipal, status, maturitydate , bidstatus,rateType , cprate from mm_bid_tbl where groupindex = '"+groupIndex+"'";
    }
    public String getCpPmBidDetailByIdQuery(String id, String detail){
        return "select "+detail+" from mm_bid_tbl where custrefid = '"+id+"'";
    }
    public String getCpPmBidUpdateBankQuery(String id, String cpRate, String bankRate, String maturityDate, String bidStatus, String status){
        return "update mm_bid_tbl set cprate = '"+cpRate+"', rate = '"+bankRate+"', maturitydate = '"+maturityDate+"', bidstatus = '"+bidStatus+"', status = '"+status+"', failedflag = 'N', terminateflag = 'N' where custrefid = '"+id+"' ";
    }
    public String getCpPmBidUpdatePersonalQuery(String id, String cpRate, String maturityDate, String bidStatus, String status){
        return "update mm_bid_tbl set cprate = '"+cpRate+"', maturitydate = '"+maturityDate+"', bidstatus = '"+bidStatus+"', status = '"+status+"', failedflag = 'N', terminateflag = 'N' where custrefid = '"+id+"' ";
    }
    public String getCpPmUpdateFailedBidsQuery(String id, String bidStatus){
        return "update mm_bid_tbl set bidstatus = '"+bidStatus+"', cprate = '', maturitydate = ''  , status = 'Awaiting Reversal', failedflag = 'Y', terminateflag = 'Y' where custrefid = '"+id+"'";
    }
    public String getSetSmInvestmentQuery(String id, String wiName, String process, String marketType, String winRef,String corporateName,String description,String maturityDate,String billAmount,String availableAmount,String rate,String tenor,String dtm,String status,String guaranteedCp,String openDate, String closeDate, String date){
        return "insert into mm_sminvestments_tbl (investmentid,winame,process,marketType,windowrefno,corporateissuername,description,maturitydate,billamount,availableamount,rate,tenor,dtm,status,guaranteedcp,openDate,closeDate,investmentdate) values" +
                " ('"+id+"','"+wiName+"','"+process+"','"+marketType+"','"+winRef+"','"+corporateName+"','"+description+"','"+maturityDate+"','"+billAmount+"','"+availableAmount+"','"+rate+"','"+tenor+"','"+dtm+"', '"+status+"','"+guaranteedCp+"','"+openDate+"','"+closeDate+"','"+date+"')";
    }
    public String getCpSmInvestmentsQuery(String windowRefNo ){
        return "select investmentid,corporateissuername, description, maturitydate,dtm,status, availableamount,rate, totalamountsold, mandates from mm_sminvestments_tbl " +
                "where windowrefno = '"+windowRefNo+"'  and closedflag = 'N' and maturedflag = 'N' and cancelledflag = 'N'";
    }

    public static String getCpSmInvestmentsSelectQuery(String id){
        return "select tenor, rate,availableamount,totalamountsold,mandates  from mm_sminvestments_tbl where investmentid = '"+id+"'";
    }
    public static String getUpdateCutoffTimeQuery(String id, String closeDate){
        return "update mm_setup_tbl set closedate = '"+closeDate+"' where refid = '"+id+"'";
    }

    //get all solids
    public static String getPBSolQuery(String solid) {
    	return "select sol_id from PRIVATEBANKING_SOL_TBL where sol_id ='"+solid+"'";
    }

    public static String getUpdateReDiscountRateQuery(String id,String redicsountless90, String rediscount91180, String rediscount181270, String rediscount271364){
        return "update mm_setup_tbl set REDISCOUNTRATELESS90 = '"+redicsountless90+"', REDISCOUNTRATELESS180 = '"+rediscount91180+"', REDISCOUNTRATELESS270 = '"+rediscount181270+"', REDISCOUNTRATELESS364 = '"+rediscount271364+"' where  refid = '"+id+"'";
    }

    public static String getBidForTerminationQuery(String process, String marketType, String data){
        return "SELECT reqdate,custrefid,custprincipal,custacctno,custname,maturitydate,status,winrefid,adjustedprincipal,partialterminateflag FROM MM_BID_TBL where process = '"+process+"' and markettype = '"+marketType+"' and terminateflag = 'N' and maturedflag = 'N' and  awaitingmaturityflag = 'Y' and (custrefid = '"+data+"' or custacctno = '"+data+"' )";
    }
    public static String getBidForTerminationQuery(String id){
        return "SELECT reqdate,custrefid,custprincipal,custacctno,custname,maturitydate,status,winrefid FROM MM_BID_TBL where custrefid = '"+id+"'";
    }
    public static String getBidWiNameForTermination(String id){
        return "SELECT bidwiname FROM MM_BID_TBL where custrefid = '"+id+"'";
    }
    public static String getCpBidDtlForTerminationQuery(String id, String marketType){
        return "SELECT custprincipal,maturitydate FROM MM_BID_TBL where process = 'Commercial Paper' and markettype = '"+marketType+"' and custrefid = '"+id+"'";
    }
    public static String getCpReDiscountedRateForTermQuery(String id){
        return "select rediscountrateless90, rediscountrateless180, rediscountrateless270, rediscountrateless364 from mm_setup_tbl where refid = '"+id+"'";
    }
    public static String getCpLienStatusQuery(String id){
        return "select count (lienflag) from mm_bid_tbl where custrefid = '"+id+"' and lienflag = 'Y'";
    }
    public static String getCpCusIdExistQuery(String id, String marketType){
        return "select count(custRefId) from mm_bid_tbl where custrefid = '"+id+"' and process = 'Commercial Paper' and marketType = '"+marketType+"'";
    }
    public static String getCpLienProcessQuery(String id, String marketType, String flag){
        return "update mm_bid_tbl set lienFlag = '"+flag+"' where custrefid = '"+id+"' and process = 'Commercial Paper' and markettype = '"+marketType+"'";
    }
    public static String getCpPoiMandateSearchQuery(String marketType, String data){
        return "select reqdate, custrefid,custprincipal,custacctno,custname,status from mm_bid_tbl where process = 'Commercial Paper' and marketType = '"+marketType+"' and (custacctno = '"+data+"' or custrefid = '"+data+"')";
    }
    public static String getCpPoiDtlQuery(String id){
        return "select reqdate,custrefid,custprincipal,custacctno,custname,principalatmaturity,interest,maturitydate,tenor,rate from mm_bid_tbl where custrefid = '"+id+"'";
    }
    public static String getCpTermDetailsQuery(String id){
        return "select tenor,maturitydate,custprincipal from mm_bid_tbl where custrefid = '"+id+"'";
    }
    public static String getCpUpdatePmSuccessBidQuery(String utilityWiName,String vat,String commission, String txnFee){
        return "update mm_bid_tbl set allocatedflag = 'Y' , VAT = '"+vat+"', COMMISION = '"+commission+"', TXNFEE = '"+txnFee+"' where utilitywiname = '"+utilityWiName+"' and failedflag = 'N'";
    }
    public static String getCpPmAllocatedBids(String utilityWiName){
        return "select custrefid,custprincipal,tenor,rate,maturitydate from mm_bid_tbl where utilitywiname = '"+utilityWiName+"' and allocatedflag = 'Y'";
    }
    public static String getCpPmUpdateAllocatedBids(float interest,float principalAtMaturity, String id){
        return "update mm_bid_tbl set interest = "+interest+" , principalatmaturity = "+principalAtMaturity+" where custrefid = '"+id+"'";
    }
    public static String getCpPmCheckUnallocatedBids(String wiName){
        return "select count(*) from mm_bid_tbl where status is null and utilitywiname = '"+wiName+"'";
    }
    public static String getCpPmUpdateTotalAllocation(String wiName,String totalAllocation){
        return "update moneymarket_ext set cppmtotalalloc = "+totalAllocation+" where winame = '"+wiName+"'";
    }
    public static String getCpPmTotalAllocation(String wiName){
        return "select nvl(sum(custprincipal),0) as totalAllocation from mm_bid_tbl where  bidstatus = 'Successful' and utilitywiname = '"+wiName+"'";
    }

    public static String getCpPmFailUnallocatedBids(String wiName){
        return "update mm_bid_tbl set bidstatus = 'Failed', cprate = '', maturitydate = ''  , status = 'Awaiting Reversal', failedflag = 'Y', terminateflag = 'Y' where utilitywiname = '"+wiName+"' and status is null";
    }

    public static String getCpPmUpdateSettlementDate(String wiName,String date){
        return "update moneymarket_ext set cppmsettldate = to_date('"+date+"','yyyy-mm-dd') where winame = '"+wiName+"'";
    }
    public static String getIsPbSol(String sol){
        return "select count(sol_id) from PRIVATEBANKING_SOL_TBL where sol_id = '"+sol+"' and isactive = 'Y'";
    }
    public static String setPbFlag(String wiName){
        return "update moneymarket_ext set pbflag = 'Y' where winame = '"+wiName+"'";
    }
    public static String getCpSmIsAmountAvailable(String investmentId,String amount){
        return "select count(*) from mm_sminvestments_tbl where availableamount >=  "+amount+" and investmentid = '"+investmentId+"'";
    }
    public static String getCpSmUpdateInvestment(String investmentId,String amount){
        return "update mm_sminvestments_tbl set availableamount = availableamount - "+amount+"  , mandates = mandates + 1, totalamountsold = totalamountsold + "+amount+" where availableamount >= "+amount+" and investmentid = '"+investmentId+"'";
    }
    public static String getCpSmUpdateRefundInvestment(String investmentId,String amount){
        return "update mm_sminvestments_tbl set availableamount = availableamount + "+amount+"  , mandates = mandates - 1, totalamountsold = totalamountsold - "+amount+" where availableamount + "+amount+" <= billamount and investmentid = '"+investmentId+"'";
    }
    public static String getCpSmAvailableAmount(String investmentId){
        return "select availableamount from mm_sminvestments_tbl where investmentid = '"+investmentId+"'";
    }
    public static String getSetCpFlag(String wiName){
        return "update moneymarket_ext set cpflag = 'Y' where winame = '"+wiName+"'";
    }
    public static String getClearCpFlag(String wiName){
        return "update moneymarket_ext set cpflag = '' where winame = '"+wiName+"'";
    }
    public static String getCpSmInvestmentQuery(String investmentId ){
        return "select investmentid,corporateissuername, description, maturitydate,dtm,status, availableamount,rate, totalamountsold, mandates  from mm_sminvestments_tbl " +
                "where investmentid = '"+investmentId+"'";
    }
    public static String getCpTerminateBid(String id){
        return "update mm_bid_tbl set terminateflag = 'Y', status = 'Terminated' where custrefid = '"+id+"'";
    }
    public static String getCpPartialTerminateBid(String id,String adjustedPrincipal){
        return "update mm_bid_tbl set adjustedprincipal = "+adjustedPrincipal+", partialterminateflag = 'Y' where custrefid = '"+id+"'";
    }
    public  static String getCpExistingTermBids(String id){
        return "select winame , g_currws from moneymarket_ext where cp_termcustid = '"+id+"' and cp_mandatetype = 'Termination' and g_currws not in ('Exit','Discard','Terminated') order by winame desc";
    }
}
