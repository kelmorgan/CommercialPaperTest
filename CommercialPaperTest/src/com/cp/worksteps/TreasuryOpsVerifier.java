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

public class TreasuryOpsVerifier extends Shared implements IFormServerEventHandler, Constants, SharedI {
    private static final Logger logger = LogGenerator.getLoggerInstance(TreasuryOpsVerifier.class);
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
    public String executeServerEvent(IFormReference ifr, String control, String event, String data) {

        try{
            switch (event){
                case cpApiCallEvent:{
                    switch (control) {
                        case cpTokenEvent: return new CpServiceHandler(ifr).validateTokenTest();
                        case cpPostEvent:{
                            if (isCpPrimaryMarket(ifr)) {
                               return new CpServiceHandler(ifr).postTransactionTest();
                            }
                            else if (isCpSecondaryMarket(ifr)) {
                                if (cpCheckWindowStateById(ifr, getCpWinRefId(ifr))) return new CpServiceHandler(ifr).postTransactionTest();
                                else return cpValidateWindowErrorMsg;
                            }
                        }
                    }
                }
                break;
                case formLoad:
                case onLoad:
                case onClick:{
                    switch (control){

                    }
                }
                case onChange:
                case custom:
                case onDone:{
                    switch (control){
                        case cpTerminateBidEvent:{
                            if (isCpDecisionApprove(ifr)) cpTerminateBid(ifr);
                        }
                        break;
                    }
                }
                break;
                case decisionHistory: {
                        setCpDecisionHistory(ifr);
                }
                break;
                case sendMail:
            }
        }
        catch (Exception e){
            logger.error("Exception occurred-- "+ e.getMessage());
        }
        return null;
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

    @Override
    public void cpSendMail(IFormReference ifr) {
        if (isPrevWs(ifr,treasuryOfficerVerifier) || isPrevWs(ifr,branchVerifier)){
            if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)){
                if (isCpDecisionReject(ifr)){
                    message = "A request for Commercial Paper with number "+getCpTermCusId(ifr)+"  was not approved by Money_Market_TreasuryOps_Verifier  and discarded. Workitem No. "+getWorkItemNumber(ifr)+".";
                }
            }
        }
    }

    @Override
    public void cpFormLoadActivity(IFormReference ifr) {
        hideCpSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        hideShowBackToDashboard(ifr,False);
        clearFields(ifr,new String[]{cpRemarksLocal,cpDecisionLocal});

        if (isPrevWs(ifr,branchVerifier)){
          if (isCpMandateType(ifr,cpMandateTypeTerminate)){
              setMandatory(ifr,new String[]{cpDecisionLocal,cpRemarksLocal,cpTokenLocal});
              enableFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal,cpTokenLocal});
              setVisible(ifr,new String[]{cpTermRateLocal,cpTerminationTypeLocal,cpMandateTypeSection,cpTerminationDetailsSection,cpPostSection,cpDecisionSection,cpTerminationSection,cpTermMandateTbl,cpTermSpecialRateLocal, getCpTermIsSpecialRate(ifr) ? cpTermSpecialRateValueLocal : cpTermSpecialRateLocal,cpTermAmountDueLocal});
              if (isCpTerminateType(ifr,cpTerminationTypePartial)){
                  setVisible(ifr,new String[]{cpTermAmountDueLocal,cpTermAdjustedPrincipalLocal,cpTermPartialOptionLocal,cpTermPartialAmountLocal,cpTermPartialOptionLocal});
              }
              setTerminationDetails(ifr);
          }
        }
        cpSetDecision(ifr);
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr,cpDecisionLocal,new String[]{decApprove,decReject});
    }

    private void setTerminationDetails(IFormReference ifr){
        resultSet = new DbConnect(ifr,Query.getCpTermDetailsQuery(getCpTermCusId(ifr))).getData();
        float penaltyCharge;

        if (!isEmpty(resultSet)){
            String tenor = resultSet.get(0).get(0);
            String maturityDate = resultSet.get(0).get(1);
            String principal = resultSet.get(0).get(2);
            long daysDue = Calculator.getDaysBetweenTwoDates(getCpTermBoDate(ifr),maturityDate);

            if (isLeapYear()) penaltyCharge = Calculator.getTermPenaltyChargeForLeapYear(principal,getCpTermRate(ifr),getFormattedString(daysDue));
            else penaltyCharge = Calculator.getTermPenaltyCharge(principal,getCpTermRate(ifr),getFormattedString(daysDue));

            setFields(ifr,new String[]{cpTermTenorLocal,cpTermMaturityDateLocal,cpTermPenaltyChargeLocal,cpTermNoDaysDueLocal},new String[]{tenor,maturityDate,getFormattedString(penaltyCharge),getFormattedString(daysDue)});
        }
    }
}
