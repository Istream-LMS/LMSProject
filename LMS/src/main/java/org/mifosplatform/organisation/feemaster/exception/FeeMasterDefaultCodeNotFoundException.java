package org.mifosplatform.organisation.feemaster.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

public class FeeMasterDefaultCodeNotFoundException extends AbstractPlatformResourceNotFoundException {

	 public FeeMasterDefaultCodeNotFoundException(final String feeCode) {
	        super("error.msg.loanFeeMaster.feeCode.not.found", "Fee Master Code with this identifier " + feeCode + " does not exist",
	        		feeCode);
	    }

}
