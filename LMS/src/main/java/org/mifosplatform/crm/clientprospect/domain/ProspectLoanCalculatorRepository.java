package org.mifosplatform.crm.clientprospect.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProspectLoanCalculatorRepository extends
		JpaRepository<ProspectLoanCalculator, Long>,
		JpaSpecificationExecutor<ProspectLoanCalculator> {
}
