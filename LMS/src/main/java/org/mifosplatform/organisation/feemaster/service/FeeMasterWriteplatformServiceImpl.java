package org.mifosplatform.organisation.feemaster.service;

import java.util.Map;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.feemaster.domain.FeeMaster;
import org.mifosplatform.organisation.feemaster.domain.FeeMasterRepository;
import org.mifosplatform.organisation.feemaster.exception.FeeMasterNotFoundException;
import org.mifosplatform.organisation.feemaster.serialization.FeeMasterCommandFromApiJsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class FeeMasterWriteplatformServiceImpl implements FeeMasterWriteplatformService {

	private final PlatformSecurityContext context;
	private final FeeMasterCommandFromApiJsonDeserializer feeMasterCommandFromApiJsonDeserializer;
	private final FeeMasterRepository feeMasterRepository;
	
	
	@Autowired
	public FeeMasterWriteplatformServiceImpl(final PlatformSecurityContext context,
			final FeeMasterCommandFromApiJsonDeserializer feeMasterCommandFromApiJsonDeserializer,
			final FeeMasterRepository feeMasterRepository) {

		this.context = context;
		this.feeMasterCommandFromApiJsonDeserializer = feeMasterCommandFromApiJsonDeserializer;
		this.feeMasterRepository = feeMasterRepository;
	}
	
	
	@Override
	public CommandProcessingResult createFeeMaster(JsonCommand command) {
		
		try {
			this.context.authenticatedUser();
			this.feeMasterCommandFromApiJsonDeserializer.validateForCreate(command.json());
			FeeMaster feeMaster = FeeMaster.fromJson(command);

    		this.feeMasterRepository.save(feeMaster);
    		return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(feeMaster.getId()).build();
    
    	} catch (DataIntegrityViolationException dve) {
    		handleItemDataIntegrityIssues(command, dve);
    		return CommandProcessingResult.empty();
    	}
	}
	
	 private void handleItemDataIntegrityIssues(final JsonCommand command, final DataIntegrityViolationException dve) {
	        Throwable realCause = dve.getMostSpecificCause();
	        if (realCause.getMessage().contains("fee_code")) {
	            final String name = command.stringValueOfParameterNamed("feeCode");
	            throw new PlatformDataIntegrityException("error.msg.fee.code.duplicate.name", "A Fee code with name '" + name + "' already exists");
	        } 

	        //logger.error(dve.getMessage(), dve);
	        throw new PlatformDataIntegrityException("error.msg.cund.unknown.data.integrity.issue",
	                "Unknown data integrity issue with resource: " + realCause.getMessage());
	    }


	@Override
	public CommandProcessingResult updateFeeMaster(JsonCommand command) {

   	 try{
   		 this.context.authenticatedUser();
   		 this.feeMasterCommandFromApiJsonDeserializer.validateForCreate(command.json());
   		 FeeMaster feeMaster = retrieveCodeBy(command.entityId());
   		 
   		 
   		 final Map<String, Object> changes = feeMaster.update(command);
   		 
   		 if(!changes.isEmpty()){
			 feeMasterRepository.saveAndFlush(feeMaster);
   		 }
   		
	   return new CommandProcessingResultBuilder() //
      .withCommandId(command.commandId()) //
      .withEntityId(command.entityId()) //
      .with(changes) //
      .build();
	}catch (DataIntegrityViolationException dve) {
	      handleItemDataIntegrityIssues(command, dve);
	      return new CommandProcessingResult(Long.valueOf(-1));
	  }

}
	private FeeMaster retrieveCodeBy(final Long id) {
        final FeeMaster feeMaster = this.feeMasterRepository.findOne(id);
        if (feeMaster == null) { throw new FeeMasterNotFoundException(id.toString()); }
        return feeMaster;
    }
	
	
	@Override
	public CommandProcessingResult deleteFeeMaster(Long id) {
		try{
			this.context.authenticatedUser();
			FeeMaster feeMaster=retrieveCodeBy(id);
			if(feeMaster.getDeleted()=='Y'){
				/*throw new ItemNotFoundException(id.toString());*/
			}
			feeMaster.delete();
			this.feeMasterRepository.save(feeMaster);
			return new CommandProcessingResultBuilder().withEntityId(id).build();
			
		}catch(DataIntegrityViolationException dve){
			handleItemDataIntegrityIssues(null, dve);
			return new CommandProcessingResult(Long.valueOf(-1));
		}
		
	}

}
