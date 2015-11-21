package org.mifosplatform.organisation.feemaster.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

public class LoanFeeMasterNotFoundException extends AbstractPlatformResourceNotFoundException {

    public LoanFeeMasterNotFoundException(final Long id) {
        super("error.msg.loanFeeMaster.id.invalid", "Loan Deposit with identifier " + id + " does not exist", id);
    }

    public LoanFeeMasterNotFoundException(final Long id, final Long loanId) {
        super("error.msg.loanFeeMaster.id.invalid.for.given.loan", "Loan Deposit with identifier " + id + " does not exist for loan " + loanId,
                id, loanId);
    }
}
