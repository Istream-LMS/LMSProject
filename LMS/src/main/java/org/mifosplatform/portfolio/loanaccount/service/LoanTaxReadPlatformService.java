package org.mifosplatform.portfolio.loanaccount.service;

import java.util.Collection;

import org.mifosplatform.organisation.taxmapping.data.TaxMapData;
import org.mifosplatform.portfolio.loanaccount.data.LoanTaxData;

public interface LoanTaxReadPlatformService {

	TaxMapData retrieveLoanTaxTemplate();

    Collection<LoanTaxData> retrieveLoanTaxes(Long loanId);

    LoanTaxData retrieveLoanTaxDetails(Long loanTaxId, Long loanId);

}
