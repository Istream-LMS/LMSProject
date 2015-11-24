package org.mifosplatform.portfolio.loanaccount.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformDomainRuleException;

import com.google.gson.JsonArray;

public class DepositsNotAllowMoreThanOneException extends AbstractPlatformDomainRuleException {

	public DepositsNotAllowMoreThanOneException(JsonArray array) {
		super("error.msg.loan.deposit.not.allowed.morethan.one","Deposits Not Allowed More Than One", 
				array);
	}

}
