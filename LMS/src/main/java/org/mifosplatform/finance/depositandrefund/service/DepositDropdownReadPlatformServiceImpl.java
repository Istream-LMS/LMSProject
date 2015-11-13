package org.mifosplatform.finance.depositandrefund.service;

import java.util.Arrays;
import java.util.List;

import org.mifosplatform.finance.depositandrefund.domain.DepositCalculationType;
import org.mifosplatform.finance.depositandrefund.domain.DepositTimeType;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.springframework.stereotype.Service;

import static org.mifosplatform.finance.depositandrefund.service.DepositEnumerations.*;

@Service
public class DepositDropdownReadPlatformServiceImpl implements
		DepositDropdownReadPlatformService {

	@Override
    public List<EnumOptionData> retrieveFeeMasterCollectionTimeTypes() {
        return Arrays.asList(depositTimeType(DepositTimeType.ONE_TIME_CHARGE));
    }
    
    @Override
    public List<EnumOptionData> retrieveFeeMasterCalculationTypes() {
        return Arrays.asList(depositCalculationType(DepositCalculationType.FLAT), depositCalculationType(DepositCalculationType.PERCENT_OF_AMOUNT));
    }

	@Override
	public List<EnumOptionData> retrieveFeeMasterOnDepositTypes() {
		return Arrays.asList(depositTimeType(DepositTimeType.DISBURSEMENT));
	}

}
