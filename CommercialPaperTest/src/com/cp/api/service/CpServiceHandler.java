package com.cp.api.service;

import com.cp.controller.CpController;
import com.cp.utils.Shared;
import com.cp.utils.Constants;
import com.newgen.iforms.custom.IFormReference;

public class CpServiceHandler implements Constants {

    public CpServiceHandler(IFormReference ifr) {
        this.ifr = ifr;
    }

    private final IFormReference ifr;
    public  String validateAccount (){
        CpController cpController = new CpController(ifr);
        return cpController.fetchAcctDetails();
    }
    public String validateAccountTest(){
        String email = "kelmorgan18@gmail.com";
        String sol = "191";
        String name = "Kufre Godwin Udoko";
        Shared.setFields(ifr, new String[]{cpCustomerEmailLocal, cpCustomerNameLocal, cpCustomerSolLocal}, new String[]{email, name, sol});
        return null;
    }

    public String validateLien (){
        CpController cpController = new CpController(ifr);
        return cpController.fetchLien();
    }
    public String validateLienTest (){
        Shared.setFields(ifr, cpLienStatusLocal, no);
        return null;
    }

    public String validateToken(){
        CpController cpController = new CpController(ifr);
        return cpController.tokenValidation(Shared.getCpOtp(ifr));
    }

    public String validateTokenTest(){
        Shared.disableFields(ifr,new String[]{cpTokenLocal});
        Shared.setVisible(ifr,new String[]{cpPostBtn});
        Shared.enableFields(ifr,new String[]{cpPostBtn});
        return null;
    }

    public String postTransactionTest(){
        Shared.setVisible(ifr,new String[]{cpTxnIdLocal});
        Shared.setFields(ifr, new String[]{cpTxnIdLocal, cpDecisionLocal,cpPostFlag}, new String[]{"M20", decApprove,flag});
        Shared.disableFields(ifr, new String[]{cpDecisionLocal, cpPostBtn,cpTxnIdLocal});
        if (Shared.isCpSecondaryMarket(ifr))
            Shared.setVisible(ifr,new String[]{cpSetupSection,cpInvestBtn});
        return null;
    }
}
