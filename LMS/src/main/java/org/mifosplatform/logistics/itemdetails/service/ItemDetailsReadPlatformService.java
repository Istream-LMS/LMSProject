package org.mifosplatform.logistics.itemdetails.service;

import java.util.List;

import org.mifosplatform.infrastructure.core.service.Page;
import org.mifosplatform.logistics.item.data.ItemData;
import org.mifosplatform.logistics.itemdetails.data.AllocationHardwareData;
import org.mifosplatform.logistics.itemdetails.data.ItemDetailsData;
import org.mifosplatform.logistics.itemdetails.data.ItemSerialNumberData;
import org.mifosplatform.logistics.itemdetails.data.ItemMasterIdData;
import org.mifosplatform.logistics.itemdetails.data.QuantityData;
import org.mifosplatform.logistics.supplier.service.SearchSqlQuery;

public interface ItemDetailsReadPlatformService {


	public ItemSerialNumberData retriveAllocationData(List<String> itemSerialNumbers,QuantityData quantityData, ItemMasterIdData itemMasterIdData);
	
	public AllocationHardwareData retriveInventoryItemDetail(String serialNumber);

	List<String> retriveSerialNumbers();

	public Page<ItemDetailsData> retriveAllItemDetails(SearchSqlQuery searchItemDetails,String officeName,String itemCode,String manufacturer);

	public List<String> retriveSerialNumbersOnKeyStroke(Long oneTimeSaleId,String query, Long officeId);

	public List<ItemDetailsData> retriveSerialNumbersOnKeyStroke(String query);
	
	public ItemDetailsData retriveSingleItemDetail(Long itemId);


	public ItemData retriveItemDetailsDataBySerialNum(String query, Long clientId);
	
	List<ItemData> retrieveItemData();
}
