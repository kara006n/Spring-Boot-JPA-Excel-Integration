package com.poc.Excelexport.Service.impl;

import com.poc.Excelexport.Repository.CourseRepository;
import com.poc.Excelexport.Service.CourseService;
import com.poc.Excelexport.dto.CourseDto;
import com.poc.Excelexport.entity.Course;
import com.poc.Excelexport.util.ExcelService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {


      @Autowired
      private CourseRepository courseRepository;

      @Autowired
      private ModelMapper mapper;

      @Autowired
      private JavaMailSender mailSender;

      @Value("${spring.mail.username}")
      private String fromMail;

      @Override
      public CourseDto createCourse(CourseDto courseDto) {

            Course course = mapper.map(courseDto, Course.class);
            Course save = courseRepository.save(course);
            return mapper.map(save, CourseDto.class);
      }

      @Override
      public CourseDto getCourse(int courseId) {

            Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found with given Id"));
            return mapper.map(course, CourseDto.class);
      }

      @Override
      public void generateExcel(HttpServletResponse response) throws IOException {

            List<Course> courses = courseRepository.findAll();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Course Info");
            HSSFRow row = sheet.createRow(0);

            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("Name");
            row.createCell(2).setCellValue("Price");

            int dataRowIndex = 1;

            for(Course course: courses){
                  HSSFRow dataRow = sheet.createRow(dataRowIndex);
                  dataRow.createCell(0).setCellValue(course.getCid());
                  dataRow.createCell(1).setCellValue(course.getCname());
                  dataRow.createCell(2).setCellValue(course.getFees());
                  dataRowIndex++;
            }


            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

      }


      public void sendMailWithAttachment(String toEmail,
                                         String body,
                                         String subject
                                         ) throws MessagingException, IOException {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromMail);
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setText(body);
            mimeMessageHelper.setSubject(subject);


            List<Course> courses = courseRepository.findAll();
            List<CourseDto> collect = courses.stream().map(course -> mapper.map(course, CourseDto.class)).collect(Collectors.toList());

            ExcelService excelService =new ExcelService();
            String htmlTable = excelService.generateHtmlTable(collect, "1");

            mimeMessage.setContent(htmlTable, "text/html");
            mailSender.send(mimeMessage);



      }


}
