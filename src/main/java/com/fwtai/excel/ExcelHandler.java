package com.fwtai.excel;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 通用批量导入大数据添加了根据对应标题字段获取数据库字段
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017年11月2日 上午9:47:35
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public abstract class ExcelHandler extends DefaultHandler {

	private int countrows = 0;
	protected int count = 0;

	private int minColumns = 1;
	private String titles = null;

	private StylesTable stylesTable;

	private ReadOnlySharedStringsTable sharedStringsTable;

	private int minColumnCount = 0;

	private boolean vIsOpen;

	private ExcelHandlerXssfDataType nextDataType;

	private short formatIndex;
	private String formatString;
	private DataFormatter formatter;

	private int thisColumn = -1;

	private int lastColumnNumber = -1;

	private StringBuffer value;
	private ArrayList<String> rowlist = null;
	private ArrayList<String> titlelist = null;

	private StringBuffer sqlBuf;

	/**
	 * 通用定义回调方法进行批量处理
	 * @param countrows 当前行数
	 * @param titlelist  显示字段列名称
	 * @param rowlist 批量导入数据
	*/
	public abstract void optRows(int countrows, ArrayList<String> titlelist, ArrayList<String> rowlist);

	/**
	 * 统一调用Excel批量导入数据,标题行必须放在第1行
	 * @param excelPath 所属完整路径
	 */
	public void readExcel(String excelPath) throws Exception{
		if (excelPath == null || excelPath.equals("")){
			throw new Exception("构造Excel导入器失败,未指定文件全名");
		}
		final File file = new File(excelPath);
		if (!file.exists()){
			throw new Exception("构造Excel导入器失败，指定的文件不存在:" + excelPath);
		}
		if(excelPath.endsWith(".xlsx")){
			rowlist = PoiExcel2007(excelPath);
			optRows(countrows, titlelist, rowlist);
		}
	}

	/**
	 * 统一调用Excel导入数据 2007数据导入
	 */
	private ArrayList<String> PoiExcel2007(final String excelPath){
		countrows = 0;
		this.sqlBuf = new StringBuffer();
		try {
			return process(excelPath);
		} catch (Exception e){
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	/**
	 * 开始解析文档，即开始解析XML根元素时调用该方法
	 */
	@Override
	public void startDocument() throws SAXException {
		rowlist = new ArrayList<String>();
		titlelist = new ArrayList<String>();
	}

	// 结束解析文档，即解析根元素结束标签时调用该方法
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		if("inlineStr".equals(name) || "v".equals(name)) {
			vIsOpen = true;
			value.setLength(0);
		}
		// c => cell
		else if ("c".equals(name)) {
			// Get the cell reference
			String r = attributes.getValue("r");
			int firstDigit = -1;
			for (int c = 0; c < r.length(); ++c) {
				if (Character.isDigit(r.charAt(c))) {
					firstDigit = c;
					break;
				}
			}
			thisColumn = nameToColumn(r.substring(0, firstDigit));
			// Set up defaults.
			this.nextDataType = ExcelHandlerXssfDataType.NUMBER;
			this.formatIndex = -1;
			this.formatString = null;
			String cellType = attributes.getValue("t");
			String cellStyleStr = attributes.getValue("s");

			if ("b".equals(cellType)) {
				nextDataType = ExcelHandlerXssfDataType.BOOL;
			} else if ("e".equals(cellType)) {
				nextDataType = ExcelHandlerXssfDataType.ERROR;
			} else if ("inlineStr".equals(cellType)) {
				nextDataType = ExcelHandlerXssfDataType.INLINESTR;
			} else if ("s".equals(cellType)) {
				nextDataType = ExcelHandlerXssfDataType.SSTINDEX;
			} else if ("str".equals(cellType)) {
				nextDataType = ExcelHandlerXssfDataType.FORMULA;
			} else if (cellStyleStr != null) {
				int styleIndex = Integer.parseInt(cellStyleStr);
				XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
				this.formatIndex = style.getDataFormat();
				this.formatString = style.getDataFormatString();
				if (formatString == null) {
					nextDataType = ExcelHandlerXssfDataType.NULL;
					this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
				}
				if ("m/d/yy h:mm" == formatString) {
					nextDataType = ExcelHandlerXssfDataType.DATE;
					formatString = "yyyy-MM-dd HH:mm:ss";
				}
				if ("m/d/yy" == formatString) {
					nextDataType = ExcelHandlerXssfDataType.DATE;
					formatString = "yyyy-MM-dd HH:mm:ss";
				}
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		String thisStr = "";
		// v => contents of a cell
		if ("v".equals(name)) {
			switch (nextDataType) {
			case BOOL:
				char first = value.charAt(0);
				thisStr = first == 0 ? "false" : "true";
				thisStr = "" + thisStr + "";
				break;
			case ERROR:
				thisStr = "\"ERROR:" + value.toString();
				thisStr = "" + value.toString() + "";
				break;
			case FORMULA:
				thisStr = "" + value.toString() + "";
				break;
			case INLINESTR:
				XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
				thisStr = "" + rtsi.toString() + "";
				break;
			case SSTINDEX:
				String sstIndex = value.toString();
				try {
					int idx = Integer.parseInt(sstIndex);
					XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
					thisStr = "" + rtss.toString() + "";
				} catch (NumberFormatException ex) {
					System.out.println("Failed to parse SST index " + sstIndex + ": " + ex.toString());
				}
				break;
			case NUMBER:
				String n = value.toString();
				thisStr = "" + n + "";
				// this.sqlBuf.append(thisStr);
				break;
			case DATE:
				String strDate = value.toString();
				// 对日期字符串作特殊处理
				thisStr = "" + formatter.formatRawCellContents(Double.parseDouble(strDate), this.formatIndex, this.formatString) + "";
				break;
			case NULL:
				thisStr = "null";
				break;
			default:
				thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
				break;
			}
			if (lastColumnNumber == -1) {
				lastColumnNumber = 0;
			}
			for (int i = lastColumnNumber; i < thisColumn; ++i){
				this.sqlBuf.append(",");//叠加
			}
			// 计算字段个数
			count++;
			this.sqlBuf.append(thisStr.trim());
			if (thisColumn > -1){
				lastColumnNumber = thisColumn;
			}
		} else if ("row".equals(name)){
			count = 0;
			if (minColumns > 0){
				if(lastColumnNumber == -1){
					lastColumnNumber = 0;
				}
				for(int i = lastColumnNumber; i < (this.minColumnCount); i++){
					this.sqlBuf.append(",");
				}
			}
			String strValue = this.sqlBuf.substring(0, this.sqlBuf.length());
			countrows++;
			if (countrows >= this.minColumns && countrows == this.minColumns){
				titlelist.add(strValue);//表头
				if (titles == null){
					titles = strValue;
				}
			} else if (countrows > this.minColumns){
				if(!titles.equals(strValue)){
					rowlist.add(strValue);//数据行
				}
			}
			optRows(countrows,titlelist,rowlist);
			rowlist.clear();
			rowlist = new ArrayList<String>();
			lastColumnNumber = -1;
			this.sqlBuf.delete(0,this.sqlBuf.length());
		}
	}

	private ArrayList<String> processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream, int cols, StringBuffer sqlBuf) throws IOException, ParserConfigurationException, SAXException {
		InputSource sheetSource = new InputSource(sheetInputStream);
		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxFactory.newSAXParser();
		XMLReader sheetParser = saxParser.getXMLReader();
		this.stylesTable = styles;
		this.sharedStringsTable = strings;
		this.minColumnCount = cols;
		this.value = new StringBuffer();
		this.nextDataType = ExcelHandlerXssfDataType.NUMBER;
		this.formatter = new DataFormatter();
		this.sqlBuf = sqlBuf;
		sheetParser.setContentHandler(this);
		sheetParser.parse(sheetSource);
		return rowlist;
	}

	private ArrayList<String> process(final String excelPath) throws Exception{
		final OPCPackage pkg = OPCPackage.open(excelPath);
		final ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
		final XSSFReader xssf = new XSSFReader(pkg);
		final StylesTable styles = xssf.getStylesTable();
		final Iterator<InputStream> sheets = xssf.getSheetsData();
		while (sheets.hasNext()){
			final InputStream stream = sheets.next();
			rowlist = processSheet(styles,strings,stream,this.minColumnCount,this.sqlBuf);
			stream.close();
		}
		return rowlist;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (vIsOpen)value.append(ch, start,length);
	}

	private int nameToColumn(String name) {
		int column = -1;
		for (int i = 0; i < name.length(); ++i) {
			int c = name.charAt(i);
			column = (column + 1) * 26 + c - 'A';
		}
		return column;
	}
	
	private enum ExcelHandlerXssfDataType {
		BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL,
	}
}