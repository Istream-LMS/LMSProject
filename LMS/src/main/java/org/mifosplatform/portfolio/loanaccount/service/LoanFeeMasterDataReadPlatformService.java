package org.mifosplatform.portfolio.loanaccount.service;

import java.util.Collection;

import org.mifosplatform.portfolio.loanaccount.data.LoanFeeMasterData;

public interface LoanFeeMasterDataReadPlatformService {
	
    Collection<LoanFeeMasterData> retrieveLoanFeeMasterData(Long loanId);


}
