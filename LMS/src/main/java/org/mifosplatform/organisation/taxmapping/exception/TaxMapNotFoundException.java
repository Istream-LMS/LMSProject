package org.mifosplatform.organisation.taxmapping.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

public class TaxMapNotFoundException extends AbstractPlatformResourceNotFoundException {

    public TaxMapNotFoundException(final Long id) {
        super("error.msg.taxmap.id.invalid", "Taxmap with identifier " + id + " does not exist", id);
    }

	public TaxMapNotFoundException(String taxCode) {
		super("error.msg.taxmap.taxCode.invalid", "Taxmap with this " + taxCode + " does not exist", taxCode);
	}
}
