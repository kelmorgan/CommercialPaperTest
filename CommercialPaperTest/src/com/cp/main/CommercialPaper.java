package com.cp.main;

import com.cp.utils.Constants;
import com.cp.utils.LogGenerator;
import com.cp.worksteps.*;
import com.initiator.worksteps.BranchInitiator;
import com.initiator.worksteps.TreasuryOfficerInitiator;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;

import org.apache.log4j.Logger;

public class CommercialPaper implements  Constants {
private final Logger logger = LogGenerator.getLoggerInstance(CommercialPaper.class);
private final IFormReference ifr;

	public CommercialPaper(IFormReference ifr) {
		this.ifr = ifr;
	}

	public IFormServerEventHandler getClassInstance() {
		IFormServerEventHandler objActivity  = null;
		String activityName = ifr.getActivityName();
		logger.info("activityName in cp jar: "+activityName);
		try {
			switch (activityName){
				case treasuryOfficerInitiator:{
					objActivity = new TreasuryOfficerInitiator();
					break;
				}
				case treasuryOfficerMaker:{
					objActivity = new TreasuryOfficerMaker();
					break;
				}
				case treasuryOfficerVerifier:{
					objActivity = new TreasuryOfficerVerifier();
					break;
				}
				case treasuryOpsVerifier:{
					objActivity = new TreasuryOpsVerifier();
					break;
				}
				case treasuryOpsMature:{
					objActivity = new TreasuryOpsMature();
					break;
				}
				case awaitingMaturity:
				case awaitingMaturityUtility:{
					objActivity = new AwaitingMaturity();
					break;
				}
				case treasuryOpsMatureOnMaturity:{
					objActivity = new TreasuryOpsMatureOnMaturity();
					break;
				}
				case branchMakerWs:{
					objActivity = new Branch_Maker();
					break;
				}
				case branchException:{
					objActivity = new BranchException();
					break;
				}
				case branchInitiator:{
					objActivity = new BranchInitiator();
					break;
				}
				case branchVerifier:{
					objActivity = new BranchVerifier();
					break;
				}
				case rpcVerifier:{
					objActivity = new RpcVerifier();
					break;
				}
				case treasuryOpsFailed:{
					objActivity = new TreasuryOpsFailed();
					break;
				}
				case treasuryOpsSuccessful:{
					objActivity = new TreasuryOpsSuccessful();
					break;
				}
				case utilityWs:{
					objActivity = new UtilityTemp();
					break;
				}
				case discardWs:
				case query:
				case exit:{
					objActivity = new Exit();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return objActivity;
	}

}
