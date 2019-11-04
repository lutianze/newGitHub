package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.ItemcatMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Itemcat;
import com.xiaoshu.entity.ItemcatExample;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class ItemcatService {

	@Autowired
	UserMapper userMapper;
		
    @Autowired
    ItemcatMapper itemcatMapper;
	// 查询所有
	public List<User> findUser(User t) throws Exception {
		return userMapper.select(t);
	};

	// 数量
	public int countUser(User t) throws Exception {
		return userMapper.selectCount(t);
	};

	// 通过ID查询
	public User findOneUser(Integer id) throws Exception {
		return userMapper.selectByPrimaryKey(id);
	};

	// 新增
	public void addUser(User t) throws Exception {
		userMapper.insert(t);
	};

	// 修改
	public void updateUser(User t) throws Exception {
		userMapper.updateByPrimaryKeySelective(t);
	};

	// 删除
	public void deleteUser(Integer id) throws Exception {
		userMapper.deleteByPrimaryKey(id);
	};

	// 登录
	public User loginUser(User user) throws Exception {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andPasswordEqualTo(user.getPassword()).andUsernameEqualTo(user.getUsername());
		List<User> userList = userMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	};

	// 通过用户名判断是否存在，（新增时不能重名）
	public User existUserWithUserName(String userName) throws Exception {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(userName);
		List<User> userList = userMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	};

	// 通过角色判断是否存在
	public User existUserWithRoleId(Integer roleId) throws Exception {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleidEqualTo(roleId);
		List<User> userList = userMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	}

	public PageInfo<Itemcat> findUserPage(Itemcat itemcat, int pageNum, int pageSize, String ordername, String order) {
		PageHelper.startPage(pageNum, pageSize);
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"userid";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		
		
//		UserExample example = new UserExample();
//		example.setOrderByClause(ordername+" "+order);
//		Criteria criteria = example.createCriteria();
		ItemcatExample example = new  ItemcatExample();
		com.xiaoshu.entity.ItemcatExample.Criteria criteria = example.createCriteria();
		if(StringUtil.isNotEmpty(itemcat.getName())){
			criteria.andNameLike("%"+itemcat.getName()+"%");
		}
		
//		if (itemcat.getLevel() !=null) { 
//			 level 不可能等于null ，因为默认值0
//		}
		if(itemcat.getLevel()!=0){
			criteria.andLevelEqualTo(itemcat.getLevel());
		}
		
//		if(StringUtil.isNotEmpty(user.getUsername())){
//			criteria.andUsernameLike("%"+user.getUsername()+"%");
//		}
//		if(user.getUsertype() != null){
//			criteria.andUsertypeEqualTo(user.getUsertype());
//		}
//		if(user.getRoleid() != null){
//			criteria.andRoleidEqualTo(user.getRoleid());
//		}
//		List<User> userList = userMapper.selectUserAndRoleByExample(example);
		List<Itemcat> itemcatList = itemcatMapper.selectByExample(example);
		
		PageInfo<Itemcat> pageInfo = new PageInfo<Itemcat>(itemcatList);
		return pageInfo;
	}

	
	// 查询上级分类
	public List<Itemcat> getItemcatByLevel(Long level) {
		// example:查询对象；  他可以创建查询对象，
		// criteria:条件对象； 封装查询条件；
		ItemcatExample example = new  ItemcatExample();
		com.xiaoshu.entity.ItemcatExample.Criteria criteria = example.createCriteria();
		criteria.andLevelEqualTo(level);
		return  itemcatMapper.selectByExample(example);
	}

	// 添加
	public void addItemcat(Itemcat itemcat) {
		itemcatMapper.insert(itemcat);
	}

	// 修改
	public void updateItemcat(Itemcat itemcat) {
		// 如果以后碰见多表； 不能使用逆向工程了；直接写mapper和sql语句；
		itemcatMapper.updateByPrimaryKey(itemcat);
	}

	// 删除
	public void deleteItemcat(long itemcatid) {
		itemcatMapper.deleteByPrimaryKey(itemcatid);
	}

	// 导出  查询所有
	public List<Itemcat> findAll(){
		ItemcatExample example = new ItemcatExample();
		return itemcatMapper.selectByExample(example);
	}

	// 导入
	public void insertItemcat(Itemcat itemcat) {
		itemcatMapper.insert(itemcat);
	}
}
