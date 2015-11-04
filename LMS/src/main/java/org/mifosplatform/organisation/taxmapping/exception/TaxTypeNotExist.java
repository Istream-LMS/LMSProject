package org.mifosplatform.organisation.taxmapping.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

/**
 * A {@link RuntimeException} thrown when taxmap resources are not found.
 */
public class TaxTypeNotExist extends AbstractPlatformResourceNotFoundException {

	public TaxTypeNotExist(final String taxType) {
        super("error.msg.tax.taxType.invalid", "taxType with this identifier " + taxType + " does not exist", taxType);  
 
	}
	
}
