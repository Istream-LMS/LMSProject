package org.mifosplatform.portfolio.loanproduct.exception;

import java.math.BigDecimal;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

public class PrincipalAmountGreaterThanDepositException extends AbstractPlatformResourceNotFoundException{

	public PrincipalAmountGreaterThanDepositException(final BigDecimal principal, final BigDecimal deposit) {
        super("error.msg.loan.calculator.deposit.invalid", "Deposit Amount '"+ deposit +
        		"' must be less than Principal Amount '" + principal + "'.", principal, deposit);
    }
	
}
