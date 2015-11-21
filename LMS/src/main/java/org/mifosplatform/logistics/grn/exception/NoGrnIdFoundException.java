package org.mifosplatform.logistics.grn.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;


public class NoGrnIdFoundException extends AbstractPlatformDomainRuleException {

    

	public NoGrnIdFoundException(Long orderId) {
		 super("error.msg.order.quantity..exceeds", "Grn id "+orderId+" not found. ",orderId);
		 
	}

	public NoGrnIdFoundException(String msg) {
		super("error.msg.grn.id.not.exists", "Grn id "+msg+" not found. ",msg);
	}
}
