package org.mifosplatform.organisation.taxmapping.domain;

import org.mifosplatform.organisation.taxmapping.exception.TaxMapNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxMapRepositoryWrapper {

	private final TaxMapRepository repository;

    @Autowired
    public TaxMapRepositoryWrapper(final TaxMapRepository repository) {
        this.repository = repository;
    }

    public TaxMap findOneWithNotFoundDetection(final Long id) {

        final TaxMap taxDefinition = this.repository.findOne(id);
        if (taxDefinition == null) { throw new TaxMapNotFoundException(id); }

        return taxDefinition;
    }

	public TaxMap findByTaxCode(String taxCode) {
		
		final TaxMap taxDefinition = this.repository.findByTaxCode(taxCode);
        if (taxDefinition == null) { throw new TaxMapNotFoundException(taxCode); }

        return taxDefinition;
	}
}
