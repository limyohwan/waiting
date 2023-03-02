package com.yohwan.waiting.web.controller.common;

import com.yohwan.waiting.domain.visitor.Visitor;
import com.yohwan.waiting.service.VisitorService;
import com.yohwan.waiting.web.exception.custom.CommonException;
import com.yohwan.waiting.web.excel.VisitorExcelExporter;
import com.yohwan.waiting.web.controller.visitor.dto.VisitorSearch;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ExcelController {
    private final VisitorService visitorService;

    @GetMapping("/api/excel/visitors")
    public void downloadExcel(@Valid VisitorSearch visitorSearch, HttpServletResponse response) throws Exception {
        checkDateTime(visitorSearch);
        List<Visitor> result = visitorService.searchForExcel(visitorSearch);
        VisitorExcelExporter excelExporter = new VisitorExcelExporter(result);
        excelExporter.export(response);
    }

    private void checkDateTime(VisitorSearch visitorSearch){
        Duration duration = Duration.between(visitorSearch.getStartDateTime(), visitorSearch.getEndDateTime());
        if(duration.toDays() > 365) {
            throw new CommonException("날짜는 1년 이상 차이날 수 없습니다.");
        }
    }
}
