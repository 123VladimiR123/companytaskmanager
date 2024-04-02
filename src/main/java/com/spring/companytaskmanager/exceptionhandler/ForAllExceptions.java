package com.spring.companytaskmanager.exceptionhandler;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ForAllExceptions {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView forAll() {
        return new ModelAndView("ooops");
    }
}
