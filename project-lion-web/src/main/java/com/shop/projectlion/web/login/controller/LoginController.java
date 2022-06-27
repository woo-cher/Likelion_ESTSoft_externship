package com.shop.projectlion.web.login.controller;

import com.shop.projectlion.global.error.exception.BusinessException;
import com.shop.projectlion.web.login.dto.MemberRegisterDto;
import com.shop.projectlion.web.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService service;

    /**
     * Because '405 : Request method not supported' <br>
     * see <a href="https://www.baeldung.com/spring-request-method-not-supported-405"></a>
     */
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(Model model) {
        return "login/loginform";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("memberRegisterDto", new MemberRegisterDto());
        return "login/registerform";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Valid MemberRegisterDto dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "login/registerform";
        }

        try {
            service.save(dto);
        } catch (BusinessException e) {
            log.error(e.getMessage());
            model.addAttribute("errorCode", e.getErrorCode());
            return "login/registerform";
        }

        return "redirect:/login";
    }
}
