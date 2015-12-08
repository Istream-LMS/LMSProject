package org.mifosplatform.portfolio.loanproduct.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

public class ExportFileNameNotNullException extends AbstractPlatformDomainRuleException {
	
	public ExportFileNameNotNullException() {
		super("error.msg.export.file.name.not.null", "File Name should be not null ", "");
	}

}
