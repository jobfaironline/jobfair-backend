package org.capstone.job_fair.services.impl.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.SneakyThrows;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.capstone.job_fair.services.interfaces.util.XSLSFileService;
import org.capstone.job_fair.utils.AwsUtil;
import org.capstone.job_fair.utils.FileUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class XSLSFileServiceImpl implements XSLSFileService {

    @Autowired
    private AwsUtil awsUtil;

    @Autowired
    private FileStorageService fileStorageService;


    @Override
    public List<List<String>> readXSLSheet(Sheet sheet, int maxRowNum) {
        List<List<String>> data = new ArrayList<>();
        int i = 0;
        for (Row row : sheet) {
            data.add(new ArrayList<>());
            for (int j = 0; j < maxRowNum; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    data.get(i).add(" ");
                    continue;
                }
                switch (cell.getCellType()) {
                    case STRING:
                        data.get(i).add(cell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            data.get(i).add(cell.getDateCellValue() + "");
                        } else {
                            data.get(i).add(cell.getNumericCellValue() + "");
                        }
                        break;
                    case BOOLEAN:
                        data.get(i).add(cell.getBooleanCellValue() + "");
                        break;
                    case FORMULA:
                        data.get(i).add(cell.getCellFormula() + "");
                        break;
                    case BLANK:
                        data.get(i).add(" ");
                        break;
                }
            }
            i++;
        }
        return data;
    }

    @Override
    @SneakyThrows
    public List<List<String>> readCSVFile(InputStream stream) {
        List<List<String>> list = new ArrayList<>();
        try (Reader reader = new InputStreamReader(stream);
             CSVReader csvReader = new CSVReader(reader)) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                list.add(Arrays.asList(line));
            }
        }
        return list;
    }

    @Override
    @SneakyThrows
    public String uploadErrorXSLFile(Workbook workbook, Map<Integer, String> errors, String fileOriginalName, int errorIndex) {
        String url = "";
        try {
            if (workbook.getNumberOfSheets() != 1) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.XSL_NO_SHEET));
            }
            Sheet sheet = workbook.getSheetAt(0);
            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);
            style.setFont(font);

            Row headerRow = sheet.getRow(0);
            Cell cell = headerRow.createCell(errorIndex);
            cell.setCellValue("error");
            cell.setCellStyle(style);

            errors.forEach((key, value) -> {
                Row row = sheet.getRow(key);
                Cell newCell = row.createCell(errorIndex);
                newCell.setCellValue(value);
                newCell.setCellStyle(style);
            });

            String id = UUID.randomUUID().toString();
            String fileName = id + "." + FileUtil.getExtensionByStringHandling(fileOriginalName);
            url = awsUtil.generateAwsS3AccessString(AWSConstant.ERROR_FILE_FOLDER, fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            fileStorageService.store(bos.toByteArray(), AWSConstant.ERROR_FILE_FOLDER + "/" + fileName);
        } finally {
            workbook.close();
        }
        return url;
    }

    @Override
    @SneakyThrows
    public String uploadErrorCSVFile(List<List<String>> data, Map<Integer, String> errors, String fileOriginalName) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             OutputStreamWriter streamWriter = new OutputStreamWriter(bos);
             CSVWriter writer = new CSVWriter(streamWriter)) {
            List<String> header = new ArrayList<String>(data.get(0));
            header.add("error");
            writer.writeNext(header.toArray(new String[0]));
            for (int i = 1; i < data.size(); i++) {
                List<String> line = new ArrayList<String>(data.get(i));
                String errorMessage = errors.get(i);
                if (errorMessage != null) line.add(errorMessage);
                else line.add("");
                writer.writeNext(line.toArray(new String[0]));
            }
            streamWriter.flush();
            String id = UUID.randomUUID().toString();
            String fileName = id + "." + FileUtil.getExtensionByStringHandling(fileOriginalName);
            String url = awsUtil.generateAwsS3AccessString(AWSConstant.ERROR_FILE_FOLDER, fileName);
            fileStorageService.store(bos.toByteArray(), AWSConstant.ERROR_FILE_FOLDER + "/" + fileName);
            return url;
        }
    }

    @Override
    @SneakyThrows
    public String uploadXSLFile(Workbook workbook, String folderName) {
        String url = "";
        try {
            if (workbook.getNumberOfSheets() != 1) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.XSL_NO_SHEET));
            }
            String id = UUID.randomUUID().toString();
            String fileName = "application_report_" + id + ".xlsx";
            url = awsUtil.generateAwsS3AccessString(folderName, fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            fileStorageService.store(bos.toByteArray(), folderName + "/" + fileName);
        } finally {
            workbook.close();
        }
        return url;
    }
}
