package org.capstone.job_fair.utils.OpenCSV;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.capstone.job_fair.constants.FileConstant;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ListConverter extends AbstractBeanField {

    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        return Arrays.stream(s.split(FileConstant.CSV_CONSTANT.MULTIPLE_VALUE_DELIMITER))
                .map(Integer::valueOf).collect(Collectors.toList());
    }
}
