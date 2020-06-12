package com.quang.da.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Service
public class MailService {
	@Autowired
	private JavaMailSender sender;

	@Autowired
	private Configuration config;

	@Async
	public void sendMail(String[] to, String temp, Map<String, Object> model) throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {

		MimeMessage message = sender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		Template template = config.getTemplate(temp);
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		helper.setSubject("OfficeLink ");
		helper.setTo(to);
		helper.setText(html, true);
		helper.setFrom("officelinksup@gmail.com");
		sender.send(message);

	}

}
