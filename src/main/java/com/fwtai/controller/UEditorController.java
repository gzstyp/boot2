package com.fwtai.controller;

import com.baidu.ueditor.ActionEnter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping(value="/ueditor")
public final class UEditorController {

    @RequestMapping(value="/config")
    public void config(final HttpServletRequest request,final HttpServletResponse response) {
        response.setContentType("application/json");
        final String rootPath = request.getSession().getServletContext().getRealPath("/");
        try {
            final String exec = new ActionEnter(request,rootPath).exec();
            final PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}