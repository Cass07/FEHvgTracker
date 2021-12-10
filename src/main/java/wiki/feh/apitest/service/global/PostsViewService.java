package wiki.feh.apitest.service.global;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.service.posts.PostsService;
import wiki.feh.apitest.web.dto.PostsListResponceDto;
import wiki.feh.apitest.web.dto.PostsViewDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PostsViewService {
    private final PostsService postsService;

    @Transactional(readOnly = true)
    public PostsViewDto getPostsListView(int page)
    {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        Page<PostsListResponceDto> listPage = postsService.findAllDescPage(page-1);

        /* TODO :: pagination용 model 리턴해야함
         총 5가지 - first : 첫번째 페이지로
         prev - 이전 페이지 묶음으로
         page - 현재 묶음
         page active - 현재 묶음에서 현재 페이지
         next - 다음 페이지 묶음
         last - 마지막 페이지로
         페이지네이션에서 한 페이지에 들어갈 데이터 개수는 PostsService에서 PageRequest할때 결정되었고
         페이지 묶음은 이 페이지네이션한 페이지 데이터를 페이지네이션하는거임
         */

        setModeltoPagenation(viewModel, page, listPage.getTotalPages(), 10);
        return PostsViewDto.builder()
                .postsList(listPage.getContent())
                .viewModel(viewModel)
                .build();
    }

    private void setModeltoPagenation(Map<String, Object> map, int currentPage, int totalPage, int pageSize)
    {
        int currentPagePage = (currentPage-1) / pageSize + 1;
        int totalPagePage = (totalPage-1) / pageSize + 1;
        if(currentPagePage != 1)
        {
            map.put("pageFirst", "1");
            map.put("pagePrev", String.valueOf((currentPagePage-2) * pageSize + 1));
        }
        if(currentPagePage != totalPagePage)
        {
            map.put("pageLast", String.valueOf(totalPage));
            map.put("pageNext", String.valueOf(currentPagePage * pageSize + 1));
        }
        if(currentPage % pageSize != 1) {
            List<String> prevPageNum = new ArrayList<>();
            for (int i = (currentPagePage - 1) * pageSize + 1; i < currentPage; i++) {
                prevPageNum.add(String.valueOf(i));
            }
            map.put("pageCurrentPrevList", prevPageNum);
        }
        map.put("pageCurrent", currentPage);
        if(currentPage % pageSize != 0)
        {
            List<String> nextPageNum = new ArrayList<>();
            for(int i = currentPage + 1; i <= (Math.min(currentPagePage * pageSize, totalPage)); i++)
            {
                nextPageNum.add(String.valueOf(i));
            }
            map.put("pageCurrentNextList", nextPageNum);
        }
    }
}
