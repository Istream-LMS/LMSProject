package org.mifosplatform.portfolio.loanaccount.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.mifosplatform.finance.depositandrefund.service.DepositEnumerations;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.RoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.portfolio.loanaccount.data.LoanFeeMasterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class LoanFeeMasterDataReadPlatformServiceImpl implements LoanFeeMasterDataReadPlatformService {


	
	private final JdbcTemplate jdbcTemplate;
    private final PlatformSecurityContext context;
    
    @Autowired
    public LoanFeeMasterDataReadPlatformServiceImpl(final PlatformSecurityContext context, final RoutingDataSource dataSource) {
        this.context = context;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    private static final class LoanFeeMasterDataMapper implements RowMapper<LoanFeeMasterData> {

        public String schema() {
            return " lfm.id as id, lfm.loan_id as loanId, fm.id as feeMasterId,lfm.fee_code as feeCode ,lfm.fee_description as feeDescription, " +
            		" lfm.transaction_type as transactionType,lfm.deposit_time_enum as depositTime, " +
					" lfm.deposit_calculation_enum as depositCalculation,lfm.deposit_on_enum as depositOn," +
					" lfm.calculation_percentage as percentageOf, lfm.calculation_on_amount as amountPercentageAppliedTo, "+
					" lfm.deposit_amount_or_percentage as amountOrPercentage, fm.amount as amount, " +
					" lfm.is_refundable as isRefundable from m_fee_master fm "+
                    " join m_loan_fee_master lfm on lfm.fee_master_id = fm.id ";
        }

        @Override
        public LoanFeeMasterData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

            final Long id = rs.getLong("id");
            final Long loanId = rs.getLong("loanId");
            final Long feeMasterId = rs.getLong("feeMasterId");
			final String feeCode = rs.getString("feeCode");
			final String feeDescription = rs.getString("feeDescription");
			final String transactionType = rs.getString("transactionType");
			final int depositTime = rs.getInt("depositTime");
            final EnumOptionData depositTimeType = DepositEnumerations.depositTimeType(depositTime);

            final int depositCalculation = rs.getInt("depositCalculation");
            final EnumOptionData depositCalculationType = DepositEnumerations.depositCalculationType(depositCalculation);
            final int depositOn = rs.getInt("depositOn");
            final EnumOptionData depositOnType = DepositEnumerations.depositTimeType(depositOn);
            final BigDecimal percentageOf = JdbcSupport.getBigDecimalDefaultToZeroIfNull(rs, "percentageOf");
            final BigDecimal amountPercentageAppliedTo = JdbcSupport.getBigDecimalDefaultToZeroIfNull(rs, "amountPercentageAppliedTo");
            final BigDecimal amountOrPercentage = rs.getBigDecimal("amountOrPercentage");
			final BigDecimal amount = rs.getBigDecimal("amount");
			final String isRefundable = rs.getString("isRefundable");
			return new LoanFeeMasterData(id,loanId,feeMasterId,feeCode,feeDescription,transactionType,depositTimeType,
					depositCalculationType,depositOnType,percentageOf,amountPercentageAppliedTo,amountOrPercentage,amount,isRefundable);
        }
    }


	@Override
	public Collection<LoanFeeMasterData> retrieveLoanFeeMasterData(Long loanId) {
		
		this.context.authenticatedUser();

        final LoanFeeMasterDataMapper rm = new LoanFeeMasterDataMapper();

        final String sql = "select " + rm.schema() + " where lfm.loan_id=? and lfm.is_deleted = 'N' ";

        return this.jdbcTemplate.query(sql, rm, new Object[] { loanId });
	}


}
