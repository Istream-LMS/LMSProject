package org.mifosplatform.portfolio.loanaccount.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

public class LeaseScreenReportFileNotFoundException extends AbstractPlatformDomainRuleException {

	public LeaseScreenReportFileNotFoundException(String fileName) {
        super("error.msg.loan.screen.report.file.not.found", "lease screen report pdf file Not Found with this " + fileName , fileName);
    }
}
