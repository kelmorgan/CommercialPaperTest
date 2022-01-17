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

public class Branch_Maker extends Shared implements IFormServerEventHandler, SharedI {
    private static final Logger logger = LogGenerator.getLoggerInstance(Branch_Maker.class);
    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        logger.info("-------Form load branch maker---");
        if (!isEmpty(getProcess(ifr))) showSelectedProcessSheet(ifr);
        if (isCpProcess(ifr)) cpFormLoadActivity(ifr);
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
//                        case goToDashBoard:{
//                            backToDashboard(ifr);
//                            if (isCpProcess(ifr)) cpBackToDashboard(ifr);
//                            clearFields(ifr,new String[] {selectProcessLocal});
//                            break;
//                        }
                        case cpSmApplyEvent:{cpSmInvestmentApply(ifr,Integer.parseInt(data));}
                        break;
                        case  cpSearchTermMandateEvent: {
                            return  cpFetchMandatesForTermination(ifr,getCpMarket(ifr));
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

    @Override
    public void cpSendMail(IFormReference ifr) {

    }

    @Override
    public void cpFormLoadActivity(IFormReference ifr) {
        try {
            hideCpSections(ifr);
            hideShowLandingMessageLabel(ifr, False);
            hideShowBackToDashboard(ifr, False);
            setBranchDetails(ifr);
            isPbSol(ifr, getSol(ifr));
            hideCpSections(ifr);
            hideShowLandingMessageLabel(ifr, False);
            hideShowBackToDashboard(ifr, False);
            clearFields(ifr, new String[]{cpRemarksLocal, cpDecisionLocal});

            cpSetDecision(ifr);
            setVisible(ifr, new String[]{cpDecisionSection, cpMarketSection});
            enableFields(ifr, new String[]{cpSelectMarketLocal});
            setMandatory(ifr, new String[]{cpSelectMarketLocal, cpDecisionLocal, cpRemarksLocal});
            setDropDown(ifr, cpCategoryLocal, new String[]{cpCategoryBid, cpCategoryMandate});
        } catch (Exception e){
            logger.info("Exception occurred in branch_maker formload: "+ e.getMessage());
        }
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr, cpDecisionLocal, new String[]{decSubmit, decDiscard});
    }

    private String cpSelectCategory(IFormReference ifr) {
        disableField(ifr, cpCategoryLocal);
        if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)) {
            if (isCpWindowActive(ifr)) {
                if (isCpPrimaryMarket(ifr)) {

                    setVisible(ifr, new String[]{cpWindowDetailsSection, cpBranchPriSection, cpPmIssuerSection, cpCustomerDetailsSection, cpServiceSection, landMsgLabelLocal, cpChargesSection, cpCustomerIdLocal});
                    setMandatory(ifr, new String[]{cpCustomerAcctNoLocal, cpPmTenorLocal, cpPmPrincipalLocal, cpPmRateTypeLocal});
                    enableFields(ifr, new String[]{cpCustomerAcctNoLocal, cpPmTenorLocal, cpPmPrincipalLocal, cpPmRateTypeLocal, cpAcctValidateBtn, cpIsStdCustodyFeeLocal});
                    setDropDown(ifr, cpPmReqTypeLocal, new String[]{cpPmReqFreshLabel}, new String[]{cpPmReqFreshValue});
                    setFields(ifr, new String[]{cpPmReqTypeLocal, cpPmInvestmentTypeLocal, cpCustodyFeeLocal}, new String[]{cpPmReqFreshValue, cpPmInvestmentPrincipal, LoadProp.custodyFee});
                    if (getPbFlag(ifr).equalsIgnoreCase(flag)) {
                        setVisible(ifr, new String[]{cpPbBeneDetailsSection});
                        enableFields(ifr, new String[]{cpPbBeneAcctNo, cpPbBeneName, cpCustomerNameLocal});
                        setInvisible(ifr, new String[]{cpAcctValidateBtn});
                        setMandatory(ifr, new String[]{cpPbBeneAcctNo, cpPbBeneName, cpCustomerNameLocal});
                    }
                    setCpPmWindowDetails(ifr);
                } else if (isCpSecondaryMarket(ifr)) {
                    setVisible(ifr, new String[]{cpBranchSecSection, landMsgLabelLocal, cpWindowDetailsSection, cpCustomerIdLocal});
                    enableFields(ifr, new String[]{cpApplyBtn, cpSmInvestmentTypeLocal});
                    setMandatory(ifr, new String[]{cpSmInvestmentTypeLocal});
                    setCpSmWindowDetails(ifr);
                    setCpSmInvestmentGrid(ifr);
                }
            } else return windowInactiveMessage;
        } else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryMandate)) {
            setVisible(ifr, cpMandateTypeSection);
            enableField(ifr, cpMandateTypeLocal);
            setMandatory(ifr, cpMandateTypeLocal);
        } else {
            hideCpSections(ifr);
        }
        return null;
    }


    private void cpSmInvestmentApply(IFormReference ifr, int rowIndex) {
        clearFields(ifr, new String[]{cpCustomerAcctNoLocal, cpCustomerNameLocal, cpCustomerEmailLocal, cpLienStatusLocal, cpSmInvestmentIdLocal, cpSmMaturityDateBrLocal, cpSmPrincipalBrLocal, cpSmConcessionRateLocal, cpSmConcessionRateValueLocal, cpSmTenorLocal, cpSmRateLocal});
        String id = ifr.getTableCellValue(cpSmInvestmentBrTbl, rowIndex, 0);
        String maturityDate = getTableCellValue(ifr, cpSmInvestmentBrTbl, rowIndex, 3);
        setFields(ifr, new String[]{cpSmInvestmentIdLocal, cpSmMaturityDateBrLocal}, new String[]{id, maturityDate});
        resultSet = new DbConnect(ifr, Query.getCpSmInvestmentsSelectQuery(getSelectedCpSmInvestmentId(ifr))).getData();
        String tenor = resultSet.get(0).get(0);
        String rate = resultSet.get(0).get(1);

        setFields(ifr, new String[]{cpSmTenorLocal, cpSmRateLocal}, new String[]{tenor, rate});
        setVisible(ifr, new String[]{cpCustomerDetailsSection, cpSmMaturityDateBrLocal, cpSmPrincipalBrLocal, cpSmConcessionRateLocal, cpSmTenorLocal, cpSmRateLocal, cpServiceSection});
        setMandatory(ifr, new String[]{cpSmPrincipalBrLocal, cpSmConcessionRateLocal});
        enableFields(ifr, new String[]{cpCustomerAcctNoLocal, cpAcctValidateBtn, cpSmConcessionRateLocal, cpSmPrincipalBrLocal});
    }


    private void cpSelectMandateType(IFormReference ifr) {
        if (isCpMandateType(ifr, cpMandateTypeTerminate)) {
            setVisible(ifr, new String[]{cpTerminationSection});
            setMandatory(ifr, new String[]{cpTermMandateLocal});
            enableFields(ifr, new String[]{cpTermMandateLocal, cpSearchMandateTermBtn});
            setInvisible(ifr, new String[]{cpProofOfInvestSection, cpLienSection});
        } else if (isCpMandateType(ifr, cpMandateTypePoi)) {
            setVisible(ifr, new String[]{cpProofOfInvestSection});
            enableFields(ifr, new String[]{cpPoiSearchBtn, cpPoiMandateLocal});
            setMandatory(ifr, new String[]{cpPoiSearchBtn, cpPoiMandateLocal});
            setInvisible(ifr, new String[]{cpTerminationSection, cpLienSection});
        } else if (isCpMandateType(ifr, cpMandateTypeLien)) {
            setVisible(ifr, new String[]{cpLienSection});
            enableFields(ifr, new String[]{cpLienTypeLocal, cpLienMandateIdLocal});
            setMandatory(ifr, new String[]{cpLienTypeLocal, cpLienMandateIdLocal});
            setInvisible(ifr, new String[]{cpTerminationSection, cpProofOfInvestSection});
        } else {
            setInvisible(ifr, new String[]{cpTerminationSection, cpProofOfInvestSection, cpLienSection});
        }
    }


    private void cpSelectTermSpecialRate(IFormReference ifr) {
        clearFields(ifr, new String[]{cpTermSpecialRateValueLocal});
        if (getCpTermIsSpecialRate(ifr)) {
            setVisible(ifr, cpTermSpecialRateValueLocal);
            setMandatory(ifr, cpTermSpecialRateValueLocal);
            enableFields(ifr, cpTermSpecialRateValueLocal);
        } else {
            setInvisible(ifr, cpTermSpecialRateValueLocal);
            undoMandatory(ifr, cpTermSpecialRateValueLocal);
            disableFields(ifr, cpTermSpecialRateValueLocal);
        }
    }

    private String cpSearchPoi(IFormReference ifr) {
        clearTable(ifr, cpPoiTbl);
        resultSet = new DbConnect(ifr, Query.getCpPoiMandateSearchQuery(getCpMarket(ifr), getCpPoiMandate(ifr))).getData();
        if (isEmpty(resultSet)) {
            clearFields(ifr, cpPoiMandateLocal);
            return "No Details found for this Mandate";
        }
        for (List<String> result : resultSet) {
            String date = result.get(0);
            String id = result.get(1);
            String amount = result.get(2);
            String accountNo = result.get(3);
            String accountName = result.get(4);
            String status = result.get(5);

            setTableGridData(ifr, cpPoiTbl, new String[]{cpPoiDateCol, cpPoiIdCol, cpPoiAmountCol, cpPoiAcctNoCol, cpPoiAcctNameCol, cpPoiStatusCol},
                    new String[]{date, id, amount, accountNo, accountName, status});
        }
        setVisible(ifr, new String[]{cpPoiGenerateBtn, cpPoiTbl});
        enableFields(ifr, new String[]{cpPoiGenerateBtn});

        return null;
    }

    private String cpPoiProcess(IFormReference ifr, int rowIndex) {
        String id = ifr.getTableCellValue(cpPoiTbl, rowIndex, 1);
        resultSet = new DbConnect(ifr, Query.getCpPoiDtlQuery(id)).getData();

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

            setFields(ifr, new String[]{cpPoiCustEffectiveDateLocal, cpPoiCustIdLocal, cpPoiCustAmountInvestedLocal, cpPoiCustAcctNoLocal, cpPoiCustNameLocal, cpPoiCustPrincipalAtMaturityLocal, cpPoiCustInterestLocal, cpPoiCustMaturityDateLocal, cpPoiCustTenorLocal, cpPoiCustRateLocal, cpPoiDateLocal},
                    new String[]{reqDate, custId, principal, accountNo, accountName, principalMaturity, interest, maturityDate, tenor, rate, getCurrentDate()});
            return apiSuccess;
        }
        return "Error in processing proof of investment. Contact iBPS support.";
    }
}
