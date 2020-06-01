package tla.web.mvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import tla.error.ObjectNotFoundException;
import tla.web.model.ui.BreadCrumb;

@Controller
@ControllerAdvice
public class ExceptionController extends DefaultHandlerExceptionResolver {

    @Autowired
    private MvcConfig mvcConfig;

    @ExceptionHandler(ObjectNotFoundException.class)
    public ModelAndView handleObjectNotFound(ObjectNotFoundException e, HttpServletRequest request, Model model) {
        model.addAttribute("env", mvcConfig.appVars());
        model.addAttribute("code", 404);
        model.addAttribute("msg", e.getMessage());
        model.addAttribute("url", request.getRequestURI());
        model.addAttribute(
            "breadcrumbs",
            List.of(
                BreadCrumb.of("/", "menu_global_home"),
                BreadCrumb.of("/search", "menu_global_search")
            )
        );
        return new ModelAndView("error/404", model.asMap(), HttpStatus.OK);
    }

}