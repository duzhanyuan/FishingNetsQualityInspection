package org.jaiken.tools;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class WriteExcel {

	public void checkFile(String destFileName) {

		File file = new File(destFileName);
		if (file.exists())
			return;
		// 判断目标文件所在的目录是否存在
		if (!file.getParentFile().exists()) {
			// 如果目标文件所在的目录不存在，则创建父目录
			System.out.println("目标文件所在目录不存在，准备创建它！");
			if (!file.getParentFile().mkdirs()) {
				System.out.println("创建目标文件所在目录失败！");

			}
		}
		// 创建目标文件
		if (!file.exists()) {
			try {
				if (file.createNewFile()) {
					System.out.println("创建单个文件" + destFileName + "成功！");

				} else {
					System.out.println("创建单个文件" + destFileName + "失败！");

				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());

			}
		}

	}

	public void writeExcel(String fileName, String[] data) throws Exception{
		checkFile(fileName);
		WritableWorkbook wwb = null;
		try {
			// 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
			wwb = Workbook.createWorkbook(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (wwb != null) {
			// 创建一个可写入的工作表
			// Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置
			WritableSheet ws;
			if (wwb.getNumberOfSheets() == 0) {
				ws = wwb.createSheet("sheet1", 0);
			} else {
				ws = wwb.getSheet(0);
			}

			int rowNumber = ws.getRows(); // 第一行从0开始算

			System.out.println("原始数据总行数，除属性列：" + rowNumber);

			for (int j = 0; j < data.length; j++) {
				// 这里需要注意的是，在Excel中，第一个参数表示列，第二个表示行
				Label labelC = new Label(j, rowNumber, data[j]);
				try {
					// 将生成的单元格添加到工作表中
					ws.addCell(labelC);
				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				}

			}

			try {
				// 从内存中写入文件中
				wwb.write();
				// 关闭资源，释放内存
				wwb.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
	}
}