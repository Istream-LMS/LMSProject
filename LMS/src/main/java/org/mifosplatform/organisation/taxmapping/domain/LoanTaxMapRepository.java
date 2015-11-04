package org.mifosplatform.organisation.taxmapping.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoanTaxMapRepository extends JpaRepository<LoanTaxMap, Long>,JpaSpecificationExecutor<LoanTaxMap> {

	@Query("from LoanTaxMap loanTaxMap where loanTaxMap.loanId =:loanId")
	List<LoanTaxMap> findByLoanId(@Param("loanId") Long loanId);
}
