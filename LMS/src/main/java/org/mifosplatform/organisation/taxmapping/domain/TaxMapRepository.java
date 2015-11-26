package org.mifosplatform.organisation.taxmapping.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaxMapRepository extends JpaRepository<TaxMap, Long>,JpaSpecificationExecutor<TaxMap> {

	@Query("from TaxMap taxMap where taxMap.taxCode =:taxCode and taxMap.isNew = 1")
	TaxMap findByTaxCode(@Param("taxCode") String taxCode);

}
