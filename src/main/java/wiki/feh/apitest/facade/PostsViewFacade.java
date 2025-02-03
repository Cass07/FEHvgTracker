package wiki.feh.apitest.facade;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.controller.dto.PostsGetWithPicDto;
import wiki.feh.apitest.controller.dto.PostsListResponceDto;
import wiki.feh.apitest.controller.dto.PostsResponceDto;
import wiki.feh.apitest.controller.dto.PostsViewDto;
import wiki.feh.apitest.global.exception.view.PostNotExistException;
import wiki.feh.apitest.service.posts.PostsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class PostsViewFacade {
    private final PostsService postsService;

    @Transactional(readOnly = true)
    public PostsGetWithPicDto getPostsWithPicById(long id) {
        return postsService.getByIdWithPic(id).orElseThrow(PostNotExistException::new);
    }

    @Transactional(readOnly = true)
    public PostsResponceDto findById(long id) {
        return new PostsResponceDto(postsService.findById(id).orElseThrow(PostNotExistException::new));
    }

    @Transactional(readOnly = true)
    public PostsViewDto getPostsListView(int page) {
        Page<PostsListResponceDto> listPage = postsService.findAllDescPage(page - 1);

        /*
         총 5가지 - first : 첫번째 페이지로
         prev - 이전 페이지 묶음으로
         page - 현재 묶음
         page active - 현재 묶음에서 현재 페이지
         next - 다음 페이지 묶음
         last - 마지막 페이지로
         페이지네이션에서 한 페이지에 들어갈 데이터 개수는 PostsService에서 PageRequest할때 결정되었고
         페이지 묶음은 이 페이지네이션한 페이지 데이터를 페이지네이션하는거임
         */

        Map<String, Object> viewModel = getModeltoPagenation(page, listPage.getTotalPages(), 10);
        return PostsViewDto.builder()
                .postsList(listPage.getContent())
                .viewModel(viewModel)
                .build();
    }

    private Map<String, Object> getModeltoPagenation(int currentPage, int totalPage, int pageSize) {
        Map<String, Object> map = new HashMap<>();

        // 현재 페이지가 몇번째 페이지묶음에 있는지
        int currentPagePage = (currentPage - 1) / pageSize + 1;

        // 총 페이지묶음
        int totalPagePage = (totalPage - 1) / pageSize + 1;

        // 페이지묶음의 첫번째 페이지가 아니라면, 첫번째 페이지로 가는 링크와 이전 페이지묶음의 마지막 페이지로 가는 링크를 추가
        if (currentPagePage != 1) {
            map.put("pageFirst", "1");
            map.put("pagePrev", String.valueOf((currentPagePage - 2) * pageSize + 1));
        }

        // 페이지묶음의 마지막 페이지가 아니라면, 마지막 페이지로 가는 링크와 다음 페이지묶음의 첫번째 페이지로 가는 링크를 추가
        if (currentPagePage != totalPagePage) {
            map.put("pageLast", String.valueOf(totalPage));
            map.put("pageNext", String.valueOf(currentPagePage * pageSize + 1));
        }

        // 현재 페이지가 페이지묶음의 첫번째 페이지가 아니라면, 현재 페이지묶음 첫 번째 페이지 ~ 현재 페이지 -1 까지의 페이지 번호를 prevPageNum에 추가
        if (currentPage % pageSize != 1) {
            List<String> prevPageNum = new ArrayList<>();
            for (int i = (currentPagePage - 1) * pageSize + 1; i < currentPage; i++) {
                prevPageNum.add(String.valueOf(i));
            }
            map.put("pageCurrentPrevList", prevPageNum);
        }

        // 현재 페이지 번호를 추가
        map.put("pageCurrent", currentPage);

        // 현재 페이지가 페이지묶음의 마지막 페이지가 아니라면, 현제 페이지 + 1 ~ 현재 페이지묶음 마지막 페이지까지의 페이지 번호를 nextPageNum에 추가
        if (currentPage % pageSize != 0) {
            List<String> nextPageNum = new ArrayList<>();
            for (int i = currentPage + 1; i <= (Math.min(currentPagePage * pageSize, totalPage)); i++) {
                nextPageNum.add(String.valueOf(i));
            }
            map.put("pageCurrentNextList", nextPageNum);
        }

        return map;
    }
}
