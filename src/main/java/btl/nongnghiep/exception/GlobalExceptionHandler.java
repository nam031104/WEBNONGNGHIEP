package btl.nongnghiep.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException e, Model model){
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleAllException(Exception e, Model model){
        model.addAttribute("message",e.getMessage());
        return "error";
    }
}
