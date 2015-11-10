package org.mifosplatform.portfolio.loanaccount.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

public class LeaseScreenReportFileNameNotNullException extends AbstractPlatformDomainRuleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LeaseScreenReportFileNameNotNullException() {
        super("error.msg.loan.screen.report.not.null", "lease screen report fileName Not Null");
    }
}
