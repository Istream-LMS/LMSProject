package org.mifosplatform.organisation.taxmapping.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.api.JsonQuery;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.taxmapping.data.LoanProductTaxData;
import org.mifosplatform.organisation.taxmapping.domain.LoanTaxMap;
import org.mifosplatform.organisation.taxmapping.domain.LoanTaxMapRepository;
import org.mifosplatform.organisation.taxmapping.domain.TaxMap;
import org.mifosplatform.organisation.taxmapping.domain.TaxMapRepository;
import org.mifosplatform.organisation.taxmapping.serialization.TaxMapCommandFromApiJsonDeserializer;
import org.mifosplatform.portfolio.loanaccount.loanschedule.service.LoanScheduleCalculationPlatformService;
import org.mifosplatform.portfolio.loanproduct.domain.LoanProduct;
import org.mifosplatform.portfolio.loanproduct.domain.LoanProductRepository;
import org.mifosplatform.portfolio.loanproduct.service.LoanProductReadPlatformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author raghu
 *
 */
@Service
public class TaxMapWritePlatformServiceImp implements TaxMapWritePlatformService{

	private final static Logger LOGGER = (Logger) LoggerFactory.getLogger(TaxMapWritePlatformService.class);
	
	private final PlatformSecurityContext context;
	private final TaxMapRepository taxMapRepository;
	private final TaxMapCommandFromApiJsonDeserializer apiJsonDeserializer;
	private final LoanScheduleCalculationPlatformService calculationPlatformService;
	private final FromJsonHelper fromJsonHelper;
	private final LoanTaxMapRepository loanTaxMapRepository;
	private final LoanProductRepository loanProductRepository;
	private final LoanProductReadPlatformService loanProductReadPlatformService;
	
	@Autowired
	public TaxMapWritePlatformServiceImp(final PlatformSecurityContext context,final TaxMapRepository taxMapRepository,
			final TaxMapCommandFromApiJsonDeserializer apiJsonDeserializer,final LoanScheduleCalculationPlatformService calculationPlatformService,
			final FromJsonHelper fromJsonHelper,final LoanTaxMapRepository loanTaxMapRepository,
			final LoanProductRepository loanProductRepository,
			final LoanProductReadPlatformService loanProductReadPlatformService){
		this.context = context;
		this.taxMapRepository = taxMapRepository;
		this.apiJsonDeserializer = apiJsonDeserializer;
		this.calculationPlatformService = calculationPlatformService;
		this.fromJsonHelper = fromJsonHelper;
		this.loanTaxMapRepository = loanTaxMapRepository;
		this.loanProductRepository = loanProductRepository;
		this.loanProductReadPlatformService = loanProductReadPlatformService;
	}
	
	/* (non-Javadoc)
	 * @see #createTaxMap(org.mifosplatform.infrastructure.core.api.JsonCommand)
	 */
	@Transactional
	@Override
	public CommandProcessingResult createTaxMap(final JsonCommand command){
		
		TaxMap  taxmap = null;
		try{
			this.context.authenticatedUser();
			this.apiJsonDeserializer.validateForCreate(command);
			
			String taxMapString = command.stringValueOfParameterNamed("taxCode");
			
			TaxMap oldTaxmap = this.taxMapRepository.findByTaxCode(taxMapString);
			
			if(null != oldTaxmap) {
				oldTaxmap.setEndDate(new Date());
				oldTaxmap.setIsNew(0);
				this.taxMapRepository.save(oldTaxmap);
			}
		
			taxmap = TaxMap.fromJson(command);
			this.taxMapRepository.save(taxmap);
			
			Collection<LoanProductTaxData> productDatas = this.loanProductReadPlatformService.retrieveLoanProductIds(oldTaxmap.getId());

			for (LoanProductTaxData productData : productDatas) {

				LoanProduct loanProduct = this.loanProductRepository.findOne(productData.getProductId());

				Collection<TaxMap> taxMaps = loanProduct.getTaxes();

				for (TaxMap taxMapData : taxMaps) {

					if (taxMapData.getId() == oldTaxmap.getId()) {
						taxMaps.remove(taxMapData);
						break;
					}
				}

				taxMaps.add(taxmap);
				loanProduct.updateTaxes(taxMaps);

				this.loanProductRepository.save(loanProduct);
			}
			
		}catch(final DataIntegrityViolationException dve){
			handleDataIntegrityIssues(command, dve);
			return new CommandProcessingResult(Long.valueOf(-1));
		}
		return new CommandProcessingResultBuilder().withEntityId(taxmap.getId()).build();
	}
	
	/* (non-Javadoc)
	 * @see #updateTaxMap(org.mifosplatform.infrastructure.core.api.JsonCommand, java.lang.Long)
	 */
	@Transactional
	@Override
	public CommandProcessingResult updateTaxMap(final JsonCommand command,final Long taxMapId){
		TaxMap taxMap = null;
		try{
			this.context.authenticatedUser();
			this.apiJsonDeserializer.validateForCreate(command);
			taxMap = retrieveTaxMapById(taxMapId);
			final Map<String, Object> changes = taxMap.update(command);
			
			if(!changes.isEmpty()){
				this.taxMapRepository.saveAndFlush(taxMap);
			}
			
			return new CommandProcessingResultBuilder().withCommandId(command.commandId())
					.withEntityId(taxMap.getId())
					.with(changes).build();
		}catch(final DataIntegrityViolationException dve){
			if (dve.getCause() instanceof ConstraintViolationException) {
			handleDataIntegrityIssues(command, dve);
		  }
			return new CommandProcessingResult(Long.valueOf(-1));
		}
	
	}
	
	 private TaxMap retrieveTaxMapById(final Long taxMapId) {
		 
	        final TaxMap taxMap = this.taxMapRepository.findOne(taxMapId);
	        if (taxMap == null) { 
	        	throw new PlatformDataIntegrityException("validation.error.msg.taxmap.taxcode.doesnotexist",
	        	"validation.error.msg.taxmap.taxcode.doesnotexist",taxMapId.toString(),
	        	"validation.error.msg.taxmap.taxcode.doesnotexist");
	        	}
	        return taxMap;
	    }
	
	
	private void handleDataIntegrityIssues(final JsonCommand command, final DataIntegrityViolationException dve) {

      final Throwable realCause = dve.getMostSpecificCause();
      
      LOGGER.error(dve.getMessage(), dve);   
       if (realCause.getMessage().contains("taxcode")){
       	throw new PlatformDataIntegrityException("validation.error.msg.taxmap.taxcode.duplicate",
       			"A taxcode with name'"+ command.stringValueOfParameterNamed("taxCode")+"'already exists",
       			command.stringValueOfParameterNamed("taxCode"));
       }else{
    	   throw new PlatformDataIntegrityException("error.msg.could.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "+ dve.getMessage());
       }
       
    }
	
	@Override
	public CommandProcessingResult createLoanTaxMapping(Long entityId, JsonCommand command) {
		
		final JsonElement parsedQuery = this.fromJsonHelper.parse(command.json());
		final JsonQuery query = JsonQuery.from(command.json(), parsedQuery, this.fromJsonHelper);
		boolean flag = false;
		
		if(entityId == 1L) {
			flag = true;
		}
		String data = this.calculationPlatformService.calculateTaxLoanSchedule(query, flag);
		return new CommandProcessingResultBuilder().withResourceIdAsString(data).build();
	}

	@Override
	public CommandProcessingResult updateLoanTaxMap(JsonCommand command) {
		
		final JsonElement parsedJson = this.fromJsonHelper.parse(command.json());

		if (parsedJson.isJsonObject()) {
			
            final JsonObject topLevelJsonElement = parsedJson.getAsJsonObject();
            
            final Long loanId = this.fromJsonHelper.extractLongNamed("loanId", topLevelJsonElement); 
            
            if (topLevelJsonElement.has("taxArray") && topLevelJsonElement.get("taxArray").isJsonArray()) {
            	
            	final JsonArray array = topLevelJsonElement.get("taxArray").getAsJsonArray();
                                         
				for (int i = 0; i < array.size(); i++) {					
                    final JsonObject loanChargeElement = array.get(i).getAsJsonObject();                         
                    final Long taxMapId = this.fromJsonHelper.extractLongNamed("taxMapId", loanChargeElement);                  
                    LoanTaxMap loanTaxMap = this.loanTaxMapRepository.findOne(taxMapId);                   
                    loanTaxMap.updateLoanId(loanId);
                    this.loanTaxMapRepository.save(loanTaxMap);
                }	
            }
            
            return new CommandProcessingResultBuilder().withEntityId(loanId).build();
        }
		
		return null;
	}
	
	@Override
	public CommandProcessingResult deleteLoanTaxMapId(Long loanId, JsonCommand command) {
		
		List<LoanTaxMap> loanTaxMaps = this.loanTaxMapRepository.findByLoanId(loanId);
		
		for (LoanTaxMap loanTaxMap : loanTaxMaps) {
			this.loanTaxMapRepository.delete(loanTaxMap.getId());
		}
		
		return new CommandProcessingResultBuilder().withEntityId(loanId).build();		
	}
	
	@Override
	public CommandProcessingResult deleteAllLoanTaxMapId(JsonCommand command) {
		
		final JsonElement parsedJson = this.fromJsonHelper.parse(command.json());
		
		if (parsedJson.isJsonObject()) {
			
            final JsonObject topLevelJsonElement = parsedJson.getAsJsonObject();                
            
            if (topLevelJsonElement.has("taxMapArray") && topLevelJsonElement.get("taxMapArray").isJsonArray()) {
            	
            	final JsonArray array = topLevelJsonElement.get("taxMapArray").getAsJsonArray();
                                         
				for (int i = 0; i < array.size(); i++) {					
                    final JsonObject loanChargeElement = array.get(i).getAsJsonObject();                         
                    final Long taxMapId = this.fromJsonHelper.extractLongNamed("taxMapId", loanChargeElement);                  
                    this.loanTaxMapRepository.delete(taxMapId);                                 
                }	
            }
            
            return new CommandProcessingResultBuilder().withEntityId(0L).build();
        }
		return null;
	}
}
