package com.initiator.worksteps;

import com.cp.utils.Shared;
import com.cp.utils.SharedI;
import com.cp.utils.LogGenerator;
import com.cp.utils.MailSetup;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TreasuryOfficerInitiator extends Shared implements IFormServerEventHandler, SharedI {
    private final Logger logger = LogGenerator.getLoggerInstance(TreasuryOfficerInitiator.class);

    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        try {
        	beforeFormLoadActivity(ifr);
        }
        catch (Exception e){ logger.error("Exception-- "+ e.getMessage());}
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
                case formLoad:{}
                break;
                case onLoad:{}
                break;
                case onClick:{
                    switch (control){
                        case goToDashBoard:{
                            backToDashboard(ifr);
                           if (isCpProcess(ifr)) cpBackToDashboard(ifr);

                            clearFields(ifr,new String[] {selectProcessLocal});
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
                            if (isCpWindowActive(ifr)){
                                disableCpSections(ifr);
                                setFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal},new String[]{decDiscard,windowActiveErrMessage});
                                disableFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
                                return windowActiveErrMessage;
                            }
                        }


                    }
                }
                break;
                case custom:{}
                break;
                case onDone:{
                	switch(control) {
                	}
                }
                break;
                case decisionHistory:{
                   if (isCpProcess(ifr)) setCpDecisionHistory(ifr);
                }
                break;
                case sendMail:{
                   if(isCpProcess(ifr)) cpSendMail(ifr);
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
    public void cpSendMail(IFormReference ifr){
        if (getCpDecision(ifr).equalsIgnoreCase(decSubmit)) {
            message = "A window open request for Commercial Paper has been Initiated with ref number " + getWorkItemNumber(ifr) + ".";
            new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
        }
    }
    @Override
    public void cpFormLoadActivity(IFormReference ifr){
        cpSetDecision(ifr);
        setVisible(ifr, new String[]{cpLandingMsgSection, cpDecisionSection, cpMarketSection});
        enableFields(ifr,new String[]{cpLandMsgLocal,cpSelectMarketLocal});
        setMandatory(ifr,new String [] {cpSelectMarketLocal,cpLandMsgLocal,cpDecisionLocal,cpRemarksLocal});
    }
    public void beforeFormLoadActivity(IFormReference ifr){
        hideProcess(ifr);
        hideCpSections(ifr);
        hideTbSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        hideShowBackToDashboard(ifr,False);
        setGenDetails(ifr);
        clearFields(ifr,new String [] {selectProcessLocal});
        setMandatory(ifr, new String[]{selectProcessLocal});
        setFields(ifr, new String[]{currWsLocal,prevWsLocal},new String[]{getCurrentWorkStep(ifr),na});
        setWiName(ifr);
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr,cpDecisionLocal,new String[]{decSubmit,decDiscard});
    }

    private void cpBackToDashboard(IFormReference ifr) {
        undoMandatory(ifr,new String [] {cpSelectMarketLocal,cpLandMsgLocal,cpDecisionLocal,cpRemarksLocal});
        clearFields(ifr,new String [] {cpSelectMarketLocal,cpLandMsgLocal,cpDecisionLocal,cpRemarksLocal});
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
