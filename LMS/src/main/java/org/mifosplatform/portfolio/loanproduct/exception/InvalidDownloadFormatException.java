package org.mifosplatform.portfolio.loanproduct.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

public class InvalidDownloadFormatException extends AbstractPlatformDomainRuleException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidDownloadFormatException(String commandType) {	
		super("error.msg.download.invalid.format", "Downloading with this" + commandType + "Format is Not Supported", commandType);
	}

}
