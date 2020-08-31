package com.fwtai;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Controller添加测试单元
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2018-01-22 15:19
 * @注意 测试的方法不能有参数
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Launcher.class)
public class TestUserController {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;
    private MockHttpSession session;

    @Before
    public void setupMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
        session = new MockHttpSession();
        session.setAttribute("login_key","e30f8214b33842e2b2396513de4fa7e4"); //拦截器那边会判断用户是否登录，所以这里注入一个用户
        session.setAttribute("login_user","typ");
    }

    @Test
    public final void editPwd() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/user/editPwd")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .session(session)
                .param("account","typ")
                .param("pwd","321")
                .param("repwd","321")
                .param("id","e30f8214b33842e2b2396513de4fa7e4")
                ).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
    }
}