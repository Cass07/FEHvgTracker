package wiki.feh.apitest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wiki.feh.apitest.facade.VgDataFacade;
import wiki.feh.apitest.global.config.auth.LoginUser;
import wiki.feh.apitest.global.config.auth.dto.SessionUser;
import wiki.feh.apitest.facade.PostsViewFacade;
import wiki.feh.apitest.global.exception.PostNotExistException;
import wiki.feh.apitest.global.exception.PostNotOwnedException;
import wiki.feh.apitest.service.posts.PostsService;
import wiki.feh.apitest.controller.dto.*;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    private final VgDataFacade vgDataFacade;
    private final PostsViewFacade postsViewFacade;

    @GetMapping("/admin/board/")
    public String index(Model model, @LoginUser SessionUser user) {
        PostsViewDto postsViewDto = postsViewFacade.getPostsListView(1);

        model.addAttribute("header_title", "로그 게시판");

        model.addAttribute("posts", postsViewDto.getPostsList());
        postsViewDto.getViewModel().forEach(model::addAttribute);

        if (user != null) {
            model.addAttribute("userName", user);
        }

        return "index";
    }

    @GetMapping("/admin/board/page/{page}")
    public String indexPage(Model model, @LoginUser SessionUser user, @PathVariable int page)
    {
        PostsViewDto postsViewDto = postsViewFacade.getPostsListView(page);

        model.addAttribute("header_title", "로그 게시판");

        model.addAttribute("posts", postsViewDto.getPostsList());
        postsViewDto.getViewModel().forEach(model::addAttribute);

        if (user != null) {
            model.addAttribute("userName", user);
        }

        return "index";
    }


    @GetMapping("/admin/board/posts/{id}")
    public String posts(@PathVariable long id, Model model) {
        model.addAttribute("header_title", "글 내용 보기");

        PostsGetWithPicDto postsGetWithPicDto = postsService.getByIdWithPic(id);

        if(postsGetWithPicDto == null) {
            throw new PostNotExistException();
        }

        model.addAttribute("posts", postsGetWithPicDto);
        return "posts";
    }

    @GetMapping("/admin/board/posts/save")
    public String postsSave(Model model, @LoginUser SessionUser user) {
        model.addAttribute("header_title", "글 등록");
        model.addAttribute("user", user);

        return "posts-save";
    }

    @GetMapping("/admin/board/posts/update/{id}")
    public String postsUpdate(@PathVariable long id, Model model, @LoginUser SessionUser user) {
        model.addAttribute("header_title", "글 수정하기");

        PostsResponceDto dto = postsService.findById(id);
        model.addAttribute("posts", dto);

        if (!dto.getAuthor().equals(user.getEmail())) {
            throw new PostNotOwnedException();
        }

        return "posts-update";
    }


    @GetMapping("/admin/vginfo")
    public String SetVginfo(Model model) {
        model.addAttribute("header_title", "투표대전 데이터 세팅");

        return "setting-vginfo";
    }

    @GetMapping("/admin/manualcron")
    public String vgCronManual() {
        vgDataFacade.updateVgData();
        return "redirect:/admin/board/";
    }

}
