package com.yohwan.waiting.web.excel;

import com.yohwan.waiting.domain.visitor.Visitor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class VisitorExcelExporter {
    private Workbook workbook;
    private Sheet sheet;
    private List<Visitor> visitors;

    public VisitorExcelExporter(List<Visitor> visitors) {
        this.visitors = visitors;
        workbook = new SXSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Visitors");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();

        List<String> headers = Arrays.asList("No", "고객명","생성일","핸드폰번호","인원",
                "유형","연령","성별","알바담당","영업담당","상태",
                "최종변경일","개인정보수집동의","개인정보수집동의일",
                "개인정보수집종료일","마케팅동의","마케팅동의일",
                "마케팅종료일");

        for(int i = 0; i < headers.size(); i++){
            createCell(row, i, headers.get(i), style);
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();

        for (Visitor v : visitors) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, rowCount-1, style);
            createCell(row, columnCount++, v.getName(), style);
            createCell(row, columnCount++, v.getCreatedDate().toString(), style);
            createCell(row, columnCount++, v.getPhoneNumber(), style);
            createCell(row, columnCount++, v.getPeopleNumber(), style);
            createCell(row, columnCount++, v.getVisitorType().getTitle(), style);
            createCell(row, columnCount++, v.getAge(), style);
            createCell(row, columnCount++, v.getGender(), style);
            createCell(row, columnCount++, v.getPartMember() != null ? v.getPartMember().getName() : "", style);
            createCell(row, columnCount++, v.getSalesMember() != null ? v.getSalesMember().getName() : "", style);
            createCell(row, columnCount++, v.getVisitorStatus().getTitle(), style);
            createCell(row, columnCount++, v.getModifiedDate().toString(), style);
            createCell(row, columnCount++, v.isEnabledPersonalInfo(), style);
            createCell(row, columnCount++, v.getCreatedDate().toString(), style);
            createCell(row, columnCount++, v.getCreatedDate().plusDays(30).toString(), style);
            createCell(row, columnCount++, v.isEnabledMarketingInfo() ? Boolean.valueOf(v.isEnabledMarketingInfo()) : "", style);
            createCell(row, columnCount++, v.isEnabledMarketingInfo() ? v.getCreatedDate().toString() : "", style);
            createCell(row, columnCount++, v.isEnabledMarketingInfo() ? v.getCreatedDate().plusYears(1).toString() : "", style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        createResponseForm(response);
        createExcelForm();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void createResponseForm(HttpServletResponse response) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=visitors_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
    }

    private void createExcelForm() {
        writeHeaderLine();
        writeDataLines();
    }
}
