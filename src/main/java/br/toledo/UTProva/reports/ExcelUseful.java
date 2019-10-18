package br.toledo.UTProva.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.toledo.UTProva.reports.enade.EnadeVO;

@Component
public class ExcelUseful {

    public List<EnadeVO> parseExcelToFile(MultipartFile in) {
        try {          
            Workbook workbook = new XSSFWorkbook(in.getInputStream());
            Sheet datatypeSheet = workbook.getSheet("Sheet1");
            Iterator<Row> iterator = datatypeSheet.iterator();
            List<EnadeVO> enadeVOs = new ArrayList<>();
            while(iterator.hasNext()){
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                EnadeVO enadeVO = new EnadeVO();
                boolean vazio = false;
                while(cellIterator.hasNext()){
                    Cell currentCell = cellIterator.next();
                    
                   
                    // System.out.println("Coluna " + currentCell.getColumnIndex());
                    // System.out.println("Linha " + currentCell.getRowIndex());

                    //SetAno
                    if(currentCell.getColumnIndex() == 0 && currentCell.getRowIndex() > 0){                        
                        if(currentCell.getCellTypeEnum() == CellType.STRING)  
                            enadeVO.setAno(currentCell.getStringCellValue());
                        if(currentCell.getCellTypeEnum() == CellType.NUMERIC) 
                            enadeVO.setAno(String.valueOf(Math.round(currentCell.getNumericCellValue())));                       
                    }  
                    //SetCodArea
                    if(currentCell.getColumnIndex() == 1 && currentCell.getRowIndex() > 0){                        
                        if(currentCell.getCellTypeEnum() == CellType.STRING)  
                            enadeVO.setCodArea(Long.parseLong(currentCell.getStringCellValue()));
                        if(currentCell.getCellTypeEnum() == CellType.NUMERIC) 
                            enadeVO.setCodArea(parseLong(currentCell.getNumericCellValue()));
                    }  
                    //SetArea
                    if(currentCell.getColumnIndex() == 2 && currentCell.getRowIndex() > 0){                        
                        if(currentCell.getCellTypeEnum() == CellType.STRING)  
                            enadeVO.setArea(currentCell.getStringCellValue());
                        if(currentCell.getCellTypeEnum() == CellType.NUMERIC) 
                            enadeVO.setArea(String.valueOf(currentCell.getNumericCellValue()));                       
                    }  
                    //SetCodIES
                    if(currentCell.getColumnIndex() == 3 && currentCell.getRowIndex() > 0){                        
                        if(currentCell.getCellTypeEnum() == CellType.STRING)  
                            enadeVO.setCodIES(Long.parseLong(currentCell.getStringCellValue()));
                        if(currentCell.getCellTypeEnum() == CellType.NUMERIC) 
                            enadeVO.setCodIES(parseLong(currentCell.getNumericCellValue()));
                    } 
                    //SetNomeIES
                    if(currentCell.getColumnIndex() == 4 && currentCell.getRowIndex() > 0){                        
                        if(currentCell.getCellTypeEnum() == CellType.STRING)  
                            enadeVO.setNomeIES(currentCell.getStringCellValue());
                        if(currentCell.getCellTypeEnum() == CellType.NUMERIC) 
                            enadeVO.setNomeIES(String.valueOf(currentCell.getNumericCellValue()));                       
                    }  
                    //SetNotaBrutaFG
                    if(currentCell.getColumnIndex() == 5 && currentCell.getRowIndex() > 0){                        
                        if(currentCell.getCellTypeEnum() == CellType.BLANK)  
                            vazio = true;
                        if(currentCell.getCellTypeEnum() == CellType.STRING)  
                            enadeVO.setNotaBrutaFG(Double.valueOf(currentCell.getStringCellValue()));
                        if(currentCell.getCellTypeEnum() == CellType.NUMERIC) 
                            enadeVO.setNotaBrutaFG(currentCell.getNumericCellValue());                       
                    }  
                    //SetNotaPadronizadaFG
                    if(currentCell.getColumnIndex() == 6 && currentCell.getRowIndex() > 0){                        
                        if(currentCell.getCellTypeEnum() == CellType.BLANK)  
                            vazio = true;
                        if(currentCell.getCellTypeEnum() == CellType.STRING)  
                            enadeVO.setNotaPradonizadaFG(Double.valueOf(currentCell.getStringCellValue()));
                        if(currentCell.getCellTypeEnum() == CellType.NUMERIC) 
                            enadeVO.setNotaPradonizadaFG(currentCell.getNumericCellValue());                       
                    }
                    //SetNotaBrutaCE
                    if(currentCell.getColumnIndex() == 7 && currentCell.getRowIndex() > 0){                        
                        if(currentCell.getCellTypeEnum() == CellType.BLANK)  
                            vazio = true;
                        if(currentCell.getCellTypeEnum() == CellType.STRING)  
                            enadeVO.setNotaBrutaCE(Double.valueOf(currentCell.getStringCellValue()));
                        if(currentCell.getCellTypeEnum() == CellType.NUMERIC) 
                            enadeVO.setNotaBrutaCE(currentCell.getNumericCellValue());                       
                    }
                    //SetNotaPadronizadaCE
                    if(currentCell.getColumnIndex() == 8 && currentCell.getRowIndex() > 0){                        
                        if(currentCell.getCellTypeEnum() == CellType.BLANK)  
                            vazio = true;
                        if(currentCell.getCellTypeEnum() == CellType.STRING)  
                            enadeVO.setNotaPradonizadaCE(Double.valueOf(currentCell.getStringCellValue()));
                        if(currentCell.getCellTypeEnum() == CellType.NUMERIC) 
                            enadeVO.setNotaPradonizadaCE(currentCell.getNumericCellValue());                       
                    } 
                    //SetNotaBrutaGeral
                    if(currentCell.getColumnIndex() == 9 && currentCell.getRowIndex() > 0){                        
                        if(currentCell.getCellTypeEnum() == CellType.BLANK)  
                            vazio = true;
                        if(currentCell.getCellTypeEnum() == CellType.STRING)  
                            enadeVO.setNotaBrutaGeral(Double.valueOf(currentCell.getStringCellValue()));
                        if(currentCell.getCellTypeEnum() == CellType.NUMERIC) 
                            enadeVO.setNotaBrutaGeral(currentCell.getNumericCellValue());                       
                    } 
                    //SetConceitoEnadeContinuo
                    if(currentCell.getColumnIndex() == 10 && currentCell.getRowIndex() > 0){                        
                        if(currentCell.getCellTypeEnum() == CellType.BLANK)  
                            vazio = true;
                        if(currentCell.getCellTypeEnum() == CellType.STRING)  
                            enadeVO.setConceitoEnadeContinuo(Double.valueOf(currentCell.getStringCellValue()));
                        if(currentCell.getCellTypeEnum() == CellType.NUMERIC) 
                            enadeVO.setConceitoEnadeContinuo(currentCell.getNumericCellValue());                       
                    } 
                    //SetConceitoEnadeFaixa
                    if(currentCell.getColumnIndex() == 11 && currentCell.getRowIndex() > 0){                        
                        if(currentCell.getCellTypeEnum() == CellType.BLANK)  
                            vazio = true;
                        if(currentCell.getCellTypeEnum() == CellType.STRING)  
                            enadeVO.setConceitoEnadeFaixa(Integer.valueOf(currentCell.getStringCellValue()));
                        if(currentCell.getCellTypeEnum() == CellType.NUMERIC) 
                            enadeVO.setConceitoEnadeFaixa(Integer.valueOf(String.valueOf(Math.round(currentCell.getNumericCellValue()))));                       
                    } 
                }
                if(!vazio){
                    enadeVOs.add(enadeVO);
                }
                
            }
            
            return enadeVOs;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Long parseLong(Double num){
        Long numLong = Math.round(num);        
        return numLong;
    }
    
}