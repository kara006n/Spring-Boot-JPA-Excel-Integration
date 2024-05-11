package com.poc.Excelexport.Controller;

import com.poc.Excelexport.Service.CourseService;
import com.poc.Excelexport.dto.CourseDto;
import com.poc.Excelexport.dto.MailStructure;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/course")
public class CourseController {

     Logger logger = LoggerFactory.getLogger(CourseController.class);

      @Autowired
      private CourseService courseService;

      @PostMapping("/create")
      public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto){

            logger.info("courseDto id {}",courseDto);
           //System.out.println(courseDto);
            CourseDto course = courseService.createCourse(courseDto);
            return new ResponseEntity<>(course, HttpStatus.CREATED);

      }

      @GetMapping("/fetch/{courseId}")
      public ResponseEntity<CourseDto> getCourse(@PathVariable int courseId){

            CourseDto course = courseService.getCourse(courseId);
            return new ResponseEntity<>(course, HttpStatus.CREATED);

      }


      @GetMapping("/getExcel")
      public void getExcelData(HttpServletResponse response) throws IOException {

            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename = courses.xls";

            response.setHeader(headerKey,headerValue);
            courseService.generateExcel(response);
      }


      @PostMapping("/sendWithAttachment/{toEmail}")
      public String sendMailWithAttachment(@PathVariable String toEmail, @RequestBody MailStructure mailStructure) throws MessagingException, IOException {

            System.out.println("entered in message");
            courseService.sendMailWithAttachment(toEmail, mailStructure.getMessage(), mailStructure.getSubject());
            return "Succesfully send the mail with attachment";
      }


}
