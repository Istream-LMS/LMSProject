package org.mifosplatform.portfolio.loanaccount.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.RoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.mcodevalues.api.CodeNameConstants;
import org.mifosplatform.organisation.mcodevalues.data.MCodeData;
import org.mifosplatform.organisation.mcodevalues.service.MCodeReadPlatformService;
import org.mifosplatform.organisation.taxmapping.data.TaxMapData;
import org.mifosplatform.portfolio.loanaccount.data.LoanTaxData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class LoanTaxReadPlatformServiceImpl implements LoanTaxReadPlatformService {
	
	private final JdbcTemplate jdbcTemplate;
    private final PlatformSecurityContext context;
    private final MCodeReadPlatformService mCodeReadPlatformService;
    
    @Autowired
    public LoanTaxReadPlatformServiceImpl(final PlatformSecurityContext context, final RoutingDataSource dataSource,
    		final MCodeReadPlatformService mCodeReadPlatformService) {
        this.context = context;
        this.mCodeReadPlatformService = mCodeReadPlatformService;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    private static final class LoanTaxMapper implements RowMapper<LoanTaxData> {

        public String schema() {
            return " ltm.id as Id, ltm.id as loanId, tm.id as taxId, tm.tax_code as taxCode, "
                    + "ltm.amount as amount, "
                    + "ltm.start_date as startDate, "
                    + "tm.charge_type as chargeType,tm.tax_type as taxType,tm.rate as rate,tm.tax_inclusive as taxInclusive "
                    + "from m_tax_mapping tm "
                    + "join m_loan_tax_mapping ltm on ltm.tax_id = tm.id ";
        }

        @Override
        public LoanTaxData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

            final Long id = rs.getLong("id");
            final Long loanId = rs.getLong("loanId");
            final Long taxId = rs.getLong("taxId");
            final String chargeType = rs.getString("chargeType");
			final String taxCode = rs.getString("taxCode");
			final LocalDate startDate = JdbcSupport.getLocalDate(rs, "startDate");
			final String taxType = rs.getString("taxType");
			final BigDecimal rate = rs.getBigDecimal("rate");
			final Integer taxInclusive = rs.getInt("taxInclusive");

            return new LoanTaxData(id, loanId, taxId, chargeType, taxCode, startDate, taxType, rate, taxInclusive);
        }
    }


	@Override
	public TaxMapData retrieveLoanTaxTemplate() {
		
		 this.context.authenticatedUser();

		 	final Collection<MCodeData> taxTypeData = this.mCodeReadPlatformService.getCodeValue(CodeNameConstants.CODE_TAX_TYPE);
			final Collection<MCodeData> chargeTypeData = this.mCodeReadPlatformService.getCodeValue(CodeNameConstants.CODE_CHARGE_TYPE);
			final TaxMapData taxMapData=new TaxMapData(taxTypeData,chargeTypeData);
			return taxMapData;
		
	}

	@Override
	public Collection<LoanTaxData> retrieveLoanTaxes(Long loanId) {
		
		this.context.authenticatedUser();

        final LoanTaxMapper rm = new LoanTaxMapper();

        final String sql = "select " + rm.schema() + " where ltm.loan_id=? ";

        return this.jdbcTemplate.query(sql, rm, new Object[] { loanId });
	}

	@Override
	public LoanTaxData retrieveLoanTaxDetails(Long id, Long loanId) {
		
		 this.context.authenticatedUser();

        final LoanTaxMapper rm = new LoanTaxMapper();

        final String sql = "select " + rm.schema() + " where ltm.id=? and ltm.loan_id=?";

        return this.jdbcTemplate.queryForObject(sql, rm, new Object[] { id, loanId });
	}


}
