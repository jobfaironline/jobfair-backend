package org.capstone.job_fair.services.interfaces.util;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface XSLSFileService {
    List<List<String>> readXSLSheet(Sheet sheet, int maxRowNum);

    List<List<String>> readCSVFile(InputStream stream);

    String uploadErrorXSLFile(Workbook workbook, Map<Integer, String> errors, String fileOriginalName, int errorIndex);

    String uploadErrorCSVFile(List<List<String>> data, Map<Integer, String> errors, String fileOriginalName);

    String uploadXSLFile(Workbook workbook, String folderName);
}
