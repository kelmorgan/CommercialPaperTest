package com.cp.worksteps;

import com.cp.api.service.CpServiceHandler;
import com.cp.utils.*;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TreasuryOfficerVerifier extends Shared implements IFormServerEventHandler, SharedI {
    private static Logger logger = LogGenerator.getLoggerInstance(TreasuryOfficerVerifier.class);

    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        clearDecHisFlag(ifr);
        if (!isEmpty(getProcess(ifr))) showSelectedProcessSheet(ifr);
        cpFormLoadActivity(ifr);

    }

    @Override
    public String setMaskedValue(String s, String s1) {
        return s1;
    }

    @Override
    public JSONArray executeEvent(FormDef formDef, IFormReference iFormReference, String s, String s1) {
        return null;
    }

    @Override
    public String executeServerEvent(IFormReference ifr, String controlName, String eventName, String data) {
        try {
            switch (eventName){
                case cpApiCallEvent:{
                    switch (controlName) {
                        case cpTokenEvent: return new CpServiceHandler(ifr).validateTokenTest();
                        case cpPostEvent: return new CpServiceHandler(ifr).postTransactionTest();
                    }
                    break;
                }
                case formLoad:
                break;
                case onLoad:
                break;
                case onClick:{
                    switch (controlName){
                        case cpSetupWindowEvent:{
                          return cpSetupSmWindow(ifr, Integer.parseInt(data));
                        }
                        case cpSmInvestEvent:{
                           return  setupCpSmBid(ifr);
                        }
                        case cpUpdateCutOffTimeEvent:{
                            return cpUpdateCutOffTime(ifr);
                        }
                        case cpUpdateReDiscountRateEvent:{
                            return cpUpdateReDiscountRate(ifr);
                        }
                    }
                }
                break;
                case onChange:
                case custom:
                case onDone:{
                	switch(controlName){
                	}
                }
                break;
                case decisionHistory:{
                	 setCpDecisionHistory(ifr);

                }
                break;
                case sendMail:{
                    cpSendMail(ifr);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            logger.info("Exception Occurred-- "+ e.getMessage());
        }
        return null;
    }

    

	@Override
    public void cpSendMail(IFormReference ifr) {
        if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerInitiator)){
            if (getCpDecision(ifr).equalsIgnoreCase(decApprove)) {
                message = "Landing Message has been approved by the treasury officer verifier with ref No. "+getWorkItemNumber(ifr)+". Login to setup market.";
                new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
            }
            else if (getCpDecision(ifr).equalsIgnoreCase(decReject)){
                message = "Landing Message has been rejected by the treasury officer verifier with ref No. "+getWorkItemNumber(ifr)+". Login to make necessary corrections.";
                new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
            }
        }
        else if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerMaker)){
                if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryModifyCutOffTime)){
                  if (getCpDecision(ifr).equalsIgnoreCase(decApprove)){
                      message = "Cutoff time has now been updated.";
                      new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
                  }
                  else if (getCpDecision(ifr).equalsIgnoreCase(decReject)){
                      message = "Cutoff time was rejected.";
                      new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
                  }
                }
        }
        else if (getPrevWs(ifr).equalsIgnoreCase(branchVerifier)){
            if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)){
                if (getCpDecision(ifr).equalsIgnoreCase(decApprove)){
                    message = "A Termination request for "+getCpMarket(ifr)+" Market Commercial Paper with number "+getCpTermCusId(ifr)+" has been  approved by Money_Market_Branch_Verifier and is now pending in your queue for approval. Workitem No. "+getWorkItemNumber(ifr)+".";
                    new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
                }
                else if (getCpDecision(ifr).equalsIgnoreCase(decReject)){
                    message = "A Termination request for Commercial paper with number "+getCpTermCusId(ifr)+" has been rejected by Money_Market_Treasury_Verifier and is now pending in your queue. Workitem No. "+getWorkItemNumber(ifr)+".";
                    new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
                }
            }
        }
    }
    @Override
    public void cpFormLoadActivity(IFormReference ifr){
        hideCpSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        setGenDetails(ifr);
        hideShowBackToDashboard(ifr,False);
        clearFields(ifr,new String[]{cpRemarksLocal,cpDecisionLocal});
        if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerInitiator)) {
            setVisible(ifr,new String[] {cpLandingMsgSection,cpDecisionSection,cpMarketSection});
            enableFields(ifr,new String[]{cpDecisionSection});
            setMandatory(ifr, new String[]{cpDecisionLocal,cpRemarksLocal});
        }
        else if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerMaker)){
            if (isEmpty(getWindowSetupFlag(ifr))){
                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
                    setVisible(ifr, new String[]{cpLandingMsgSection, cpDecisionSection, cpMarketSection});
                    setMandatory(ifr,new String[] {cpDecisionLocal,cpRemarksLocal});
                }
                else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                    setVisible(ifr, new String[]{cpLandingMsgSection,cpDecisionSection,cpMarketSection,cpTreasurySecSection,cpCutOffTimeSection,cpSmCutOffTimeLocal,cpSetupSection,cpSetupWindowBtn,cpSmCpBidTbl});
                    setInvisible(ifr,new String[]{cpOpenDateLocal,cpCloseDateLocal});
                    setMandatory(ifr,new String[] {cpDecisionLocal,cpRemarksLocal});
                    disableFields(ifr,new String[]{cpSmCpBidTbl,cpSmMinPrincipalLocal});
                }
            }
            else {
                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket) || getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                    setVisible(ifr,new String[]{cpMarketSection,cpDecisionSection,cpCategoryLocal});
                    setMandatory(ifr, new String[]{cpDecisionLocal,cpRemarksLocal});
                    disableFields(ifr,new String[]{cpMarketSection});
                    if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryModifyCutOffTime)){
                        setVisible(ifr,new String[]{cpCutOffTimeSection,cpSetupSection,cpUpdateCutoffTimeBtn});
                    }
                   else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryReDiscountRate)){
                        setVisible(ifr,new String[]{cpReDiscountRateSection,cpSetupSection,cpSetReDiscountRateBtn});
                    }
                   else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryUpdateLandingMsg)){
                       setVisible(ifr,new String[]{cpLandingMsgSection});
                    }
                }
            }
        }
        else if (getPrevWs(ifr).equalsIgnoreCase(branchVerifier)){
            if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)){
                    setVisible(ifr,new String[]{cpBranchSecSection,cpCustomerDetailsSection,cpDecisionSection,landMsgLabelLocal,
                            cpSmMaturityDateBrLocal,cpPostSection,cpTokenLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal, (getCpSmConcessionRateValue(ifr).equalsIgnoreCase(empty)) ? empty : cpSmConcessionRateValueLocal});
                    setInvisible(ifr, new String[]{cpAcctValidateBtn,cpApplyBtn,cpSmInvestmentBrTbl});
                    disableFields(ifr,new String[]{cpCustomerDetailsSection,cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal,cpSmConcessionRateValueLocal, cpSmInvestmentTypeLocal});
                    setMandatory(ifr,new String[]{cpDecisionLocal,cpRemarksLocal,cpTokenLocal});
                    enableFields(ifr,new String[]{cpTokenLocal});
                }
            }
        }
        cpSetDecision(ifr);
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr, cpDecisionLocal,new String[]{decApprove,decReject});
    }

    private String cpSetupSmWindow(IFormReference ifr, int rowCount){
        if (isEmpty(getWindowSetupFlag(ifr))){
             if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                return cpSetupSecondaryMarketWindow(ifr,rowCount);
            }
        }
        return "Window already setup.";
    }
    private String cpUpdateCutOffTime(IFormReference ifr){
       String id = getCpWinRefId(ifr);
      int validate = new DbConnect(ifr,Query.getUpdateCutoffTimeQuery(id,getCpCloseDate(ifr))).saveQuery();
        if (validate >=0 ) {
            setFields(ifr,cpDecisionLocal,decApprove);
            disableFields(ifr,new String[]{cpDecisionLocal,cpUpdateCutoffTimeBtn});
            return "Cut off time updated successfully. Kindly submit workitem";
        }
        return "Unable to update cut off time. Contact iBPS support";
    }
    private String cpUpdateReDiscountRate(IFormReference ifr){
        String id = getCpWinRefId(ifr);

        String reDiscount90 = getFieldValue(ifr,cpReDiscountRateLess90Local);
        String reDiscount91180 = getFieldValue(ifr, cpReDiscountRate91To180Local);
        String reDiscount181270 = getFieldValue(ifr,cpReDiscountRate181To270Local);
        String reDiscount271364 = getFieldValue(ifr,cpReDiscountRate271To364Local);



        int validate = new DbConnect(ifr,Query.getUpdateReDiscountRateQuery(id,reDiscount90,reDiscount91180,reDiscount181270,reDiscount271364)).saveQuery();
        if (validate >=0 ) {
            setFields(ifr,cpDecisionLocal,decApprove);
            disableFields(ifr,new String[]{cpDecisionLocal,cpSetReDiscountRateBtn});

            String table = "<table>" +
                    "<tr>" +
                    "<th>Days to Maturity</th>" +
                    "<th>Re-discount rate</th>" +
                    "</tr>" +
                    "<tr>" +
                    "<td><= 90 days</td>" +
                    "<td>"+reDiscount90+"</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>91 - 180 days</td>" +
                    "<td>"+reDiscount91180+"</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>181 – 270 days</td>" +
                    "<td>"+reDiscount181270+"</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>271 – 364 days</td>" +
                    "<td>"+reDiscount271364+"</td>" +
                    "</tr>" +
                    "</table><br> ";

            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket))
                message = "A commercial paper re-discount for primary market has now been updated with the below details.<br> ";
            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket))
                message = "A commercial paper re-discount for secondary market has now been updated with the below details.<br> ";

            message += table;
            new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
            return "Re-discount Rate updated successfully. Kindly submit workitem";
        }
        return "Unable to update Re-discount Rate. Contact iBPS support";
    }


    @Override
    public JSONArray validateSubmittedForm(FormDef formDef, IFormReference iFormReference, String s) {
        return null;
    }

    @Override
    public String executeCustomService(FormDef formDef, IFormReference iFormReference, String s, String s1, String s2) {
        return null;
    }

    @Override
    public String getCustomFilterXML(FormDef formDef, IFormReference iFormReference, String s) {
        return null;
    }

    @Override
    public String generateHTML(EControl eControl) {
        return null;
    }

    @Override
    public String introduceWorkItemInWorkFlow(IFormReference iFormReference, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return null;
    }
}
