package com.fwtai;

import com.fwtai.bean.PageFormData;
import com.fwtai.service.core.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * service层的单元测试!需要注释掉面向切面编程,切面处理操作日志com.dwlai.service.module.LogAspect
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2018-01-09 11:38
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Launcher.class)
public class TestUserService {

    @Autowired
    private UserService service;

    @Test
    public void editPwd(){
        final PageFormData pageFormData = new PageFormData();
        try {
            pageFormData.put("account","typ");
            pageFormData.put("pwd","1230");
            pageFormData.put("repwd","1230");
            pageFormData.put("id","e30f8214b33842e2b2396513de4fa7e4");
            System.out.println("测试结果:"+service.editPwd(pageFormData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}