package com.umler.warehouses.Converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;


@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate locDate) {
        if (locDate != null) {
            return Date.valueOf(locDate);
        } else {
            return null;
        }
    }

    @Override
    public LocalDate convertToEntityAttribute(Date sqlDate) {
       if (sqlDate != null) {
           return sqlDate.toLocalDate();
       } else {
           return null;
       }
    }
}
