package org.mifosplatform.organisation.taxmapping.handler;

import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.organisation.taxmapping.service.TaxMapWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteAllLoanTaxMapCommandHandler implements NewCommandSourceHandler  {
 
	private final TaxMapWritePlatformService taxMapWritePlatformService;  
	
	@Autowired
	public DeleteAllLoanTaxMapCommandHandler(final TaxMapWritePlatformService taxMapWritePlatformService){
		this.taxMapWritePlatformService =  taxMapWritePlatformService;
	}
	
	@Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		return taxMapWritePlatformService.deleteAllLoanTaxMapId(command);
	}
	
}
