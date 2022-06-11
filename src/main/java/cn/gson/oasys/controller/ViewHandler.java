package cn.gson.oasys.controller;

import org.springframework.web.servlet.view.InternalResourceView;

import java.io.File;
import java.util.Locale;

public class ViewHandler extends InternalResourceView {
    @Override
    public boolean checkResource(Locale locale) {
        File file = new File(this.getServletContext().getRealPath("/") + getUrl());
        //判断页面是否存在
        return file.exists();
    }
}
