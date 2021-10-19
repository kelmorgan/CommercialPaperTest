package com.initiator.worksteps;

import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import com.cp.api.service.CpServiceHandler;
import com.cp.utils.*;

public class BranchInitiator extends Shared implements IFormServerEventHandler, SharedI {
     private static final Logger logger = LogGenerator.getLoggerInstance(BranchInitiator.class);

    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        beforeFormLoadActivity(ifr);
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
                        case goToDashBoard:{
                            backToDashboard(ifr);
                          if (isCpProcess(ifr)) cpBackToDashboard(ifr);
                            clearFields(ifr,new String[] {selectProcessLocal});
                            break;
                        }
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
                        case generateTemplateEvent:{
                            return GenerateDocument.generateDoc(ifr,data);
                        }
                        case cpPoiSearchEvent:{
                            return cpSearchPoi(ifr);
                        }
                        case cpPoiProcessEvent:{
                            return cpPoiProcess(ifr,Integer.parseInt(data));
                        }
                        case cpChargeCustodyFeeEvent:{
                            checkCpIsStdCustodyFee(ifr);
                            break;
                        }
                    }
                }
                break;
                case onChange:{
                    switch (control){
                    case onChangeProcess: {
                        selectProcessSheet(ifr);
                        if (isCpProcess(ifr)) cpFormLoadActivity(ifr);
                        break;
                    }
                        case cpOnSelectMarket:{
                            if (isCpPrimaryMarket(ifr)|| isCpSecondaryMarket(ifr)){
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
                            if (isCpPrimaryMarket(ifr)){
                                return cpPmCheckPrincipal(ifr);
                            }
                            else if (isCpSecondaryMarket(ifr)){
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
                            if (cpCheckWindowStateById(ifr, getCpWinRefId(ifr))) {
                                 cpGenerateCustomerId(ifr);
                                 if(isCpSecondaryMarket(ifr)) return cpUpdateSmInvestment(ifr);
                            }
                            else return cpValidateWindowErrorMsg;
                        }
                        break;
                    }
                }
                break;
                case decisionHistory:{
                        if(isCpProcess(ifr)) setCpDecisionHistory(ifr);
                }
                break;
                case sendMail:{
                       if(isCpProcess(ifr)) cpSendMail(ifr);
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


    private void cpBackToDashboard(IFormReference ifr) {
        undoMandatory(ifr,new String [] {cpSelectMarketLocal,cpDecisionLocal,cpRemarksLocal});
        clearFields(ifr,new String [] {cpSelectMarketLocal,landMsgLabelLocal,cpLandMsgLocal,cpDecisionLocal,cpRemarksLocal});
    }

    @Override
    public void cpSendMail(IFormReference ifr) {
        if (getCpDecision(ifr).equalsIgnoreCase(decSubmit)) {
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)) {
                    message = "A request for Commercial paper (" + getCpMarket(ifr) + " market) with Workitem No. " + getWorkItemNumber(ifr) + " has been initiated and is now pending in your queue for approval.";
                    new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
            } else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryMandate)) {
                if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)) {
                    message = "A request for Commercial Paper with WorkItem No. " + getWorkItemNumber(ifr) + " termination request was initiated and is now pending in your queue for approval";
                    new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
                } else if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeLien)) {
                    message = "A request to " + getCpLienType(ifr) + " lien on " + getCpMarket(ifr) + " market Commercial paper with WorkItem No. " + getWorkItemNumber(ifr) + " has been initiated and request is now pending in your queue for approval.";
                    new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
                }
            }
        }
    }
    public void beforeFormLoadActivity(IFormReference ifr){
        hideProcess(ifr);
        hideCpSections(ifr);
        hideTbSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        hideShowBackToDashboard(ifr,False);
        setBranchDetails(ifr);
        clearFields(ifr,new String [] {selectProcessLocal});
        setMandatory(ifr, new String[]{selectProcessLocal});
        setFields(ifr, new String[]{currWsLocal,prevWsLocal},new String[]{getCurrentWorkStep(ifr),na});
        setWiName(ifr);
        isPbSol(ifr,getSol(ifr));
    }

    @Override
    public void cpFormLoadActivity(IFormReference ifr) {
        cpSetDecision(ifr);
        setVisible(ifr, new String[]{cpDecisionSection, cpMarketSection});
        enableFields(ifr,new String[]{cpSelectMarketLocal});
        setMandatory(ifr,new String [] {cpSelectMarketLocal,cpDecisionLocal,cpRemarksLocal});
        setDropDown(ifr,cpCategoryLocal,new String[]{cpCategoryBid,cpCategoryMandate});
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
        clearFields(ifr,new String[]{cpCustomerAcctNoLocal,cpCustomerNameLocal,cpCustomerEmailLocal,cpLienStatusLocal,cpSmInvestmentIdLocal,cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal,cpSmConcessionRateValueLocal,cpSmTenorLocal,cpSmRateLocal});
        String id = ifr.getTableCellValue(cpSmInvestmentBrTbl,rowIndex,0);
        String maturityDate = getTableCellValue(ifr,cpSmInvestmentBrTbl,rowIndex,3);
        setFields(ifr, new String[]{cpSmInvestmentIdLocal,cpSmMaturityDateBrLocal},new String[]{id,maturityDate});
        resultSet = new DbConnect(ifr,Query.getCpSmInvestmentsSelectQuery(getSelectedCpSmInvestmentId(ifr))).getData();
        String tenor = resultSet.get(0).get(0);
        String rate = resultSet.get(0).get(1);

        setFields(ifr, new String[]{cpSmTenorLocal,cpSmRateLocal},new String[]{tenor,rate});
        setVisible(ifr,new String[]{cpCustomerDetailsSection,cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal,cpSmTenorLocal,cpSmRateLocal,cpServiceSection});
        setMandatory(ifr,new String[]{cpSmPrincipalBrLocal,cpSmConcessionRateLocal});
        enableFields(ifr,new String []{cpCustomerAcctNoLocal,cpAcctValidateBtn,cpSmConcessionRateLocal,cpSmPrincipalBrLocal});
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
        else if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeLien)){
            setVisible(ifr,new String[]{cpLienSection});
            enableFields(ifr, new String[]{cpLienTypeLocal, cpLienMandateIdLocal});
            setMandatory(ifr, new String[]{cpLienTypeLocal, cpLienMandateIdLocal});
            setInvisible(ifr,new String[]{cpTerminationSection,cpProofOfInvestSection});
        }
        else { setInvisible(ifr,new String[]{cpTerminationSection,cpProofOfInvestSection,cpLienSection});}
    }
    private void cpFetchMandatesForTermination(IFormReference ifr, String marketType){
        clearTable(ifr,cpTermMandateTbl);
        resultSet = new DbConnect(ifr,Query.getBidForTerminationQuery(commercialProcessName,marketType,getCpMandateToTerminate(ifr))).getData();
        logger.info("result set mandates-- "+ resultSet);
        for (List<String> result : resultSet){
            String date = result.get(0);
            String custId = result.get(1);
            String amount = result.get(2);
            String accountNo = result.get(3);
            String accountName = result.get(4);
            String maturityDate = result.get(5);
            String status = result.get(6);
            String winId = result.get(7);
            String dtm = getFormattedString(getDaysToMaturity(maturityDate));
            setTableGridData(ifr,cpTermMandateTbl,new String[]{cpTermMandateDateCol,cpTermMandateRefNoCol,cpTermMandateAmountCol,cpTermMandateAcctNoCol,cpTermMandateCustNameCol,cpTermMandateDtmCol,cpTermMandateStatusCol,cpTermMandateWinRefCol},
                    new String [] {date,custId,amount,accountNo,accountName,dtm,status,winId});
        }
        setVisible(ifr,new String[]{cpTermMandateTbl,cpSelectMandateTermBtn});
        enableFields(ifr,new String[]{cpSelectMandateTermBtn});
    }
    private String cpSelectMandateForTermination(IFormReference ifr, int rowIndex){
        String issueDate = ifr.getTableCellValue(cpTermMandateTbl,rowIndex,0);
        String custId = ifr.getTableCellValue(cpTermMandateTbl,rowIndex,1);
        String winId = ifr.getTableCellValue(cpTermMandateTbl,rowIndex,7);
        String dtm = ifr.getTableCellValue(cpTermMandateTbl,rowIndex,5);
        logger.info("dtm: "+dtm);
        String rate;
        String errMsg = "No Re-Discount rate set by Treasury for this bid.Termination cancelled. Contact Treasury Department.";
        setInvisible(ifr, new String[]{cpTerminationTypeLocal});
        undoMandatory(ifr, new String[]{cpTerminationTypeLocal});
        disableFields(ifr, new String[]{cpTerminationTypeLocal});
        clearFields(ifr,new String[]{cpTermCustIdLocal,cpTerminationTypeLocal,cpTermDtmLocal,cpTermIssueDateLocal,cpTermBoDateLocal});

        if (isCpLien(ifr,custId)) return cpLienErrMsg;

        resultSet = new DbConnect(ifr,Query.getCpReDiscountedRateForTermQuery(winId)).getData();


        if (Calculator.getFormattedInteger(dtm) <= 90){
            rate = resultSet.get(0).get(0);
            if (!isEmpty(rate)) {
                setVisible(ifr, new String[]{cpReDiscountRateSection, cpReDiscountRateLess90Local});
                setFields(ifr, cpReDiscountRateLess90Local,rate);
            }
            else return errMsg;
        }
        else if (Calculator.getFormattedInteger(dtm) >= 91 && Calculator.getFormattedInteger(dtm) <= 180){
            rate   = resultSet.get(0).get(1);
            if (isEmpty(rate)) {
                setVisible(ifr, new String[]{cpReDiscountRateSection, cpReDiscountRate91To180Local});
                setFields(ifr, cpReDiscountRate91To180Local,rate );
            }
            else return errMsg;
        }
        else if (Calculator.getFormattedInteger(dtm) >= 181 && Calculator.getFormattedInteger(dtm) <= 270){
            rate = resultSet.get(0).get(2);
            if (!isEmpty(rate)) {
                setVisible(ifr, new String[]{cpReDiscountRateSection, cpReDiscountRate181To270Local});
                setFields(ifr, cpReDiscountRate181To270Local,rate );
            }
            else return errMsg;
        }
        else if (Calculator.getFormattedInteger(dtm) >= 271 && Calculator.getFormattedInteger(dtm) <= 364){
            rate = resultSet.get(0).get(3);
            if (!isEmpty(rate)) {
                setVisible(ifr, new String[]{cpReDiscountRateSection, cpReDiscountRate271To364Local});
                setFields(ifr, cpReDiscountRate271To364Local, rate);
            }
            else return errMsg;
        }
        setVisible(ifr, new String[]{cpTerminationTypeLocal});
        setMandatory(ifr, new String[]{cpTerminationTypeLocal});
        enableFields(ifr, new String[]{cpTerminationTypeLocal});
        setFields(ifr,new String[]{cpTermCustIdLocal,cpTermDtmLocal,cpTermIssueDateLocal,cpTermBoDateLocal},new String[]{custId,dtm,issueDate,getCurrentDate()});
        return null;
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



    private String cpSearchPoi(IFormReference ifr){
        clearTable(ifr,cpPoiTbl);
        resultSet = new DbConnect(ifr,Query.getCpPoiMandateSearchQuery(getCpMarket(ifr),getCpPoiMandate(ifr))).getData();
        if (isEmpty(resultSet)) {
            clearFields(ifr,cpPoiMandateLocal);
            return "No Details found for this Mandate";
        }
        for (List<String> result : resultSet){
            String date = result.get(0);
            String id = result.get(1);
            String amount = result.get(2);
            String accountNo = result.get(3);
            String accountName = result.get(4);
            String status = result.get(5);

            setTableGridData(ifr,cpPoiTbl,new String[]{cpPoiDateCol,cpPoiIdCol,cpPoiAmountCol,cpPoiAcctNoCol,cpPoiAcctNameCol,cpPoiStatusCol},
                    new String[]{date,id,amount,accountNo,accountName,status});
        }
        setVisible(ifr,new String[]{cpPoiGenerateBtn,cpPoiTbl});
        enableFields(ifr,new String[]{cpPoiGenerateBtn});

        return null;
    }

    private String cpPoiProcess (IFormReference ifr, int rowIndex){
        String id = ifr.getTableCellValue(cpPoiTbl,rowIndex,1);
        resultSet = new DbConnect(ifr,Query.getCpPoiDtlQuery(id)).getData();

        if (!isEmpty(resultSet)) {
            String reqDate = resultSet.get(0).get(0);
            String custId = resultSet.get(0).get(1);
            String principal = resultSet.get(0).get(2);
            String accountNo = resultSet.get(0).get(3);
            String accountName = resultSet.get(0).get(4);
            String principalMaturity = resultSet.get(0).get(5);
            String interest = resultSet.get(0).get(6);
            String maturityDate = resultSet.get(0).get(7);
            String tenor = resultSet.get(0).get(8);
            String rate = resultSet.get(0).get(9);

            setFields(ifr, new String[]{cpPoiCustEffectiveDateLocal,cpPoiCustIdLocal,cpPoiCustAmountInvestedLocal,cpPoiCustAcctNoLocal,cpPoiCustNameLocal,cpPoiCustPrincipalAtMaturityLocal,cpPoiCustInterestLocal,cpPoiCustMaturityDateLocal,cpPoiCustTenorLocal,cpPoiCustRateLocal,cpPoiDateLocal},
                    new String[]{reqDate,custId,principal,accountNo,accountName,principalMaturity,interest,maturityDate,tenor,rate,getCurrentDate()});
            return apiSuccess;
        }
        return "Error in processing proof of investment. Contact iBPS support.";
    }
}
