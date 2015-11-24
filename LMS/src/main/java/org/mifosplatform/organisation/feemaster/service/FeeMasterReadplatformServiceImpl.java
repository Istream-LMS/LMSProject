package org.mifosplatform.organisation.feemaster.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.mifosplatform.finance.depositandrefund.service.DepositEnumerations;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.service.RoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.feemaster.data.FeeMasterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class FeeMasterReadplatformServiceImpl implements FeeMasterReadplatformService {
	
	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;
	
	@Autowired
	public FeeMasterReadplatformServiceImpl(final PlatformSecurityContext context,final RoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	




	@Override
	public FeeMasterData retrieveSingleFeeMasterDetails(String feeCode) {
		try {
			context.authenticatedUser();
			FeeMasterDataMapper mapper = new FeeMasterDataMapper();
			String sql = "select " + mapper.schema()+" where fm.fee_code=? and  fm.is_deleted='N'"; 
		
			return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] { feeCode });
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public FeeMasterData retrieveSingleFeeMasterDetails(Long id) {
		try {
			context.authenticatedUser();
			FeeMasterDataMapper mapper = new FeeMasterDataMapper();
			String sql = "select " + mapper.schema()+" where fm.id=? and  fm.is_deleted='N'"; 
		
			return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] { id });
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	private static final class FeeMasterDataMapper implements RowMapper<FeeMasterData> {

			public String schema() {
				return " fm.id as id,fm.fee_code as feeCode,fm.fee_description as feeDescription,fm.transaction_type as transactionType,fm.deposit_time_enum as depositTime," +
						" fm.deposit_calculation_enum as depositCalculation,fm.deposit_on_enum as depositOn,fm.amount as amount, fm.is_refundable as isRefundable from m_fee_master fm";
				
			}
			
			public String loanProductFeeMasterSchema() {
	            return schema() + " join m_product_loan_feemaster plfm on plfm.feemaster_id = fm.id";
	       }
			
			@Override
			public FeeMasterData mapRow(ResultSet rs, int rowNum)
					throws SQLException {
			
				final Long id = rs.getLong("id");
				final String feeCode = rs.getString("feeCode");
				final String feeDescription = rs.getString("feeDescription");
				final String transactionType = rs.getString("transactionType");
				final int depositTime = rs.getInt("depositTime");
	            final EnumOptionData depositTimeType = DepositEnumerations.depositTimeType(depositTime);

	            final int depositCalculation = rs.getInt("depositCalculation");
	            final EnumOptionData depositCalculationType = DepositEnumerations.depositCalculationType(depositCalculation);
	            final int depositOn = rs.getInt("depositOn");
	            final EnumOptionData depositOnType = DepositEnumerations.depositTimeType(depositOn);
				final BigDecimal amount = rs.getBigDecimal("amount");
				final String isRefundable = rs.getString("isRefundable");
				return new FeeMasterData(id,feeCode,feeDescription,transactionType,depositTimeType,depositCalculationType,depositOnType,amount,isRefundable);
			
			
			}
	}
	
	@Override
	public Collection<FeeMasterData> retrieveAllData(final String transactionType) {
		
		try{
			final FeeMasterDataMapper mapper = new FeeMasterDataMapper();			
			 String sql = "select " + mapper.schema() +"  where is_deleted = 'N'";
			if(transactionType != null){
				sql = sql + " and fm.transaction_type ='"+transactionType+"'";
			}
	    	return this.jdbcTemplate.query(sql, mapper, new Object[] {});
		
		}catch (final EmptyResultDataAccessException e) {
		    return null;
		}
	}
	
	@Override
	public Collection<FeeMasterData> retrieveLoanProductFeeMasterData(Long loanProductId) {
		
		try {		
			this.context.authenticatedUser();
	        final FeeMasterDataMapper tm = new FeeMasterDataMapper();
	        
	        final String transactionType = "Deposit";

	        final String sql = "select " + tm.loanProductFeeMasterSchema() + " where plfm.product_loan_id=? and fm.is_deleted = 'N' and fm.transaction_type = '"+transactionType+"'";

	        return this.jdbcTemplate.query(sql, tm, new Object[] { loanProductId });
	        
		} catch (final EmptyResultDataAccessException e) {
			return null;
		}
		
	}

}
