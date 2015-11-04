package org.mifosplatform.organisation.taxmapping.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.stereotype.Service;
@Service
public interface TaxMapWritePlatformService {

	public CommandProcessingResult createTaxMap(final JsonCommand command);
	public CommandProcessingResult updateTaxMap(final JsonCommand command, final Long taxMapId);
	public CommandProcessingResult createLoanTaxMapping(Long entityId, JsonCommand command);
	public CommandProcessingResult updateLoanTaxMap(JsonCommand command);
	public CommandProcessingResult deleteLoanTaxMapId(Long entityId, JsonCommand command);
	public CommandProcessingResult deleteAllLoanTaxMapId(JsonCommand command);
}
