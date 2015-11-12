package org.mifosplatform.organisation.feemaster.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.service.RoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.feemaster.data.FeeMasterData;
import org.mifosplatform.organisation.taxmapping.data.TaxMapData;
import org.mifosplatform.organisation.taxmapping.service.TaxMapReadPlatformServiceImp.TaxMapDataMapper;
import org.mifosplatform.portfolio.charge.service.ChargeEnumerations;
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
	public FeeMasterData retrieveSingleFeeMasterDetails(Long id) {
		try {
			context.authenticatedUser();
			FeeMasterDataMapper mapper = new FeeMasterDataMapper();
			String sql;
				sql = "select " + mapper.schema()+" where fm.id=? and  fm.is_deleted='N'"; 
		
			return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] { id });
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	private static final class FeeMasterDataMapper implements RowMapper<FeeMasterData> {

			public String schema() {
				return " fm.id as id,fm.fee_code as feeCode,fm.fee_description as feeDescription,fm.transaction_type as transactionType,fm.charge_time_enum as chargeTime," +
						" fm.charge_calculation_enum as chargeCalculation,fm.default_fee_amount as defaultFeeAmount, fm.is_refundable as isRefundable from m_fee_master fm";
				
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
				final int chargeTime = rs.getInt("chargeTime");
	            final EnumOptionData chargeTimeType = ChargeEnumerations.chargeTimeType(chargeTime);

	            final int chargeCalculation = rs.getInt("chargeCalculation");
	            final EnumOptionData chargeCalculationType = ChargeEnumerations.chargeCalculationType(chargeCalculation);
				final BigDecimal defaultFeeAmount = rs.getBigDecimal("defaultFeeAmount");
				final String isRefundable = rs.getString("isRefundable");
				return new FeeMasterData(id,feeCode,feeDescription,transactionType,chargeTimeType,chargeCalculationType,defaultFeeAmount,isRefundable);
			
			
			}
	}
	
	@Override
	public Collection<FeeMasterData> retrieveAllData(final String transactionType) {
		
		try{
			final FeeMasterDataMapper mapper = new FeeMasterDataMapper();			
			 String sql = "select " + mapper.schema() +"  where is_deleted = 'N'";
			if(transactionType != null){
				sql = "select " + mapper.schema() +"  where is_deleted = 'N' and fm.transaction_type ="+transactionType;
			}
	    	return this.jdbcTemplate.query(sql, mapper, new Object[] {});
		
		}catch (final EmptyResultDataAccessException e) {
		    return null;
		}
	}
	
	@Override
	public Collection<FeeMasterData> retrieveLoanProductFeeMasterData(Long loanProductId,String transactionType) {
		
		this.context.authenticatedUser();
        final FeeMasterDataMapper tm = new FeeMasterDataMapper();

        final String sql = "select " + tm.loanProductFeeMasterSchema() + " where plfm.product_loan_id=? and fm.transaction_type = ?";

        return this.jdbcTemplate.query(sql, tm, new Object[] { loanProductId,transactionType });
	}

}
