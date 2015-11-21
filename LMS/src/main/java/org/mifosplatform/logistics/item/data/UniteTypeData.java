package org.mifosplatform.logistics.item.data;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.logistics.item.domain.UnitEnumType;

public class UniteTypeData {

	public static EnumOptionData UnitClassType(final int id) {
		return UnitClassType(UnitEnumType.fromInt(id));
	}

	public static EnumOptionData UnitClassType(final UnitEnumType type) {
		final String codePrefix = "deposit.interest.compounding.period.";
		EnumOptionData optionData = null;
		switch (type) {
		case GALLON:
			optionData = new EnumOptionData(UnitEnumType.GALLON.getValue().longValue(), codePrefix + UnitEnumType.GALLON.getCode(), "GALLON");
			break;
			
		case QUART:
			optionData = new EnumOptionData(UnitEnumType.QUART.getValue().longValue(), codePrefix + UnitEnumType.QUART.getCode(), "QUART");
			break;

		case EACH:
			optionData = new EnumOptionData(UnitEnumType.EACH.getValue().longValue(), codePrefix + UnitEnumType.EACH.getCode(), "EACH");
			break;
		
		case SET:
			optionData = new EnumOptionData(UnitEnumType.SET.getValue().longValue(), codePrefix + UnitEnumType.SET.getCode(), "SET");
			break;
			
		case TUBE:
			optionData = new EnumOptionData(UnitEnumType.TUBE.getValue().longValue(), codePrefix + UnitEnumType.TUBE.getCode(), "TUBE");
			break;
				
		default:
			optionData = new EnumOptionData(UnitEnumType.INVALID.getValue().longValue(), UnitEnumType.INVALID.getCode(), "INVALID");
			break;
		}
		return optionData;
	}

}
