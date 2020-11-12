package com.example.wssh.web;

import com.example.wssh.po.OpenTerminalVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yangzq80@gmail.com
 * @date 2020-11-04
 */
@Controller
@Slf4j
public class TerminalController {

    @RequestMapping("/wssh")
    public String websshpage(OpenTerminalVM vm, Model model) {

        log.debug("Open websocket ssh terminal {}",vm.toString());


        model.addAttribute("vm",vm);

        return "webssh";
    }
}
