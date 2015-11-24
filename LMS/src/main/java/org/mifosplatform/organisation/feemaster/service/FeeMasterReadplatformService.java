package org.mifosplatform.organisation.feemaster.service;

import java.util.Collection;

import org.mifosplatform.organisation.feemaster.data.FeeMasterData;

public interface FeeMasterReadplatformService {
	
	FeeMasterData retrieveSingleFeeMasterDetails(Long id);

	Collection<FeeMasterData> retrieveAllData(String transactionType);
	
	Collection<FeeMasterData> retrieveLoanProductFeeMasterData(Long loanProductId);
	
	FeeMasterData retrieveSingleFeeMasterDetails(String feeCode);
	
}
