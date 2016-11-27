package cn.gov.cbrc.bankriskcontrol.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test3/*")
// ��request����url
public class TestController3 {

    @RequestMapping("login.do")
    // ��request����url��ƴ�Ӻ�ȼ���/test3/login.do
    public String testLogin(String username, String password, int age) {
        if (!"admin".equals(username) || !"admin".equals(password) || age < 5) {
            return "loginError";
        }
        return "loginSuccess";
    }
}
