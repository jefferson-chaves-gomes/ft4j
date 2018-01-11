package app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.conf.Routes;

@RestController
public class CommServiceController {

    @RequestMapping(Routes.PING)
    public String greeting(@RequestParam(value = "pid") final String pidNumber) {
        return String.format("PID %s still alive", pidNumber);
    }
}