package com.shanxin.core.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import com.shanxin.core.context.MyContext;
import com.shanxin.core.exception.CoreException;
import com.shanxin.core.util.MyStringUtils;

@RestController
public abstract class ApiRest {

	protected Response<?> api(HttpServletRequest request) throws ApiException {
		Response<ApiResponse> rs = new Response<ApiResponse>();

		try {
			ApiBo<?> bo = null;
			String sign = null;
			String contentType = null;
			String method = null;

			sign = MyStringUtils.isEmpty(request.getHeader("Sign")) ? "" : request.getHeader("Sign");
			contentType = MyStringUtils.isEmpty(request.getHeader("Content-Type")) ? (MyStringUtils.isEmpty(request.getHeader("Content-type")) ? "" : request.getHeader("Content-type")) : request.getHeader("Content-Type");
			contentType = contentType.indexOf(";") == -1 ? contentType : contentType.substring(0, contentType.indexOf(";"));

			// String qStr = request.getQueryString();
			String line = null;
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), Charset.forName("utf-8")));
			while ((line = br.readLine()) != null)
				sb.append(line);

			String messageBody = sb.toString();
			if (MyStringUtils.isEmpty(messageBody))
				throw new ApiException("messageBody is empty.");

			if (contentType.equalsIgnoreCase(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
				String prefix = "";
				Pattern pattern = Pattern.compile("([^&]*)method=([^=&]+)");
				Matcher matcher = pattern.matcher(messageBody);
				while (matcher.find()) {
					prefix = matcher.group(1);
					method = matcher.group(2);
					if (!MyStringUtils.isEmpty(prefix))
						continue;
					else
						break;
				}
				if (!MyStringUtils.isEmpty(prefix))
					throw new ApiException("no sush method.");
			} else if (contentType.equalsIgnoreCase(MediaType.APPLICATION_XML_VALUE)) {
				Pattern pattern = Pattern.compile("<\\s*method\\s*>([^\\<\\>]+)<\\s*/method\\s*>");
				Matcher matcher = pattern.matcher(messageBody);
				while (matcher.find()) {
					method = matcher.group(1);
					break;
				}
			} else if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
				Pattern pattern = Pattern.compile("['\"]{1}method['\"]{1}\\s*\\:\\s*['\"]{1}([^'\"]+)['\"]{1}");
				Matcher matcher = pattern.matcher(messageBody);
				while (matcher.find()) {
					method = matcher.group(1);
					break;
				}
			} else
				throw new ApiException("do not support this contentType.");

			if (method == null)
				throw new ApiException("no sush method.");

			// transform the exception
			try {
				bo = (ApiBo<?>) MyContext.getRootApplicationContext().getBean(method);
			} catch (Throwable ex) {
				throw new ApiException(ex);
			}

			bo.doInit(sign, contentType, messageBody);
			bo.doCheck();
			bo.doService();

			rs.setResult(bo.getApiResponse());
			rs.setSuccess(true);
		} catch (Throwable ex) {
			if (ex instanceof CoreException) {
				CoreException ce = (CoreException) ex;
				rs.setCode(ce.getCode());
				rs.setMsg(ce.getMessage());
			} else {
				rs.setCode("default");
				rs.setMsg(ex.getMessage());
			}
		}
		return rs;
	}
}
