package com.taskssystem.service;

import com.taskssystem.dto.TaskDto;
import com.taskssystem.model.Task;
import com.taskssystem.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final TaskService taskService;
    private final UserService userService;
    private final JavaMailSender mailSender;
    private final ITemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String from;


    @Scheduled(cron = "0 30 8 * * *")
//    @Scheduled(fixedRate = 10000)
    private void sendTaskForToday() throws MessagingException {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            List<TaskDto> tasksForToday = taskService.getTasksForToday(user);

            log.info("tasks: " + tasksForToday);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setSubject("Today's Tasks");

            Context context = new Context();
            context.setVariable("tasks", tasksForToday);

            String content = templateEngine.process("email-template", context);

            helper.setText(content, true);

            mailSender.send(message);

        }
    }

}
