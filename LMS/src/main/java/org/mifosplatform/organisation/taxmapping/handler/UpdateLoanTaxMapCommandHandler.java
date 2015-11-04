package org.mifosplatform.organisation.taxmapping.handler;

import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.organisation.taxmapping.service.TaxMapWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateLoanTaxMapCommandHandler implements NewCommandSourceHandler {
 
	private final TaxMapWritePlatformService taxMapWritePlatformService;  
	
	@Autowired
	public UpdateLoanTaxMapCommandHandler(final TaxMapWritePlatformService taxMapWritePlatformService){
		this.taxMapWritePlatformService =  taxMapWritePlatformService;
	}
	
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return taxMapWritePlatformService.updateLoanTaxMap(command);
	}
	
}
