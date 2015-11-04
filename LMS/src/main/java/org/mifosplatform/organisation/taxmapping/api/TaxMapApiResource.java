package org.mifosplatform.organisation.taxmapping.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.organisation.taxmapping.data.TaxMapData;
import org.mifosplatform.organisation.taxmapping.service.TaxMapReadPlatformService;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.mcodevalues.api.CodeNameConstants;
import org.mifosplatform.organisation.mcodevalues.data.MCodeData;
import org.mifosplatform.organisation.mcodevalues.service.MCodeReadPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Raghu
 *
 *this api class use to create,update taxes 
 */
@Path("taxmap")
@Component
@Scope("singleton")
public class TaxMapApiResource {

	private final Set<String> RESPONSE_TAXMAPPING_PARAMETERS = new HashSet<String>(Arrays.asList("id","taxCode", "chargeType", "startDate", "taxType",
					"rate", "taxInclusive"));

	private String resourceNameForPermissions = "TAXMAPPING";
	private final ApiRequestParameterHelper apiRequestParameterHelper;
	private final PlatformSecurityContext context;
	private final DefaultToApiJsonSerializer<TaxMapData> apiJsonSerializer;
	private final PortfolioCommandSourceWritePlatformService commandSourceWritePlatformService;
	private final TaxMapReadPlatformService taxMapReadPlatformService;
	private final MCodeReadPlatformService mCodeReadPlatformService;

	@Autowired
	public TaxMapApiResource(final ApiRequestParameterHelper apiRequestParameterHelper,final PlatformSecurityContext context,
			final DefaultToApiJsonSerializer<TaxMapData> apiJsonSerializer,final PortfolioCommandSourceWritePlatformService commandSourceWritePlatformService,
			final TaxMapReadPlatformService taxMapReadPlatformService,final MCodeReadPlatformService mCodeReadPlatformService) {
		
		this.context = context;
		this.apiJsonSerializer = apiJsonSerializer;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.commandSourceWritePlatformService = commandSourceWritePlatformService;
		this.taxMapReadPlatformService = taxMapReadPlatformService;
		this.mCodeReadPlatformService = mCodeReadPlatformService;
	}

	@GET
	@Path("template")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retriveTaxMapTemplate(@QueryParam("chargeCode") final String chargeCode,@Context final UriInfo uriInfo) {
		
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		final Collection<MCodeData> taxTypeData = this.mCodeReadPlatformService.getCodeValue(CodeNameConstants.CODE_TAX_TYPE);
		final Collection<MCodeData> chargeTypeData = this.mCodeReadPlatformService.getCodeValue(CodeNameConstants.CODE_CHARGE_TYPE);
		final TaxMapData taxMapData=new TaxMapData(taxTypeData,chargeTypeData);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.apiJsonSerializer.serialize(settings, taxMapData,RESPONSE_TAXMAPPING_PARAMETERS);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String createTaxMap(final String jsonRequestBody) {

	final CommandWrapper command = new CommandWrapperBuilder().createTaxMap().withJson(jsonRequestBody).build();
	final CommandProcessingResult result = commandSourceWritePlatformService.logCommandSource(command);
	return apiJsonSerializer.serialize(result);
	}
	
	@GET
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retriveTaxDetailsForChargeCode(@Context final UriInfo uriInfo) {
		
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		final List<TaxMapData> taxMapData = taxMapReadPlatformService.retriveTaxMapData();
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		return this.apiJsonSerializer.serialize(settings, taxMapData,RESPONSE_TAXMAPPING_PARAMETERS);
	}


	@GET
	@Path("{taxMapId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String retrievedSingleTaxMap(@PathParam("taxMapId") final Long taxMapId,@Context final UriInfo uriInfo) {
		
		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		TaxMapData taxMapData = taxMapReadPlatformService.retrievedSingleTaxMapData(taxMapId);
		final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
		if(settings.isTemplate()){
		final Collection<MCodeData> taxTypeData = this.mCodeReadPlatformService.getCodeValue(CodeNameConstants.CODE_TAX_TYPE);
		final Collection<MCodeData> chargeTypeData = this.mCodeReadPlatformService.getCodeValue(CodeNameConstants.CODE_CHARGE_TYPE);
		taxMapData.setTaxTypeData(taxTypeData);
		taxMapData.setChargeTypeData(chargeTypeData);
		}
		return this.apiJsonSerializer.serialize(settings, taxMapData,RESPONSE_TAXMAPPING_PARAMETERS);

	}

	@PUT
	@Path("{taxMapId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateTaxMapData(@PathParam("taxMapId") final Long taxMapId,final String jsonRequestBody) {

		context.authenticatedUser().validateHasReadPermission(resourceNameForPermissions);
		final CommandWrapper commandRequest = new CommandWrapperBuilder().updateTaxMap(taxMapId).withJson(jsonRequestBody).build();
		final CommandProcessingResult result = commandSourceWritePlatformService.logCommandSource(commandRequest);
		return apiJsonSerializer.serialize(result);
	}
}
