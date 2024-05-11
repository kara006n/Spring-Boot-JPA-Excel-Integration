package com.poc.Excelexport.util;

import com.poc.Excelexport.dto.CourseDto;

import java.util.List;

public class ExcelService {

      public String generateHtmlTable(List<CourseDto> courseDtoListList, String borderWidth) {
            StringBuilder sb = new StringBuilder();
            sb.append("<table class='course-table' style='border-collapse: collapse;'>"); // Removed border-width from table style

            // Header Row with Borders (unchanged)
            sb.append("<tr>");
            sb.append("<th class='header' style='border: ").append(borderWidth).append("px solid black;'>ID</th>");
            sb.append("<th class='header' style='border: ").append(borderWidth).append("px solid black;'>Name</th>");
            sb.append("<th class='header' style='border: ").append(borderWidth).append("px solid black;'>Fees</th>");
            sb.append("</tr>");

            // Handle Empty Data (unchanged)
            if (courseDtoListList.isEmpty()) {
                  sb.append("<tr><td colspan='3' style='border: ").append(borderWidth).append("px solid black;'>No courses found!</td></tr>");
            } else {
                  for (CourseDto courseDto : courseDtoListList) {
                        String rowStyle = ""; // Initialize empty row style
                        if (courseDto.getFees() < 5000) {
                              rowStyle = " style='background-color: red;'"; // Set style for row if fees < 5000
                        }
                        sb.append("<tr" + rowStyle + ">");  // Apply row style if set
                        sb.append("<td style='border: ").append(borderWidth).append("px solid black;'>").append(courseDto.getCid()).append("</td>");
                        sb.append("<td style='border: ").append(borderWidth).append("px solid black;'>").append(courseDto.getCname()).append("</td>");
                        sb.append("<td style='border: ").append(borderWidth).append("px solid black;'>").append(courseDto.getFees()).append("</td>");
                        sb.append("</tr>");
                  }
            }

            sb.append("</table>");
            return sb.toString();
      }



}

