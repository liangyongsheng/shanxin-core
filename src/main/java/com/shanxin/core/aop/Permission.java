package com.shanxin.core.aop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shanxin.core.api.ApiBo;
import com.shanxin.core.api.ApiResponse;
import com.shanxin.core.api.Response;
import com.shanxin.core.exception.CoreException;
import com.shanxin.core.exception.ServiceException;
import com.shanxin.core.util.MyAlgorithmUtils;
import com.shanxin.core.util.MyStringUtils;

@Aspect
public class Permission {

	@SuppressWarnings("unchecked")
	public void before(JoinPoint joinPoint) throws ServiceException {
		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Object target = joinPoint.getTarget();
			if (target instanceof ApiBo<?>) {
				ApiBo<?> bo = (ApiBo<?>) target;
				if (bo.getApiRequest() == null)
					return;

				// 操作员登录时是没有oprtId
				if (bo.getApiRequest().getOprtId() == null)
					return;

				Integer oprtId = bo.getApiRequest().getOprtId();
				String oprtSecret = bo.getApiRequest().getOprtSecret();
				String oprtAccessToken = bo.getApiRequest().getOprtAccessToken();
				String method = bo.getApiRequest().getMethod();

				String requstUrl = null;
				String apiMethod = null;
				int connectTimeout = 30; // 以秒
				int readTimeout = 30; // 以秒
				Properties props = null;

				try {
					Resource resource = new ClassPathResource("permission.properties");
					props = PropertiesLoaderUtils.loadProperties(resource);
					requstUrl = props.getProperty("permission.url");
					apiMethod = props.getProperty("permission.method");
				} catch (Throwable ex) {
				}
				try {
					connectTimeout = props == null ? connectTimeout : Integer.parseInt(props.getProperty("permission.connectTimeout").trim());
				} catch (Throwable ex) {
				}
				try {
					readTimeout = props == null ? readTimeout : Integer.parseInt(props.getProperty("permission.readTimeout").trim());
				} catch (Throwable ex) {
				}

				if (!MyStringUtils.isEmpty(method) && !MyStringUtils.isEmpty(requstUrl) && !MyStringUtils.isEmpty(apiMethod)) {
					method = method.trim();
					requstUrl = requstUrl.trim();
					apiMethod = apiMethod.trim();
					// 调用接口apiMethod方法，验证method方法，apiMethod就不可与method一样，否则循环调用
					if (method.equalsIgnoreCase(apiMethod))
						return;

					Map<String, Object> request = new HashMap<String, Object>();
					request.put("method", apiMethod);
					request.put("timestamp", sd.format(new Date()));
					request.put("oprtId", oprtId);
					request.put("oprtSecret", oprtSecret);
					request.put("oprtAccessToken", oprtAccessToken);
					request.put("checkMethod", method);

					// POST数据JSON
					ObjectMapper om = new ObjectMapper();
					String postData = om.writeValueAsString(request);
					String sign = MyAlgorithmUtils.MD5(postData);

					if (requstUrl.toLowerCase().startsWith("https")) {
						// TODO HTTPS信道
					}
					// 非HTTP信道
					else if (requstUrl.toLowerCase().startsWith("http")) {
						URL url = new URL(requstUrl);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod("POST");
						connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_VALUE);
						connection.setRequestProperty("Accept", MediaType.APPLICATION_JSON_VALUE);
						connection.setRequestProperty("Sign", sign);
						connection.setConnectTimeout(connectTimeout * 1000);
						connection.setReadTimeout(readTimeout * 1000);
						connection.setUseCaches(false);
						connection.setDoInput(true);

						if (!MyStringUtils.isEmpty(postData)) {
							connection.setDoOutput(true);
							OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream(), Charset.forName("utf-8"));
							os.write(postData);
							os.flush();
							os.close();
						}

						// get response
						BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("utf-8")));
						String line = null;
						StringBuffer rspSb = new StringBuffer();
						while ((line = br.readLine()) != null)
							rspSb.append(line);
						br.close();
						connection.disconnect();

						boolean check = false;
						Pattern pattern = Pattern.compile("\"success\"\\s*:\\s*(true|false)");
						Matcher matcher = pattern.matcher(rspSb.toString());
						while (matcher.find()) {
							if (matcher.group(1).equalsIgnoreCase("true"))
								check = true;
							else {
								check = false;
								break;
							}
						}
						if (check == false) {
							// 读回msg
							Response<ApiResponse> rsp = new Response<ApiResponse>();
							rsp = om.readValue(rspSb.toString(), rsp.getClass());
							throw new ServiceException(MyStringUtils.isEmpty(rsp.getMsg()) ? "无此权限" : rsp.getMsg(), "permission-deny");
						}
					}
					// 直接放行
					else {
					}
				}
			}
		} catch (Throwable ex) {
			if (ex instanceof CoreException)
				throw new ServiceException(ex.getMessage(), ((CoreException) ex).getCode());
			else
				throw new ServiceException(ex.getMessage());
		}
	}
}
