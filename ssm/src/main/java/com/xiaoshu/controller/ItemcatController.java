package com.xiaoshu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.dao.ItemcatMapper;
import com.xiaoshu.entity.Attachment;
import com.xiaoshu.entity.Itemcat;
import com.xiaoshu.entity.Log;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.ItemcatService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.UserService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("itemcat")
public class ItemcatController extends LogController{
	static Logger logger = Logger.getLogger(ItemcatController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService ;
	
	@Autowired
	private OperationService operationService;
	
	@Autowired
	private ItemcatService itemcatService;
	
	@RequestMapping("itemcatIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Role> roleList = roleService.findRole(new Role());
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "itemcat";
	}
	
	
	@RequestMapping(value="itemcatList",method=RequestMethod.POST)
	public void itemcatList(HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			
			Itemcat itemcat = new Itemcat();
			String name = request.getParameter("name");
			if (StringUtil.isNotEmpty(name)) {
				itemcat.setName(name);
			}
			String level = request.getParameter("level");
			if (StringUtil.isNotEmpty(level)) {
				itemcat.setLevel(Long.parseLong(level));
			}
			
			User user = new User();
			String username = request.getParameter("username");
			String roleid = request.getParameter("roleid");
			String usertype = request.getParameter("usertype");
			String order = request.getParameter("order");
			String ordername = request.getParameter("ordername");
			if (StringUtil.isNotEmpty(username)) {
				user.setUsername(username);
			}
			if (StringUtil.isNotEmpty(roleid) && !"0".equals(roleid)) {
				user.setRoleid(Integer.parseInt(roleid));
			}
			if (StringUtil.isNotEmpty(usertype)) {
				user.setUsertype(usertype.getBytes()[0]);
			}
			
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<Itemcat> itemcatList= itemcatService.findUserPage(itemcat,pageNum,pageSize,ordername,order);
//			List<User> list = userList.getList();
			request.setAttribute("username", username);
			request.setAttribute("roleid", roleid);
			request.setAttribute("usertype", usertype);
			
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",itemcatList.getTotal() );
			jsonObj.put("rows", itemcatList.getList());
	        WriterUtil.write(response,jsonObj.toString());
	        
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户展示错误",e);
			throw e;
		}
	}
	
	
	// 新增或修改
	@RequestMapping("reserveItemcat")
	public void reserveItemcat(HttpServletRequest request,Itemcat itemcat,HttpServletResponse response){
//		Integer userId = user.getUserid();
		
		// itemcati为空是添加；不为空时修改；
		Long itemcatid = itemcat.getItemcatid();
		
		JSONObject result=new JSONObject();
		try {
//			if (userId != null) {   // userId不为空 说明是修改
//				User userName = userService.existUserWithUserName(user.getUsername());
//				if(userName != null && userName.getUserid().compareTo(userId)==0){
//					user.setUserid(userId);
//					userService.updateUser(user);
//					result.put("success", true);
//				}else{
//					result.put("success", true);
//					result.put("errorMsg", "该用户名被使用");
//				}
//				
//			}else {   // 添加
//				if(userService.existUserWithUserName(user.getUsername())==null){  // 没有重复可以添加
//					userService.addUser(user);
//					result.put("success", true);
//				} else {
//					result.put("success", true);
//					result.put("errorMsg", "该用户名被使用");
//				}
			if(itemcatid !=null){ // 修改
				itemcatService.updateItemcat(itemcat);
				result.put("success", true);				
			}else{
				//添加
				result.put("success", true);
				itemcatService.addItemcat(itemcat);
			}
									
//			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存用户信息错误",e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	
	@RequestMapping("getItemcatByLevel")
	public void getItemcatByLevel(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String parameter = request.getParameter("level");
			Long level = Long.parseLong(parameter);
			List<Itemcat> itemcatList = itemcatService.getItemcatByLevel(level);
			result.put("success", true);
			result.put("parents", itemcatList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询上级分类信息错误",e);
			result.put("errorMsg", "对不起，查询失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	// 删除分类
	@RequestMapping("deleteItemcat")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
//				userService.deleteUser(Integer.parseInt(id));
				itemcatService.deleteItemcat(Long.parseLong(id));
			}
			result.put("success", true);
			result.put("delNums", ids.length);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}	
	
	@RequestMapping("editPassword")
	public void editPassword(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		String oldpassword = request.getParameter("oldpassword");
		String newpassword = request.getParameter("newpassword");
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		if(currentUser.getPassword().equals(oldpassword)){
			User user = new User();
			user.setUserid(currentUser.getUserid());
			user.setPassword(newpassword);
			try {
				userService.updateUser(user);
				currentUser.setPassword(newpassword);
				session.removeAttribute("currentUser"); 
				session.setAttribute("currentUser", currentUser);
				result.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("修改密码错误",e);
				result.put("errorMsg", "对不起，修改密码失败");
			}
		}else{
			logger.error(currentUser.getUsername()+"修改密码时原密码输入错误！");
			result.put("errorMsg", "对不起，原密码输入错误！");
		}
		WriterUtil.write(response, result.toString());
	}
	
	// 导出
	@RequestMapping("exportBus")
	public void backup(HttpServletRequest request,HttpServletResponse response){
		JSONObject result = new JSONObject();
		try {
			String time = TimeUtil.formatTime(new Date(), "yyyyMMddHHmmss");
		    String excelName = "商品分类"+time;// 准备文件名
//			Log log = new Log();
//			List<Log> list = logService.findLog(log); // 查询所有
			//Itemcat itemcat = new Itemcat();
			List<Itemcat> list = itemcatService.findAll();
			// 表头
			String[] handers = {"分类id","上级分类id","名称","分类级别"};
			// 1导入硬盘
			ExportExcelToDisk(request,handers,list, excelName);
			
			// 2导出的位置放入attachment表
//			Attachment attachment = new Attachment();
//			attachment.setAttachmentname(excelName+".xls");
//			attachment.setAttachmentpath("logs/backup");
//			attachment.setAttachmenttime(new Date());
//			attachmentService.insertAttachment(attachment);
			// 3删除log表
//			logService.truncateLog();
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("", "对不起，导出失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	// 导出到硬盘
		@SuppressWarnings("resource")
		private void ExportExcelToDisk(HttpServletRequest request,
				String[] handers, List<Itemcat> list, String excleName) throws Exception {
			
			try {
				HSSFWorkbook wb = new HSSFWorkbook();//创建工作簿
				HSSFSheet sheet = wb.createSheet("商品分类");//第一个sheet
				HSSFRow rowFirst = sheet.createRow(0);//第一个sheet第一行为标题
				rowFirst.setHeight((short) 500);
				
				for (int i = 0; i < handers.length; i++) {
					sheet.setColumnWidth((short) i, (short) 4000);// 设置列宽
				}
				//写标题了
				for (int i = 0; i < handers.length; i++) {
				    //获取第一行的每一个单元格
				    HSSFCell cell = rowFirst.createCell(i);
				    //往单元格里面写入值
				    cell.setCellValue(handers[i]);
				}
				
				for (int i = 0;i < list.size(); i++) {
				    //获取list里面存在是数据集对象
				    Itemcat itemcat = list.get(i);
				    //创建数据行
				    HSSFRow row = sheet.createRow(i+1);
				    //设置对应单元格的值
				    row.setHeight((short)400);   // 设置每行的高度
				    //"序号","操作人","IP地址","操作时间","操作模块","操作类型","详情"
				    row.createCell(0).setCellValue(itemcat.getItemcatid());// 自己给编号，不是商品id
				    row.createCell(1).setCellValue(itemcat.getParentId());
				    row.createCell(2).setCellValue(itemcat.getName());
				    row.createCell(3).setCellValue(itemcat.getLevel());
//				    row.createCell(4).setCellValue(log.getOperation());
//				    row.createCell(5).setCellValue(log.getModule());
//				    row.createCell(6).setCellValue(log.getContent());
				}
				//写出文件（path为文件路径含文件名）
					OutputStream os;
					// 路径可以随便给，可以放桌面；
					File file = new File(request.getSession().getServletContext().getRealPath("/")+"logs"+File.separator+"backup"+File.separator+excleName+".xls");
					System.out.println(file);
					if (!file.exists()){//若此目录不存在，则创建之  
						file.createNewFile();  
						logger.debug("创建文件夹路径为："+ file.getPath());  
		            } 
					os = new FileOutputStream(file);
					wb.write(os);
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
		}

// 导入
		@RequestMapping(value = "importBus")
		@ResponseBody
		public void importBus(@RequestParam("loginfo")  CommonsMultipartFile file,
				HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

			
			// 创建工作簿  参数： file转化的流
			HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
			
			//拿到第一个sheet
			HSSFSheet sheet = wb.getSheetAt(0);
			
			// 获取所有行的迭代器 rows
			Iterator<Row> rowIterator = sheet.rowIterator();
			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				// 循环rows拿到每一行的单元格的迭代器 cells
				if(row.getRowNum()!=0){  // 我就跳过，第一行是表头
					// 拿到每一个单元格的迭代器
					Iterator<Cell> cellIterator = row.cellIterator();
					
					// 创建一个itemcat ，封装单元格的参数；
					Itemcat itemcat = new Itemcat();
					while(cellIterator.hasNext()){
						Cell cell = cellIterator.next();
						setItemcatInfo(itemcat,cell);
						// 去给itemcat各个属性赋值；
					}
					
					// 插入到数据库
					itemcatService.insertItemcat(itemcat);
				}
				
			}
			
		//转发一下；
		request.getRequestDispatcher("itemcatIndex.htm?menuid=10").forward(request, response);
			
			
			
		}


		private void setItemcatInfo(Itemcat itemcat, Cell cell) {
			if(itemcat == null || cell == null){
				return;
			}
			// 使用switch来判断
			// 设置字符串；
			cell.setCellType(Cell.CELL_TYPE_STRING); //将单元格类型设置为字符串
			
			String value = cell.getStringCellValue();
			
			switch (cell.getColumnIndex()) {
			case 0:
				// 什么也不做；  itemcat第一列是itemcatid，数据库自增
				break;
			case 1:
				itemcat.setName(value);
				break;
			case 2:
				itemcat.setLevel(new Long(value));
				break;
			case 3:
				itemcat.setParentId(new Long(value));
				break;
			default:
				break;
			}
		}
}
