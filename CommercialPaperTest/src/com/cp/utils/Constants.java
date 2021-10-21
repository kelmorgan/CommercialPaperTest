package com.cp.utils;

import java.io.File;

public interface Constants {
	String ProcessName = "MoneyMarketW";
	//WorkSteps
	String treasuryOfficerInitiator = "Treasury_Officer_Initiator";
	String treasuryOfficerVerifier = "Treasury_Officer_Verifier";
	String treasuryOfficerMaker = "Treasury_Officer_Maker";
	String treasuryOpsVerifier = "TreasuryOps_Verifier";
	String treasuryOpsMature = "TreasuryOps_Mature";
	String awaitingMaturityUtility = "AwaitingMaturity_Utility";
	String treasuryOpsMatureOnMaturity = "TreasuryOps_Mature_on_maturity";
	String branchInitiator = "Branch_Initiator";
	String branchVerifier = "Branch_Verifier";
	String branchException = "Branch_Exception";
	String rpcVerifier = "RPC_Verifier";
	String treasuryOpsFailed = "TreasuryOps_Failed";
	String awaitingMaturity = "AwaitingMaturity";
	String treasuryOpsSuccessful = "TreasuryOps_Successful";
	String utilityWs = "Utility_Initiation";
	String discardWs = "Discard";
	String exit = "Exit";
	String query = "Query";
	// Please input workSteps between comment bracket

	//general process Ids
	String selectProcessLocal = "g_select_market";
	String processTabName = "tab2";
	String dashboardTab = "0";
	String commercialTab = "1";
	String treasuryTab = "2";
	String omoTab = "3";
	String moneyMarketSection = "g_moneyMarket_section";
	String landMsgLabelSection = "landingMsgLabelSection";
	String solLocal = "g_sol";
	String loginUserLocal ="g_loginUser";
	String currWsLocal = "g_currWs";
	String prevWsLocal = "g_prevWs";
	String wiNameLocal ="WorkItemName";
	String decisionHisTable = "g_decisionHistory";
	String dhRowStaffId = "Staff ID";
	String dhRowProcess = "Process";
	String dhRowDecision = "Decision";
	String dhRowRemarks = "Remarks";
	String dhRowPrevWs = "Previous Workstep";
	String dhRowEntryDate = "Entry Date";
	String dhRowExitDate = "Exit Date";
	String dhRowTat = "TAT";
	String dhRowMarketType = "Market Type";
	String entryDateLocal = "EntryDateTime";
	String decHisFlagLocal = "g_decisionHistoryFlag";
	String landMsgLabelLocal = "g_landMsg";
	String goBackDashboardSection = "g_goBackDashboard_section";
	String windowSetupFlagLocal = "g_setupFlag";
	String wiNameFormLocal ="winame";
	String utilityFlagLocal = "cp_utilityFlag";
	String downloadFlagLocal ="downloadFlag";
	String branchNameLocal = "g_branchName";

	// cp sections
	String cpBranchPriSection = "cp_branchPm_section";
	String cpBranchSecSection = "cp_BranchSec_section";
	String cpLandingMsgSection = "cp_landingMsg_section";
	String cpMarketSection = "cp_market_section";
	String cpPrimaryBidSection = "cp_primaryBid_section";
	String cpTerminationSection = "cp_termination_section";
	String cpTerminationDetailsSection = "cp_terminationdetails_section";
	String cpProofOfInvestSection = "cp_poi_section";
	String cpDecisionSection = "cp_dec_section";
	String cpTreasuryPriSection = "cp_pmTreasury_section";
	String cpTreasurySecSection = "cp_secTreasury_section";
	String cpUtilityFailedPostSection = "cp_utilityFailedPost_section";
	String cpTreasuryOpsPriSection = "cp_treasuryOpsPm_section";
	String cpPostSection = "cp_post_section";
	String cpSetupSection="cp_setup_section";
	String cpCutOffTimeSection = "cp_cutoff_section";
	String cpCustomerDetailsSection ="cp_custDetails_section";
	String cpReDiscountRateSection = "rediscountratesection";
	String cpMandateTypeSection = "cp_mandatetypesection";
	String cpLienSection = "cpLienSection";
	String cpServiceSection = "cpServiceSection";
	String cpPmIssuerSection = "cpPmIssuerSection";
	String cpPbBeneDetailsSection ="cpPbBeneDetailsSection";
	String cpChargesSection = "CPCHARGESSECTION";
	String cpWindowDetailsSection = "CPWINDOWDTLSECTION";
	String cpCustomerIncomeSection = "CPCUSTOMERINCOMESECTION";
    // end of cp sections

	// commercial Paper process ids
	String cpSmTenorLocal = "CPSMTENOR";
	String cpSmRateLocal = "CPSMRATE";
	String cpPrincipalAtMaturityLocal = "CPPRINCATMATURITY";
	String cpResidualInterestLocal = "CPRESIDUALINTEREST";
	String cpInterestAtMaturityLocal = "CPINTERESTATMATURITY";
	String cpResidualInterestFlagLocal = "CPRESIDUALINTERESTFLAG";
	String cpCustomerIdLocal = "CPCUSTOMERID";
	String cpVatLocal = "CPVAT";
	String cpTxnFeeLocal = "CPTXNFEE";
	String cpPbBeneName = "CPPBBENENAME";
	String cpPbBeneAcctNo = "CPPBBENEACCTNO";
	String cpChargesBtn ="CPCHARGESBTN";
	String cpCommissionLocal = "CPCOMMISSION";
	String cpCustodyFeeLocal ="CPCUSTODYFEE";
	String cpIsStdCustodyFeeLocal = "CPISSTDCUSTODYFEE";
	String cpPmSettlementDateLocal = "CPPMSETTLDATE";
	String cpPmTotalAllocLocal ="CPPMTOTALALLOC";
	String cpPmIssuerNameLocal = "CP_PM_ISSUERNAME";
	String cpSelectMarketLocal = "cp_select_market";
	String cpRemarksLocal = "cp_remarks";
	String cpDecisionLocal = "cp_decision";
	String cpLandMsgLocal = "cp_landingMsg";
	String cpPrimaryMarket = "primary";
	String cpSecondaryMarket = "secondary";
	String cpCategoryLocal = "cp_category";
	String cpCategorySetup = "Setup";
	String cpCategoryBid = "Bid";
	String cpCategoryReDiscountRate = "Re-discount Rate";
	String cpCategoryModifyCutOffTime = "Cut off time modification";
	String cpCategoryUpdateLandingMsg = "Update Landing Message";
	String cpCategoryReport = "Report";
	String cpCategoryMandate = "Mandate";
	String cpSetupWindowBtn = "cp_setupWin_btn";
	String cpUpdateCutoffTimeBtn = "cp_updateCutoff_btn";
	String cpSetReDiscountRateBtn = "cp_rediscRate_btn";
	String cpLandingMsgSubmitBtn="cp_landMsgSubmit_btn";
	String cpUpdateLocal = "cp_updateMsg";
	String cpOpenDateLocal = "cp_open_window_date";
	String cpCloseDateLocal = "cp_close_window_date";
	String cpPmMinPriAmtLocal = "cp_mp_amount";
	String cpPmWinRefNoLocal = "cp_pmwu_ref";
	String cpPmWinRefNoBranchLocal = "cp_pmwinref_br";
	String cpSmWinRefNoBranchLocal = "cp_sec_id";
	String cpPmMinPriAmtBranchLocal ="cp_pm_mpBr";
	String cpCustomerNameLocal ="cp_custAcctName";
	String cpCustomerAcctNoLocal ="cp_custAcctNum";
	String cpCustomerEmailLocal = "cp_custAcctEmail";
	String cpCustomerSolLocal = "CPCUSTSOL";
	String cpLienStatusLocal ="cp_lien_status";
	String cpPmRateTypeLocal = "cp_rate_type";
	String cpPmPersonalRateLocal ="cp_personal_rate";
	String cpPmPrincipalLocal ="cp_principalAmt";
	String cpPmTenorLocal ="cp_tenor";
	String cpPmCustomerIdLocal ="cp_pm_custId";
	String cpPmInvestmentTypeLocal ="cp_investment_type";
	String cpPmReqTypeLocal ="cp_request_type";
	String cpPmReqFreshLabel ="Fresh Mandate";
	String cpPmReqFreshValue ="freshMandate";
	String cpAcctValidateBtn ="cp_acctValidateBtn";
	String cpTxnIdLocal ="cp_tsnId";
	String cpPostBtn ="cp_post_btn";
	String cpTokenLocal ="cp_token";
	String cpAllocSummaryTbl ="table88";
	String cpAllocationReqTbl ="table89";
	String cpBidReportTbl ="table90";
	String cpAllocTenorCol = "Tenor";
	String cpAllocRateCol = "Rate";
	String cpAllocTotalAmountCol = "Total Amount";
	String cpAllocRateTypeCol = "Rate Type";
	String cpAllocCountCol = "Count";
	String cpAllocStatusCol = "Status";
	String cpAllocGroupIndexCol = "Group Index";
	String cpDownloadBtn ="cp_downloadReport_btn";
	String cpUpdateBtn ="cp_upDateReport_btn";
	String cpViewGroupBtn ="cp_viewGroup_btn";
	String cpViewReportBtn ="cp_viewReport_btn";
	String cpAllocBankRateLocal ="cp_bank_rate";
	String cpAllocDefaultAllocLocal ="cp_default_ap";
	String cpAllocCpRateLocal ="cp_cpRate";
	String cpBidCustIdCol ="Customer ID";
	String cpBidAcctNoCol ="Account Number";
	String cpBidAcctNameCol ="Account Name";
	String cpBidTenorCol ="Tenor";
	String cpBidCpRateCol ="CP Rate";
	String cpBidBankRateCol = "Bank Rate";
	String cpBidPersonalRateCol ="Personal Rate";
	String cpBidMaturityDateCol = "Maturity Date";
	String cpBidDefAllocCol = "Default Allocation percentage";
	String cpBidNewAllocCol = "New Allocation Percentage";
	String cpBidTotalAmountCol = "Total Amount";
	String cpBidStatusCol ="Status";
	String cpBidStatusBidCol ="Bid Status";
	String cpPmAllocFlagLocal = "cp_pm_allocflag";
	String cpSmCutOffTimeLocal = "cp_secCuttoff";
	String cpSmCpBidTbl = "table93";
	String cpSmSetupLocal = "cp_sec_setUp";
	String cpSmWinRefLocal = "cp_sm_winref";
	String cpSmMinPrincipalLocal = "cp_sec_miniPrincipalAmt";
	String cpSmIFrameLocal = "cp_downloadBid_frame";
	String cpSmCustIdLocal = "cp_sec_custId";
	String cpSmMaturityDateBrLocal ="cp_sec_maturityDate";
	String cpSmInvestmentTypeLocal = "cp_sec_investmentType";
	String cpSmConcessionRateLocal ="cp_sec_concessionRate";
	String cpSmConcessionRateValueLocal ="cp_sec_concessionValue";
	String cpApplyBtn = "apply_btn";
	String cpSmInvestmentIdLocal = "cp_sec_investmentID";
	String cpSmPrincipalBrLocal = "cp_sec_principal_br";
	String cpSmBidInvestmentIdCol = "Investment ID";
	String cpSmBidIssuerCol = "CP Issuer";
	String cpSmBidDescCol = "CP Description";
	String cpSmBidMaturityDateCol = "Maturity Date";
	String cpSmBidDtmCol = "DTM (Days to Maturity)";
	String cpSmBidStatusCol = "Status";
	String cpSmBidAvailableAmountCol = "Available Amount";
	String cpSmBidRateCol = "Rate";
	String cpSmBidAmountSoldCol = "Amount Sold";
	String cpSmBidMandatesCol = "Mandates";
	String cpSmBidReminderCol = "Remainder";
	String cpSmInvestmentBrTbl = "table91";
	String cpSmMinPrincipalBrLocal = "cp_sec_miniPrincipalAmt_brch";
	String cpInvestBtn = "invest_btn";
	String cpReDiscountRateLess90Local = "cp_less90_prim";
	String cpReDiscountRate91To180Local = "cp_91_180days_prim";
	String cpReDiscountRate181To270Local = "cp_181_270days_prim";
	String cpReDiscountRate271To364Local = "cp_271_364days_prim";
	String cpMandateTypeLocal = "cp_mandatetype";
	String cpMandateTypeTerminate = "Termination";
	String cpMandateTypePoi = "POI";
	String cpMandateTypeLien = "Lien";
    String cpTermMandateLocal = "CP_TERMMANDATE";
	String cpTermMandateDateCol = "Date";
	String cpTermMandateRefNoCol = "Reference Number";
	String cpTermMandateAmountCol = "Amount";
	String cpTermMandateAcctNoCol = "Account Number";
	String cpTermMandateCustNameCol = "Customer Name";
	String cpTermMandateDtmCol = "Number of Days to Maturity";
	String cpTermMandateStatusCol = "Status";
	String cpTermMandateWinRefCol = "winref";
	String cpTermMandateTbl = "table94";
	String cpSelectMandateTermBtn = "cpTerminateMandateBtn";
	String cpSearchMandateTermBtn = "cpTermMandateSearchBtn";
	String cpTerminationTypeLocal = "cp_term_type";
	String cpTermSpecialRateLocal = "cp_special_rate";
	String cpTermSpecialRateValueLocal = "cp_special_rate_value";
	String cpTerminationTypeFull = "fullTerm";
	String cpTerminationTypePartial = "partTerm";
	String cpTermPartialAmountLocal = "CP_TERMPARTIALAMT";
	String cpTermPartialOptionLocal = "CP_TERMPTOPTION";
	String cpTermCustIdLocal = "CP_TERMCUSTID";
	String cpTermAmountDueLocal = "CP_TERMAMTDUE";
	String cpTermAdjustedPrincipalLocal = "CP_TERMADPRINCIPAL";
	String cpTermCalculateBtn = "calculateTermBtn";
	String cpTermDtmLocal = "CPTERMDTM";
	String cpTerminateBtn = "cpTerminateBtn";
	String cpLienTypeLocal = "CPLIENTYPE";
	String cpLienTypeSet = "SET";
	String cpLienTypeRemove = "REMOVE";
	String cpLienMandateIdLocal = "CPLIENMANDATEID";
	String cpPoiTbl = "table56";
	String cpPoiSearchBtn = "cpPoiSearchBtn";
	String cpPoiGenerateBtn = "cpPoiGenerateBtn";
	String cpPoiCustNameLocal = "cp_poi_acctName";
	String cpPoiCustAcctNoLocal = "cp_poi_acctNumb";
	String cpPoiCustEffectiveDateLocal = "cp_poi_effectiveDate";
	String cpPoiCustInterestLocal = "cp_poi_interest";
	String cpPoiCustMaturityDateLocal = "cp_poi_maturityDate";
	String cpPoiCustPrincipalAtMaturityLocal = "cp_poi_principalMaturity";
	String cpPoiCustTenorLocal = "cp_poi_tenor";
	String cpPoiCustIdLocal = "cp_poi_refCode";
	String cpPoiCustRateLocal = "cp_poi_rate";
	String cpPoiDateLocal = "POI_DATE";
	String cpPoiCustAmountInvestedLocal = "cp_poi_amtInvested";
	String cpPoiMandateLocal = "CPPOIMANDATE";
	String cpPoiDateCol = "Date";
	String cpPoiIdCol = "Reference Number";
	String cpPoiAmountCol = "Amount";
	String cpPoiAcctNoCol = "Account Number";
	String cpPoiAcctNameCol = "Customer Name";
	String cpPoiStatusCol = "Status";
	String cpTermIssueDateLocal = "CPTERMISSUEDATE";
	String cpTermBoDateLocal = "CPTERMBODATE";
	String cpTermTenorLocal = "CPTERMTENOR";
	String cpTermMaturityDateLocal = "CPTERMMATURITYDATE";
	String cpTermNoDaysDueLocal = "CPTERMNODAYSDUE";
	String cpTermPenaltyChargeLocal = "CPTERMPCHARGE";
	String cpTermRateLocal ="CPTERMRATE";
	String cpUploadExcelBtn = "cpUploadExcelBtn";
	String cpFileNameLocal = "CPFILENAME";
	String cpFetchMandateBtn = "cpFetchMandateBtn";
	String cpCheckLienBtn = "cpCheckLienBtn";
	String pbFlagLocal = "PBFLAG";
	String cpDownloadXlsTempBtn = "DOWNLOADXLSTEMPLATEBTN";
	String cpWinRefNoLocal = "CPWINREFNO";
	String cpFlagLocal = "CPFLAG";

	//common variables
	String omoProcess = "omo_market";
	String omoProcessName = "OMO Auctions";
	String treasuryProcess = "tb_market";
	String treasuryProcessName = "Treasury Bills";
	String commercialProcess = "cp_market";
	String commercialProcessName = "Commercial Paper";
	String visible = "visible";
	String disable = "disable";
	String mandatory = "mandatory";
	String True = "true";
	String False = "false";
	String na = "N.A";
	String decDiscard = "Discard";
	String decSubmit = "Submit";
	String decApprove = "Approve";
	String decReject = "Reject";
	String decReturnLabel ="Return to Initiator";
	String decReturn ="Return";
	String dbDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	String dbDateFormat = "yyyy-MM-dd";
	String flag = "Y";
	String endMail = "@firstbanknigeria.com";
	String groupName = "T_USERS";
	String empty = "";
	String mailSubject = "Money Market Workflow Notification";
	String primary = "Primary";
	String secondary = "Secondary";
	String cpPmLabel = "CPPMA";
	String cpSmLabel = "CPSMA";
	String cpIdLabel ="CP";
	String cpSmIdInvestmentLabel = "CPSMI";
	String cpRefNoDateFormat ="ddMMyyyy";
	String windowOpenFlag ="N";
	String windowCloseFlag ="Y";
	String windowOpen = "opened";
	String windowClosed = "closed";
	String windowInactiveMessage = "No Window is currently open, Try again later";
	String windowActiveErrMessage = "A Window for this market is currently open, Kindly wait till it elapse to open a new window";
	String invalidSchemeCode1 = "SA231";
	String invalidSchemeCode2 = "SA310";
	String invalidSchemeCode3 = "SA340";
	String invalidSchemeCode4 = "SA327";
	String cpInvalidAccountErrorMessage ="This account is not valid for CP processing";
	String rateTypeBank ="Bank";
	String rateTypePersonal ="Personal";
	String minPrincipalErrorMsg ="Customer principal cannot be less than window minimum principal";
	String cpSmMinPrincipalErrorMsg ="Customer principal cannot be less than window minimum principal or greater than available investment amount";
	String tenorErrorMsg ="Tenor must be between 7 to 270 days. Please enter a valid number";
	String cpValidateWindowErrorMsg ="This CP window has been closed. Kindly wait till the next window to initiate";
	String tbTemplateName ="TB_POI";
	String cpTemplateName ="CP_POI";
	String cpEmailMsg = "Update email of customer on account maintenance workflow";
	String cpPmInvestmentPrincipal ="Principal";
	String cpPostSuccessMsg = "Posting Done Successfully";
	String currencyNgn ="NGN";
	//String apiSuccess ="success";
	String cpCusMailErrMsg ="Update email of customer on account maintenance workflow";
	String cpApiLimitErrorMsg ="Transaction above your limit to Post. Kindly enhance your limit";
	String exceptionMsg ="Exception occurred contact IBPS support";
	String statusAwaitingTreasury = "Awaiting Treasury";
	String statusAwaitingMaturity = "Awaiting Maturity";
	String bidSuccess = "Successful";
	String bidFailed = "Failed";
	String defaultAllocation = "100";
	String rateBidTblCol = "rate";
	String tenorBidTblCol = "tenor";
	String rateTypeBidTblCol = "ratetype";
	String smDefaultCutOffTime = "2 PM";
	String smMinPrincipal = "1000000";
	String smSetupNew = "New";
	String smSetupUpdate = "UpDate";
	String smStatusOpen= "Open";
	String smStatusClosed= "Closed";
	String smStatusMature= "Matured";
	String yes = "YES";
	String no = "NO";
	String cpSmMaturityDateErrMsg = "Maturity date differs from selected bid maturity date, please amend.";
	String cpLienErrMsg ="Commercial paper is Lien, kindly remove Lien on Commercial paper and try again.";
	String cpLienSetFlag = "Y";
	String cpLienRemoveFlag = "N";
	String apiSuccess = "SUCCESS";
	String apiFailed = "FAILED";
	String apiFailure = "FAILURE";
	String apiStatus = "Status";
	String debitFlag = "D";
	String creditFlag = "C";
	String transType = "T";
	String transSubTypeC = "CI";
	String transSubTypeB = "BI";
	String apiNoResponse = "No Response Found";
	String apiLimitErrMsg = "This transaction is above your limit to Post. Kindly increase your limit and try again";
	String principalNotInThousandMsg = "Principal must be in thousands";
	String cpSmInvestmentTypePrincipal ="Principal";
	String cpSmInvestmentTypePrincipalInterest="PrincipalInterest";
	String cpSearchTermErrMsg = "No mandate/s to display for your search criteria. Try again with another.";

	//eventName/controlName
	String formLoad = "formLoad";
	String onClick = "onClick";
	String onChange = "onChange";
	String onDone = "onDone";
	String onLoad = "onLoad";
	String custom = "custom";
	String sendMail = "sendMail";
	String onChangeProcess = "onChangeProcess";
	String decisionHistory = "decisionHistory";
	String goToDashBoard = "onClickGoBackToDashboard";
	String cpUpdateMsg = "onClickUpdateMsg";
	String cpOnSelectCategory = "onChangeCategory";
	String cpSetupWindowEvent = "setupWin";
	String cpOnSelectMarket = "onChangeMarket";
	String cpApiCallEvent = "cpApiCall";
	String cpOnChangeRateType ="onChangeRate";
	String validateWindowEvent ="validateWindow";
	String cpCheckPrincipalEvent ="onChangePrincipal";
	String cpCheckTenorEvent="onChangeTenor";
	String cpTokenEvent ="cpTokenEvent";
	String cpPostEvent ="cpPostEvent";
	String cpPostFlag ="cp_postFlag";
	String cpViewReportEvent ="cpViewReport";
	String cpDownloadEvent = "cpDownloadGrid";
	String cpGetPmGridEvent = "cpPmGrid";
	String cpViewGroupBidEvent = "viewGroupBids";
	String cpUpdateBidEvent = "updateBids";
	String cpSmSetupEvent = "smSetup";
	String cpSmCpUpdateEvent = "smCpBidUpdate";
	String cpSmApplyEvent ="smApply";
	String cpSmConcessionRateEvent = "smConcession";
	String cpSmCheckMaturityDateEvent = "checkMaturityDate";
	String cpSmInvestEvent = "cpSmInvest";
	String cpUpdateCutOffTimeEvent = "updateCutOffTime";
	String cpUpdateReDiscountRateEvent = "updateRediscountRate";
	String cpMandateTypeEvent = "mandateType";
	String cpSearchTermMandateEvent ="searchTermMandate";
	String cpSelectTermMandateEvent = "selectTermMandate";
	String cpSelectTermSpecialRateEvent = "selectSpecialRateTerm";
	String cpSelectTermTypeEvent = "selectTermType";
	String cpCalculateTermEvent = "calculateTermination";
	String cpPartialTermOptionEvent = "partialTermOption";
	String cpLienEvent = "lienEvent";
	String generateTemplateEvent = "TemplateGeneration";
	String cpPoiSearchEvent="poiSearch";
	String cpPoiProcessEvent="poiProcess";
	String cpFetchMandateEvent = "cpFetchMandate";
	String cpValidateAcctEvent = "cpValidateAcct";
	String cpValidateLienEvent = "cpValidateLien";
	String cpPmProcessAllocatedBidsEvent = "cpPmProcessAllocatedBids";
	String cpPmCheckUnAllocatedBidsEvent = "cpCheckUnAllocatedBids";
	String cpCheckDecisionEvent = "cpCheckDecision";
	String cpConfigureChargesEvent = "cpConfigureCharges";
	String cpFailUnallocatedBidsEvent= "cpFailUnallocatedBids";
	String cpUpdateSmInvestmentEvent ="cpUpdateSmInvestment";
	String cpSmReverseInvestmentEvent = "cpSmReverseInvestment";
	String cpChargeCustodyFeeEvent = "cpChargeCustodyFee";
	String cpTerminateBidEvent = "cpTerminateBid";

	//process info
	String externalTable = "moneymarket_ext";


	//config
	String logPath = "nglogs/NGF_Logs/MoneyMarket/";
	String configPath = System.getProperty("user.dir") + File.separator + "FBNConfig" + File.separator + "moneymarket.properties";
	String mailFromField ="MAILFROM";
	String processDefIdField = "PROCESSDEFID";
	String serverPortField = "SERVERPORT";
	String serverIpField = "SERVERIP";
	String templatePortField = "TEMPLATEPORT";
	String vatField ="VAT";
	String custodyFeeField ="CUSTODYFEE";
	String txnField ="TRANSACTIONFEE";
	String commissionField ="COMMISSION";

	//API SERVICENAME
	String postServiceName = "postRequestToFinacle";
	String fetchOdaAcctServiceName ="CURRENTACCOUNT";
	String fetchCaaAcctServiceName ="SPECIALACCOUNT";
	String fetchSbaAcctServiceName ="SAVINGACCOUNT";
	String fetchLienServiceName = "FETCHLIEN";
	String fetchLimitServiceName = "CIGETUSERLIMIT";
	String searchTranServiceName = "CISEARCHTRANSACTION";
	String tokenValidationServiceName = "TOKENVALIDATION";
	String placeLienServiceName = "placeLien";
	String removeLienServiceName = "REMOVELIEN";


	
	//treasury sections
	String tbSearchCustSection ="tb_searchCust_section";
	String tbMarketSection = "tb_market_section";
	String tbLandingMsgSection = "tb_setupmsg_section";
	String tbPriSetupSection = "tb_treasuryPm_section";
	String tbBranchPriSection = "tb_pm_br_section";
	String tbBrnchCusotmerDetails ="tb_custdetails_section";
	String tbBranchSecSection = "tb_sec_br_section";
	String tbTreasurySecSection = "tb_treasurySec_section";
	String tbPrimaryBidSection = "tb_pmBid_section";
	String tbTerminationSection = "tb_termination_section";
	String tbProofOfInvestSection = "tb_poi_section";
	String tbDecisionSection = "tb_dec_section";
	String tbTreasuryOpsSection ="tb_treasuryOps_section";
	String tbTreasurySecReportSection ="tb_secReport_section";
	String tbPostSection = "tb_post_section";
	String tbCutOffTimeSection = "tb_cutoff_section";
	String tbRediscountRate ="tb_rediscount_section";
	String tbCustBidSection ="tb_cust_bid_section";
	String tbUtilityFailedPostingSection ="tb_utilityFailedPosting_section";
	String tbLienSection ="tbLienSection";
	String tbPBCustDetailsSection = "tbPBCustDetailsSection";
	String tbUpdatePriMaturityDte ="tbUpdatePriMaturityDte";
	String tbCustodyFeeSection = "tbCustodyFeeSection";
	String[] allTbSections = {tbCustodyFeeSection,tbUtilityFailedPostingSection,tbSearchCustSection,tbCustBidSection,tbBrnchCusotmerDetails,tbMarketSection,tbLandingMsgSection ,tbPriSetupSection, tbTreasurySecSection,
			tbPrimaryBidSection,tbBranchPriSection, tbBranchSecSection,tbTerminationSection,tbProofOfInvestSection , tbDecisionSection ,
			tbTreasuryOpsSection,tbLienSection,tbTreasurySecReportSection,tbPostSection,tbRediscountRate,tbPBCustDetailsSection};

}
