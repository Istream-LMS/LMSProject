package org.mifosplatform.organisation.feemaster.domain;

import org.mifosplatform.organisation.feemaster.exception.FeeMasterNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FeeMasterRepositoryWrapper {

    private final FeeMasterRepository repository;

    @Autowired
    public FeeMasterRepositoryWrapper(final FeeMasterRepository repository) {
        this.repository = repository;
    }

    public FeeMaster findOneWithNotFoundDetection(final Long id) {

        final FeeMaster feeMasterDefinition = this.repository.findOne(id);
        if (feeMasterDefinition == null || feeMasterDefinition.isDeleted()) { throw new FeeMasterNotFoundException(id); }

        return feeMasterDefinition;
    }
}
