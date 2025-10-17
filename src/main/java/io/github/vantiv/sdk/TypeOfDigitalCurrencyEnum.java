package io.github.vantiv.sdk;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
// This class is deprecated we are going to delete this class in upcoming version.
@XmlType(name = "typeOfDigitalCurrencyEnum")
@XmlEnum
public enum TypeOfDigitalCurrencyEnum {

    @XmlEnumValue("1")
    ONE("1"),
    @XmlEnumValue("2")
    TWO("2"),
    @XmlEnumValue("3")
    THREE("3"),
    @XmlEnumValue("4")
    FOUR("4"),
    @XmlEnumValue("7")
    SEVEN("7");

    private final String value;

    TypeOfDigitalCurrencyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;

}


    public static TypeOfDigitalCurrencyEnum fromValue(String value) {
        for (TypeOfDigitalCurrencyEnum enumValue : TypeOfDigitalCurrencyEnum.values()) {
            if (enumValue.value.equals(value)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}