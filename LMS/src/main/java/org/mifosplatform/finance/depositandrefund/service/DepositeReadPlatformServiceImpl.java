package org.mifosplatform.finance.depositandrefund.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mifosplatform.infrastructure.core.service.RoutingDataSource;
import org.mifosplatform.organisation.feemaster.data.FeeMasterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 * @author hugo
 * 
 */
@Service
public class DepositeReadPlatformServiceImpl implements DepositeReadPlatformService {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public DepositeReadPlatformServiceImpl(final RoutingDataSource dataSource) {
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public FeeMasterData retrieveDepositDetails(Long feeId, Long clientId) {

		try{
		final String transaction="Deposit";
		
		final DepositeMapper mapper = new DepositeMapper();

		final String sql = "Select " + mapper.schema();

		return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] {transaction,feeId});
		}catch(EmptyResultDataAccessException accessException){
			return null;
		}
		
	}

	private static final class DepositeMapper implements RowMapper<FeeMasterData> {

		public String schema() {
			return "fm.id AS id,fm.fee_code AS feeCode,fm.is_refundable as isRefundable,fm.charge_code AS chargeCode,"+
				      " fm.default_fee_amount AS amount FROM b_fee_master fm "+
				       " WHERE fm.transaction_type =? AND fm.is_deleted = 'N' AND fm.id = ? GROUP BY fm.id";
			}

		@Override
		public FeeMasterData mapRow(final ResultSet rs, final int rowNum)
				throws SQLException {

			final Long id = rs.getLong("id");
			final String feeCode = rs.getString("feeCode");
			final String chargeCode = rs.getString("chargeCode");
			final BigDecimal defaultFeeAmount = rs.getBigDecimal("amount");
			final String isRefundable = rs.getString("isRefundable");
			return new FeeMasterData(id, feeCode, " ", "Deposit", null,null, defaultFeeAmount, isRefundable);

		}
	}

}
