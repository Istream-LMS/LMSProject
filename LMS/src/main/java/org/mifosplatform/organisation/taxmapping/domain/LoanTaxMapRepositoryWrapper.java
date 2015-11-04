package org.mifosplatform.organisation.taxmapping.domain;

import org.mifosplatform.organisation.taxmapping.exception.TaxMapInstanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Wrapper for {@link LoanTaxMapRepository} that is responsible for
 * checking if {@link LoanTaxMap} is returned when using
 * <code>findOne</code> repository method and throwing an appropriate not found
 * exception.
 * </p>
 * 
 * <p>
 * This is to avoid need for checking and throwing in multiple areas of code
 * base where {@link LoanTaxMapRepository} is required.
 * </p>
 */
@Service
public class LoanTaxMapRepositoryWrapper {

	private LoanTaxMapRepository repository;
	
	@Autowired
    public LoanTaxMapRepositoryWrapper(final LoanTaxMapRepository repository) {
        this.repository = repository;
    }

    public LoanTaxMap findOneWithNotFoundDetection(final Long taxId) {
        final LoanTaxMap loanTaxMap = this.repository.findOne(taxId);
        if (loanTaxMap == null) { throw new TaxMapInstanceNotFoundException(taxId); }
        return loanTaxMap;
    }

    public void save(final LoanTaxMap loanTaxMap) {
        this.repository.save(loanTaxMap);
    }

    public void delete(final LoanTaxMap loanTaxMap) {
        this.repository.delete(loanTaxMap);
    }

    public void saveAndFlush(final LoanTaxMap loanTaxMap) {
        this.repository.saveAndFlush(loanTaxMap);
    }
}
