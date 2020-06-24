<#ftl encoding="utf-8"/>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${nameSpace}">
	
	<!-- 添加 -->
	<insert id="add" parameterType="pfd">
		<![CDATA[ INSERT INTO ${tableName}(${keyId}) VALUES (${r"#{"}${keyId}${r"}"}) ]]>
	</insert>
	
	<!-- 编辑 -->
	<update id="edit" parameterType="pfd">
		<![CDATA[ UPDATE ${tableName} SET column1 = ${r"#{column1}"},column2 = ${r"#{column2}"} WHERE ${keyId} = ${r"#{"}${keyId}${r"}"} ]]>
	</update>
	
	<!-- 行删除 -->
	<delete id="delById" parameterType="String">
		<![CDATA[ DELETE FROM ${tableName} WHERE ${keyId} = ${r"#{"}${keyId}${r"}"} ]]> 
	</delete>
	
	<!-- 删除|批量删除-->
	<delete id="delByIds">
		DELETE FROM ${tableName} WHERE 
			${keyId} IN
		<foreach item="item" index="index" collection="list" open="(" separator="," close=")">
                 ${r"#{item}"}
		</foreach>
	</delete>
	
	<!-- 列表数据 -->
	<select id="listData" parameterType="HashMap" resultType="HashMap">
		<![CDATA[ 
			SELECT ${keyId} FROM (${keyId}, ROWNUM RN 
			FROM (SELECT ${keyId} FROM ${tableName}
		]]>
		<trim prefix="WHERE" prefixOverrides="AND">
			<if test="key_work != null and key_work != '' ">
				AND column LIKE CONCAT('%',${r"#{key_work}"},'%')
			</if>
		</trim>
		<![CDATA[ 
			) TB WHERE ROWNUM <= ${r"#{KEYROWS}"}) WHERE RN >= ${r"#{KEYRN}"}
		]]>
	</select>
	
	<!-- 总条数总记录数 -->
	<select id="listTotal"  parameterType="HashMap" resultType="Integer">
		<![CDATA[ SELECT COUNT(${keyId})TOTAL FROM ${tableName} ]]>
		<trim prefix="WHERE" prefixOverrides="AND">
			<if test="key_work != null and key_work != '' ">
				AND column LIKE CONCAT('%',${r"#{key_work}"},'%')
			</if>
		</trim>
	</select>
	
	<!-- 根据id获取全字段数据 -->
	<select id="queryById" parameterType="HashMap" resultType="HashMap">
		<![CDATA[ SELECT column1,column2 FROM ${tableName} WHERE ${keyId} = ${r"#{"}${keyId}${r"}"} ]]>
	</select>
</mapper>