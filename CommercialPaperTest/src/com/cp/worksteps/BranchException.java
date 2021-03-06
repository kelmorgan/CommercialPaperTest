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
import java.util.List;

public class BranchException extends Shared implements IFormServerEventHandler , Constants, SharedI {
    private static final Logger logger = LogGenerator.getLoggerInstance(BranchException.class);
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
        try {
            switch (event){
                case formLoad:
                case onLoad:
                case cpApiCallEvent:{
                    switch (control) {
                        case cpValidateAcctEvent: return new CpServiceHandler(ifr).validateAccountTest();
                        case cpFetchMandateEvent: return getCpAcctNo(ifr);
                        case cpValidateLienEvent: return new CpServiceHandler(ifr).validateLienTest();
                    }
                }
                case onClick:{
                    switch (control){
                        case cpSmApplyEvent:{cpSmInvestmentApply(ifr,Integer.parseInt(data));}
                        break;
                        case  cpSearchTermMandateEvent: {
                            cpFetchMandatesForTermination(ifr,getCpMarket(ifr));
                            break;
                        }
                        case cpSelectTermMandateEvent:{
                            return cpSelectMandateForTermination(ifr,Integer.parseInt(data));
                        }
                        case cpSelectTermSpecialRateEvent:{
                            cpSelectTermSpecialRate(ifr);
                            break;
                        }

                        case  cpCalculateTermEvent:{
                            cpCalculateTermination(ifr);
                            break;
                        }
                    }
                }
                break;
                case onChange:{
                    switch (control){
                        case cpOnSelectMarket:{
                            if (isCpPrimaryMarket(ifr) || isCpSecondaryMarket(ifr)){
                                setVisible(ifr,new String[]{cpCategoryLocal});
                                enableFields(ifr,new String[]{cpCategoryLocal});
                                setMandatory(ifr,new String[]{cpCategoryLocal});
                            }
                            else {
                                setInvisible(ifr,new String[]{cpCategoryLocal});
                                undoMandatory(ifr,new String[]{cpCategoryLocal});
                                hideCpSections(ifr);
                            }
                        }
                        break;
                        case cpOnSelectCategory:{return cpSelectCategory(ifr);}
                        case cpOnChangeRateType:{
                            if (getCpPmRateType(ifr).equalsIgnoreCase(rateTypePersonal)){
                                setVisible(ifr,new String[]{cpPmPersonalRateLocal});
                                setMandatory(ifr,new String[]{cpPmPersonalRateLocal});
                                enableFields(ifr,new String[]{cpPmPersonalRateLocal});
                            }
                            else{
                                clearFields(ifr,cpPmPersonalRateLocal);
                                undoMandatory(ifr,cpPmPersonalRateLocal);
                                setInvisible(ifr,cpPmPersonalRateLocal);
                            }
                        }
                        break;
                        case cpCheckPrincipalEvent:{
                            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
                                return cpPmCheckPrincipal(ifr);
                            }
                            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                                return cpSmCheckPrincipal(ifr);
                            }
                        }
                        break;
                        case  cpCheckTenorEvent:{
                            return cpCheckPmTenor(ifr,getCpPmTenor(ifr));
                        }
                        case cpSmConcessionRateEvent:{ cpSmSetConcessionRate(ifr);}
                        break;
                        case cpSmCheckMaturityDateEvent:{return cpSmCheckMaturityDate(ifr,Integer.parseInt(data));}
                        case cpMandateTypeEvent:{
                            cpSelectMandateType(ifr);
                            break;
                        }
                        case cpSelectTermTypeEvent:{
                            cpSelectTerminationType(ifr);
                            break;
                        }
                        case cpLienEvent:{
                            return  cpValidateLienMandate(ifr);
                        }
                    }


                }
                break;
                case custom:
                case onDone:{
                    switch (control){
                        case validateWindowEvent:{
                            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                                if (!cpCheckWindowStateById(ifr, getCpWinRefId(ifr)))
                                    return cpValidateWindowErrorMsg;
                            }
                            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) {
                                if (!cpCheckWindowStateById(ifr, getCpWinRefId(ifr)))
                                    return cpValidateWindowErrorMsg;
                            }
                        }
                        break;
                    }
                }
                break;
                case decisionHistory:{
                        setCpDecisionHistory(ifr);
                }
                break;
                case sendMail:{
                    if (getProcess(ifr).equalsIgnoreCase(commercialProcess))
                        cpSendMail(ifr);

                }
            }
        }
        catch (Exception e){
            logger.error("Exception occurred in executeServerEvent-- "+ e.getMessage());
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
        if (getCpDecision(ifr).equalsIgnoreCase(decSubmit)) {
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)) {
                message = "A request for Commercial paper (" + getCpMarket(ifr) + " market) with Workitem No. " + getWorkItemNumber(ifr) + " has been corrected and returned and is now pending in your queue for approval.";
                new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
            } else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryMandate)) {
                if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)) {
                    message = "A request for Commercial Paper with WorkItem No. " + getWorkItemNumber(ifr) + " termination request was corrected and returned  and is now pending in your queue for approval";
                    new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
                } else if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeLien)) {
                    message = "A request to " + getCpLienType(ifr) + " lien on " + getCpMarket(ifr) + " market Commercial paper with WorkItem No. " + getWorkItemNumber(ifr) + " has been corrected and returned  initiated and request is now pending in your queue for approval.";
                    new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
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
        clearCpFlag(ifr);
        setVisible(ifr,new String[]{cpDecisionSection});
        setMandatory(ifr, new String[]{cpDecisionLocal,cpRemarksLocal});
        enableFields(ifr, new String[]{cpDecisionLocal,cpRemarksLocal,cpMarketSection});
        if (isPrevWs(ifr,branchVerifier)){
            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
                if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)){
                    setVisible(ifr, new String[]{cpBranchPriSection, cpCustomerDetailsSection,cpServiceSection, landMsgLabelLocal});
                    setMandatory(ifr, new String[]{cpCustomerAcctNoLocal, cpPmTenorLocal, cpPmPrincipalLocal, cpPmRateTypeLocal});
                    enableFields(ifr, new String[]{cpCustomerAcctNoLocal, cpPmTenorLocal, cpPmPrincipalLocal, cpPmRateTypeLocal,cpAcctValidateBtn});
                    setDropDown(ifr, cpPmReqTypeLocal, new String[]{cpPmReqFreshLabel}, new String[]{cpPmReqFreshValue});
                    setFields(ifr, new String[]{cpPmReqTypeLocal}, new String[]{cpPmReqFreshValue});
                }
            }
            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)){
                    setVisible(ifr,new String[]{cpWindowDetailsSection,cpCustomerIdLocal,cpBranchSecSection,landMsgLabelLocal});
                    enableFields(ifr,new String[]{cpApplyBtn, cpSmInvestmentTypeLocal});
                    setMandatory(ifr,new String[]{cpSmInvestmentTypeLocal});
                    setCpSmInvestmentGrid(ifr);
                }
            }
        }
        cpSetDecision(ifr);
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr,cpDecisionLocal,new String[]{decSubmit,decDiscard});
    }


    private String cpSelectCategory(IFormReference ifr) {
        disableField(ifr,cpCategoryLocal);
        if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)) {
            if (isCpWindowActive(ifr)) {
                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {

                    setVisible(ifr, new String[]{cpWindowDetailsSection,cpBranchPriSection,cpPmIssuerSection, cpCustomerDetailsSection,cpServiceSection, landMsgLabelLocal,cpChargesSection,cpCustomerIdLocal});
                    setMandatory(ifr, new String[]{cpCustomerAcctNoLocal, cpPmTenorLocal, cpPmPrincipalLocal, cpPmRateTypeLocal});
                    enableFields(ifr, new String[]{cpCustomerAcctNoLocal, cpPmTenorLocal, cpPmPrincipalLocal, cpPmRateTypeLocal,cpAcctValidateBtn,cpIsStdCustodyFeeLocal});
                    setDropDown(ifr, cpPmReqTypeLocal, new String[]{cpPmReqFreshLabel}, new String[]{cpPmReqFreshValue});
                    setFields(ifr, new String[]{cpPmReqTypeLocal, cpPmInvestmentTypeLocal,cpCustodyFeeLocal}, new String[]{cpPmReqFreshValue, cpPmInvestmentPrincipal,LoadProp.custodyFee});
                    if(getPbFlag(ifr).equalsIgnoreCase(flag)) {
                        setVisible(ifr, new String[]{cpPbBeneDetailsSection});
                        enableFields(ifr,new String []{cpPbBeneAcctNo,cpPbBeneName,cpCustomerNameLocal});
                        setInvisible(ifr,new String []{cpAcctValidateBtn});
                        setMandatory(ifr,new String []{cpPbBeneAcctNo,cpPbBeneName,cpCustomerNameLocal});
                    }
                    setCpPmWindowDetails(ifr);
                }
                else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                    setVisible(ifr,new String[]{cpBranchSecSection,landMsgLabelLocal,cpWindowDetailsSection,cpCustomerIdLocal});
                    enableFields(ifr,new String[]{cpApplyBtn, cpSmInvestmentTypeLocal});
                    setMandatory(ifr,new String[]{cpSmInvestmentTypeLocal});
                    setCpSmWindowDetails(ifr);
                    setCpSmInvestmentGrid(ifr);
                }
            } else return windowInactiveMessage;
        }
        else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryMandate)){
            setVisible(ifr,cpMandateTypeSection);
            enableField(ifr,cpMandateTypeLocal);
            setMandatory(ifr,cpMandateTypeLocal);
        }
        else {
            hideCpSections(ifr);
        }
        return null;
    }

    private void cpSmInvestmentApply (IFormReference ifr, int rowIndex){
        clearFields(ifr,new String[]{cpCustomerAcctNoLocal,cpCustomerNameLocal,cpCustomerEmailLocal,cpLienStatusLocal,cpSmInvestmentIdLocal,cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal,cpSmConcessionRateValueLocal});
        String id = ifr.getTableCellValue(cpSmInvestmentBrTbl,rowIndex,0);
        setFields(ifr, new String[]{cpSmInvestmentIdLocal},new String[]{id});
        setVisible(ifr,new String[]{cpCustomerDetailsSection,cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal,cpServiceSection});
        setMandatory(ifr,new String[]{cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal});
        enableFields(ifr,new String []{cpCustomerAcctNoLocal,cpAcctValidateBtn});
    }




    private void cpSelectMandateType(IFormReference ifr){
        if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)){
            setVisible(ifr,new String[]{cpTerminationSection});
            setMandatory(ifr,new String[]{cpTermMandateLocal});
            enableFields(ifr,new String[]{cpTermMandateLocal,cpSearchMandateTermBtn});
            setInvisible(ifr,new String[]{cpProofOfInvestSection,cpLienSection});
        }
        else if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypePoi)){
            setVisible(ifr,new String[]{cpProofOfInvestSection});
            enableFields(ifr,new String[]{cpPoiSearchBtn,cpPoiMandateLocal});
            setMandatory(ifr,new String[]{cpPoiSearchBtn,cpPoiMandateLocal});
            setInvisible(ifr,new String[]{cpTerminationSection,cpLienSection});
        }
        else if ( getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeLien)){
            setVisible(ifr,new String[]{cpLienSection});
            enableFields(ifr, new String[]{cpLienTypeLocal, cpLienMandateIdLocal});
            setMandatory(ifr, new String[]{cpLienTypeLocal, cpLienMandateIdLocal});
            setInvisible(ifr,new String[]{cpTerminationSection,cpProofOfInvestSection});
        }
        else { setInvisible(ifr,new String[]{cpTerminationSection,cpProofOfInvestSection,cpLienSection});}
    }



    private void cpSelectTermSpecialRate (IFormReference ifr){
        clearFields(ifr,new String[]{cpTermSpecialRateValueLocal});
        if (getFieldValue(ifr,cpTermSpecialRateLocal).equalsIgnoreCase(True)){
            setVisible(ifr,cpTermSpecialRateValueLocal);
            setMandatory(ifr,cpTermSpecialRateValueLocal);
            enableFields(ifr,cpTermSpecialRateValueLocal);
        }
        else {
            setInvisible(ifr,cpTermSpecialRateValueLocal);
            undoMandatory(ifr,cpTermSpecialRateValueLocal);
            disableFields(ifr,cpTermSpecialRateValueLocal);
        }
    }

    private void cpPartialTermOption(IFormReference ifr){

    }
}
