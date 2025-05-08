package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import service.TeamsNotifyService;

@RestController
public class NotifyController {

    @Autowired
    private TeamsNotifyService service;

    @GetMapping("/test-teams")
    public String sendNotification() {
        service.sendMessage("✅ 測試 Teams 通知");
        return "發送完成";
    }

}
