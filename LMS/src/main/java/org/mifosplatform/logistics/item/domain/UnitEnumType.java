package org.mifosplatform.logistics.item.domain;
public enum UnitEnumType {

		GALLON(1, "CategoryType.gallon"), //
		QUART(2, "CategoryType.quart"),
        EACH(3, "CategoryType.each"),
        SET(4, "CategoryType.set"),
        TUBE(5, "CategoryType.tube"),
		INVALID(4, "CategoryType.invalid");


	    private final Integer value;
		private final String code;

	    private UnitEnumType(final Integer value, final String code) {
	        this.value = value;
			this.code = code;
	    }

	    public Integer getValue() {
	        return this.value;
	    }

		public String getCode() {
			return code;
		}

		public static UnitEnumType fromInt(final Integer frequency) {

			UnitEnumType enumType = UnitEnumType.INVALID;
			switch (frequency) {
			case 1:
				enumType = UnitEnumType.GALLON;
				break;
			case 2:
				enumType = UnitEnumType.QUART;
				break;

			case 3:
				enumType = UnitEnumType.EACH;
				break;
				
			case 4:
				enumType = UnitEnumType.SET;
				break;
				
			case 5:
				enumType = UnitEnumType.TUBE;
				break;
				
			default:
				enumType = UnitEnumType.INVALID;
				break;
			}
			return enumType;
		}
	}



