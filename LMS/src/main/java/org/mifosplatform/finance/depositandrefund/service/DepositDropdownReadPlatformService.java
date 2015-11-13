package org.mifosplatform.finance.depositandrefund.service;

import java.util.List;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public interface DepositDropdownReadPlatformService {
	
	List<EnumOptionData> retrieveFeeMasterCollectionTimeTypes();
	
	List<EnumOptionData> retrieveFeeMasterCalculationTypes();

	List<EnumOptionData> retrieveFeeMasterOnDepositTypes();
	

}
