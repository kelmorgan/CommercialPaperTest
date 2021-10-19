package com.cp.utils;

import com.fbn.service.Service;
import com.newgen.iforms.custom.IFormReference;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class Shared implements Constants {
    private static final Logger logger = LogGenerator.getLoggerInstance(Shared.class);
    public static List<List<String>> resultSet;
    public static int validate;
    public static String message;

    /************************* COMMERCIAL PAPER CODE BEGINS **************************/
    //test push
    private String getTat (String entryDate, String exitDate){
        SimpleDateFormat sdf = new SimpleDateFormat(dbDateTimeFormat);
        try {
            Date d1 = sdf.parse(entryDate);
            Date d2 = sdf.parse(exitDate);

            long difference_In_Time = d2.getTime() - d1.getTime();
            long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
            logger.info("getTat method -- tat-- "+ difference_In_Days + " days, " + difference_In_Hours + " hrs, " + difference_In_Minutes + " mins, " + difference_In_Seconds + " sec");

            return  difference_In_Days + " days, " + difference_In_Hours + " hrs, " + difference_In_Minutes + " min, " + difference_In_Seconds + " sec";
        }
        catch (ParseException e) {
            e.printStackTrace();
            logger.error("Exception occurred in getTat method-- "+ e.getMessage());
        }
        return null;
    }
    private static void setDecisionHistory(IFormReference ifr, String staffId, String process, String marketType, String decision,
                                    String remarks, String prevWs, String entryDate, String exitDate, String tat){
        JSONArray jsRowArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(dhRowStaffId,staffId);
        jsonObject.put(dhRowProcess,process);
        jsonObject.put(dhRowMarketType,marketType);
        jsonObject.put(dhRowDecision,decision);
        jsonObject.put(dhRowRemarks,remarks);
        jsonObject.put(dhRowPrevWs,prevWs);
        jsonObject.put(dhRowEntryDate,entryDate);
        jsonObject.put(dhRowExitDate,exitDate);
        jsonObject.put(dhRowTat,tat);
        jsRowArray.add(jsonObject);

        ifr.addDataToGrid(decisionHisTable, jsRowArray);
    }
    public String getUsersMailsInGroup(IFormReference ifr, String groupName){
        StringBuilder groupMail= new StringBuilder();
        try {
            resultSet = new DbConnect(ifr, new Query().getUsersInGroup(groupName)).getData();
            for (List<String> result : resultSet)
                groupMail.append(result.get(0)).append(endMail).append(",");
        } catch (Exception e){
            logger.error("Exception occurred in getUsersMailInGroup Method-- "+ e.getMessage());
            return null;
        }
        logger.info("getUsersMailsGroup method --mail of users-- "+ groupMail.toString().trim());
        return groupMail.toString().trim();
    }
    public void setCpDecisionHistory (IFormReference ifr){
        String marketType = getCpMarketName(ifr);
        String remarks = (String)ifr.getValue(cpRemarksLocal);
        String entryDate = (String)ifr.getValue(entryDateLocal);
        String exitDate = getCurrentDateTime();
        setDecisionHistory(ifr,getLoginUser(ifr),commercialProcessName,marketType,getCpDecision(ifr),remarks, getCurrWs(ifr),entryDate,exitDate,getTat(entryDate,exitDate));
        ifr.setValue(decHisFlagLocal,flag);
    }
    public static String getCpMarket(IFormReference ifr){return  getFieldValue(ifr,cpSelectMarketLocal);}
    public static String getProcess(IFormReference ifr){
        return getFieldValue(ifr,selectProcessLocal);
    }
    public String getCurrentDateTime (String format){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }
    public static String getCurrentDateTime (){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dbDateTimeFormat));
    }
    public static String  getCurrentDate (){
        return LocalDate.now().toString();
    }
    public static String getCpDecision (IFormReference ifr){ return getFieldValue(ifr,cpDecisionLocal);}
    public static String getWorkItemNumber (IFormReference ifr){
        return (String)ifr.getControlValue(wiNameLocal);
    }
    public static String getLoginUser(IFormReference ifr){
        return ifr.getUserName().toUpperCase();
    }
    public static String getCurrWs(IFormReference ifr){
        return ifr.getActivityName();
    }
    public static String getPrevWs (IFormReference ifr){return getFieldValue(ifr,prevWsLocal);}
    public void setGenDetails(IFormReference ifr){
        setFields(ifr,new String[]{solLocal,loginUserLocal}, new String[]{getSol(ifr),getLoginUser(ifr)});
    }
    public void hideProcess(IFormReference ifr){
        ifr.setTabStyle(processTabName,commercialTab,visible,False);
        ifr.setTabStyle(processTabName,treasuryTab,visible,False);
        ifr.setTabStyle(processTabName,omoTab,visible,False);
    }
    public void hideShowDashBoardTab(IFormReference ifr,String state){ifr.setTabStyle(processTabName,dashboardTab,visible,state);}
    public void selectProcessSheet(IFormReference ifr){
        hideShowBackToDashboard(ifr,True);
        hideShowDashBoardTab(ifr, False);
        if(getProcess(ifr).equalsIgnoreCase(commercialProcess)) {
            ifr.setTabStyle(processTabName, commercialTab, visible, True);
            ifr.setTabStyle(processTabName, treasuryTab, visible, False);
            ifr.setTabStyle(processTabName, omoTab, visible, False);
        }
        else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) {
            ifr.setTabStyle(processTabName, treasuryTab, visible, True);
            ifr.setTabStyle(processTabName, commercialTab, visible, False);
            ifr.setTabStyle(processTabName, omoTab, visible, False);
        }
        else if (getProcess(ifr).equalsIgnoreCase(omoProcess)){
            ifr.setTabStyle(processTabName,omoTab,visible,False);
            ifr.setTabStyle(processTabName,commercialTab,visible,False);
            ifr.setTabStyle(processTabName,treasuryTab,visible,False);
        }
        else {
            ifr.setTabStyle(processTabName,omoTab,visible,False);
            ifr.setTabStyle(processTabName,commercialTab,visible,False);
            ifr.setTabStyle(processTabName,treasuryTab,visible,False);
            hideShowBackToDashboard(ifr,False);
            hideShowDashBoardTab(ifr, True);
        }
    }
    public void hideShowBackToDashboard(IFormReference ifr, String state){
        hideShow(ifr,new String[]{goBackDashboardSection},state);
    }
    public void showSelectedProcessSheet(IFormReference ifr){
        logger.info("showSelectedProcessMethod -- selected process -- "+getProcess(ifr));
        hideShowDashBoardTab(ifr,False);
        if(getProcess(ifr).equalsIgnoreCase(commercialProcess)) {
            ifr.setTabStyle(processTabName, commercialTab, visible, True);
            ifr.setTabStyle(processTabName, treasuryTab, visible, False);
            ifr.setTabStyle(processTabName, omoTab, visible, False);
        }
        else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) {
            ifr.setTabStyle(processTabName, treasuryTab, visible, True);
            ifr.setTabStyle(processTabName, commercialTab, visible, False);
            ifr.setTabStyle(processTabName, omoTab, visible, False);
        }
        else if (getProcess(ifr).equalsIgnoreCase(omoProcess)){
            ifr.setTabStyle(processTabName,omoTab,visible,True);
            ifr.setTabStyle(processTabName,commercialTab,visible,False);
            ifr.setTabStyle(processTabName,treasuryTab,visible,False);
        }
        else {
            ifr.setTabStyle(processTabName,omoTab,visible,False);
            ifr.setTabStyle(processTabName,commercialTab,visible,False);
            ifr.setTabStyle(processTabName,treasuryTab,visible,False);
        }
    }
    public void showCommercialProcessSheet(IFormReference ifr){
        hideShowDashBoardTab(ifr,False);
        ifr.setTabStyle(processTabName,omoTab,visible,False);
        ifr.setTabStyle(processTabName,commercialTab,visible,True);
        ifr.setTabStyle(processTabName,treasuryTab,visible,False);
    }
    public static String getSol (IFormReference ifr){
        try {
            return new DbConnect(ifr, new Query().getSolQuery(getLoginUser(ifr))).getData().get(0).get(0);
        } catch (Exception e){ return empty;}
    }
    public static void setBranchDetails(IFormReference ifr){
        resultSet = new DbConnect(ifr, Query.getUserDetailsQuery(getLoginUser(ifr))).getData();
        setFields(ifr, new String[]{loginUserLocal,solLocal,branchNameLocal}, new String[]{getLoginUser(ifr),resultSet.get(0).get(0), resultSet.get(0).get(1)});
    }
    public void hideCpSections (IFormReference ifr){
        setInvisible(ifr,new String []{cpServiceSection,cpTerminationDetailsSection,cpLienSection,cpMandateTypeSection, cpReDiscountRateSection,cpBranchPriSection,cpBranchSecSection,cpLandingMsgSection,cpMarketSection,cpPrimaryBidSection,cpProofOfInvestSection,
        cpTerminationSection,cpCutOffTimeSection,cpDecisionSection,cpTreasuryPriSection,cpTreasurySecSection,cpTreasuryOpsPriSection,cpUtilityFailedPostSection,cpPostSection,cpSetupSection,cpCustomerDetailsSection,cpPmIssuerSection,cpPbBeneDetailsSection,cpChargesSection,cpWindowDetailsSection,cpCustomerIncomeSection});
    }
    public void disableCpSections (IFormReference ifr){
        disableFields(ifr,new String []{cpBranchPriSection,cpBranchSecSection,cpLandingMsgSection,cpMarketSection,cpPrimaryBidSection,cpProofOfInvestSection, cpTerminationSection,
                cpCutOffTimeSection,cpTreasuryPriSection,cpTreasurySecSection,cpTreasuryOpsPriSection,cpUtilityFailedPostSection,cpPostSection});
    }
    public void hideShowLandingMessageLabel(IFormReference ifr,String state){ifr.setStyle(landMsgLabelLocal,visible,state);}
    public void hideShow (IFormReference ifr, String[] fields,String state) { for(String field: fields) ifr.setStyle(field,visible,state);}
    public static void setFields (IFormReference ifr, String [] locals,String [] values){
        for (int i = 0; i < locals.length; i++) ifr.setValue(locals[i], values[i]);
    }
    public static void setFields (IFormReference ifr, String local,String value){
        ifr.setValue(local,value);
    }
    public void setDropDown (IFormReference ifr,String local, String [] values){
        ifr.clearCombo(local);
        for (String value: values) ifr.addItemInCombo(local,value,value);
        clearFields(ifr,local);
    }
    public void setDropDown (IFormReference ifr,String local,String [] labels, String [] values){
        ifr.clearCombo(local);
        for (int i = 0; i < values.length; i++) ifr.addItemInCombo(local,labels[i],values[i]);
    }
    public  void setDecision (IFormReference ifr,String decisionLocal, String [] values){
        ifr.clearCombo(decisionLocal);
        for (String value: values) ifr.addItemInCombo(decisionLocal,value,value);
        clearFields(ifr,new String[]{decisionLocal});
    }
    public  void setDecision (IFormReference ifr,String decisionLocal,String [] labels, String [] values){
        ifr.clearCombo(decisionLocal);
        for (int i = 0; i < values.length; i++) ifr.addItemInCombo(decisionLocal,labels[i],values[i]);
        clearFields(ifr,new String[]{decisionLocal});
    }
    public void addToDropDown (IFormReference ifr,String local,String [] labels, String [] values){
        for (int i = 0; i < values.length; i++) ifr.addItemInCombo(local,labels[i],values[i]);
    }
    public static void disableFields(IFormReference ifr, String [] fields) { for(String field: fields) ifr.setStyle(field,disable, True); }
    public static void disableFields(IFormReference ifr, String field) { ifr.setStyle(field,disable, True); }
    public static void clearFields(IFormReference ifr, String [] fields) { for(String field: fields) ifr.setValue(field, empty); }
    public static void setVisible(IFormReference ifr, String[] fields) { for(String field: fields) ifr.setStyle(field,visible, True);}
    public static void setInvisible(IFormReference ifr, String [] fields ) { for(String field: fields) ifr.setStyle(field,visible, False); }
    public static void setInvisible(IFormReference ifr, String field ) { ifr.setStyle(field,visible, False); }
    public static void enableFields(IFormReference ifr, String [] fields) {for(String field: fields) ifr.setStyle(field,disable, False);}
    public static void enableFields(IFormReference ifr, String field) {ifr.setStyle(field,disable, False);}
    public static void setMandatory(IFormReference ifr, String []fields) { for(String field: fields) ifr.setStyle(field,mandatory, True);}
    public static void undoMandatory(IFormReference ifr, String [] fields) { for(String field: fields) ifr.setStyle(field,mandatory, False); }
    public static void clearTables(IFormReference ifr, String [] tables){for (String table: tables) ifr.clearTable(table);}
    public static void clearTables(IFormReference ifr, String table){ifr.clearTable(table);}
    public static String getFieldValue(IFormReference ifr, String local){return ifr.getValue(local).toString();}
    public static boolean isEmpty(String s) {return s == null || s.trim().isEmpty();}
    public void backToDashboard(IFormReference ifr){
        hideProcess(ifr);
        hideShowDashBoardTab(ifr,True);
        hideShowBackToDashboard(ifr,False);
    }
    public void clearDecHisFlag (IFormReference ifr){clearFields(ifr,new String[]{decHisFlagLocal});}
    public String getSetupFlag(IFormReference ifr){return getFieldValue(ifr,windowSetupFlagLocal);}
    public void setCpCategory(IFormReference ifr, String [] values){
        ifr.clearCombo(cpCategoryLocal);
        for (String value: values)
            ifr.addItemInCombo(cpCategoryLocal,value,value);
    }
    public static String getCpCategory(IFormReference ifr){return getFieldValue(ifr,cpCategoryLocal);}
    public static void setCpDecisionValue (IFormReference ifr, String value){ifr.setValue(cpDecisionLocal,value);}
    public String getCpUpdateMsg (IFormReference ifr){return getFieldValue(ifr,cpUpdateLocal);}
    public String getCpMarketName (IFormReference ifr){
        if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) return primary;
        else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) return secondary;
        return empty;
    }
    public boolean compareDate(String startDate, String endDate){
      return  LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern(dbDateTimeFormat)).isAfter(LocalDateTime.parse(startDate,DateTimeFormatter.ofPattern(dbDateTimeFormat)));
    }
    public String getCpOpenDate(IFormReference ifr){return getFieldValue(ifr,cpOpenDateLocal);}
    public String getCpCloseDate(IFormReference ifr){return getFieldValue(ifr,cpCloseDateLocal);}

    public String getCpPmCusRefNo(IFormReference ifr){return getFieldValue(ifr,cpPmCustomerIdLocal);}

    public String getCpLandingMsg (IFormReference ifr){return getFieldValue(ifr,cpLandMsgLocal);}
    public String getPmMinPrincipal(IFormReference ifr){return getFieldValue(ifr,cpPmMinPriAmtLocal);}
    public String cpSetupPrimaryMarketWindow(IFormReference ifr){
        String winRefNo =  generateCpWinRefNo(cpPmLabel);
          validate = new DbConnect(ifr,new Query().getSetupMarketWindowQuery(winRefNo,getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),getCpLandingMsg(ifr),getPmMinPrincipal(ifr),getCpPmIssuerName(ifr),getCpOpenDate(ifr),getCpCloseDate(ifr))).saveQuery();
       if (validate > 0) {
           setFields(ifr,new String[]{cpWinRefNoLocal,windowSetupFlagLocal},new String[]{winRefNo,flag});
           disableFields(ifr, new String[]{cpTreasuryPriSection,cpCutOffTimeSection,cpMarketSection,cpSetupSection});
            message = "Window for Commercial Paper primary market has been setup, bidding can commence. <br> Cut-Off time: "+getCpCloseDate(ifr)+".";
            new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
           return apiSuccess;
       }
       else return "Unable to setup window. Kindly contact iBPS support.";
    }
    public String generateCpWinRefNo(String cpLabel) {
        return cpLabel + getCurrentDateTime(cpRefNoDateFormat);
    }
    public String getWindowSetupFlag (IFormReference ifr){return getFieldValue(ifr,windowSetupFlagLocal);}
    public String getCurrentWorkStep(IFormReference ifr){
        return ifr.getActivityName();
    }
    public boolean isCpWindowActive(IFormReference ifr){
        return Integer.parseInt(new DbConnect(ifr,
                Query.getCheckActiveWindowQuery(commercialProcessName,getCpMarket(ifr))).getData().get(0).get(0)) > 0;
    }
    public void setCpPmWindowDetails (IFormReference ifr){
        resultSet = new DbConnect(ifr,new Query().getActiveWindowDetailsQuery(commercialProcessName,getCpMarket(ifr))).getData();
        setFields(ifr, new String[]{cpWinRefNoLocal,landMsgLabelLocal,cpPmMinPriAmtBranchLocal,cpPmIssuerNameLocal},
                new String[]{resultSet.get(0).get(0),resultSet.get(0).get(1),resultSet.get(0).get(2),resultSet.get(0).get(3)});
    }
    public void setCpSmWindowDetails(IFormReference ifr){
      resultSet = new DbConnect(ifr,new Query().getActiveWindowDetailsQuery(commercialProcessName,getCpMarket(ifr))).getData();
        setFields(ifr, new String[]{cpWinRefNoLocal,landMsgLabelLocal,cpSmMinPrincipalBrLocal},
                new String[]{resultSet.get(0).get(0),resultSet.get(0).get(1),resultSet.get(0).get(2)});
    }
    public static String getCpPmRateType (IFormReference ifr){
        return getFieldValue(ifr,cpPmRateTypeLocal);
    }
    public static String getCpPmWinPrincipalAmt(IFormReference ifr){return getFieldValue(ifr,cpPmMinPriAmtBranchLocal);}
    public static String getCpPmCustomerPrincipal(IFormReference ifr){return getFieldValue(ifr,cpPmPrincipalLocal);}
    public static int getCpPmTenor (IFormReference ifr){
        return Integer.parseInt(getFieldValue(ifr,cpPmTenorLocal));
    }
    public void  cpGenerateCustomerId (IFormReference ifr){
     setFields(ifr,cpCustomerIdLocal,cpIdLabel+getUserSol(ifr)+getCpRandomId());
    }
    public static String getUtilityFlag (IFormReference ifr){
        return getFieldValue(ifr,utilityFlagLocal);
    }
    private String getCpRandomId(){
        return getFormattedString(Calculator.getRandomNumber());
    }
    public String getUserSol(IFormReference ifr){return getFieldValue(ifr,solLocal);}
    public boolean cpCheckWindowStateById(IFormReference ifr,String id){
        return Integer.parseInt(new DbConnect(ifr,Query.getCheckActiveWindowByIdQuery(id)).getData().get(0).get(0)) > 0;
    }

    public String setupCpSmBid(IFormReference ifr){
        logger.info("Setup Cp Secondary Market Bid Method");

         validate = new DbConnect(ifr,new Query().getSetupCpSmBidQuery(
                getCurrentDateTime(),getCpCustomerId(ifr), getCpWinRefId(ifr),getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),
                getCpAcctNo(ifr),getCpAcctName(ifr),getCpAcctEmail(ifr),getCpSmPrincipalBr(ifr),
                getCpSmTenor(ifr),getCpSmRate(ifr),getCpSmMaturityDate(ifr),getCpSmInvestmentType(ifr),getCpInterestAtMaturity(ifr),getCpPrincipalAtMaturity(ifr), getCpSmInvestmentId(ifr))).saveQuery();

        logger.info("is saved -- "+ validate);
        if (isSaved(validate)){
            disableFields(ifr,cpInvestBtn);
            disableFields(ifr,new String[] {cpDecisionLocal,cpInvestBtn});
            setCpDecisionValue(ifr,decApprove);

            return "Customer Bid has been invested, Thank You.";
        }
        return "Customer Bid cannot be invested right now. Try again later or Contact iBPS support";
    }
    public static String getCpAcctNo(IFormReference ifr){return getFieldValue(ifr,cpCustomerAcctNoLocal);}
    public static String getCpAcctName(IFormReference ifr){return getFieldValue(ifr,cpCustomerNameLocal);}
    public static String getCpAcctEmail(IFormReference ifr){return getFieldValue(ifr,cpCustomerEmailLocal);}
    public static String getCpAcctSol(IFormReference ifr){return getFieldValue(ifr,cpCustomerSolLocal);}
    public static String getCpSmInvestmentId(IFormReference ifr){return getFieldValue(ifr,cpSmInvestmentIdLocal);}
    public static String getCpSmMaturityDate(IFormReference ifr){return getFieldValue(ifr,cpSmMaturityDateBrLocal);}
    public static void setTableGridData(IFormReference ifr, String tableLocal, String [] columns, String [] rowValues){
        JSONArray jsRowArray = new JSONArray();
        jsRowArray.add(setTableRows(columns,rowValues));
        ifr.addDataToGrid(tableLocal,jsRowArray);
    }
    private static JSONObject setTableRows(String [] columns, String [] rowValues){
        JSONObject jsonObject = new JSONObject();
        for (int i = 0 ; i < columns.length; i++)
            jsonObject.put(columns[i],rowValues[i]);

        return jsonObject;
    }
    public String getDownloadFlag (IFormReference ifr){
        return getFieldValue(ifr,downloadFlagLocal);
    }
    public boolean checkBidStatus(String rate, String cpRate){
        return Calculator.getFormattedFloat(rate) <= Calculator.getFormattedFloat(cpRate);
    }
    public boolean checkBidStatus(double rate, double cbnRate){
        return rate <= cbnRate;
    }
    public static String getMaturityDate(int tenor){
        return LocalDate.now().plusDays(tenor).toString();
    }
    public static String getMaturityDate(String startDate,int tenor){
        return LocalDate.parse(startDate).plusDays(tenor).toString();
    }
    public static String getMaturityDate(String startDate,int tenor,String dateFormat){
        return LocalDate.parse(startDate,DateTimeFormatter.ofPattern(dateFormat)).plusDays(tenor).toString();
    }
    public static boolean isLeapYear (){
        return LocalDate.now().isLeapYear();
    }
    public static boolean isLeapYear (String date){
            return LocalDate.parse(date).isLeapYear();
    }
    public static String getCpSmInvestmentSetupType(IFormReference ifr){return getFieldValue(ifr,cpSmSetupLocal);}
    public static long getDaysToMaturity(String maturityDate){
        return ChronoUnit.DAYS.between(LocalDate.now(),LocalDate.parse(maturityDate));
    }
    public static long getDaysBetweenTwoDates(String startDate, String endDate){
        return ChronoUnit.DAYS.between(LocalDate.parse(startDate),LocalDate.parse(endDate));
    }
    public String cpSetupSecondaryMarketWindow(IFormReference ifr, int rowCount){
        setCpSmCloseDate(ifr);
         validate = new DbConnect(ifr,new Query().getSetupMarketWindowQuery(getCpWinRefId(ifr),getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),getCpLandingMsg(ifr),getFieldValue(ifr,cpSmMinPrincipalLocal),getCpOpenDate(ifr),getCpCloseDate(ifr))).saveQuery();
        if (isSaved(validate)){
            setCpSmInvestmentOnSetup(ifr,rowCount);
            setWindowSetupFlag(ifr);
            disableFields(ifr,new String[]{cpSetupWindowBtn,cpDecisionLocal});
            setFields(ifr,cpDecisionLocal,decApprove);
           return apiSuccess;
        }
        else return "Unable to setup window. Kindly contact iBPS support.";
    }
    private String generateCpSmInvestmentId(){
        return cpSmIdInvestmentLabel+getCpRandomId();
    }
    public static String  getSelectedCpSmInvestmentId(IFormReference ifr){
        return getFieldValue(ifr,cpSmInvestmentIdLocal);
    }
    private void setCpSmCloseDate(IFormReference ifr){
        String time = " 14:00:01";
        setFields(ifr,cpCloseDateLocal,LocalDate.now().toString() +time);
    }
    private void setCpSmInvestmentOnSetup(IFormReference ifr, int rowCount){
        for( int i = 0; i < rowCount; i++){
            String corporateName = ifr.getTableCellValue(cpSmCpBidTbl,i,0);
            String description = ifr.getTableCellValue(cpSmCpBidTbl,i,1);
            String maturityDate = ifr.getTableCellValue(cpSmCpBidTbl,i,2);
            String billAmount = ifr.getTableCellValue(cpSmCpBidTbl,i,3);
            String rate = ifr.getTableCellValue(cpSmCpBidTbl,i,4);
            String tenor = ifr.getTableCellValue(cpSmCpBidTbl,i,5);
            String dtm = ifr.getTableCellValue(cpSmCpBidTbl,i,6);
            String status = ifr.getTableCellValue(cpSmCpBidTbl,i,7);
            String guaranteedCp = ifr.getTableCellValue(cpSmCpBidTbl,i,9);

            new DbConnect(ifr,new Query().getSetSmInvestmentQuery(generateCpSmInvestmentId(),getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),getCpWinRefId(ifr),
                    corporateName,description,maturityDate,billAmount,billAmount,rate,tenor,dtm,status,guaranteedCp,getCpOpenDate(ifr),getCpCloseDate(ifr),getCurrentDateTime())).saveQuery();
        }
    }
    public void setWindowSetupFlag (IFormReference ifr){
        setFields(ifr,windowSetupFlagLocal,flag);
    }
    public String getCpSmConcessionRate(IFormReference ifr){
        return getFieldValue(ifr,cpSmConcessionRateLocal);
    }
    public String getCpSmConcessionRateValue(IFormReference ifr){
        return getFieldValue(ifr,cpSmConcessionRateValueLocal);
    }
    public void clearTable(IFormReference ifr, String tableLocal){
        ifr.clearTable(tableLocal);
    }
    public static String getCpSmPrincipalBr(IFormReference ifr){
        return getFieldValue(ifr,cpSmPrincipalBrLocal);
    }
    public static String getCpSmWindowMinPrincipal(IFormReference ifr){
        return getFieldValue(ifr,cpSmMinPrincipalBrLocal);
    }
    public boolean isDateEqual (String date1, String date2){
        return LocalDate.parse(date1).isEqual(LocalDate.parse(date2));
    }
    public static String getCpMandateType(IFormReference ifr){
        return getFieldValue(ifr,cpMandateTypeLocal);
    }
    public String getCpMandateToTerminate(IFormReference ifr){
        return getFieldValue(ifr,cpTermMandateLocal);
    }
    public static boolean isCpLien(IFormReference ifr, String id){
        return Integer.parseInt(new DbConnect(ifr,Query.getCpLienStatusQuery(id)).getData().get(0).get(0)) > 0;
    }
    public static String getCpTerminationType(IFormReference ifr){
        return getFieldValue(ifr,cpTerminationTypeLocal);
    }
    public static String getCpTermSpecialRateValue(IFormReference ifr){
        return getFieldValue(ifr,cpTermSpecialRateValueLocal);
    }
    public static String getCpTermSpecialRate(IFormReference ifr){
        return getFieldValue(ifr,cpTermSpecialRateLocal);
    }
    public static String getCpTermDtm(IFormReference ifr){
        return getFieldValue(ifr,cpTermDtmLocal);
    }
    public static String getCpTermCusId(IFormReference ifr){
        return getFieldValue(ifr,cpTermCustIdLocal);
    }
    public static String getCpTermPartialAmt(IFormReference ifr){
        return  getFieldValue(ifr,cpTermPartialAmountLocal);
    }
    public static boolean getCpTermIsSpecialRate(IFormReference ifr){
        return  getCpTermSpecialRate(ifr).equalsIgnoreCase(True);
    }
    public static boolean isEmpty(List<List<String>> resultSet){
        return  resultSet.size() == 0;
    }
    public static String getCpPartialTermOption(IFormReference ifr){
        return getFieldValue(ifr,cpTermPartialOptionLocal);
    }
    public static  String getCpLienType (IFormReference ifr){
        return getFieldValue(ifr,cpLienTypeLocal);
    }
    public static String getCpLienMandateId(IFormReference ifr){
        return getFieldValue(ifr, cpLienMandateIdLocal);
    }
    public static boolean doesCpIdExist(IFormReference ifr, String id, String marketType){
        return Integer.parseInt(new DbConnect(ifr,Query.getCpCusIdExistQuery(id,marketType)).getData().get(0).get(0)) > 0;
    }
    public  static String getCpPoiMandate(IFormReference ifr){
        return getFieldValue(ifr,cpPoiMandateLocal);
    }
    public static String getCpPoiCusId(IFormReference ifr){
        return getFieldValue(ifr,cpPoiCustIdLocal);
    }
    public static String getCpTermRate(IFormReference ifr){return getFieldValue(ifr,cpTermRateLocal);}
    public static String getCpTermBoDate(IFormReference ifr){return getFieldValue(ifr,cpTermBoDateLocal);}
    public static String getCpOtp(IFormReference ifr){return  getFieldValue(ifr,cpTokenLocal);}
    public static  String getCpPmInvestmentType(IFormReference ifr){return  getFieldValue(ifr,cpPmInvestmentTypeLocal);}
    public static String getCpSmInvestmentType (IFormReference ifr){return getFieldValue(ifr,cpSmInvestmentTypeLocal);}
    public static String getCpRemarks(IFormReference ifr){return getFieldValue(ifr,cpRemarksLocal);}
    public static String getCpPmIssuerName(IFormReference ifr){
        return getFieldValue(ifr,cpPmIssuerNameLocal);
    }
    public static void setTableCellValue(IFormReference ifr,String tableName, int rowIndex, int columnIndex,String value){
        ifr.setTableCellValue(tableName,rowIndex,columnIndex,value);
    }
    public static String getTableCellValue(IFormReference ifr,String tableName,int rowIndex, int columnIndex){
        return ifr.getTableCellValue(tableName,rowIndex,columnIndex);
    }
    public static void setCpPmTotalAllocation(IFormReference ifr){
        String totalAllocation = new DbConnect(ifr,Query.getCpPmTotalAllocation(getWorkItemNumber(ifr))).getData().get(0).get(0);
        setFields(ifr,cpPmTotalAllocLocal,totalAllocation);
        new DbConnect(ifr,Query.getCpPmUpdateTotalAllocation(getWorkItemNumber(ifr),totalAllocation)).saveQuery();
    }

    public static String getCpPmTotalAllocation(IFormReference ifr){
        return getFieldValue(ifr,cpPmTotalAllocLocal);
    }
    public static String getCpPmSettlementDate(IFormReference ifr){
        return getFieldValue(ifr,cpPmSettlementDateLocal);
    }
    public static void isPbSol(IFormReference ifr, String sol){
        int isPbSol = Integer.parseInt(new DbConnect(ifr,Query.getPBSolQuery(sol)).getData().get(0).get(0));
        if (isPbSol > 0)
            setFields(ifr,pbFlagLocal,flag);
    }
    public static String getPbFlag(IFormReference ifr){
        return getFieldValue(ifr,pbFlagLocal);
    }
    public static String getCpCustodyFee(IFormReference ifr){
        return getFieldValue(ifr,cpCustodyFeeLocal);
    }
    public static String getCpIsStdCustodyFeeStatus(IFormReference ifr){
        return getFieldValue(ifr,cpIsStdCustodyFeeLocal);
    }
    public static String getCpPbBeneName(IFormReference ifr){
        return getFieldValue(ifr,cpPbBeneName);
    }
    public static String getCpPbBeneAcctNo(IFormReference ifr){
        return getFieldValue(ifr,cpPbBeneAcctNo);
    }
    public void cpConfigureCharges(IFormReference ifr){
        enableFields(ifr,new String[]{cpVatLocal,cpTxnFeeLocal,cpCommissionLocal});
    }
    public static String getCpVat(IFormReference ifr){
        return getFieldValue(ifr,cpVatLocal);
    }
    public static String getCpCommission(IFormReference ifr){
        return getFieldValue(ifr,cpCommissionLocal);
    }
    public static String getCpTxnFee(IFormReference ifr){
        return getFieldValue(ifr,cpTxnFeeLocal);
    }
    public static String getCpWinRefId(IFormReference ifr){
        return getFieldValue(ifr,cpWinRefNoLocal);
    }
    public static String getFormattedString(float value){
        return String.format("%.2f",value);
    }
    public static String getFormattedString(long value){
        return String.valueOf(value);
    }
    public static String getFormattedString(int value){
        return String.valueOf(value);
    }
    public static String getCpCustomerId(IFormReference ifr){
        return getFieldValue(ifr,cpCustomerIdLocal);
    }
    public static boolean isCpPrimaryMarket(IFormReference ifr){
        return getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket);
    }
    public static boolean isCpSecondaryMarket(IFormReference ifr){
        return getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket);
    }
    public static String cpUpdateSmInvestment(IFormReference ifr){
        if (isCpFlagNotSet(ifr)) {
            validate = new DbConnect(ifr, Query.getCpSmUpdateInvestment(getCpSmInvestmentId(ifr), getCpSmPrincipalBr(ifr))).saveQuery();
            logger.info("No of rows: " + validate);
            if (!isSaved(validate)) {
                String availableAmount = new DbConnect(ifr, Query.getCpSmAvailableAmount(getCpSmInvestmentId(ifr))).getData().get(0).get(0);
                return "Principal cannot be greater than the available amount for this investment. Available Amount : " + availableAmount + "";
            }
            setCpFlag(ifr);
        }
       return empty;
    }
    public static boolean isCpProcess(IFormReference ifr){
        return getProcess(ifr).equalsIgnoreCase(commercialProcess);
    }
    public static String cpSmCheckPrincipal(IFormReference ifr){
        if (isAmountNotInThousands(getCpSmPrincipalBr(ifr))) {
            clearFields(ifr, cpSmPrincipalBrLocal);
            return "Principal must be in thousands";
        }
        if(isValidPrincipal(getCpSmPrincipalBr(ifr),getCpSmWindowMinPrincipal(ifr))) {
            clearFields(ifr, cpSmPrincipalBrLocal);
            return "Principal cannot be less than Window minimum amount";
        }
        if (!isCpSmAmountAvailable(ifr)) {
            String availableAmount = new DbConnect(ifr,Query.getCpSmAvailableAmount(getCpSmInvestmentId(ifr))).getData().get(0).get(0);
            clearFields(ifr, cpSmPrincipalBrLocal);
            return "Principal cannot be greater than the available amount for this investment. Available Amount : "+availableAmount+"";
        }
        return empty;
    }

    public static boolean isAmountNotInThousands(String amount){
        return Calculator.getModulusOfThousands(amount) > 0;
    }

    private static boolean isCpSmAmountAvailable (IFormReference ifr){
        return Integer.parseInt(new DbConnect(ifr,Query.getCpSmIsAmountAvailable(getCpSmInvestmentId(ifr),getCpSmPrincipalBr(ifr))).getData().get(0).get(0)) > 0;

    }
    public static boolean isCpDecisionApprove(IFormReference ifr){
        return getCpDecision(ifr).equalsIgnoreCase(decApprove);
    }
    public static boolean isCpDecisionSubmit(IFormReference ifr){
        return getCpDecision(ifr).equalsIgnoreCase(decSubmit);
    }

    public static boolean isCpDecisionReturn(IFormReference ifr){
        return getCpDecision(ifr).equalsIgnoreCase(decReturn);
    }
    public static boolean isCpDecisionReject(IFormReference ifr){
        return getCpDecision(ifr).equalsIgnoreCase(decReject);
    }
    public static LocalDate getDate(String date){
        return (!isEmpty(date)) ? LocalDate.parse(date) : null;
    }
    public static LocalDate getDate(String date,String format){
        return (!(isEmpty(date) && isEmpty(format))) ? LocalDate.parse(date,DateTimeFormatter.ofPattern(format)) : null;
    }

    public static LocalDateTime getDateTime(String date){
        return (!isEmpty(date)) ? LocalDateTime.parse(date) : null;
    }
    public static LocalDateTime getDateTime(String date,String format){
        return (!(isEmpty(date) && isEmpty(format))) ? LocalDateTime.parse(date,DateTimeFormatter.ofPattern(format)) : null;
    }
    public static void setCpFlag(IFormReference ifr){
        setFields(ifr,cpFlagLocal,flag);
        new DbConnect(ifr,Query.getSetCpFlag(getWorkItemNumber(ifr))).saveQuery();
    }
    public static void clearCpFlag(IFormReference ifr){
        clearFields(ifr,cpFlagLocal);
        new DbConnect(ifr,Query.getClearCpFlag(getWorkItemNumber(ifr))).saveQuery();
    }
    public static String getCpFlag(IFormReference ifr){
        return getFieldValue(ifr,cpFlagLocal);
    }
    public static boolean isCpFlagNotSet(IFormReference ifr){
        return isEmpty(getCpFlag(ifr));
    }
    public static boolean isPrevWs(IFormReference ifr,String workStep){
        return getPrevWs(ifr).equalsIgnoreCase(workStep);
    }
    public static boolean isCurrWs(IFormReference ifr,String workStep){
        return getCurrWs(ifr).equalsIgnoreCase(workStep);
    }
    public void setCpSmInvestmentGrid(IFormReference ifr){
        clearTables(ifr,cpSmInvestmentBrTbl);
        resultSet = new DbConnect(ifr,new Query().getCpSmInvestmentsQuery(getCpWinRefId(ifr))).getData();
        for (List<String> result : resultSet){
            String id = result.get(0);
            String corporateName = result.get(1);
            String description = result.get(2);
            String maturityDate = result.get(3);
            String dtm = result.get(4);
            String status = result.get(5);
            String availableAmount = result.get(6);
            String rate = result.get(7);
            String amountSold = result.get(8);
            String mandates = result.get(9);

            setTableGridData(ifr,cpSmInvestmentBrTbl,new String[]{cpSmBidInvestmentIdCol,cpSmBidIssuerCol,cpSmBidDescCol,cpSmBidMaturityDateCol,cpSmBidDtmCol,cpSmBidStatusCol,cpSmBidAvailableAmountCol,cpSmBidRateCol,cpSmBidAmountSoldCol,cpSmBidMandatesCol},
                    new String[]{id,corporateName,description,maturityDate,dtm,status,availableAmount,rate,amountSold,mandates});
        }
    }
    public static String getCpPrincipalAtMaturity(IFormReference ifr){
        return getFieldValue(ifr,cpPrincipalAtMaturityLocal);
    }
    public static String getCpResidualInterest(IFormReference ifr){
        return getFieldValue(ifr,cpResidualInterestLocal);
    }
    public static String getCpResidualInterestFlag(IFormReference ifr){
        return getFieldValue(ifr,cpResidualInterestFlagLocal);
    }
    public static String getCpInterestAtMaturity(IFormReference ifr){
        return getFieldValue(ifr,cpInterestAtMaturityLocal);
    }
    public static String getCpSmTenor(IFormReference ifr){
        return getFieldValue(ifr,cpSmTenorLocal);
    }
    public static String getCpSmRate(IFormReference ifr){
        return getFieldValue(ifr,cpSmRateLocal);
    }
    public static void setCpSmCustomerIncome(IFormReference ifr){
        String interest = getFormattedString(Calculator.getCpSmBidInterest(getCpSmTenor(ifr),getCpSmRate(ifr),getCpSmPrincipalBr(ifr),getCpSmMaturityDate(ifr)));
        String principalAtMaturity = getFormattedString(Calculator.getCpSmBidPrincipalAtMaturity(getCpSmPrincipalBr(ifr),interest,getCpSmRate(ifr),getCpSmTenor(ifr)));
        if (isCpSmInvestmentType(ifr,cpSmInvestmentTypePrincipalInterest)){
            float residualInterest = Calculator.getModulusOfThousands(interest);
            if (residualInterest > 0) setFields(ifr,new String[]{cpResidualInterestLocal,cpResidualInterestFlagLocal},new String[]{getFormattedString(residualInterest),flag});
        }
        setFields(ifr,new String[]{cpPrincipalAtMaturityLocal,cpInterestAtMaturityLocal},new String[]{principalAtMaturity,interest});
    }
    public static boolean isCpSmInvestmentType(IFormReference ifr,String type){
        return getCpSmInvestmentType(ifr).equalsIgnoreCase(type);
    }

    public static void hideTbSections (IFormReference ifr){hideFields(ifr,allTbSections);}
    public static void disableField(IFormReference ifr, String field) {ifr.setStyle(field,disable,True);}
    public static void clearFields(IFormReference ifr, String field) {ifr.setValue(field,empty);}
    public static void setVisible(IFormReference ifr, String field) { ifr.setStyle(field,visible,True);}
    public static void hideField(IFormReference ifr, String field) {ifr.setStyle(field,visible,False);}
    public static void hideFields(IFormReference ifr, String [] fields ) { for(String field: fields) ifr.setStyle(field,visible,False); }
    public static void enableField(IFormReference ifr, String field) {ifr.setStyle(field,disable,False);}
    public static void setMandatory(IFormReference ifr, String field) { ifr.setStyle(field,mandatory,True); }
    public static void undoMandatory(IFormReference ifr, String field) { ifr.setStyle(field,mandatory,False); }
    public static void setWiName(IFormReference ifr){
	    setFields(ifr,wiNameFormLocal,getWorkItemNumber(ifr));
	}
	public static String getCpIsStdCustodyFee (IFormReference ifr){
        return getFieldValue(ifr,cpIsStdCustodyFeeLocal);
    }
    public static void checkCpIsStdCustodyFee(IFormReference ifr){
        if (getCpIsStdCustodyFee(ifr).equalsIgnoreCase(no)){
            clearFields(ifr,cpCustodyFeeLocal);
            enableFields(ifr,cpCustodyFeeLocal);
            setMandatory(ifr,cpCustodyFeeLocal);
        }
        else {
            undoMandatory(ifr,cpCustodyFeeLocal);
            disableFields(ifr,cpCustodyFeeLocal);
            setFields(ifr,cpCustodyFeeLocal,LoadProp.custodyFee);
        }
    }
    public static boolean isSaved(int value){
        return value > 0;
    }
    public static boolean isValidPrincipal(String principal, String winPrincipal){
        return Calculator.getFormattedFloat(principal) < Calculator.getFormattedFloat(winPrincipal);
    }

    public String cpPmCheckPrincipal (IFormReference ifr){
        if (isAmountNotInThousands(getCpPmCustomerPrincipal(ifr))) {
            clearFields(ifr,cpPmPrincipalLocal);
            return principalNotInThousandMsg;
        }

        else if (isValidPrincipal(getCpPmCustomerPrincipal(ifr),getCpPmWinPrincipalAmt(ifr))) {
            clearFields(ifr,cpPmPrincipalLocal);
            return minPrincipalErrorMsg;
        }
        return empty;
    }

    public void cpCalculateTermination(IFormReference ifr){
        try {
            resultSet = new DbConnect(ifr, Query.getCpBidDtlForTerminationQuery(getCpTermCusId(ifr), getCpMarket(ifr))).getData();

            String maturityDate = resultSet.get(0).get(1).trim();
            boolean isLeapYear = isLeapYear(maturityDate);
            String reDiscountRate = empty;
            int dtm = Calculator.getFormattedInteger(getCpTermDtm(ifr));
            if (getCpTermIsSpecialRate(ifr))
                reDiscountRate = getFieldValue(ifr, cpTermSpecialRateValueLocal);
            else if (dtm <= 90)
                reDiscountRate = getFieldValue(ifr, cpReDiscountRateLess90Local);
            else if (dtm <= 180)
                reDiscountRate = getFieldValue(ifr, cpReDiscountRate91To180Local);
            else if (dtm <= 270)
                reDiscountRate = getFieldValue(ifr, cpReDiscountRate181To270Local);
            else if (dtm <= 364)
                reDiscountRate = getFieldValue(ifr, cpReDiscountRate271To364Local);


            if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypeFull)) {
                String principal = resultSet.get(0).get(0);

                float amountDue = Calculator.getCpTermAmountDue(principal,reDiscountRate,getCpTermDtm(ifr));

                if (isLeapYear) amountDue = Calculator.getCpTermAmountDueForLeapYear(getFormattedString(amountDue),principal,reDiscountRate,getCpTermDtm(ifr));

                setFields(ifr, new String[]{cpTermAmountDueLocal,cpTermRateLocal}, new String[]{getFormattedString(amountDue),reDiscountRate});
            } else if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypePartial)) {
                if (isLeapYear) {
                    float adjustedPrincipal = Calculator.getCpPartialTermAdjustedPrincipalForLeapYear(getCpTermPartialAmt(ifr),reDiscountRate,getCpTermDtm(ifr));
                    float amountDue = Calculator.getCpPartialTermAmountDueForLeapYear(getFormattedString(adjustedPrincipal),reDiscountRate,getCpTermDtm(ifr));
                    setFields(ifr, new String[]{cpTermAmountDueLocal, cpTermAdjustedPrincipalLocal,cpTermRateLocal}, new String[]{String.valueOf(amountDue), String.valueOf(adjustedPrincipal),reDiscountRate});
                } else {
                    float adjustedPrincipal = Calculator.getCpPartialTermAdjustedPrincipal(getCpTermPartialAmt(ifr),reDiscountRate,getCpTermDtm(ifr));
                    float amountDue = Calculator.getPartialTermAmountDue(getFormattedString(adjustedPrincipal),reDiscountRate,getCpTermDtm(ifr));
                    setFields(ifr, new String[]{cpTermAmountDueLocal, cpTermAdjustedPrincipalLocal,cpTermRateLocal}, new String[]{getFormattedString(amountDue), getFormattedString(adjustedPrincipal),reDiscountRate});
                }
            }
            disableFields(ifr,cpTermCalculateBtn);
        }
        catch (Exception e){
            logger.info("Exception occurred-- "+ e.getMessage());
            e.printStackTrace();
        }
    }

    public static String cpCheckPmTenor(IFormReference ifr,int tenor){
        if (tenor < 7 || tenor > 270) {
            clearFields(ifr,cpPmTenorLocal);
            return tenorErrorMsg;
        }
        return null;
    }
    public void cpSmSetConcessionRate(IFormReference ifr){
        clearFields(ifr,cpSmConcessionRateValueLocal);
        if (getCpSmConcessionRate(ifr).equalsIgnoreCase(yes)) {
            setVisible(ifr, cpSmConcessionRateValueLocal);
            setMandatory(ifr, cpSmConcessionRateValueLocal);
            enableField(ifr,cpSmConcessionRateValueLocal);
        }
        else{
            setInvisible(ifr, cpSmConcessionRateValueLocal);
            undoMandatory(ifr, cpSmConcessionRateValueLocal);
            disableField(ifr,cpSmConcessionRateValueLocal);
        }
    }
    public String cpSmCheckMaturityDate(IFormReference ifr ,int rowIndex){
        String maturityDateInvestmentTbl = ifr.getTableCellValue(cpSmInvestmentBrTbl,rowIndex,3).trim();
        String maturityDate = getFieldValue(ifr,cpSmMaturityDateBrLocal);
        if (!isDateEqual(maturityDate,maturityDateInvestmentTbl)) {
            clearFields(ifr,cpSmMaturityDateBrLocal);
            return cpSmMaturityDateErrMsg + " Investment Date: " + maturityDateInvestmentTbl + "";
        }
        return empty;
    }
    public void cpSelectTerminationType (IFormReference ifr){
        if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypeFull)){
            setVisible(ifr, new String[]{cpTermSpecialRateLocal,cpTermCalculateBtn,cpTermAdjustedPrincipalLocal,cpTermAmountDueLocal});
            setMandatory(ifr, new String[]{cpTermCalculateBtn});
            enableFields(ifr, new String[]{cpTermSpecialRateLocal,cpTermCalculateBtn});
        }
        else if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypePartial)){
            setVisible(ifr, new String[]{cpTermSpecialRateLocal,cpTermPartialOptionLocal,cpTermPartialAmountLocal,cpTermCalculateBtn,cpTermAdjustedPrincipalLocal,cpTermAmountDueLocal});
            setMandatory(ifr, new String[]{cpTermPartialOptionLocal,cpTermPartialAmountLocal});
            enableFields(ifr, new String[]{cpTermSpecialRateLocal,cpTermPartialOptionLocal,cpTermPartialAmountLocal,cpTermCalculateBtn});
        }
        else {
            setInvisible(ifr, new String[]{cpTermSpecialRateLocal,cpTermCalculateBtn,cpTermPartialOptionLocal,cpTermPartialAmountLocal,cpTermAdjustedPrincipalLocal,cpTermAmountDueLocal});
            undoMandatory(ifr, new String[]{cpTermPartialOptionLocal,cpTermPartialAmountLocal});
            disableFields(ifr, new String[]{cpTermSpecialRateLocal,cpTermCalculateBtn,cpTermPartialOptionLocal,cpTermPartialAmountLocal});
            clearFields(ifr, new String[]{cpTermSpecialRateLocal,cpTermPartialOptionLocal,cpTermPartialAmountLocal});
        }
    }
    public String cpValidateLienMandate(IFormReference ifr){
        if (!doesCpIdExist(ifr,getCpLienMandateId(ifr),getCpMarket(ifr))) {
            clearFields(ifr, cpLienMandateIdLocal);
            return "CP ID does not exist. Check and enter the correct ID.";
        }
        return null;
    }
    public static boolean isCpTerminateType(IFormReference ifr,String terminateType){
        return getCpTerminationType(ifr).equalsIgnoreCase(terminateType);
    }
    public static boolean isCpMandateType(IFormReference ifr, String mandateType){
        return getCpMandateType(ifr).equalsIgnoreCase(mandateType);
    }
    public static boolean isCpCategoryType(IFormReference ifr,String categoryType){
        return getCpCategory(ifr).equalsIgnoreCase(categoryType);
    }
    public static void cpTerminateBid(IFormReference ifr){
        if (isCpTerminateType(ifr,cpTerminationTypeFull)){
            new DbConnect(ifr,Query.getCpTerminateBid(getCpTermCusId(ifr))).saveQuery();
            routeFullTerminatedBids(ifr,initializeService(configPath));
        }
        else if (isCpTerminateType(ifr,cpTerminationTypePartial)){
            new DbConnect(ifr,Query.getCpPartialTerminateBid(getCpCustomerId(ifr),getCpAdjustedPrincipal(ifr))).saveQuery();
        }
    }

    public static String getCpAdjustedPrincipal(IFormReference ifr){
        return getFieldValue(ifr,cpTermAdjustedPrincipalLocal);
    }

    public static Service initializeService(String configPath){
        Service.setConfigPath(configPath);
        return new Service(Service.getSessionId());
    }
    public static Service initializeService(String configPath,String cabinetName,String userName, String passWord){
        Service.setConfigPath(configPath);
        return new Service(Service.getSessionId(userName,passWord));
    }
    public static String getDataByCoordinates(List<List<String>> resultSet, int row, int column){
       return  resultSet.get(row).get(column);
    }
    public static void routeFullTerminatedBids(IFormReference ifr, Service service){
        String wiName = getDataByCoordinates(new DbConnect(ifr,Query.getBidWiNameForTermination(getCpTermCusId(ifr))).getData(),0,0) ;
        String column = "terminateflag";
        String condition = "winame = '"+wiName+"'";
        service.completeWorkItem(wiName,externalTable,column,flag,condition);
        service.disconnectCabinet();
    }
}
