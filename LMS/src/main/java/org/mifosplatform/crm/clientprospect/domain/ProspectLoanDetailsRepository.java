package org.mifosplatform.crm.clientprospect.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProspectLoanDetailsRepository extends
		JpaRepository<ProspectLoanDetails, Long>,
		JpaSpecificationExecutor<ProspectLoanDetails> {

	@Query("from ProspectLoanDetails prospectLoanDetails where prospectLoanDetails.clientProspect =:clientProspect")
	ProspectLoanDetails findByProspectId(@Param("clientProspect") final ClientProspect clientProspect);
	
}
