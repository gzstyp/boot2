package com.fwtai.dao;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**dao底层操作处理工具类*/
@Repository
public class DaoBase {
	
	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;

	/**
	 * 通用的更新;删除;插入添加
	 * @作者 田应平
	 * @返回值类型 int
	 * @创建时间 2016年12月24日 23:00:14
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public int execute(final String sqlMapId) throws Exception {
		return sqlSession.update(sqlMapId);
	}

	/**
	 * 通用的更新;删除;插入添加
	 * @作者 田应平
	 * @返回值类型 int
	 * @创建时间 2016年12月24日 23:00:09
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public int execute(final String sqlMapId,final Object objParam) throws Exception{
		return sqlSession.update(sqlMapId,objParam);
	}
	
	/**批量插入|更新|删除*/
	public int executeBatch(final String sqlMapId) throws Exception {
		return sqlSession.update(sqlMapId);
	}
	
	/**批量插入|更新|删除,objParam可以是List<HashMap<String, Object>>*/
	public int executeBatch(final String sqlMapId, final Object objParam) throws Exception {
		return sqlSession.update(sqlMapId,objParam);
	}

	/**
	 * 用于查询返回Integer
	 * @作者 田应平
	 * @返回值类型 Integer
	 * @创建时间 2016年12月24日 23:00:55
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public Integer queryForInteger(final String sqlMapId) throws Exception {
		return sqlSession.selectOne(sqlMapId);
	}

	/**
	 * 用于查询返回Integer
	 * @作者 田应平
	 * @返回值类型 Integer
	 * @创建时间 2016年12月24日 23:01:35
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public Integer queryForInteger(final String sqlMapId, final Object objParam) throws Exception {
		return sqlSession.selectOne(sqlMapId,objParam);
	}

	/**
	 * 用于查询返回Integer
	 * @作者 田应平
	 * @返回值类型 Integer
	 * @创建时间 2016年12月24日 23:00:55
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	 */
	public Long queryForLong(final String sqlMapId) throws Exception {
		return sqlSession.selectOne(sqlMapId);
	}

	/**
	 * 用于查询返回Integer
	 * @作者 田应平
	 * @返回值类型 Integer
	 * @创建时间 2016年12月24日 23:01:35
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	 */
	public Long queryForLong(final String sqlMapId, final Object objParam) throws Exception {
		return sqlSession.selectOne(sqlMapId,objParam);
	}

    /**
     * 用于金额查询返回BigDecimal
     * @作者 田应平
     * @返回值类型 Integer
     * @创建时间 2016年12月24日 23:01:35
     * @QQ号码 444141300
     * @主页 http://www.yinlz.com
    */
    public BigDecimal queryForBigDecimal(final String sqlMapId) throws Exception {
        return sqlSession.selectOne(sqlMapId);
    }

    /**
     * 用于金额查询返回BigDecimal
     * @作者 田应平
     * @返回值类型 Integer
     * @创建时间 2016年12月24日 23:01:35
     * @QQ号码 444141300
     * @主页 http://www.yinlz.com
    */
    public BigDecimal queryForBigDecimal(final String sqlMapId,final Object objParam) throws Exception {
        return sqlSession.selectOne(sqlMapId,objParam);
    }

	/**
	 * 用于查询返回String
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年11月20日 下午2:24:52
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public String queryForString(final String sqlMapId) throws Exception {
		return sqlSession.selectOne(sqlMapId);
	}

	/**
	 * 用于查询返回String
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年12月25日 00:44:39
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public String queryForString(final String sqlMapId, final Object objParam) throws Exception {
		return sqlSession.selectOne(sqlMapId,objParam);
	}

	/**
	 * 根据id去查询,或必须保证sql所查询的结果只有一条或限制返回一条数据
	 * @作者 田应平
	 * @返回值类型 Map<String,Object>
	 * @创建时间 2016年12月24日 23:03:07
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public Map<String, Object> queryForMap(final String sqlMapId) throws Exception {
		return sqlSession.selectOne(sqlMapId);
	}

	/**
	 * 必须保存sql所查询的结果只有一条或限制返回一条数据
	 * @作者 田应平
	 * @返回值类型 Map<String,Object>
	 * @创建时间 2016年12月24日 23:03:49
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public Map<String, Object> queryForMap(final String sqlMapId, final Object objParam) throws Exception {
		return sqlSession.selectOne(sqlMapId,objParam);
	}
	
	/**
	 * 根据id去查询,或必须保证sql所查询的结果只有一条或限制返回一条数据
	 * @作者 田应平
	 * @返回值类型 HashMap《String,Object》
	 * @创建时间 2016年12月24日 23:03:07
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public HashMap<String, Object> queryForHashMap(final String sqlMapId) throws Exception {
		return sqlSession.selectOne(sqlMapId);
	}

	/**
	 * 必须保存sql所查询的结果只有一条或限制返回一条数据
	 * @作者 田应平
	 * @返回值类型 HashMap《String,Object》
	 * @创建时间 2016年12月24日 23:03:49
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public HashMap<String, Object> queryForHashMap(final String sqlMapId, final Object objParam) throws Exception {
		return sqlSession.selectOne(sqlMapId,objParam);
	}

	/**
	 * 查询返回List《Map》,含分页
	 * @作者 田应平
	 * @返回值类型 List<Map<String,Object>>
	 * @创建时间 2016年12月24日 23:04:14
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public List<Map<String, Object>> queryForListMap(final String sqlMapId) throws Exception {
		return sqlSession.selectList(sqlMapId);
	}

	/**
	 * 查询返回List《Map》,含分页
	 * @param sqlMapId
	 * @param objParam
	 * @throws Exception
	 * @作者 田应平
	 * @返回值类型 List<Map<String,Object>>
	 * @创建时间 2016年12月25日 上午12:47:44
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public List<Map<String, Object>> queryForListMap(final String sqlMapId, final Object objParam) throws Exception {
		return sqlSession.selectList(sqlMapId,objParam);
	}
	
	/**
	 * 查询返回List《HashMap》,含分页
	 * @作者 田应平
	 * @返回值类型 List<Map<String,Object>>
	 * @创建时间 2016年12月24日 23:04:14
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public List<HashMap<String, Object>> queryForListHashMap(final String sqlMapId) throws Exception {
		return sqlSession.selectList(sqlMapId);
	}

	/**
	 * 查询返回List《HashMap》,含分页
	 * @param sqlMapId
	 * @param objParam
	 * @throws Exception
	 * @作者 田应平
	 * @返回值类型 List<Map<String,Object>>
	 * @创建时间 2016年12月25日 上午12:47:44
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public List<HashMap<String,Object>> queryForListHashMap(final String sqlMapId, final Object objParam) throws Exception {
		return sqlSession.selectList(sqlMapId,objParam);
	}

    /**
     * 查询对象数据对象
     * @param sqlMapId
     * @作者 田应平
     * @返回值类型 PageFormData
     * @创建时间 2016年12月24日 下午11:12:57
     * @QQ号码 444141300
     * @官网 http://www.yinlz.com
    */
    public <T> T queryForEntity(final String sqlMapId){
        return sqlSession.selectOne(sqlMapId);
    }

    /**
     * 查询对象数据对象
     * @param sqlMapId
     * @param objParam
     * @作者 田应平
     * @返回值类型 PageFormData
     * @创建时间 2016年12月25日 上午12:46:20
     * @QQ号码 444141300
     * @官网 http://www.yinlz.com
    */
    public <T> T queryForEntity(final String sqlMapId, final Object objParam){
        return sqlSession.selectOne(sqlMapId,objParam);
    }

    /**
     * 返回List集合
     * @作者 田应平
     * @返回值类型 int
     * @创建时间 2016年12月24日 23:00:14
     * @QQ号码 444141300
     * @主页 http://www.yinlz.com
    */
    public <T> List<T> selectListEntity(final String sqlMapId){
        return sqlSession.selectList(sqlMapId);
    }

    /**
     * 带参数的LIST
     * @作者 田应平
     * @返回值类型 int
     * @创建时间 2016年12月24日 23:00:14
     * @QQ号码 444141300
     * @主页 http://www.yinlz.com
    */
    public <T> List<T> selectListEntity(final String sqlMapId, final Object objParam){
        return sqlSession.selectList(sqlMapId, objParam);
    }

    /**
     * 请谨慎使用,如果报错则换成返回List<HashMap<String,Object>>
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018年9月14日 13:55
    */
    public List<HashMap<String,String>> queryForListString(final String sqlMapId) throws Exception {
        return sqlSession.selectList(sqlMapId);
    }

    /**
     * 请谨慎使用,如果报错则换成返回List<HashMap<String,Object>>
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018年9月14日 13:55
    */
    public List<HashMap<String,String>> queryForListString(final String sqlMapId,final Object objParam) throws Exception {
        return sqlSession.selectList(sqlMapId,objParam);
    }
	
	/**
	 * 查询PageFormData数据对象
	 * @param sqlMapId
	 * @throws Exception
	 * @作者 田应平
	 * @返回值类型 PageFormData
	 * @创建时间 2016年12月24日 下午11:12:57
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public PageFormData queryForPageFormData(final String sqlMapId) throws Exception {
		return sqlSession.selectOne(sqlMapId);
	}

	/**
	 * 查询PageFormData数据对象
	 * @param sqlMapId
	 * @param objParam
	 * @throws Exception
	 * @作者 田应平
	 * @返回值类型 PageFormData
	 * @创建时间 2016年12月25日 上午12:46:20
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public PageFormData queryForPageFormData(final String sqlMapId, final Object objParam) throws Exception {
		return sqlSession.selectOne(sqlMapId,objParam);
	}

	/**
	 * 查询List《PageFormData》数据对象 ,含分页
	 * @param sqlMapId
	 * @throws Exception
	 * @作者 田应平
	 * @返回值类型 List<PageFormData>
	 * @创建时间 2016年12月25日 上午12:46:48
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public List<PageFormData> queryForListPageFormData(final String sqlMapId) throws Exception {
		return sqlSession.selectList(sqlMapId);
	}

	/**
	 * 查询List《PageFormData》数据对象 ,含分页
	 * @param sqlMapId
	 * @param objParam
	 * @throws Exception
	 * @作者 田应平
	 * @返回值类型 List<PageFormData>
	 * @创建时间 2016年12月25日 上午12:46:53
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public List<PageFormData> queryForListPageFormData(final String sqlMapId, final Object objParam) throws Exception {
		return sqlSession.selectList(sqlMapId,objParam);
	}
	
	/**
	 * 带分页查询功能;返回key为total总条数总记录数,key为listData返回的list数据集合
	 * @param params 参数
     * @param sqlMapIdListData 总条数总记录数的sql映射
     * @param sqlMapIdTotal 返回的list数据集合的sql映射
	 * @作者 田应平
	 * @返回值类型 HashMap< String,Object >,含total总条数总记录数;listData分页的数据
	 * @创建时间 2017年1月10日 下午5:56:08
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public HashMap<String,Object> queryForPage(final HashMap<String,Object> params,final String sqlMapIdListData,final String sqlMapIdTotal)throws Exception{
		final HashMap<String, Object> map = new HashMap<String,Object>(0);
		final Integer total = sqlSession.selectOne(sqlMapIdTotal,params);
		final List<Object> list = sqlSession.selectList(sqlMapIdListData,params);
		map.put(ConfigFile.total,total);
		map.put(ConfigFile.listData,list);
		return map;
	}
	
	/**
	 * 带分页查询功能;返回key为total总条数总记录数,key为listData返回的list数据集合
	 * @param params 参数
     * @param sqlMapIdListData 总条数总记录数的sql映射
     * @param sqlMapIdTotal 返回的list数据集合的sql映射
	 * @作者 田应平
	 * @返回值类型 HashMap< String,Object >,含total总条数总记录数;listData分页的数据
	 * @创建时间 2017年1月10日 下午5:56:08
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public HashMap<String,Object> queryForPage(final PageFormData params,final String sqlMapIdListData,final String sqlMapIdTotal)throws Exception{
		final HashMap<String, Object> map = new HashMap<String,Object>(0);
		final Integer total = sqlSession.selectOne(sqlMapIdTotal,params);
		final List<Object> list = sqlSession.selectList(sqlMapIdListData,params);
		map.put(ConfigFile.total,total);
		map.put(ConfigFile.listData,list);
		return map;
	}

	/**
	 * windows下执行sql文件
	 * @param mySqlBinDir Mysql安装目录的bin目录,如:D:\\MyTools\\mysql-5.7.19-winx64\\bin\\
	 * @param dbUserName mysql的用户名,一般是root
	 * @param dbPWD mysql的密码
	 * @param DB 数据库名
	 * @param sqlPath sql文件的路径
	 * @作者 田应平
	 * @创建时间 2017年10月29日 下午12:11:28
	 * @QQ号码 444141300
	 * @官网 http://www.yinlz.com
	*/
	public final static boolean exeSql(final String mySqlBinDir,final String dbUserName,final String dbPWD,final String DB,final String sqlPath){
		final Runtime runtime = Runtime.getRuntime();
		// 因为在命令窗口进行mysql数据库的导入一般分三步走，所以所执行的命令将以字符串数组的形式出现
		final String cmdarray[] = {mySqlBinDir + "mysql.exe -u"+dbUserName+" -p"+dbPWD, "use "+DB, "source "+ sqlPath};// 根据属性文件的配置获取数据库导入所需的命令，组成一个数组
		Process process;
		try {
			process = runtime.exec("cmd /c " + cmdarray[0]);
			final OutputStream os = process.getOutputStream();//执行了第一条命令以后已经登录到mysql了，所以之后就是利用mysql的命令窗口,进程执行后面的代码
			final OutputStreamWriter writer = new OutputStreamWriter(os);
			writer.write(cmdarray[1] + "\r\n" + cmdarray[2]);//命令1和命令2要放在一起执行
			writer.flush();
			writer.close();
			os.close();
			return true;
		} catch (IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	//源代码|示例代码
	protected final boolean exeSql(){
		final Runtime runtime = Runtime.getRuntime();
		// 因为在命令窗口进行mysql数据库的导入一般分三步走，所以所执行的命令将以字符串数组的形式出现
		String cmdarray[] = {"D:\\MyTools\\mysql-5.7.19-winx64\\bin\\mysql.exe -uroot -proot", "use yfjz", "source C:\\sql.sql" };// 根据属性文件的配置获取数据库导入所需的命令，组成一个数组
		Process process;
		try {
			process = runtime.exec("cmd /c " + cmdarray[0]);
			// 执行了第一条命令以后已经登录到mysql了，所以之后就是利用mysql的命令窗口
			// 进程执行后面的代码
			OutputStream os = process.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(os);
			// 命令1和命令2要放在一起执行
			writer.write(cmdarray[1] + "\r\n" + cmdarray[2]);
			writer.flush();
			writer.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("执行完成");
		return false;
	}
}