package org.capstone.job_fair.utils.OpenCSV;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListConverter extends AbstractBeanField {

    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        List<Integer> myList = Arrays.stream(s.split(";")).map(s1 -> Integer.valueOf(s1)).collect(Collectors.toList());
        return myList;
    }
}
