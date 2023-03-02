package com.yohwan.waiting.repository.visitor;

import com.yohwan.waiting.domain.visitor.Visitor;
import com.yohwan.waiting.web.controller.visitor.dto.VisitorResponseDto;
import com.yohwan.waiting.web.controller.visitor.dto.VisitorSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VisitorRepositoryCustom {
    Page<VisitorResponseDto> search(VisitorSearch visitorSearch, Pageable pageable);
    List<Visitor> searchForExcel(VisitorSearch visitorSearch);

}
