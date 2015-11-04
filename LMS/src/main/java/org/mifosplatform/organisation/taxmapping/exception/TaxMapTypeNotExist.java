package org.mifosplatform.organisation.taxmapping.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

/**
 * A {@link RuntimeException} thrown when taxmap resources are not found.
 */
public class TaxMapTypeNotExist extends AbstractPlatformResourceNotFoundException {

	public TaxMapTypeNotExist(final String type) {
        super("error.msg.taxmap.type.invalid", "TaxMap with this identifier " + type + " does not exist", type);  
 }

}
