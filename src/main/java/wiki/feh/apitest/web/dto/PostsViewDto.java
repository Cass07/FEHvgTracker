package wiki.feh.apitest.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class PostsViewDto {

    private List<PostsListResponceDto> postsList;
    private Map<String, Object> viewModel;

    @Builder
    public PostsViewDto(List<PostsListResponceDto> postsList, Map<String, Object> viewModel) {
        this.postsList = postsList;
        this.viewModel = viewModel;
    }

}
