package io.github.vantiv.sdk;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "tlidValidationActionIndicatorType")
@XmlEnum(Integer.class)
public enum TlidValidationActionIndicatorType {

    @XmlEnumValue("1")
    ONE(1),
    @XmlEnumValue("2")
    TWO(2);

    private final int value;

    TlidValidationActionIndicatorType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TlidValidationActionIndicatorType fromValue(int value) {
        for (TlidValidationActionIndicatorType enumValue : TlidValidationActionIndicatorType.values()) {
            if (enumValue.value == value) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}