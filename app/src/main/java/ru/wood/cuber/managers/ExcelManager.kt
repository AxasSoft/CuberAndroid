package ru.wood.cuber.managers

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import org.apache.poi.hssf.usermodel.*
import ru.wood.cuber.Loger
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ExcelManager (val context: Context)  {

    private lateinit var dir: File
    private lateinit var fileTarget: String
    private val titleList : List<String> = arrayListOf("key","data","order_no","container_no","length","diameter","pieces","volume")


    fun createFile(list : List<List<String>>): HSSFWorkbook{
        val workbook = HSSFWorkbook()
        val firstSheet = workbook.createSheet("Расчет")

        val stroke=firstSheet.createRow(0)
        for (x in titleList.indices){
            val column = stroke.createCell(x)
            column.setCellValue(HSSFRichTextString(titleList[x]))
        }

        val size=list.size
        for (count in list.indices){
            val stroke=firstSheet.createRow(count+1)
            val temp=list[count]
            for (x in temp.indices){
                Loger.log(temp[x])
                val column = stroke.createCell(x)
                column.setCellValue(HSSFRichTextString(temp[x]))
            }
        }


       /* val row2=firstSheet.createRow(1)
        val temp=list[0]
        for (x in temp.indices){
            Loger.log(temp[x])
            val cell = row2.createCell(x)
            cell.setCellValue(HSSFRichTextString(temp[x]))
        }
*/


        /*for (stroke in list){
            Loger.log("♦♦♦"+stroke)
            val count : Int=stroke[0].toInt()
            val row=firstSheet.createRow(count)

            val listRow: List<String> =stroke
            for (i in listRow.indices){
                val cellA = row.createCell(i+1)
                cellA.setCellValue(HSSFRichTextString(listRow[i]))
            }
        }*/



/*
        for (i in list.indices){
            val row = firstSheet.createRow(i+1)

            val listRow: List<String> =list[i]
            for (j in listRow.indices) {
                val cellA = row.createCell(i)
                cellA.setCellValue(HSSFRichTextString(listRow[j]))
            }
        }*/
        return workbook
    }

   /* fun createFile(valueList: List<String> ): HSSFWorkbook? {
        val workbook = HSSFWorkbook()
        val firstSheet = workbook.createSheet("Sheet No 1")
        //HSSFSheet secondSheet = workbook.createSheet("Sheet No 2");
        val row1 = firstSheet.createRow(0)
        for (i in titleList.indices) {
            val cellA = row1.createCell(i)
            cellA.setCellValue(HSSFRichTextString(titleList.get(i)))
        }
        val row2 = firstSheet.createRow(1)
        for (i in valueList.indices) {
            val cellA = row2.createCell(i)
            cellA.setCellValue(HSSFRichTextString(valueList.get(i)))
        }

        return workbook
    }*/

    fun writeFile(workbook: HSSFWorkbook): Boolean {
        var fos: FileOutputStream? = null
        try {
            dir = context.getExternalFilesDir("xls")!!
            if (!dir.exists()) {
                dir.mkdirs()
            }
            fileTarget = dir.getAbsolutePath() + "/order.xls"
            val filePath: File = File(fileTarget)
            fos = FileOutputStream(filePath)
            workbook.write(fos)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.flush()
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            println("Excel Sheet Generated")
            //Toast.makeText(context, "Формируется Excel таблица", Toast.LENGTH_SHORT).show()
            return true
        }
    }

    fun openFile() {
        val uri = FileProvider.getUriForFile(
            context,
            context.getApplicationContext().getPackageName() + ".provider", File(fileTarget)
        )
        Log.d("path", uri.toString())
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/vnd.ms-excel")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val intent1 = Intent.createChooser(intent, "Open With")
        context.startActivity(intent1)
    }
}