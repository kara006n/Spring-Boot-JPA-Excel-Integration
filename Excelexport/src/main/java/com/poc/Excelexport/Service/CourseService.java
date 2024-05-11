package com.poc.Excelexport.Service;

import com.poc.Excelexport.dto.CourseDto;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface CourseService {

      public CourseDto createCourse(CourseDto courseDto);
      public CourseDto getCourse(int courseId);

      public void generateExcel(HttpServletResponse response) throws IOException;
      public void sendMailWithAttachment(String toEmail,
                                         String body,
                                         String subject
                                         ) throws MessagingException, IOException;

}
