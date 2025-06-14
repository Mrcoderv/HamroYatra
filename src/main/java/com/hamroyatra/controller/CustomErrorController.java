package com.hamroyatra.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = Logger.getLogger(CustomErrorController.class.getName());

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "An unexpected error occurred. Please try again later.";

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMessage = "The page you are looking for does not exist.";
                logger.warning("404 error occurred: " + request.getRequestURI());
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMessage = "Internal server error. Please try again later.";
                logger.severe("500 error occurred: " + request.getRequestURI());
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMessage = "You don't have permission to access this resource.";
                logger.warning("403 error occurred: " + request.getRequestURI());
            }
        }

        model.addAttribute("errorMessage", errorMessage);
        return "error/general";
    }
}
