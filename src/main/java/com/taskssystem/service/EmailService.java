package com.taskssystem.service;

import com.taskssystem.dto.TaskDto;
import com.taskssystem.model.Task;
import com.taskssystem.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final TaskService taskService;
    private final UserService userService;
    private final JavaMailSender mailSender;
    private final ITemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String from;

    private void sendTaskForToday() throws MessagingException {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            List<TaskDto> tasksForToday = taskService.getTasksForToday(user);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(user.getEmail()); // Assuming you have an email field in the User entity
            helper.setSubject("Today's Tasks");

            Context context = new Context();
            context.setVariable("tasks", tasksForToday);

            String content = templateEngine.process("email-template", context);

            helper.setText(content, true);

            mailSender.send(message);

        }
    }

}
