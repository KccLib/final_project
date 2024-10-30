package com.kcc.trioffice.domain.common.controller;


import com.kcc.trioffice.domain.common.domain.ResponseSearch;
import com.kcc.trioffice.domain.common.domain.SearchChatRoom;
import com.kcc.trioffice.domain.common.service.SearchService;
import com.kcc.trioffice.domain.employee.dto.response.SearchEmployee;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchRestController {

    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<ResponseSearch> search(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<SearchEmployee> searchEmployee =  searchService.getEmployeeList(principalDetail.getEmployeeId());
        List<SearchChatRoom> searchChatRoom = searchService.getSearchChatRoom(principalDetail.getEmployeeId());

        ResponseSearch responseSearch = ResponseSearch.builder()
                .searchEmployeeList(searchEmployee)
                .searchChatRoomList(searchChatRoom)
                .build();

        return ResponseEntity.ok(responseSearch);
    }

    @GetMapping("/search/change")
    public ResponseEntity<ResponseSearch> searchChange(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestParam String keyword) {
        List<SearchEmployee> searchEmployee =  searchService.getChangeEmployeeList(principalDetail.getEmployeeId(), keyword);
        List<SearchChatRoom> searchChatRoom = searchService.getChangeSearchChatRoom(principalDetail.getEmployeeId(), keyword);

        ResponseSearch responseSearch = ResponseSearch.builder()
                .searchEmployeeList(searchEmployee)
                .searchChatRoomList(searchChatRoom)
                .build();

        return ResponseEntity.ok(responseSearch);
    }
}
