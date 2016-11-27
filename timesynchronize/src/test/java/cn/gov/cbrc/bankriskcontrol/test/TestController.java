package cn.gov.cbrc.bankriskcontrol.test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * http://blog.csdn.net/wangpeng047/article/details/6983027
 * 
 * @author pl
 * 
 */
@Controller
public class TestController {

    @RequestMapping("/test/login.do")
    // ����url��ַӳ�䣬����Struts��action-mapping
    public ModelAndView  testLogin(@RequestParam(value = "username") String username, String password,
            HttpServletRequest request) {
        // @RequestParam��ָ����url��ַӳ���б��뺬�еĲ���(�������required=false)
        // @RequestParam�ɼ�дΪ��@RequestParam("username")

        if (!"admin".equals(username) || !"admin".equals(password)) {
            return new ModelAndView("loginError"); // ��תҳ��·����Ĭ��Ϊת���������Զ���������ļ������õ�ǰ׺�ͺ�׺
        }
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("username", username);
        map.put("password", password);
        return new ModelAndView("success",map);
    }

    @RequestMapping("/test/login2.do")
    public ModelAndView testLogin2(String username, String password, int age,HttpServletRequest request,HttpServletResponse response) {
        // request��response���ط�Ҫ�����ڷ����У�����ò��ϵĻ�����ȥ��
        // ������������ҳ��ؼ���name��ƥ�䣬�������ͻ��Զ���ת��

        if (!"admin".equals(username) || !"admin".equals(password) || age < 5) {
            return new ModelAndView("loginError"); // �ֶ�ʵ��ModelAndView�����תҳ�棨ת������Ч���ͬ������ķ��������ַ�
        }
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id", 1);
        map.put("type", "000001");//ʹ��../index.jsp����request.getContextPath()+"/index.jsp������/../index.jsp���
        System.out.println(request.getContextPath());
        return new ModelAndView(new RedirectView("../index.jsp"),map); // �����ض���ʽ��תҳ��
        //return new ModelAndView(new RedirectView(request.getContextPath()+"/index.jsp"),map);
        // �ض�����һ�ּ�д��
        // return new ModelAndView("redirect:../index.jsp");
    }

    @RequestMapping("/test/login3.do")
    public ModelAndView testLogin3(User user) {
        // ͬ��֧�ֲ���Ϊ�?����ֻ��Ҫ����Ҫ�ļ������Լ���
        String username = user.getUsername();
        String password = user.getPassword();
        int age = user.getAge();

        if (!"admin".equals(username) || !"admin".equals(password) || age < 5) {
            return new ModelAndView("loginError");
        }
        return new ModelAndView("loginSuccess");
    }
}

class User {
    private String username;
    private String password;
    private int age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
