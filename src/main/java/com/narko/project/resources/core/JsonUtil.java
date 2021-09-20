package com.narko.project.resources.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.narko.project.model.audit.AuditEntity;

public class JsonUtil<T> {
	private static final Logger log = LogManager.getLogger(JsonUtil.class);

	public String JsonUtil(T data) {

		System.out.println("masuk jsonutil");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";

		JSONObject dataJson = new JSONObject();

		String getterID = "get" + AuditEntity.COL_ID.substring(0, 1).toUpperCase() + AuditEntity.COL_ID.substring(1);
		String getterStatus = "get" + AuditEntity.COL_STATUS.substring(0, 1).toUpperCase()
				+ AuditEntity.COL_STATUS.substring(1);

		try {

			if (data != null) {
				System.out.println(data.getClass().getSimpleName());

				Field[] fieldData = data.getClass().getDeclaredFields();

				Method getId = data.getClass().getMethod(getterID, null);
				Method getStatus = data.getClass().getMethod(getterStatus, null);

				dataJson.put(AuditEntity.COL_ID, getId.invoke(data, null));
				dataJson.put(AuditEntity.COL_STATUS, getStatus.invoke(data, null));

				for (Field field : fieldData) {
					field.setAccessible(true);

					if (!field.getName().contains("_") && field.getAnnotation(JsonIgnore.class) == null) {
						System.out.println(field.getName() + " : " + field.get(data));
						if (field.getType().equals(Long.class) || field.getType().equals(String.class)
								|| field.getType().equals(Double.class)) {

							dataJson.put(field.getName(), field.get(data));
						}
//						else {
//							System.out.println(field.getType());
//							String getterObj = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
//							Method getObj = data.getClass().getMethod(getterObj, null);
//							System.out.println(getObj.getName());
//							Object obj = getObj.invoke(data, null);
//							JsonUtil<Object> json =  new JsonUtil<>();
//							json.JsonUtil(obj);
//						}

					}
				}
			}
			return dataJson.toJSONString();


		} catch (

		Exception e) {
			e.printStackTrace(ps);
			ps.close();
			message = e.getMessage();

			return null;
		} finally {
			if (!message.isEmpty() || !message.equals("") || baos.size() > 1) {
				log.trace(JsonUtil.class);
				log.error(message);
				log.error("stackTrace : " + baos.toString());
			}

		}
	}

}