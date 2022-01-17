package com.cp.worksteps;

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

public class TreasuryOfficerMaker extends Shared implements IFormServerEventHandler, SharedI {
    private static final Logger logger = LogGenerator.getLoggerInstance(TreasuryOfficerMaker.class);


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
    public JSONArray executeEvent(FormDef formDef, IFormReference ifr, String event, String data) {

        logger.info("called executeEvent event" + event);

        return null;
    }

    @Override
    public String executeServerEvent(IFormReference ifr, String controlName, String eventName, String data) {

        try {
            switch (eventName) {
                case formLoad: {
                }
                break;
                case onLoad:
                    break;
                case onClick: {
                    switch (controlName) {
                        case cpConfigureChargesEvent: {
                            cpConfigureCharges(ifr);
                            break;
                        }
                        case cpUpdateMsg: {
                            cpUpdateLandingMsg(ifr);
                        }
                        break;
                        case cpSetupWindowEvent: {
                            return cpSetupPmWindow(ifr);
                        }

                        case cpViewReportEvent: {
                            viewReport(ifr);
                            break;
                        }
                        case cpDownloadEvent: {
                            setFields(ifr, downloadFlagLocal, flag);
                            disableField(ifr, cpDownloadBtn);
                            break;
                        }
                        case cpViewGroupBidEvent: {
                            viewCpGroupBids(ifr, Integer.parseInt(data));
                            break;
                        }
                        case cpUpdateBidEvent: {
                            return updateCpPmBids(ifr, Integer.parseInt(data));
                        }

                    }
                }
                break;
                case onChange: {
                    switch (controlName) {
                        case cpOnSelectCategory: {
                            cpSelectCategory(ifr);
                        }
                        break;
                        case cpSmSetupEvent: {
                            cpSmInvestmentSetup(ifr);
                            break;
                        }
                        case cpOnSelectMarket:{
                            if (isCpWindowActive(ifr) && isPrevWsDashboard(ifr)){
                                disableCpSections(ifr);
                                setFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal},new String[]{decDiscard,windowActiveErrMessage});
                                disableFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
                                return windowActiveErrMessage;
                            }
                        }
                    }
                }
                break;
                case custom: {
                    switch (controlName) {
                        case cpGetPmGridEvent: {
                            return getCpPmBidGrid(ifr, Integer.parseInt(data));
                        }
                    }
                }
                break;
                case onDone: {
                    switch (controlName) {
                        case cpSmCpUpdateEvent: {
                            return updateCpSmDetails(ifr, Integer.parseInt(data));
                        }
                        case cpPmCheckUnAllocatedBidsEvent: {
                            return cpCheckUnAllocatedBids(ifr);
                        }
                        case cpFailUnallocatedBidsEvent: {
                            cpPmFailUnallocatedBids(ifr);
                            break;
                        }
                        case cpPmProcessAllocatedBidsEvent: {
                            updateCpPmSuccessBids(ifr);
                            break;
                        }
                    }
                }
                break;
                case decisionHistory: {
                    setCpDecisionHistory(ifr);

                }
                break;
                case sendMail: {
                    cpSendMail(ifr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception Occurred-- " + e.getMessage());
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
        if (!isEmpty(getWindowSetupFlag(ifr))) {
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryModifyCutOffTime)) {
                message = "Commercial Paper time for " + getCpMarket(ifr) + " market has now been modified and requires your approval.";
                new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
            } else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryReDiscountRate)) {
                message = "Commercial Paper re-discount rate for " + getCpMarket(ifr) + " market has now been set and requires your approval.";
                new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
            }
        }
    }

    @Override
    public void cpFormLoadActivity(IFormReference ifr) {
        hideCpSections(ifr);
        hideShowLandingMessageLabel(ifr, False);
        setInvisible(ifr, new String[]{goBackDashboardSection});
        clearFields(ifr, new String[]{cpRemarksLocal});
        if (getUtilityFlag(ifr).equalsIgnoreCase(flag)) {
            if (getDownloadFlag(ifr).equalsIgnoreCase(flag)) {
                showCommercialProcessSheet(ifr);
                setVisible(ifr, new String[]{cpPrimaryBidSection, cpAllocSummaryTbl, cpViewGroupBtn, cpChargesSection, cpCommissionLocal, cpTxnFeeLocal, cpVatLocal, cpChargesBtn});
                enableFields(ifr, new String[]{cpPmSettlementDateLocal, cpAllocCpRateLocal, cpAllocBankRateLocal});
                if (!isEmpty(getCpPmSettlementDate(ifr))) disableFields(ifr, cpPmSettlementDateLocal);
                setMandatory(ifr, new String[]{cpPmSettlementDateLocal});
                setInvisible(ifr, new String[]{cpViewReportBtn, cpDownloadBtn, cpCustodyFeeLocal, cpIsStdCustodyFeeLocal});
                setFields(ifr, new String[]{cpPmAllocFlagLocal, cpCommissionLocal, cpTxnFeeLocal, cpVatLocal}, new String[]{flag, LoadProp.commission, LoadProp.txnFee, LoadProp.vat});
            } else {
                setGenDetails(ifr);
                // setFields(ifr, new String[]{prevWsLocal, selectProcessLocal, cpSelectMarketLocal}, new String[]{utilityWs, commercialProcess, cpPrimaryMarket});
                enableFields(ifr, cpViewReportBtn);
                showCommercialProcessSheet(ifr);
                setVisible(ifr, cpPrimaryBidSection);
            }
        } else if (getPrevWs(ifr).equalsIgnoreCase(dashBoardWs)) {
            setGenDetails(ifr);
            cpSetDecision(ifr);
            setVisible(ifr, new String[]{cpDecisionSection, cpMarketSection});
            enableFields(ifr, new String[]{cpLandMsgLocal, cpSelectMarketLocal});
            setMandatory(ifr, new String[]{cpSelectMarketLocal, cpDecisionLocal, cpRemarksLocal});
        } else if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerVerifier)) {
            if (isEmpty(getWindowSetupFlag(ifr))) {
                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                    if (getCpDecision(ifr).equalsIgnoreCase(decReject)) {
                        setVisible(ifr, new String[]{cpLandingMsgSection, cpDecisionSection});
                        setMandatory(ifr, new String[]{cpDecisionLocal, cpRemarksLocal, cpLandMsgLocal});
                        enableFields(ifr, new String[]{cpLandingMsgSection, cpDecisionSection});
                    } else if (getCpDecision(ifr).equalsIgnoreCase(decApprove)) {
                        setVisible(ifr, new String[]{cpLandingMsgSection, cpMarketSection, cpCategoryLocal, cpDecisionSection});
                        setInvisible(ifr, new String[]{cpDecisionSection});
                        enableFields(ifr, new String[]{cpDecisionSection, cpCategoryLocal});
                        disableFields(ifr, new String[]{cpSelectMarketLocal, cpLandingMsgSection});
                        setMandatory(ifr, new String[]{cpCategoryLocal});
                        setCpCategory(ifr, new String[]{cpCategorySetup});
                    }
                } else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) {
                    if (getCpDecision(ifr).equalsIgnoreCase(decApprove)) {
                        setVisible(ifr, new String[]{cpLandingMsgSection, cpMarketSection, cpCategoryLocal, cpDecisionSection});
                        enableFields(ifr, new String[]{cpDecisionSection, cpCategoryLocal});
                        disableFields(ifr, new String[]{cpSelectMarketLocal, cpLandingMsgSection});
                        setMandatory(ifr, new String[]{cpCategoryLocal, cpDecisionLocal, cpRemarksLocal});
                        setCpCategory(ifr, new String[]{cpCategorySetup});
                    } else if (getCpDecision(ifr).equalsIgnoreCase(decReject)) {

                    }
                }
            } else {
                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                    setVisible(ifr, new String[]{cpMarketSection, cpTreasuryPriSection, cpCategoryLocal, cpDecisionSection});
                    setMandatory(ifr, new String[]{cpCategoryLocal, cpDecisionLocal, cpRemarksLocal});
                    setCpCategory(ifr, new String[]{cpCategoryUpdateLandingMsg, cpCategoryReDiscountRate, cpCategoryModifyCutOffTime});
                } else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) {
                    setVisible(ifr, new String[]{cpMarketSection, cpTreasurySecSection, cpCategoryLocal, cpDecisionSection});
                    setMandatory(ifr, new String[]{cpCategoryLocal, cpDecisionLocal, cpRemarksLocal});
                    setCpCategory(ifr, new String[]{cpCategoryUpdateLandingMsg, cpCategoryReDiscountRate, cpCategoryModifyCutOffTime});
                }
            }
        } else if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerMaker)) {
            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                if (!isEmpty(getWindowSetupFlag(ifr))) {
                    setVisible(ifr, new String[]{cpMarketSection, cpTreasuryPriSection, cpCategoryLocal, cpDecisionSection});
                    setMandatory(ifr, new String[]{cpCategoryLocal, cpDecisionLocal, cpRemarksLocal});
                    setCpCategory(ifr, new String[]{cpCategoryUpdateLandingMsg, cpCategoryReDiscountRate, cpCategoryModifyCutOffTime});
                }
            } else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) {
                if (!isEmpty(getWindowSetupFlag(ifr))) {
                    setVisible(ifr, new String[]{cpMarketSection, cpTreasurySecSection, cpCategoryLocal, cpDecisionSection});
                    setMandatory(ifr, new String[]{cpCategoryLocal, cpDecisionLocal, cpRemarksLocal});
                    setCpCategory(ifr, new String[]{cpCategoryUpdateLandingMsg, cpCategoryReDiscountRate, cpCategoryModifyCutOffTime});
                }
            }
        }
        cpSetDecision(ifr);
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        clearFields(ifr, new String[]{cpDecisionLocal, cpRemarksLocal});
        setDecision(ifr, cpDecisionLocal, new String[]{decSubmit, decDiscard});
    }

    private String cpSetupPmWindow(IFormReference ifr) {
        if (isEmpty(getWindowSetupFlag(ifr))) {
            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                if (compareDate(getCpOpenDate(ifr), getCpCloseDate(ifr))) return cpSetupPrimaryMarketWindow(ifr);
                else return "Close date cannot be before Open date.";
            }
        }
        return empty;
    }

    private void cpUpdateLandingMsg(IFormReference ifr) {
        if (getCpUpdateMsg(ifr).equalsIgnoreCase(True)) {
            setFields(ifr, new String[]{cpDecisionLocal, cpRemarksLocal}, new String[]{decSubmit, "Kindly approve landing message update."});
            disableFields(ifr, new String[]{cpDecisionLocal, cpRemarksLocal});
            setInvisible(ifr, new String[]{cpSetupSection});
            undoMandatory(ifr, new String[]{cpRemarksLocal, cpDecisionLocal});
            setMandatory(ifr, new String[]{cpLandMsgLocal});
            enableFields(ifr, new String[]{cpLandMsgLocal});
        } else {
            clearFields(ifr, new String[]{cpDecisionLocal, cpRemarksLocal});
            setVisible(ifr, new String[]{cpSetupSection, cpDecisionSection});
            setMandatory(ifr, new String[]{cpRemarksLocal, cpDecisionLocal});
            undoMandatory(ifr, new String[]{cpLandMsgLocal});
            disableFields(ifr, new String[]{cpLandMsgLocal});
        }
    }

    private void cpSelectCategory(IFormReference ifr) {
        disableField(ifr, cpCategoryLocal);
        if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategorySetup)) {
                setVisible(ifr, new String[]{cpTreasuryPriSection, cpPmIssuerSection, cpSetupSection, cpSetupWindowBtn, cpCutOffTimeSection, cpPmIssuerSection});
                setMandatory(ifr, new String[]{cpOpenDateLocal, cpPmMinPriAmtLocal, cpCloseDateLocal, cpPmIssuerNameLocal});
                enableFields(ifr, new String[]{cpPmMinPriAmtLocal, cpCloseDateLocal, cpSetupWindowBtn, cpPmIssuerNameLocal});
            } else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryModifyCutOffTime)) {
                setVisible(ifr, new String[]{cpCutOffTimeSection});
                enableFields(ifr, new String[]{cpCloseDateLocal});
                undoMandatory(ifr, new String[]{cpReDiscountRateLess90Local, cpReDiscountRate91To180Local, cpReDiscountRate181To270Local, cpReDiscountRate271To364Local});
                setInvisible(ifr, new String[]{cpReDiscountRateSection, cpLandingMsgSection});
            } else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryReDiscountRate)) {
                setVisible(ifr, new String[]{cpReDiscountRateSection});
                setMandatory(ifr, new String[]{cpReDiscountRateLess90Local, cpReDiscountRate91To180Local, cpReDiscountRate181To270Local, cpReDiscountRate271To364Local});
                enableFields(ifr, new String[]{cpReDiscountRateLess90Local, cpReDiscountRate91To180Local, cpReDiscountRate181To270Local, cpReDiscountRate271To364Local});
                setInvisible(ifr, new String[]{cpCutOffTimeSection, cpLandingMsgSection});
            } else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryUpdateLandingMsg)) {
                setVisible(ifr, new String[]{cpLandingMsgSection});
                enableFields(ifr, new String[]{cpLandMsgLocal});
                setMandatory(ifr, new String[]{cpLandMsgLocal});
                undoMandatory(ifr, new String[]{cpReDiscountRateLess90Local, cpReDiscountRate91To180Local, cpReDiscountRate181To270Local, cpReDiscountRate271To364Local});
                setInvisible(ifr, new String[]{cpReDiscountRateSection, cpCutOffTimeSection});
            } else {
                setInvisible(ifr, new String[]{cpReDiscountRateSection, cpCutOffTimeSection, cpLandingMsgSection});
            }
        } else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) {
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategorySetup)) {
                setVisible(ifr, new String[]{cpTreasurySecSection, cpCutOffTimeSection, cpSmCutOffTimeLocal});
                setInvisible(ifr, new String[]{cpOpenDateLocal, cpCloseDateLocal});
                setFields(ifr, new String[]{cpSmCutOffTimeLocal, cpSmMinPrincipalLocal}, new String[]{smDefaultCutOffTime, smMinPrincipal});
                enableFields(ifr, new String[]{cpSmSetupLocal});
                setMandatory(ifr, new String[]{cpSmSetupLocal, cpSmMinPrincipalLocal});
            } else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryModifyCutOffTime)) {
                setVisible(ifr, new String[]{cpCutOffTimeSection});
                enableFields(ifr, new String[]{cpCloseDateLocal});
                undoMandatory(ifr, new String[]{cpReDiscountRateLess90Local, cpReDiscountRate91To180Local, cpReDiscountRate181To270Local, cpReDiscountRate271To364Local});
                setInvisible(ifr, new String[]{cpReDiscountRateSection, cpLandingMsgSection});
            } else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryReDiscountRate)) {
                setVisible(ifr, new String[]{cpReDiscountRateSection});
                setMandatory(ifr, new String[]{cpReDiscountRateLess90Local, cpReDiscountRate91To180Local, cpReDiscountRate181To270Local, cpReDiscountRate271To364Local});
                enableFields(ifr, new String[]{cpReDiscountRateLess90Local, cpReDiscountRate91To180Local, cpReDiscountRate181To270Local, cpReDiscountRate271To364Local});
                setInvisible(ifr, new String[]{cpCutOffTimeSection, cpLandingMsgSection});
            } else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryUpdateLandingMsg)) {
                setVisible(ifr, new String[]{cpLandingMsgSection});
                enableFields(ifr, new String[]{cpLandMsgLocal});
                setMandatory(ifr, new String[]{cpLandMsgLocal});
                undoMandatory(ifr, new String[]{cpReDiscountRateLess90Local, cpReDiscountRate91To180Local, cpReDiscountRate181To270Local, cpReDiscountRate271To364Local});
                setInvisible(ifr, new String[]{cpReDiscountRateSection, cpCutOffTimeSection});
            } else {
                setInvisible(ifr, new String[]{cpReDiscountRateSection, cpCutOffTimeSection, cpLandingMsgSection});
            }
        }
    }

    private void viewReport(IFormReference ifr) {
        resultSet = new DbConnect(ifr, new Query().getCpPmSummaryBidsQuery(getWorkItemNumber(ifr))).getData();
        for (List<String> result : resultSet) {
            String tenor = result.get(0);
            logger.info("tenor-- " + tenor);
            String rate = result.get(1);
            logger.info("rate-- " + rate);
            String totalAmount = result.get(2);
            logger.info("totalAmount-- " + totalAmount);
            String rateType = result.get(3);
            logger.info("rateType-- " + rateType);
            String count = result.get(4);
            logger.info("count-- " + count);
            String groupIndex = result.get(5);
            logger.info("groupIndex-- " + groupIndex);


            setTableGridData(ifr, cpAllocSummaryTbl, new String[]{cpAllocTenorCol, cpAllocRateCol, cpAllocTotalAmountCol, cpAllocRateTypeCol, cpAllocCountCol, cpAllocStatusCol, cpAllocGroupIndexCol},
                    new String[]{tenor, rate, totalAmount, rateType, count, statusAwaitingTreasury, groupIndex});
        }
        setVisible(ifr, new String[]{cpAllocSummaryTbl, cpDownloadBtn});
        setInvisible(ifr, new String[]{cpViewReportBtn});
    }

    private String getCpPmBidGrid(IFormReference ifr, int rowCount) {
        StringBuilder output = new StringBuilder(empty);
        for (int i = 0; i < rowCount; i++) {
            String tenor = ifr.getTableCellValue(cpAllocSummaryTbl, i, 0);
            String rate = ifr.getTableCellValue(cpAllocSummaryTbl, i, 1);
            String totalAmount = ifr.getTableCellValue(cpAllocSummaryTbl, i, 2);
            String rateType = ifr.getTableCellValue(cpAllocSummaryTbl, i, 3);
            String count = ifr.getTableCellValue(cpAllocSummaryTbl, i, 4);
            String status = ifr.getTableCellValue(cpAllocSummaryTbl, i, 5);
            output.append("{\"tenor\": \"").append(tenor).append("\", \"rate\": \"").append(rate).append("\", \"totalAmount\": \"").append(totalAmount).append("\", \"rateType\": \"").append(rateType).append("\", \"count\": \"").append(count).append("\", \"status\": \"").append(status).append("\"}$");
        }
        logger.info("output from grid: " + output.toString());
        return output.toString().trim();
    }

    private void viewCpGroupBids(IFormReference ifr, int rowIndex) {
        ifr.clearTable(cpBidReportTbl);
        String groupIndex = ifr.getTableCellValue(cpAllocSummaryTbl, rowIndex, 6);
        logger.info("group index: " + groupIndex);
        resultSet = new DbConnect(ifr, new Query().getCpPmGroupBidsQuery(groupIndex)).getData();
        for (List<String> result : resultSet) {
            String id = result.get(0);
            String acctNo = result.get(1);
            String acctName = result.get(2);
            String tenor = result.get(3);
            String rate = result.get(4);
            String principal = result.get(5);
            String status = result.get(6);
            String maturityDate = result.get(7);
            String bidStatus = result.get(8);
            String rateType = result.get(9);
            String cpRate = result.get(10);
            float principal1 = Float.parseFloat(principal);
            principal = String.format("%.2f", principal1);
            logger.info("principal: " + principal);

            if (!isEmpty(status)) {
                setTableGridData(ifr, cpBidReportTbl,
                        new String[]{cpBidCustIdCol, cpBidAcctNoCol, cpBidAcctNameCol, cpBidTenorCol, cpBidBankRateCol, cpBidPersonalRateCol, cpBidMaturityDateCol, cpBidTotalAmountCol, cpBidStatusBidCol, cpBidStatusCol, cpBidCpRateCol},
                        new String[]{id, acctNo, acctName, tenor, (rateType.equalsIgnoreCase(rateTypeBank)) ? rate : empty, (rateType.equalsIgnoreCase(rateTypePersonal)) ? rate : empty, maturityDate, principal, bidStatus, status, cpRate});
            } else {
                setTableGridData(ifr, cpBidReportTbl, new String[]{cpBidCustIdCol, cpBidAcctNoCol, cpBidAcctNameCol, cpBidTenorCol, cpBidPersonalRateCol, cpBidTotalAmountCol, cpBidStatusCol, cpBidDefAllocCol},
                        new String[]{id, acctNo, acctName, tenor, rate, principal, statusAwaitingTreasury, defaultAllocation});
            }
        }
        setVisible(ifr, new String[]{cpBidReportTbl, cpUpdateBtn, cpPmSettlementDateLocal, cpPmTotalAllocLocal, cpAllocCpRateLocal, cpAllocBankRateLocal});
        //  enableFields(ifr,new String[]{cpAllocCpRateLocal,cpAllocBankRateLocal});
    }

    private String updateCpPmBids(IFormReference ifr, int rowIndex) {
        String settlementDate = getCpPmSettlementDate(ifr);
        if (isEmpty(settlementDate)) return "Kindly Enter settlement date to update record";

        new DbConnect(ifr, Query.getCpPmUpdateSettlementDate(getWorkItemNumber(ifr), settlementDate)).saveQuery();
        String bankRate = getFieldValue(ifr, cpAllocBankRateLocal);
        String personalRate = empty;
        String cpRate = getFieldValue(ifr, cpAllocCpRateLocal);
        //String defaultAlloc = getFieldValue(ifr,cpAllocDefaultAllocLocal);
        String id = ifr.getTableCellValue(cpBidReportTbl, rowIndex, 0);
        String rateType = new DbConnect(ifr, new Query().getCpPmBidDetailByIdQuery(id, rateTypeBidTblCol)).getData().get(0).get(0);
        String tenor = new DbConnect(ifr, new Query().getCpPmBidDetailByIdQuery(id, tenorBidTblCol)).getData().get(0).get(0);

        if (isEmpty(cpRate)) return "Kindly enter Commercial Paper rate";

        if (rateType.equalsIgnoreCase(rateTypePersonal))
            personalRate = new DbConnect(ifr, new Query().getCpPmBidDetailByIdQuery(id, rateBidTblCol)).getData().get(0).get(0);

        if (rateType.equalsIgnoreCase(rateTypeBank)) {
            if (isEmpty(bankRate)) return "Kindly enter Bank Rate";

            if (checkBidStatus(bankRate, cpRate)) {
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 4, cpRate);
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 5, bankRate);
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 7, getMaturityDate(Integer.parseInt(tenor)));
                //ifr.setTableCellValue(cpBidReportTbl,rowIndex,9,defaultAlloc);
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 11, bidSuccess);
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 12, statusAwaitingMaturity);
                new DbConnect(ifr, new Query().getCpPmBidUpdateBankQuery(id, cpRate, bankRate, getMaturityDate(settlementDate, Integer.parseInt(tenor)), bidSuccess, statusAwaitingMaturity)).saveQuery();
                setCpPmTotalAllocation(ifr);
            } else {
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 11, bidFailed);
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 12, statusAwaitingTreasury);
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 7, empty);
                new DbConnect(ifr, new Query().getCpPmUpdateFailedBidsQuery(id, bidFailed)).saveQuery();
                setCpPmTotalAllocation(ifr);
            }
        } else if (rateType.equalsIgnoreCase(rateTypePersonal)) {
            if (checkBidStatus(personalRate, cpRate)) {
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 4, cpRate);
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 7, getMaturityDate(Integer.parseInt(tenor)));
                //ifr.setTableCellValue(cpBidReportTbl,rowIndex,9,defaultAlloc);
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 11, bidSuccess);
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 12, statusAwaitingMaturity);
                new DbConnect(ifr, new Query().getCpPmBidUpdatePersonalQuery(id, cpRate, getMaturityDate(settlementDate, Integer.parseInt(tenor)), bidSuccess, statusAwaitingMaturity)).saveQuery();
                setCpPmTotalAllocation(ifr);
            } else {
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 11, bidFailed);
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 12, statusAwaitingTreasury);
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 7, empty);
                new DbConnect(ifr, new Query().getCpPmUpdateFailedBidsQuery(id, bidFailed)).saveQuery();
                setCpPmTotalAllocation(ifr);
            }
        }
        return empty;
    }

    private String updateCpSmDetails(IFormReference ifr, int rowCount) {

        for (int i = 0; i < rowCount; i++) {
            String maturityDate = ifr.getTableCellValue(cpSmCpBidTbl, i, 2);
            String cpBillAmount = ifr.getTableCellValue(cpSmCpBidTbl, i, 3);
            String minimumPrincipal = getFieldValue(ifr, cpSmMinPrincipalLocal);
            long dayToMaturity = getDaysToMaturity(maturityDate);
            logger.info("days to maturity-- " + dayToMaturity);

            if (Float.parseFloat(cpBillAmount) < Float.parseFloat(minimumPrincipal))
                return "CP Bill Amount cannot be less than the minimum principal Amount. Correct row No. " + i + ".";

            if (dayToMaturity > 270)
                return "Number of days to maturity Cannot be more 270. Please correct Maturity Date Column. Days to Maturity: " + dayToMaturity + "";

            if (dayToMaturity < 1)
                return "Number of days to maturity Cannot be less than 1. Please correct Maturity Date Column. Days to Maturity: " + dayToMaturity + "";


            ifr.setTableCellValue(cpSmCpBidTbl, i, 6, String.valueOf(dayToMaturity));
            ifr.setTableCellValue(cpSmCpBidTbl, i, 7, smStatusOpen);
        }
        setFields(ifr, new String[]{cpWinRefNoLocal}, new String[]{generateCpWinRefNo(cpSmLabel)});
        return empty;
    }

    private void cpSmInvestmentSetup(IFormReference ifr) {
        disableFields(ifr, cpSmSetupLocal);
        if (getCpSmInvestmentSetupType(ifr).equalsIgnoreCase(smSetupNew)) {
            if (isEmpty(getWindowSetupFlag(ifr))) {
                setVisible(ifr, new String[]{cpSmCpBidTbl, cpUploadExcelBtn, cpDownloadXlsTempBtn, cpFileNameLocal});
                enableFields(ifr, new String[]{cpUploadExcelBtn, cpSmCpBidTbl, cpDownloadXlsTempBtn});
            }
        } else if (getCpSmInvestmentSetupType(ifr).equalsIgnoreCase(smSetupUpdate)) {
            setVisible(ifr, new String[]{cpSmCpBidTbl, cpUploadExcelBtn, cpFileNameLocal});
            setInvisible(ifr, new String[]{cpUploadExcelBtn, cpFileNameLocal, cpDownloadXlsTempBtn});
            disableFields(ifr, new String[]{cpSmCpBidTbl});
        } else {
            setInvisible(ifr, new String[]{cpUploadExcelBtn, cpFileNameLocal, cpSmCpBidTbl, cpDownloadXlsTempBtn});
        }
    }

    private void updateCpPmSuccessBids(IFormReference ifr) {
        new DbConnect(ifr, Query.getCpUpdatePmSuccessBidQuery(getWorkItemNumber(ifr), getCpVat(ifr), getCpCommission(ifr), getCpTxnFee(ifr))).saveQuery();
    }

    private String cpCheckUnAllocatedBids(IFormReference ifr) {
        resultSet = new DbConnect(ifr, Query.getCpPmCheckUnallocatedBids(getWorkItemNumber(ifr))).getData();
        int count = Integer.parseInt(resultSet.get(0).get(0));
        if (count > 0)
            return "Some bids are yet to be allocated, system will automatically fail these bids. Do you want to proceed? Number of bids (" + count + ")";

        return empty;
    }

    private void cpPmFailUnallocatedBids(IFormReference ifr) {
        new DbConnect(ifr, Query.getCpPmFailUnallocatedBids(getWorkItemNumber(ifr))).saveQuery();
    }
}
