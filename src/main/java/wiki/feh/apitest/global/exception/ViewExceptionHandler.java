package wiki.feh.apitest.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ViewExceptionHandler {
    @ExceptionHandler(ViewException.class)
    public String handleViewException(ViewException e, Model model) {
        log.error(e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "posts-error";
    }
}
