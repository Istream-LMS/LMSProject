package org.mifosplatform.organisation.taxmapping.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.RoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.taxmapping.data.TaxMapData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 * @author RAghu
 *
 */
@Service
public class TaxMapReadPlatformServiceImp implements TaxMapReadPlatformService {

	private final PlatformSecurityContext context;
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public TaxMapReadPlatformServiceImp(final PlatformSecurityContext context,final RoutingDataSource dataSource) {
		
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);

	}

	/* (non-Javadoc)
	 * @see #retriveTaxMapData(java.lang.String)
	 * based on charge code
	 */
	@Override
	public List<TaxMapData> retriveTaxMapData(final boolean isNew) {

		final TaxMapDataMapper mapper = new TaxMapDataMapper();
		
		String sql = "Select " + mapper.schema();
		
		if(isNew) {
			sql = sql + " where tm.is_new=1 ";
		}
		
		sql += " order by tm.end_date asc ";
		
		return jdbcTemplate.query(sql,mapper,new Object[]{});
	}

	/* (non-Javadoc)
	 * @see #retrievedSingleTaxMapData(java.lang.Long)
	 */
	@Override
	public TaxMapData retrievedSingleTaxMapData(final Long id) {
		
		try{
			this.context.authenticatedUser();
        final TaxMapDataMapper mapper = new TaxMapDataMapper();
		final String sql = "Select " + mapper.schema()+ " where tm.id = ?";
		return jdbcTemplate.queryForObject(sql, mapper, new Object[] { id });
		
		}catch (EmptyResultDataAccessException accessException) {
			return null;
		}
		
	}

	private class TaxMapDataMapper implements RowMapper<TaxMapData> {
		
		
		public String schema(){
			
			return " tm.id AS id,tm.charge_type AS chargeType,tm.tax_code AS taxCode,tm.start_date AS startDate,tm.tax_type as taxType,"
				+ "tm.rate AS rate,tm.tax_inclusive as taxInclusive,tm.end_date as endDate FROM m_tax_mapping tm ";
		}
		
		 public String loanProductTaxSchema() {
	            return schema() + " join m_product_loan_tax plt on plt.tax_id = tm.id";
	     }

		@Override
		public TaxMapData mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			final Long id = rs.getLong("id");
			final String chargeType = rs.getString("chargeType");
			final String taxCode = rs.getString("taxCode");
			final LocalDate startDate = JdbcSupport.getLocalDate(rs, "startDate");
			final String taxType = rs.getString("taxType");
			final BigDecimal rate = rs.getBigDecimal("rate");
			final Integer taxInclusive = rs.getInt("taxInclusive");
			final LocalDate endDate = JdbcSupport.getLocalDate(rs, "endDate");
			
			return new TaxMapData(id, chargeType, taxCode, startDate, taxType,
					              rate, taxInclusive, endDate);
		}

	}

	@Override
	public Collection<TaxMapData> retrieveLoanProductTaxes(Long loanProductId) {
		
		this.context.authenticatedUser();
        final TaxMapDataMapper tm = new TaxMapDataMapper();

        final String sql = "select " + tm.loanProductTaxSchema() + " where plt.product_loan_id=?";

        return this.jdbcTemplate.query(sql, tm, new Object[] { loanProductId });
	}
	
	}


