package org.mifosplatform.logistics.agent.service;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.logistics.agent.data.AgentItemSaleData;
import org.mifosplatform.logistics.mrn.data.MRNDetailsData;
import org.mifosplatform.organisation.mcodevalues.data.MCodeData;

public interface ItemSaleReadPlatformService {

	List<AgentItemSaleData> retrieveAllData();

	AgentItemSaleData retrieveSingleItemSaleData(Long id);
	
	List<MRNDetailsData> retriveItemsaleIds();

}
