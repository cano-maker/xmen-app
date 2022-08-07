package org.project.enums;

public enum NumbersEnum
{
    MINIMUM_SIZE(4),
    MINIMUM_CANT_MUTANT(2);

    private Integer value;

    NumbersEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue()
    {
        return this.value;
    }
}
