package org.mifosplatform.organisation.taxmapping.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;


/**
 * A {@link RuntimeException} thrown when taxmap resources are not found.
 */
public class TaxMapInstanceNotFoundException extends AbstractPlatformResourceNotFoundException {

	 public TaxMapInstanceNotFoundException(final Long id) {
	        super("error.msg.tax.id.invalid", "TaxMap with this identifier " + id + " does not exist", id);  
	 }
	 
}
