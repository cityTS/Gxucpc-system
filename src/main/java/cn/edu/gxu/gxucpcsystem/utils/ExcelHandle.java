package cn.edu.gxu.gxucpcsystem.utils;

/**
 * @Author: MaoMao
 * @License: (C) Copyright 2005-2019, xxx Corporation Limited.
 * @Contact: 2986325137@qq.com
 * @Date: 9/6/2022 7:32 PM
 * @Version: 1.0
 * @Description:
 */

import cn.edu.gxu.gxucpcsystem.domain.Medal;
import cn.edu.gxu.gxucpcsystem.domain.Player;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * ExcelHandle
 *
 * @author gongweixin
 * @date 2021/3/15
 */
@Slf4j
public class ExcelHandle {


    /**
     * Excel表格导出
     *
     * @param excelData   Excel表格的数据，封装为List<Player>
     * @param sheetName   sheet的名字
     * @param fileName    导出Excel的文件名
     * @param columnWidth Excel表格的宽度，建议为15
     * @throws IOException 抛IO异常
     */
    public static void exportExcel(
            HttpServletResponse response,
            List<Player> excelData,
            String sheetName,
            String fileName,
            int columnWidth) throws IOException {
//        OutputStream os = null;
        fileName += ".xls";
        setResponseHeader(response,fileName);
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
//        os = response.getOutputStream();
//        SXSSFWorkbook wb = new SXSSFWorkbook(1000);
        //生成一个表格，设置表格名称
        HSSFSheet sheet = workbook.createSheet(sheetName);
        //设置表格列宽度
        sheet.setDefaultColumnWidth(columnWidth);
        //写入List<List<String>>中的数据
        int rowIndex = 0;
        HSSFRow r = sheet.createRow(rowIndex++);

        String[] head = {"表单号","学号","姓名","性别","专业","班级","QQ","邮箱","是否打星","组别"};
        for (int i = 0 ; i < head.length ;  i++){
            HSSFCell cell = r.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(head[i]);
            cell.setCellValue(text);
        }
        for (Player data : excelData) {
            //创建一个row行，然后自增1
            r = sheet.createRow(rowIndex++);
            HSSFCell cell = r.createCell(0);
            cell.setCellValue(new HSSFRichTextString(String.valueOf(data.getInformationId())));
            cell = r.createCell(1);
            cell.setCellValue(new HSSFRichTextString(data.getUserId()));
            cell = r.createCell(2);
            cell.setCellValue(new HSSFRichTextString(data.getUserName()));
            cell = r.createCell(3);
            cell.setCellValue(new HSSFRichTextString(data.getUserSex()));
            cell = r.createCell(4);
            cell.setCellValue(new HSSFRichTextString(data.getUserCourse()));
            cell = r.createCell(5);
            cell.setCellValue(new HSSFRichTextString(data.getUserClass()));
            cell = r.createCell(6);
            cell.setCellValue(new HSSFRichTextString(data.getUserQQ()));
            cell = r.createCell(7);
            cell.setCellValue(new HSSFRichTextString(data.getUserMail()));
            cell = r.createCell(8);
            if (data.isStar())
                cell.setCellValue(new HSSFRichTextString("打星"));

            cell = r.createCell(9);
            if (data.isGroup())
                cell.setCellValue(new HSSFRichTextString("正式组"));
            else
                cell.setCellValue(new HSSFRichTextString("新生组"));
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
//        byte[] bytes = byteArrayOutputStream.toByteArray();




        //测试写入本地文件
        //workbook.write(new File("C:\\Users\\Administrator\\Desktop\\excel测试\\fileName.xlsx"));
        //workbook将Excel写入到response的输出流中，供页面下载该Excel文件
        workbook.write(response.getOutputStream());

        //关闭workbook
        workbook.close();
//        return bytes;
    }
    private static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
