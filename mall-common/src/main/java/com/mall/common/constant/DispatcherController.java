package com.mall.common.constant;

import cn.hutool.core.util.StrUtil;
import com.mall.common.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2023/3/26 19:54
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class DispatcherController extends BaseController {

    /**
     * 请求内部转发机制
     * apiUrl: 转发地址 function=/api/xxx/xxx&accessSecretData=AES加密结果
     *
     * @param request  入参
     * @param response
     */
    @PostMapping(value = {"/forward", "/dispatcher"})
    public void dispatcher(HttpServletRequest request, HttpServletResponse response) {
        try {
            String apiUrl = request.getParameter("accessSecretData");
            StringBuilder param = new StringBuilder();
            String function = "";
            if (StrUtil.isNotBlank(apiUrl)) {
                apiUrl = URLDecoder.decode(apiUrl, "utf-8");
                apiUrl = SecurityUtil.DecryptAllPara(apiUrl);
                String[] arrUrl = apiUrl.split("&");
                Map<String, String> urlParam = new HashMap<>(16);
                for (String str : arrUrl) {
                    urlParam.put(str.substring(0, str.indexOf("=")), str.substring(str.indexOf("=") + 1));
                }
                for (Map.Entry<String, String> entry : urlParam.entrySet()) {
                    if ("function".equals(entry.getKey())) {
                        function = URLDecoder.decode(entry.getValue(), "utf-8") + "?";
                    } else {
                        //处理特殊参数带有http的 url进行编码
                        if (entry.getValue().contains("+") || (!entry.getKey().contains("avatar") && entry.getValue().contains("http"))) {
                            param.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8")).append("&");
                        } else {
                            param.append(entry.getKey()).append("=").append(URLDecoder.decode(entry.getValue(), "utf-8")).append("&");
                        }
                    }
                }
            } else {
                function = request.getParameter("function") + "?";
            }
            String url = function + param;
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            log.error("请求参数解析异常");
        }
    }
}
