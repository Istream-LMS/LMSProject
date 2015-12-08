package org.mifosplatform.portfolio.loanaccount.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface LoanCalculatorWritePlatformService {

	CommandProcessingResult createLoanCalculator(final Long entityId, final JsonCommand command);
	
	String getExportJsonString(String apiRequestBodyAsJson, String commandParam);
	
}
